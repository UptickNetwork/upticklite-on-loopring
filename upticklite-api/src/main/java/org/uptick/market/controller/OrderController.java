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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.uptick.market.denum.AssetLevel;
import org.uptick.market.denum.AssetStatus;
import org.uptick.market.denum.OrderStatus;
import org.uptick.market.dto.AssetDto;
import org.uptick.market.dto.OrderInfoDto;
import org.uptick.market.dto.OrderStatementsDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.AssetExtension;
import org.uptick.market.entity.OrderDetails;
import org.uptick.market.entity.OrderInfo;
import org.uptick.market.service.AssetExtensionService;
import org.uptick.market.service.AssetService;
import org.uptick.market.service.OrderDetailsService;
import org.uptick.market.service.OrderInfoService;
import org.uptick.market.util.JsonResult;
import org.uptick.market.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RestController
@RequestMapping(org.uptick.market.constant.Api.VERSION + "/order")
@Api(tags = "Order  api")
public class OrderController {


    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private AssetExtensionService assetExtensionService;


    @Autowired
    private AssetService assetService;

    @Autowired
    private OrderDetailsService orderDetailsService;


    @PostMapping("/list.json")
    @ApiOperation(value = "Order list", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "User wallet address", required = true, dataType = "string", paramType = "query"),

    })
    @ResponseBody
    public JsonResult<PageQuery<OrderInfoDto>> list(PageFrame pageFrame, @RequestParam String owner) {
        if (StringUtils.isEmpty(owner)) {
            return null;
        }
        OrderInfo param = new OrderInfo();
        param.setDeleted(false);
        param.setRecipient(owner);
        PageQuery<OrderInfoDto> pageQuery = orderInfoService.getOrderPage(param, pageFrame);
        return JsonResult.success(pageQuery);
    }


    @PostMapping("/statements.json")
    @ApiOperation(value = "Order statements", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenAddress", value = "Token address", required = true, dataType = "string", paramType = "query"),

    })
    @ResponseBody
    public JsonResult<OrderStatementsDto> statements(PageFrame pageFrame, @RequestParam String tokenAddress, String nftId) {


        OrderStatementsDto statementsDto = new OrderStatementsDto();

        Asset param1 = new Asset();
        param1.setDeleted(false);
        param1.setContractAddress(tokenAddress);
        param1.setNftId(nftId);
        param1.setLevel(AssetLevel.MARKET.intValue());
        Asset asset = assetService.templateOne(param1);
        AssetExtension assetExtension = new AssetExtension();
        assetExtension.setNftId(nftId);
        assetExtension = assetExtensionService.templateOne(assetExtension);
        statementsDto.setAssetExtension(assetExtension);
        if (asset == null) {
            statementsDto.setAmount(0L);
            statementsDto.setListAmount(0L);
        } else {
            statementsDto.setAmount(Long.valueOf(asset.getAmount()));
            statementsDto.setListAmount(Long.valueOf(asset.getListAmount()));
        }

        OrderDetails param2 = new OrderDetails();
        param2.setDeleted(false);
        param2.setContractAddress(tokenAddress);
        param2.setLevel(AssetLevel.MARKET.intValue());
        param2.setOrderStatus(OrderStatus.SUCCESS.intValue());
        param2.setNftId(nftId);
        Long saleAmount = orderDetailsService.templateCount(param2);
        statementsDto.setSaleAmount(saleAmount);

        OrderInfo param3 = new OrderInfo();
        param3.setDeleted(false);
        param3.setContractAddress(tokenAddress);
        param3.setLevel(AssetLevel.MARKET.intValue());
        param3.setOrderStatus(OrderStatus.SUCCESS.intValue());
        param3.setNftId(nftId);
        PageQuery<OrderInfoDto> pageQuery = orderInfoService.getOrderPage(param3, pageFrame);
        statementsDto.setOrderList(pageQuery.getList());
        return JsonResult.success(statementsDto);
    }

}
