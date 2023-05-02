// +---------------------------------------------------------------------------- 
// | UptickLite is the community version of Uptick NFT marketpalce, including    
// | three major components - service, api and web, powered by Uptick Network    
// | and Loopring. It is available to the Uptick and Loopring dev community      
// | through the open source license.                                            
// | Uptick Network is a busienss grade NFT infrastructure for NFT applications  
// | with multichian and interchain support. Loopring is a zkRollup layer2 on top
// | of Ethereum.                                                                
// +---------------------------------------------------------------------------- 
// | Copyright (c) 2022~2099 Uptick Network (https://uptick.network/)            
// | All rights reserved.                                                        
// +---------------------------------------------------------------------------- 
// | Licensed ( https://www.gnu.org/licenses/gpl-3.0.en.html )                   
// +---------------------------------------------------------------------------- 
// | Author: Uptick Network development team tech@uptickproject.com              
// +---------------------------------------------------------------------------- 
package org.uptick.market.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.uptick.market.constant.Key;
import org.uptick.market.denum.*;
import org.uptick.market.dto.AssetDto;
import org.uptick.market.dto.ContractDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.dto.TransferLogDto;
import org.uptick.market.entity.*;
import org.uptick.market.middleware.MiddlewareService;
import org.uptick.market.middleware.entity.NFTData;
import org.uptick.market.service.*;
import org.uptick.market.task.AssetSync;
import org.uptick.market.util.IdUtils;
import org.uptick.market.util.JsonResult;
import org.uptick.market.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RestController
@RequestMapping(org.uptick.market.constant.Api.VERSION + "/nft")
@Api(tags = "NFT  api")
public class NFTController {


    @Autowired
    private ContractService contractService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private MakerOrderService makerOrderService;


    @Autowired
    private TakerOrderService takerOrderService;


    @Autowired
    private MiddlewareService middlewareService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderDetailsService orderDetailsService;


    @Autowired
    private TransferLogService transferLogService;

    @Autowired
    private AssetExtensionService assetExtensionService;

    @Autowired
    private UserService userService;


    @Autowired
    private AssetSync assetSync;

    @PostMapping("/deploy.do")
    @ApiOperation(value = "Deploy contract", notes = "")
    @ResponseBody
    public JsonResult deploy(Contract param) {

        Contract baseUrl = contractService.getByBaseUrl(param.getLinkUrl());
        if (baseUrl != null) {
            return JsonResult.error(Action.NFT_BASEURL_EXIST);
        }
        Contract contract = contractService.getContractByTokenAddress(param.getAddress());
        if (contract != null) {
            return JsonResult.error(Action.NFT_TOKEN_EXIST);
        }
        param.setCreateTime(new Date());
        param.setDeleted(false);
        param.setAmount(0);
        contractService.save(param);
        return JsonResult.success();
    }


    @PostMapping("/mint.do")
    @ApiOperation(value = "Mint nft", notes = "")
    @ResponseBody
    public JsonResult mint(Asset asset) {
        Contract contract = contractService.getContractByTokenAddress(asset.getContractAddress());
        if (contract == null) {
            return JsonResult.error(Action.NFT_TOKEN_NOT_EXIST);
        }

        Asset param = new Asset();
        param.setContractAddress(asset.getContractAddress());
        param.setNftId(asset.getNftId());
        param.setDeleted(false);
        param = assetService.templateOne(param);
        if (param != null) {
            return JsonResult.error(Action.NFT_ID_EXIST);
        }
        asset.setCreateTime(new Date());
        asset.setDeleted(false);
        asset.setLevel(AssetLevel.MARKET.intValue());
        asset.setSaleStatus(AssetStatus.DELIST.getStatus());
        asset.setCreator(asset.getOwner());
        asset.setChannel(ChannelType.UPTICK_LOOPRING.intValue());
        asset.setListAmount(0);
        asset.setAssetStatus(ContractAndNftStatus.normal.getStatus());
        log.info("saveAsset134" + asset);
        Asset newNFT = assetService.save(asset);

        AssetExtension assetExtension = new AssetExtension();
        assetExtension.setCreateTime(new Date());
        assetExtension.setNftId(asset.getNftId());
        assetExtension.setDeleted(false);
        assetExtension.setTotalInventory(Long.valueOf(asset.getAmount()));
        assetExtension.setIssuerPrice(new BigDecimal(0));
        assetExtension.setDaiTotalPrice(new BigDecimal(0));
        assetExtension.setEthTotalPrice(new BigDecimal(0));
        assetExtension.setLrcTotalPrice(new BigDecimal(0));
        assetExtension.setUsdcTotalPrice(new BigDecimal(0));
        assetExtension.setUsdtTotalPrice(new BigDecimal(0));
        assetExtension.setWeight(100);
        assetExtension.setLikeCount(0L);
        assetExtension.setViewCount(0L);
        assetExtension.setCreateOwner(asset.getCreator());
        assetExtensionService.save(assetExtension);

        transferLogService.addLog(Arrays.asList(newNFT), asset.getCreator(), asset.getOwner(), null, TxType.MINT);

        Contract update = new Contract();
        update.setUpdateTime(new Date());
        update.setId(contract.getId());
        update.setAmount(contract.getAmount() + 1);
        contractService.updateTemplateById(update);
        return JsonResult.success();
    }


    @PostMapping("/list.do")
    @ApiOperation(value = "Nft list", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "User wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftAddress", value = "nft token address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftId", value = "nft id ", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "marketPrice", value = "market price", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "tokenType", value = "Pay token type(ETH、USDT、DAI、LRC、USDC)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "makerOrderJson", value = "Maker order json", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "Maker signature", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "Bug limit", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "listAmount", value = "List amount", required = true, dataType = "int", paramType = "query")
    })
    @ResponseBody
    public JsonResult<PageQuery<Asset>> NFTList(@RequestParam String owner, @RequestParam String nftAddress, @RequestParam String nftId,
                                                @RequestParam BigDecimal marketPrice, @RequestParam String tokenType,
                                                @RequestParam String makerOrderJson, @RequestParam String signature,
                                                @RequestParam(required = false, defaultValue = "0") Integer limit,
                                                @RequestParam Integer listAmount,
                                                String lockAssetHash) {


        Asset param = new Asset();
        param.setOwner(owner);
        param.setContractAddress(nftAddress);
        param.setNftId(nftId);
        param.setSaleStatus(AssetStatus.DELIST.getStatus());
        Asset asset = assetService.templateOne(param);
        if (asset == null) {
            return JsonResult.error(Action.PARAM_ERROR);
        }

        if (asset.getAmount() < listAmount) {
            return JsonResult.error(Action.NFT_NOT_ENOUGH);
        }

        MakerOrder makerOrder = new MakerOrder();
        makerOrder.setDeleted(false);
        makerOrder.setMakerJson(makerOrderJson);
        makerOrder.setContractAddress(nftAddress);
        makerOrder.setSignature(signature);
        makerOrder.setCreateTime(new Date());
        makerOrder.setNftId(nftId);
        makerOrder.setSigner(owner);
        makerOrder.setSaleLimit(limit);
        makerOrder.setLockAssetHash(lockAssetHash);
        makerOrder = makerOrderService.save(makerOrder);

        asset.setMarketOwner(owner);
        asset.setMarketTime(new Date());
        asset.setMarketPrice(marketPrice);
        asset.setUpdateTime(new Date());
        asset.setMakerOrderId(makerOrder.getId());
        asset.setSaleStatus(AssetStatus.LIST.getStatus());
        asset.setMarketTokenType(tokenType);
        asset.setListAmount(listAmount);
        asset.setMakerType(MarkeType.SALE.intValue());

        assetService.updateTemplateById(asset);
        if (asset.getCreator().equals(asset.getOwner())) {
            AssetExtension assetExtension = new AssetExtension();
            assetExtension.setNftId(asset.getNftId());
            assetExtension.setIssuerTokenType(tokenType);
            assetExtension.setIssuerPrice(marketPrice);
            assetExtensionService.updateByNftId(assetExtension);
        }

        transferLogService.addLog(Arrays.asList(asset), owner, owner, null, TxType.LIST);
        return JsonResult.success();
    }

    @PostMapping("/deList.do")
    @ApiOperation(value = "Nft deList", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "User wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftAddress", value = "nft token address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftId", value = "nft id ", required = true, dataType = "long", paramType = "query")
    })
    @ResponseBody
    public JsonResult<PageQuery<Asset>> deList(@RequestParam String owner, @RequestParam String nftAddress, @RequestParam String nftId) {

        Asset asset = new Asset();
        asset.setOwner(owner);
        asset.setContractAddress(nftAddress);
        asset.setNftId(nftId);
        asset.setSaleStatus(AssetStatus.LIST.getStatus());
        asset = assetService.templateOne(asset);
        if (asset == null) {
            return JsonResult.error(Action.PARAM_ERROR);
        }

        MakerOrder makerOrder = new MakerOrder();
        makerOrder.setId(asset.getMakerOrderId());
        makerOrder = makerOrderService.templateOne(makerOrder);
        if (makerOrder == null) {
            return JsonResult.error(Action.PARAM_ERROR);
        }

        makerOrderService.deleteById(asset.getMakerOrderId());

        asset.setMarketOwner(null);
        asset.setMarketTime(null);
        asset.setMarketPrice(null);
        asset.setUpdateTime(new Date());
        asset.setMakerOrderId(null);
        asset.setSaleStatus(AssetStatus.DELIST.getStatus());
        asset.setListAmount(0);
        asset.setMakerType(null);
        asset.setMarketTokenType(null);
        assetService.updateById(asset);

        transferLogService.addLog(Arrays.asList(asset), owner, owner, null, TxType.DELIST);
        return JsonResult.success();
    }


    @PostMapping("/singleList.do")
    @ApiOperation(value = "Single Nft list", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "User wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "assetId", value = "nft asset id", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "marketPrice", value = "market price", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "tokenType", value = "Pay token type(ETH、USDT、DAI、LRC、USDC)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "makerOrderJson", value = "Maker order json", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "Maker signature", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "Bug limit", required = false, dataType = "int", paramType = "query")
    })
    @ResponseBody
    public JsonResult<PageQuery<Asset>> singleList(@RequestParam String owner, @RequestParam Long assetId,
                                                   @RequestParam BigDecimal marketPrice, @RequestParam String tokenType,
                                                   @RequestParam String makerOrderJson, @RequestParam String signature, @RequestParam(required = false, defaultValue = "0") Integer limit) {


        log.info("makerOrderJson:{}", makerOrderJson);
        log.info("signature:{}", signature);

        Asset asset = assetService.single(assetId);
        if (asset == null) {
            return JsonResult.error(Action.PARAM_ERROR);
        }

        MakerOrder makerOrder = new MakerOrder();
        makerOrder.setDeleted(false);
        makerOrder.setMakerJson(makerOrderJson);
        makerOrder.setContractAddress(asset.getContractAddress());
        makerOrder.setSignature(signature);
        makerOrder.setCreateTime(new Date());
        makerOrder.setNftId(asset.getNftId());
        makerOrder.setSigner(owner);
        makerOrder.setSaleLimit(limit);
        makerOrder = makerOrderService.save(makerOrder);
        List<Asset> param = new ArrayList<>();

        Asset update = new Asset();
        update.setId(asset.getId());
        update.setMarketOwner(asset.getOwner());
        update.setMarketTime(new Date());
        update.setMarketPrice(marketPrice);
        update.setUpdateTime(new Date());
        update.setMakerOrderId(makerOrder.getId());
        update.setSaleStatus(AssetStatus.LIST.getStatus());
        update.setMarketTokenType(tokenType);
        param.add(update);

        assetService.batchUpdate(param);
        transferLogService.addLog(param, owner, owner, null, TxType.LIST);
        return JsonResult.success();
    }


    @PostMapping("/singleDeList.do")
    @ApiOperation(value = "Single nft deList", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assetId", value = "nft asset id", required = true, dataType = "long", paramType = "query"),
    })
    @ResponseBody
    public JsonResult<PageQuery<Asset>> singleDeList(@RequestParam Long assetId) {


        Asset asset = assetService.single(assetId);
        if (asset == null) {
            return JsonResult.error(Action.PARAM_ERROR);
        }
        makerOrderService.deleteById(asset.getMakerOrderId());

        asset.setMarketOwner(null);
        asset.setMarketTime(null);
        asset.setMarketPrice(null);
        asset.setUpdateTime(new Date());
        asset.setMakerOrderId(null);
        asset.setSaleStatus(AssetStatus.DELIST.getStatus());
        assetService.updateById(asset);
        transferLogService.addLog(Arrays.asList(asset), asset.getOwner(), asset.getOwner(), null, TxType.DELIST);
        return JsonResult.success();
    }


    @PostMapping("/trade.do")
    @ApiOperation(value = "Nft trade", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "User wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "makerOwner", value = "nft maker Owner", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nftAddress", value = "nft token address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftId", value = "nft id ", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "takerOrderJson", value = "Maker order json", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "Maker signature", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "Bug amount", required = true, dataType = "int", paramType = "query"),

    })
    @ResponseBody
    public JsonResult trade(@RequestParam String owner, @RequestParam String makerOwner, @RequestParam String nftAddress, @RequestParam String nftId,
                            @RequestParam String takerOrderJson, @RequestParam String signature, @RequestParam Integer amount, BigDecimal bidPrice) {

        Asset asset = new Asset();
        asset.setContractAddress(nftAddress);
        asset.setNftId(nftId);
        asset.setSaleStatus(AssetStatus.LIST.getStatus());
        asset.setMarketOwner(makerOwner);
        asset = assetService.templateOne(asset);
        if (asset == null) {
            return JsonResult.error(Action.PARAM_ERROR);
        }

        if (asset.getListAmount() < amount) {
            return JsonResult.error(Action.INSUFFICIENT_INVENTORY);
        }

        BigDecimal price = bidPrice == null ? asset.getMarketPrice() : bidPrice;
        String tokenType = asset.getMarketTokenType();

        MakerOrder makerOrder = makerOrderService.single(asset.getMakerOrderId());

        String result = middlewareService.tradeNFT(JSONObject.parseObject(makerOrder.getMakerJson()), makerOrder.getSignature(), JSONObject.parseObject(takerOrderJson), signature);
        log.info("trade:{}", result);
        if (StringUtils.isNotEmpty(result)) {

            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null) {
                String tradeHash = data.getString("tradeHash");
                if (StringUtils.isNotEmpty(tradeHash)) {

                    asset.setListAmount(asset.getListAmount() - amount);
                    asset.setAmount(asset.getAmount() - amount);

                    createOrder(makerOwner, owner, asset.getLevel(), price, asset, amount, tokenType, tradeHash, makerOrder.getMakerJson(), takerOrderJson);

                    transferLogService.addLog(Arrays.asList(asset), makerOwner, owner, tradeHash, TxType.TRADE);

                    if (asset.getListAmount() <= 0) {
                        asset.setMarketOwner(null);
                        asset.setMarketTime(null);
                        asset.setMarketPrice(null);
                        asset.setUpdateTime(new Date());
                        asset.setMakerOrderId(null);
                        asset.setSaleStatus(AssetStatus.DELIST.getStatus());
                        asset.setListAmount(0);
                        asset.setMakerType(null);
                        asset.setMarketTokenType(null);
                        assetService.updateById(asset);
                    } else {
                        assetService.updateTemplateById(asset);
                    }
                    if (asset.getAmount() <= 0) {
                        assetService.deleteById(asset);
                    }

                    Asset recipientAsset = new Asset();
                    recipientAsset.setOwner(owner);
                    recipientAsset.setNftId(asset.getNftId());
                    recipientAsset = assetService.templateOne(recipientAsset);
                    if (recipientAsset != null) {
                        recipientAsset.setAmount(recipientAsset.getAmount() + amount);
                        assetService.updateTemplateById(recipientAsset);
                    } else {
                        recipientAsset = new Asset();
                        BeanUtils.copyProperties(asset, recipientAsset);
                        if (recipientAsset.getCreator().equals(owner)) {
                            recipientAsset.setLevel(AssetLevel.MARKET.intValue());
                        } else {
                            recipientAsset.setLevel(AssetLevel.FLEA_MARKET.intValue());
                        }
                        User userByAddress = userService.getUserByAddress(owner);
                        if (userByAddress != null) {
                            recipientAsset.setAccountId(userByAddress.getAccountId());
                        }
                        recipientAsset.setOwner(owner);
                        recipientAsset.setAmount(amount);
                        recipientAsset.setMarketOwner(null);
                        recipientAsset.setMarketTime(null);
                        recipientAsset.setMarketPrice(null);
                        recipientAsset.setUpdateTime(new Date());
                        recipientAsset.setMakerOrderId(null);
                        recipientAsset.setSaleStatus(AssetStatus.DELIST.getStatus());
                        recipientAsset.setListAmount(0);
                        recipientAsset.setMakerType(null);
                        recipientAsset.setMarketTokenType(null);
                        assetService.save(recipientAsset);
                    }
                    return JsonResult.success();
                } else {

                    Integer code = data.getInteger("code");
                    String msg = data.getString("message");
                    if (code != null && code == 102011) {
                        makerOrderService.deleteById(asset.getMakerOrderId());


                        asset.setMarketOwner(null);
                        asset.setMarketTime(null);
                        asset.setMarketPrice(null);
                        asset.setUpdateTime(new Date());
                        asset.setMakerOrderId(null);
                        asset.setSaleStatus(AssetStatus.DELIST.getStatus());
                        asset.setListAmount(0);
                        asset.setMakerType(null);
                        asset.setMarketTokenType(null);
                        assetService.updateById(asset);
                        //添加交易日志
                        transferLogService.addLog(Arrays.asList(asset), owner, owner, null, TxType.DELIST);
                    }
                    if (StringUtils.isNotEmpty(msg)) {
                        return JsonResult.error(Action.TRADE_ERROR.getCode(), msg);
                    }
                }
            }
        }

        return JsonResult.error(Action.TRADE_ERROR);
    }

    private void createOrder(String sender, String recipient, Integer assetLevel, BigDecimal price, Asset asset, Integer buyAmount, String tokenType, String txid, Object makerOrder, Object takerOrder) {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(IdUtils.get());
        orderInfo.setCreateTime(new Date());
        orderInfo.setDeleted(false);
        orderInfo.setLevel(assetLevel.intValue());
        orderInfo.setOrderPrice(price);
        orderInfo.setNftName(asset.getName());
        orderInfo.setNftImg(asset.getImgUrl());
        orderInfo.setSender(sender);
        orderInfo.setRecipient(recipient);
        orderInfo.setQuantity(buyAmount);
        orderInfo.setOrderStatus(OrderStatus.SUCCESS.intValue());
        orderInfo.setOrderPrice(price.multiply(new BigDecimal(buyAmount)));
        orderInfo.setContractAddress(asset.getContractAddress());
        orderInfo.setTokenType(tokenType);
        orderInfo.setTxId(txid);
        orderInfo.setNftId(asset.getNftId());
        orderInfo = orderInfoService.save(orderInfo);
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (int i = 0; i < buyAmount; i++) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderNo(orderInfo.getOrderNo());
            orderDetails.setCreateTime(new Date());
            orderDetails.setOrderStatus(OrderStatus.SUCCESS.intValue());
            orderDetails.setSender(sender);
            orderDetails.setLevel(assetLevel.intValue());
            orderDetails.setAssetId(asset.getId());
            orderDetails.setRecipient(recipient);
            orderDetails.setPayPrice(price);
            orderDetails.setNftId(asset.getNftId());
            orderDetails.setContractAddress(asset.getContractAddress());
            orderDetails.setTokenType(tokenType);
            orderDetails.setOrderMarketType(asset.getMakerType());
            orderDetails.setRoyaltyPercentage(asset.getRoyaltyPercentage() == null ? new BigDecimal(0) : new BigDecimal(asset.getRoyaltyPercentage()));
            orderDetails.setOriginalRoyaltyPercentage(asset.getOriginalRoyaltyPercentage() == null ? new BigDecimal(0) : new BigDecimal(asset.getOriginalRoyaltyPercentage()));

            if (makerOrder != null) {
                JSONObject jsonObject = JSONObject.parseObject(makerOrder.toString());
                BigDecimal maxFeeBips = new BigDecimal(jsonObject.get("maxFeeBips") == null ? "0" : jsonObject.get("maxFeeBips").toString());
                BigDecimal multiply = maxFeeBips.multiply(new BigDecimal("0.01"));
                BigDecimal platformFee = multiply.subtract(new BigDecimal(asset.getRoyaltyPercentage()));
                orderDetails.setPlatformFee(platformFee);
            }

            if (takerOrder != null) {
                JSONObject jsonObject1 = JSONObject.parseObject(takerOrder.toString());
                BigDecimal commissionCharge = new BigDecimal(jsonObject1.get("maxFeeBips") == null ? "0" : jsonObject1.get("maxFeeBips").toString());
                orderDetails.setCommissionCharge(commissionCharge);
            }
            orderDetails.setReconciliationCreator(false);
            orderDetails.setReconciliationLoopring(false);

            orderDetailsList.add(orderDetails);
            if (AssetLevel.MARKET.intValue().equals(assetLevel.intValue())) {
                AssetExtension assetExtension = new AssetExtension();
                assetExtension.setNftId(asset.getNftId());
                assetExtension = assetExtensionService.templateOne(assetExtension);
//                USDT, USDC, ETH, LRC, DIA;
                TokenType type = TokenType.parseValue(tokenType);
                switch (type) {
                    case DAI:
                        assetExtension.setDaiTotalPrice((assetExtension.getDaiTotalPrice() == null ? new BigDecimal(0) : assetExtension.getDaiTotalPrice()).add(price));
                        break;
                    case ETH:
                        assetExtension.setEthTotalPrice((assetExtension.getEthTotalPrice() == null ? new BigDecimal(0) : assetExtension.getEthTotalPrice()).add(price));
                        break;
                    case LRC:
                        assetExtension.setLrcTotalPrice((assetExtension.getLrcTotalPrice() == null ? new BigDecimal(0) : assetExtension.getLrcTotalPrice()).add(price));
                        break;
                    case USDC:
                        assetExtension.setUsdcTotalPrice((assetExtension.getUsdcTotalPrice() == null ? new BigDecimal(0) : assetExtension.getUsdcTotalPrice()).add(price));
                        break;
                    case USDT:
                        assetExtension.setUsdtTotalPrice((assetExtension.getUsdtTotalPrice() == null ? new BigDecimal(0) : assetExtension.getUsdtTotalPrice()).add(price));
                        break;
                }
                assetExtensionService.updateByNftId(assetExtension);
            }
        }
        orderDetailsService.saveBatch(orderDetailsList);
    }


    @PostMapping("/contractList.json")
    @ApiOperation(value = "Contract list", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "User wallet address", required = true, dataType = "string", paramType = "query")
    })
    @ResponseBody
    public JsonResult<PageQuery<ContractDto>> contractList(@RequestParam String owner, PageFrame pageFrame) {


        Contract param = new Contract();
        param.setOwner(owner);
        param.setDeleted(false);
        PageQuery<ContractDto> pageQuery = contractService.getContractPage(pageFrame, param);
        return JsonResult.success(pageQuery);
    }


    @PostMapping("/getCreated.json")
    @ApiOperation(value = "Query the list of tokens I created", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "baseUri", value = "shortUrl", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "tokenAddress", value = "shortUrl", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "owner", value = "User wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "Search", required = false, dataType = "string", paramType = "query"),
    })
    @ResponseBody
    public JsonResult<PageQuery<AssetDto>> getCreated(@RequestParam(required = false) String linkUrl,
                                                      @RequestParam(required = false) String tokenAddress,
                                                      @RequestParam String owner, String search, PageFrame pageFrame) {

        if (StringUtils.isEmpty(tokenAddress) && StringUtils.isEmpty(linkUrl)) {
            return JsonResult.error(Action.PARAM_ERROR);
        }
        Contract param = new Contract();
        param.setOwner(owner);

        if (StringUtils.isNotEmpty(linkUrl)) {
            param.setLinkUrl(linkUrl);
        } else if (StringUtils.isNotEmpty(tokenAddress)) {
            param.setAddress(tokenAddress);
        }


        param.setDeleted(false);
        param = contractService.templateOne(param);
        if (param == null) {
            return JsonResult.success(new PageQuery<AssetDto>());

        }

        Asset asset = new Asset();
        asset.setContractAddress(param.getAddress());
        asset.setDeleted(false);
        PageQuery<AssetDto> pageQuery = assetService.getMyCreatedListPage(asset, pageFrame, search);

        if (pageQuery != null && pageQuery.getList() != null && pageQuery.getList().size() > 0) {
            for (AssetDto dto : pageQuery.getList()) {
                Asset listParam = new Asset();
                listParam.setLevel(AssetLevel.MARKET.intValue());
                listParam.setNftId(dto.getNftId());
                listParam.setContractAddress(dto.getContractAddress());
                listParam.setSaleStatus(AssetStatus.LIST.getStatus());
                Long listAmount = assetService.templateCount(listParam);
                dto.setListAmount(listAmount.intValue());

                Asset haveParam = new Asset();
                haveParam.setLevel(AssetLevel.MARKET.intValue());
                haveParam.setNftId(dto.getNftId());
                haveParam.setContractAddress(dto.getContractAddress());
                haveParam.setSaleStatus(AssetStatus.DELIST.getStatus());
                Long haveAmount = assetService.templateCount(haveParam);
                dto.setHaveAmount(haveAmount.intValue());


                Boolean isBurn = false;
                if (haveAmount > 0 && haveAmount.intValue() == dto.getAmount()) {
                    isBurn = true;
                }
                dto.setIsBurn(isBurn);
                if (!dto.getIsBurn() && dto.getListAmount() == 0 && haveAmount == 0) {
                    dto.setSoldOut(true);
                } else {
                    dto.setSoldOut(false);
                }
            }


        }

        return JsonResult.success(pageQuery);
    }


    @PostMapping("/getOwn.json")
    @ApiOperation(value = "Query the list of tokens I own", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "User wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "creator", value = "Nft creator waller address", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "Sort(Sort by creation time：1，Sort by update time：2)", required = false, dataType = "int", paramType = "query")
    })
    @ResponseBody
    public JsonResult<PageQuery<AssetDto>> getOwn(@RequestParam String owner, @RequestParam(value = "sort", required = false, defaultValue = "1") Integer sort,
                                                  @RequestParam(value = "creator", required = false) String creator, PageFrame pageFrame) {


        Asset asset = new Asset();
        asset.setOwner(owner);
        asset.setLevel(AssetLevel.FLEA_MARKET.intValue());
        asset.setCreator(creator);
        asset.setDeleted(false);
        PageQuery<AssetDto> pageQuery = assetService.getMyOwnListPage(asset, pageFrame, sort);
        return JsonResult.success(pageQuery);
    }


    @PostMapping("/getOwnByCreator.json")
    @ApiOperation(value = "Query the list of tokens I own,sort by creator", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "User wallet address", required = true, dataType = "string", paramType = "query")
    })
    @ResponseBody
    public JsonResult<PageQuery<AssetDto>> getOwnByCreator(@RequestParam String owner, PageFrame pageFrame) {
        Asset asset = new Asset();
        asset.setOwner(owner);
        asset.setDeleted(false);
        PageQuery<AssetDto> pageQuery = assetService.getMyOwnByCreatorListPage(asset, pageFrame);
        return JsonResult.success(pageQuery);
    }


    @PostMapping("/tokenInfo.json")
    @ApiOperation(value = "Query token info", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenAddress", value = "Token address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftId", value = "Nft id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ownerAddress", value = "Nft owner wallet address", required = false, dataType = "string", paramType = "query")
    })
    @ResponseBody
    public JsonResult<AssetDto> tokenInfo(@RequestParam(required = true) String tokenAddress, @RequestParam(required = true) String nftId, @RequestParam(required = false) String owner) {
        Asset param = new Asset();
        param.setOwner(owner);
        param.setContractAddress(tokenAddress);
        param.setNftId(nftId);
        param.setDeleted(false);
        AssetDto assetDto = assetService.getTokenInfo(param);
        if (assetDto == null) {
            assetDto = new AssetDto();
            AssetExtension assetExtension = new AssetExtension();
            assetExtension.setNftId(nftId);
            assetExtension = assetExtensionService.templateOne(assetExtension);
            assetDto.setAssetExtension(assetExtension);
            Asset asset = new Asset();
            asset.setNftId(nftId);
            asset = assetService.templateOne(asset);
            BeanUtils.copyProperties(asset, assetDto);
            User userByAddress = userService.getUserByAddress(owner);
            if (userByAddress != null) {
                assetDto.setOwner(owner);
                assetDto.setOwnerPhoto(userByAddress.getProfilePhoto());
                assetDto.setOwnerName(userByAddress.getName());
            }
            User creatorByAddress = userService.getUserByAddress(asset.getCreator());
            if (creatorByAddress != null) {
                assetDto.setCreator(creatorByAddress.getAddress());
                assetDto.setCreatorName(creatorByAddress.getName());
                assetDto.setCreatorPhoto(creatorByAddress.getProfilePhoto());
            }
            assetDto.setAmount(0);
            assetDto.setListAmount(0);
            assetDto.setSaleStatus(AssetStatus.DELIST.getStatus());
            assetDto.setSoldOut(true);
            assetDto.setOwner(assetDto.getCreator());
            getAssetExtensionInfo(assetDto);


        }


        if (StringUtils.isNotEmpty(owner) && assetDto != null) {
            if (assetDto.getMakerOrderId() != null) {
                MakerOrder makerOrder = new MakerOrder();
                makerOrder.setId(assetDto.getMakerOrderId());
                MakerOrder makerOrder1 = makerOrderService.templateOne(makerOrder);
                assetDto.setLockAssetHash(makerOrder1 == null ? null : makerOrder1.getLockAssetHash());
            }

            Contract contract = new Contract();
            contract.setAddress(assetDto.getContractAddress());
            ContractDto contractDto = contractService.collectionInfo(contract);
            if (contractDto != null) {
                Asset asset = new Asset();
                asset.setSaleStatus(AssetStatus.LIST.getStatus());
                asset.setContractAddress(contractDto.getAddress());
                List<Asset> template = assetService.template(asset);
                if (template != null) {
                    if (CollectionUtils.isEmpty(template)) {
                        contractDto.setNftCount(0L);
                    } else {
                        contractDto.setNftCount(Long.valueOf(template.size()));
                    }

                }
            }
            assetDto.setContractDto(contractDto);

        }
        if (StringUtils.isNotEmpty(nftId)) {
            AssetExtension assetExtension = new AssetExtension();
            assetExtension.setNftId(nftId);
            assetExtension = assetExtensionService.templateOne(assetExtension);
            if (assetExtension != null) {
                assetExtension.setViewCount(assetExtension.getViewCount() == null ? 1L : assetExtension.getViewCount() + 1L);
                assetExtensionService.updateTemplateById(assetExtension);
            }
        }

        return JsonResult.success(assetDto);
    }


    @PostMapping("/tokenInfoByAssetId.json")
    @ApiOperation(value = "Query token info by asset id", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assetId", value = "Asset id", required = true, dataType = "int", paramType = "query"),
    })
    @ResponseBody
    public JsonResult<AssetDto> tokenInfoByAssetId(@RequestParam(required = true) Long assetId) {
        Asset param = new Asset();
        param.setId(assetId);
        param.setDeleted(false);
        AssetDto assetDto = assetService.getTokenInfo(param);
        return JsonResult.success(assetDto);
    }

    @PostMapping("/burn.do")
    @ApiOperation(value = "Burn nft", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenAddress", value = "Token address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftId", value = "Nft id", required = false, dataType = "string", paramType = "query"),
    })
    @ResponseBody
    public JsonResult burn(@RequestParam(required = true) String tokenAddress, @RequestParam(required = false) String nftId) {


        if (StringUtils.isNotEmpty(nftId)) {
            Asset param = new Asset();
            param.setContractAddress(tokenAddress);
            param.setNftId(nftId);
            param.setDeleted(false);
            param.setLevel(AssetLevel.MARKET.intValue());
            param.setSaleStatus(AssetStatus.DELIST.getStatus());
            List<Asset> assetList = assetService.template(param);
            if (assetList != null && assetList.size() > 0) {
                if (assetList.size() != assetList.get(0).getAmount()) {
                    JsonResult.error(Action.BURN_ERROR);
                }
                assetService.burnNft(tokenAddress, nftId);
                AssetExtension assetExtension = new AssetExtension();
                assetExtension.setNftId(nftId);
                assetExtension.setDeleted(true);
                assetExtensionService.updateByNftId(assetExtension);
                Contract contract = contractService.getContractByTokenAddress(tokenAddress);
                contract.setAmount(contract.getAmount() - 1);
                contractService.updateTemplateById(contract);
            } else {
                return JsonResult.error(Action.PARAM_ERROR);
            }
        } else {
            Contract contract = contractService.getContractByTokenAddress(tokenAddress);
            if (contract.getAmount() == 0) {
                contractService.deleteById(contract.getId());
            } else {
                JsonResult.error(Action.BURN_ERROR);
            }
        }
        return JsonResult.success();
    }


    @PostMapping("/history.json")
    @ApiOperation(value = "Nft history", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenAddress", value = "Token address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftId", value = "Nft id", required = true, dataType = "string", paramType = "query"),
    })
    @ResponseBody
    public JsonResult<List<TransferLogDto>> historyhistory(PageFrame pageFrame, @RequestParam(required = true) String tokenAddress, @RequestParam(required = true) String nftId, Integer aboutMe, String owner) {
        TransferLog transferLog = new TransferLog();
        transferLog.setDeleted(false);
        transferLog.setContractAddress(tokenAddress);
        transferLog.setNftId(nftId);
        List<TransferLogDto> pageQuery = transferLogService.nftHistoryPage(transferLog, pageFrame);
        if (aboutMe != null && aboutMe == 1 && owner != null) {
            pageQuery = pageQuery.stream().filter(v -> v.getFrom().equals(owner) || v.getTo().equals(owner)).collect(Collectors.toList());
        }
        return JsonResult.success(pageQuery);
    }


    @PostMapping("/collectionInfo.json")
    @ApiOperation(value = "Nft collection info", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "baseUri", value = "short url", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "tokenAddress", value = "Token address", required = false, dataType = "string", paramType = "query"),
    })
    @ResponseBody
    public JsonResult<ContractDto> collectionInfo(@RequestParam(required = false) String linkUrl, @RequestParam(required = false) String tokenAddress) {

        if (StringUtils.isEmpty(tokenAddress) && StringUtils.isEmpty(linkUrl)) {
            return JsonResult.error(Action.PARAM_ERROR);
        }
        Contract param = new Contract();


        if (StringUtils.isNotEmpty(linkUrl)) {
            Contract contract = new Contract();
            contract.setLinkUrl(linkUrl);
            contract = contractService.templateOne(contract);
            if (contract != null) {
                param.setAddress(contract.getAddress());
            }
        } else {
            param.setAddress(tokenAddress);
        }
        if (StringUtils.isEmpty(param.getAddress())) {
            JsonResult.error(Action.PARAM_ERROR);
        }

        ContractDto collectionInfo = contractService.collectionInfo(param);
        return JsonResult.success(collectionInfo);
    }


    @PostMapping("/transfer.do")
    @ApiOperation(value = "Nft transfer", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sender", value = "Sender wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "senderAccountId", value = "Sender account id", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "recipient", value = "Recipient wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "recipientAccountId", value = "Recipient account id", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "tokenAddress", value = "Nft token address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftId", value = "Nft id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "Transfer amount", required = true, dataType = "int", paramType = "query"),
    })
    @ResponseBody
    public JsonResult transfer(String sender, Long senderAccountId, String recipient, Long recipientAccountId,
                               String tokenAddress, String nftId, Integer amount) {

        //sender
        Asset senderAssetParam = new Asset();
        senderAssetParam.setContractAddress(tokenAddress);
        senderAssetParam.setNftId(nftId);
        senderAssetParam.setOwner(sender);
        senderAssetParam.setAccountId(senderAccountId);
        Asset asset = assetService.templateOne(senderAssetParam);
        if (asset == null || asset.getAmount() < amount) {
            return JsonResult.error(Action.PARAM_ERROR);
        }
        asset.setAmount(asset.getAmount() - amount);
        if (asset.getAmount() <= 0) {
            assetService.forceDeleteById(asset.getId());
        } else {
            assetService.updateTemplateById(asset);
        }

        //recipient
        Asset recipientAssetParam = new Asset();
        recipientAssetParam.setContractAddress(tokenAddress);
        recipientAssetParam.setNftId(nftId);
        recipientAssetParam.setOwner(recipient);
        recipientAssetParam.setAccountId(senderAccountId);
        Asset assetResult = assetService.templateOne(recipientAssetParam);
        if (assetResult == null || asset.getAmount() <= 0) {
            Asset asset1 = new Asset();
            BeanUtils.copyProperties(asset, asset1);
            asset1.setId(null);
            asset1.setOwner(recipient);
            asset1.setAmount(amount);
            asset1.setListAmount(0);
            if (asset.getCreator().equals(asset1.getOwner())) {
                asset1.setLevel(AssetLevel.MARKET.intValue());
            } else {
                asset1.setLevel(AssetLevel.FLEA_MARKET.intValue());
            }
            asset1.setSaleStatus(AssetStatus.DELIST.getStatus());
            asset1.setUpdateTime(new Date());
            if (asset1.getAssetStatus() == null) {
                Contract contractByTokenAddress = contractService.getContractByTokenAddress(asset1.getContractAddress());
                if (contractByTokenAddress != null) {
                    if (ContractAndNftStatus.black.getStatus().equals(contractByTokenAddress.getContractStatus())) {
                        asset1.setAssetStatus(ContractAndNftStatus.link.getStatus());
                    } else {
                        asset1.setAssetStatus(ContractAndNftStatus.normal.getStatus());
                    }
                }

            }
            assetService.save(asset1);
        }

        transferLogService.addLog(Arrays.asList(asset), sender, recipient, "", TxType.NFT_TRANSFER);

        return JsonResult.success();
    }

    @PostMapping("/singleTransfer.do")
    @ApiOperation(value = "Nft transfer", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sender", value = "Sender wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "senderAccountId", value = "Sender account id", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "recipient", value = "Recipient wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "recipientAccountId", value = "Recipient account id", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "assetId", value = "Transfer asset id", required = true, dataType = "long", paramType = "query"),
    })
    @ResponseBody
    public JsonResult singleTransfer(@RequestParam String sender, @RequestParam Long senderAccountId, @RequestParam String recipient, @RequestParam Long recipientAccountId, @RequestParam Long assetId) {


        Asset asset = assetService.single(assetId);
        if (asset == null || !asset.getOwner().equals(sender)) {
            return JsonResult.error(Action.PARAM_ERROR);
        }
        asset.setLevel(AssetLevel.FLEA_MARKET.intValue());
        asset.setOwner(recipient);
        asset.setAccountId(recipientAccountId);
        assetService.updateTemplateById(asset);
        transferLogService.addLog(Arrays.asList(asset), sender, recipient, "", TxType.NFT_TRANSFER);
        assetSync.sync(asset.getContractAddress(), asset.getNftId());
        return JsonResult.success();
    }


    @PostMapping("/getTokenId.json")
    @ApiOperation(value = "Query token id", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "Wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "tokenAddress", value = "Token address", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "nftId", value = "Nft id", required = true, dataType = "string", paramType = "query"),
    })
    @ResponseBody
    public JsonResult<Long> singleTransfer(@RequestParam String owner, @RequestParam String tokenAddress, @RequestParam String nftId) {

        List<NFTData> nftDataList = middlewareService.getNftBalances(owner, tokenAddress);

        if (nftDataList != null) {
            for (NFTData var : nftDataList) {
                if (var.getNftId().equals(nftId)) {
                    return JsonResult.success(var.getTokenId());
                }
            }
        }
        return JsonResult.success();
    }

    @PostMapping("/getMyTokenId.json")
    @ApiOperation(value = "Query token id", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "Wallet address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "filter", required = false, dataType = "string", paramType = "query"),
    })
    @ResponseBody
    public JsonResult<PageQuery<AssetDto>> getMyTokenId(PageFrame pageFrame, @RequestParam String owner, String searchKey, Integer filter, String creator) {
        //filter 1create 2 on uptick 3 onloopring
        if (StringUtils.isEmpty(owner)) {
            return null;
        }
        AssetDto asset = new AssetDto();
        asset.setOwner(owner);
        //byCreate
        asset.setCreator(creator);
        if (filter != null && filter == 0) {
            filter = null;
        }
        PageQuery<AssetDto> myCreatedTokenPage = assetService.getMyTokenPage(asset, pageFrame, searchKey, filter);
        return JsonResult.success(myCreatedTokenPage);
    }


    @PostMapping("/getMyContract.json")
    @ApiOperation(value = "Query contract", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "Wallet address", required = true, dataType = "string", paramType = "query"),
    })
    @ResponseBody
    public JsonResult<PageQuery<ContractDto>> getMyContract(PageFrame pageFrame, @RequestParam String owner, String search, Integer filter) {
        if (StringUtils.isEmpty(owner)) {
            return null;
        }
        ContractDto contract = new ContractDto();
        contract.setOwner(owner);
        if (filter != null && filter == 0) {
            filter = null;
        }
        PageQuery<ContractDto> myCreatedTokenPage = contractService.getMyContractPage(contract, pageFrame, search, filter);

        return JsonResult.success(myCreatedTokenPage);
    }


    private AssetDto getAssetExtensionInfo(AssetDto assetDto) {
        AssetExtension assetExtension = new AssetExtension();
        assetExtension.setNftId(assetDto.getNftId());
        assetExtension = assetExtensionService.templateOne(assetExtension);
        if (assetExtension != null) {
            assetDto.setAssetExtension(assetExtension);
        }
        return assetDto;
    }
}
