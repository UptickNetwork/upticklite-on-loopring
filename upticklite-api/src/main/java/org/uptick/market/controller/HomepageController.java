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
import org.uptick.market.dto.AssetDto;
import org.uptick.market.dto.ContractDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;
import org.uptick.market.service.AssetService;
import org.uptick.market.service.ContractService;
import org.uptick.market.util.JsonResult;
import org.uptick.market.util.StringUtils;

import java.util.List;

@Slf4j
@Component
@RestController
@RequestMapping(org.uptick.market.constant.Api.VERSION + "/homepage")
@Api(tags = "Homepage api")
public class HomepageController {


    @Autowired
    private ContractService contractService;

    @Autowired
    private AssetService assetService;

    @PostMapping("/token.json")
    @ApiOperation(value = "Query collection info", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "creator", value = "Creator wallet address", required = true, dataType = "string", paramType = "query"),

    })
    @ResponseBody
    public JsonResult<PageQuery<AssetDto>> token(PageFrame pageFrame, String creator, Integer filter) {
        Asset asset = new Asset();
        asset.setDeleted(false);
        asset.setOwner(creator);
        PageQuery<AssetDto> pageQuery = assetService.getOtherTokenPage(asset, pageFrame, null, filter);
        return JsonResult.success(pageQuery);
    }

    @PostMapping("/collection.json")
    @ApiOperation(value = "Query collection info", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "creator", value = "Creator wallet address", required = true, dataType = "string", paramType = "query"),

    })
    @ResponseBody
    public JsonResult<PageQuery<ContractDto>> collection(PageFrame pageFrame, @RequestParam String creator, String search, Integer sortType, Integer filter) {
        ContractDto contractDto = new ContractDto();
        contractDto.setDeleted(false);
        contractDto.setOwner(creator);
        contractDto.setSearch(search);
        contractDto.setSortType(sortType);
        contractDto.setFilter(filter);
        PageQuery<ContractDto> pageQuery = contractService.getOtherCollectionInfo(pageFrame, contractDto);
        return JsonResult.success(pageQuery);
    }
}
