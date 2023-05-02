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


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.uptick.market.denum.*;
import org.uptick.market.dto.AssetDto;
import org.uptick.market.dto.ContractDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.AssetExtension;
import org.uptick.market.entity.Contract;
import org.uptick.market.entity.MakerOrder;
import org.uptick.market.service.AssetExtensionService;
import org.uptick.market.service.AssetService;
import org.uptick.market.service.ContractService;
import org.uptick.market.service.MakerOrderService;
import org.uptick.market.util.JsonResult;
import org.uptick.market.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RestController
@RequestMapping(org.uptick.market.constant.Api.VERSION + "/market")
@Api(tags = "Market api")
public class MarketController {


    @Autowired
    private ContractService contractService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetExtensionService assetExtensionService;

    @Autowired
    private MakerOrderService makerOrderService;

    @PostMapping("/getMarketplace.json")
    @ApiOperation(value = "Query contract", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "Wallet address", required = true, dataType = "string", paramType = "query"),
    })
    @ResponseBody
    public JsonResult<PageQuery> getMarketplace(PageFrame pageFrame,
                                                String search,
                                                String marketTokenType,
                                                Integer sort,
                                                Integer nftShowType,
                                                String address
    ) {
        if (!StringUtils.isEmpty(marketTokenType) && marketTokenType.equals("All")) {
            marketTokenType = null;
        }
        PageQuery marketInfoPage = new PageQuery();
        //collection
        if (nftShowType != null && nftShowType == 3) {
            ContractDto contractDto = new ContractDto();
            contractDto.setAddress(address);
            contractDto.setSearch(search);
            contractDto.setSortType(sort);
            marketInfoPage = contractService.getCollectionInfo(pageFrame, contractDto);
        } else {
            String nftType = null;
            if (nftShowType != null) {
                if (nftShowType == 1) {
                    nftType = "ERC721";
                }
                if (nftShowType == 2) {
                    nftType = "ERC1155";
                }
            }
            marketInfoPage = assetService.getmarketPlace(pageFrame, search, marketTokenType, sort, nftType, address);
        }

        return JsonResult.success(marketInfoPage);
    }

    @PostMapping("/nftInfo.json")
    @ApiOperation(value = "Query nft market info", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenAddress", value = "Nft token address", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nftId", value = "Nft token address", required = true, dataType = "string", paramType = "query")

    })
    @ResponseBody
    public JsonResult<AssetDto> nftInfo(String nftId, String tokenAddress) {
        Asset param = new Asset();
        param.setDeleted(false);
        param.setLevel(AssetLevel.MARKET.intValue());
        param.setNftId(nftId);
        param.setContractAddress(tokenAddress);
        param.setSaleStatus(AssetStatus.LIST.getStatus());
        AssetDto assetDto = assetService.getMarketInfo(param);
        return JsonResult.success(assetDto);
    }

    @PostMapping("/list.json")
    @ApiOperation(value = "Query nft market list", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenType", value = "Pay token type(ETH、USDT、DAI、LRC、USDC)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "Search", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "sort(1:create time,2:random)", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "tokenAddress", value = "Nft token address", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "baseUri", value = "shortUrl", required = true, dataType = "string", paramType = "query"),

    })
    @ResponseBody
    public JsonResult<PageQuery<AssetDto>> list(PageFrame pageFrame, String search,
                                                @RequestParam(required = true) String tokenAddress, @RequestParam Integer sourceType, String owner) {

        if (StringUtils.isEmpty(tokenAddress)) {
            return null;
        }

        if (SourceType.FIRST_PUBLISH.intValue().equals(sourceType)) {
            AssetDto asset = new AssetDto();
            asset.setContractAddress(tokenAddress);
//            asset.setChannel("Uptick-Loopring");
            asset.setSearch(search);
            List<AssetDto> template = assetService.templateDto(asset);


            if (!StringUtils.isEmpty(search) && !CollectionUtils.isEmpty(template)) {
                template = template.stream().filter(v -> v.getOwner().contains(search) || v.getName().contains(search)).collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(template)) {
                Map<String, List<AssetDto>> collect = template.stream().collect(Collectors.groupingBy(v -> v.getNftId()));
                List<AssetDto> assetDtoList = new ArrayList<>();
                for (String nftId : collect.keySet()) {
                    List<AssetDto> assets = collect.get(nftId);
                    if (!CollectionUtils.isEmpty(assets)) {
                        List<AssetDto> assetListLevel1 = assets.stream().filter(v -> v.getLevel().equals(AssetLevel.MARKET.intValue()) && AssetStatus.LIST.getStatus().equals(v.getSaleStatus())).collect(Collectors.toList());
                        AssetDto assetDto = new AssetDto();
                        //level1
                        if (!CollectionUtils.isEmpty(assetListLevel1)) {
                            assetDto = assetListLevel1.get(0);
                            assetDtoList.add(assetDto);
                        } else {
                            List<AssetDto> assetLevel2 = assets.stream().filter(v -> v.getLevel().equals(AssetLevel.FLEA_MARKET.intValue())).collect(Collectors.toList());
                            List<Asset> collect2 = assets.stream().filter(v -> v.getLevel().equals(AssetLevel.MARKET.intValue())).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(assetLevel2) && CollectionUtils.isEmpty(collect2)) {
                                assetDto = assets.get(0);
                                assetDto.setLevel(AssetLevel.MARKET.intValue());
                                assetDto.setAmount(0);
                                assetDto.setListAmount(0);
                                assetDto.setSaleStatus(AssetStatus.DELIST.getStatus());
                                assetDto.setSoldOut(true);
                                assetDto.setOwner(assetDto.getCreator());
                                assetDto.setMakerType(null);
                                getAssetExtensionInfo(assetDto);
                                assetDtoList.add(assetDto);

                            }
                        }
                    }
                }
                PageQuery<AssetDto> pageQuery = pageQuery(pageFrame, assetDtoList);
                return JsonResult.success(pageQuery);
            }
        } else if (SourceType.DETAIL_LINK.intValue().equals(sourceType)) {
            AssetDto param = new AssetDto();
            param.setContractAddress(tokenAddress);
            param.setSaleStatus(AssetStatus.LIST.getStatus());
            param.setSearch(search);
            List<AssetDto> assetDtoList = assetService.templateDto(param);
            PageQuery<AssetDto> pageQuery = pageQuery(pageFrame, assetDtoList);
            return JsonResult.success(pageQuery);
        } else if (SourceType.ON_SALE.intValue().equals(sourceType)) {
            AssetDto param = new AssetDto();
            param.setContractAddress(tokenAddress);
            param.setOwner(owner);
            param.setSaleStatus(AssetStatus.LIST.getStatus());
            param.setSearch(search);
            List<AssetDto> template = assetService.templateDto(param);
            PageQuery<AssetDto> pageQuery = pageQuery(pageFrame, template);
            return JsonResult.success(pageQuery);
        } else if (SourceType.OFF_SALE.intValue().equals(sourceType)) {
            AssetDto param = new AssetDto();
            param.setContractAddress(tokenAddress);
            param.setOwner(owner);
            param.setSearch(search);
            param.setSaleStatus(AssetStatus.DELIST.getStatus());
            List<AssetDto> template = assetService.templateDto(param);
            PageQuery<AssetDto> pageQuery = pageQuery(pageFrame, template);
            return JsonResult.success(pageQuery);
        } else if (SourceType.OWNER.intValue().equals(sourceType)) {
            //owner
            AssetDto param = new AssetDto();
            param.setContractAddress(tokenAddress);
            param.setOwner(owner);
            param.setSearch(search);
            List<AssetDto> template = assetService.templateDto(param);
            Map<String, List<AssetDto>> collect1 = template.stream().collect(Collectors.groupingBy(v -> v.getNftId()));

            //sellout = true
            AssetDto assetDtoReq = new AssetDto();
            assetDtoReq.setContractAddress(tokenAddress);
            assetDtoReq.setCreator(owner);
            assetDtoReq.setSearch(search);
            List<AssetDto> assetDtoList = assetService.templateDto(assetDtoReq);
            if (!CollectionUtils.isEmpty(assetDtoList)) {
                Set<String> collect2 = assetDtoList.stream().map(v -> v.getNftId()).collect(Collectors.toSet());
                for (String nftId : collect2) {
                    List<AssetDto> assetDtoList1 = collect1.get(nftId);
                    if (CollectionUtils.isEmpty(assetDtoList1)) {
                        Asset asset = new Asset();
                        asset.setNftId(nftId);
                        List<Asset> template1 = assetService.template(asset);
                        AssetDto assetDto = new AssetDto();
                        BeanUtils.copyProperties(template1.get(0), assetDto);
                        assetDto.setAmount(0);
                        assetDto.setListAmount(0);
                        assetDto.setSaleStatus(AssetStatus.DELIST.getStatus());
                        assetDto.setSoldOut(true);
                        assetDto.setOwner(assetDto.getCreator());
                        getAssetExtensionInfo(assetDto);
                        template.add(assetDto);
                    }
                }
            }
            PageQuery<AssetDto> pageQuery = pageQuery(pageFrame, template);
            for (AssetDto assetDto : pageQuery.getList()) {
                MakerOrder makerOrder = new MakerOrder();
                makerOrder.setId(assetDto.getMakerOrderId());
                makerOrder = makerOrderService.templateOne(makerOrder);
                assetDto.setLockAssetHash(makerOrder == null ? null : makerOrder.getLockAssetHash());
                AssetExtension assetExtension = new AssetExtension();
                assetExtension.setNftId(assetDto.getNftId());
                assetExtension = assetExtensionService.templateOne(assetExtension);
                Boolean isBurn = false;
                if (assetExtension == null) {
                    assetExtension = new AssetExtension();
                }
                if (Long.valueOf(assetDto.getAmount()).equals(assetExtension.getTotalInventory())
                        && AssetLevel.MARKET.intValue().equals(assetDto.getLevel())
                        && AssetStatus.DELIST.getStatus().equals(assetDto.getSaleStatus())
                ) {
                    isBurn = true;
                }
                assetDto.setIsBurn(isBurn);
            }
            return JsonResult.success(pageQuery);
        }

        return JsonResult.success(new PageQuery<>());
    }

    private AssetDto getAssetExtensionInfo(AssetDto assetDto) {
        AssetExtension assetExtension = new AssetExtension();
        assetExtension.setNftId(assetDto.getNftId());
        assetExtension = assetExtensionService.templateOne(assetExtension);
        if (assetExtension != null) {
            assetDto.setAssetExtension(assetExtension);
            if (AssetLevel.MARKET.intValue().equals(assetDto.getLevel())) {
                assetDto.setCreateTime(assetExtension.getCreateTime());
            }
        }
        return assetDto;
    }

    public PageQuery<AssetDto> pageQuery(PageFrame pageFrame, List<AssetDto> assetDtoList) {
        int pageSize = pageFrame.getPageSize();
        int pageNumber = pageFrame.getPageNumber();
        PageQuery<AssetDto> pageQuery = new PageQuery<>();
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setPageSize(pageSize);
        List<AssetDto> assetDtoResult = new ArrayList<>();
        if (pageNumber == 1) {
            for (int i = 0; i < pageSize; i++) {
                int length = assetDtoList.size();
                if (length == i) {
                    break;
                }
                assetDtoResult.add(assetDtoList.get(i));
            }
            pageQuery.setList(assetDtoResult);
        } else {
            Integer s = (pageNumber - 1) * pageSize;
            Integer number = Integer.valueOf(s);
            Integer size = number + pageSize;
            for (int i = number; i < size; i++) {
                int length = assetDtoList.size();
                if (length <= i) {
                    break;
                }
                assetDtoResult.add(assetDtoList.get(i));
            }
        }
        Collections.sort(assetDtoResult, new Comparator<AssetDto>() {
            public int compare(AssetDto a, AssetDto b) {
                return b.getCreateTime().compareTo(a.getCreateTime());
            }
        });
        pageQuery.setList(assetDtoResult);
        pageQuery.setTotalRow(assetDtoList.size());
        return pageQuery;

    }
}

