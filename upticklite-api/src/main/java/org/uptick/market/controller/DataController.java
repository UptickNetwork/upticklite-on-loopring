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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.uptick.market.denum.AssetLevel;
import org.uptick.market.denum.AssetStatus;
import org.uptick.market.dto.AssetDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.DataMap;
import org.uptick.market.service.DataMapService;
import org.uptick.market.util.JsonResult;
import org.uptick.market.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RestController
@RequestMapping(org.uptick.market.constant.Api.VERSION + "/data")
@Api(tags = "Data api")
public class DataController {

    @Autowired
    private DataMapService dataMapService;

    @PostMapping("/token.json")
    @ApiOperation(value = "Query pay token type", notes = "")
    @ResponseBody
    public JsonResult token() {

        Resource resource = new ClassPathResource("token.json");
        try {
            String json = IOUtils.toString(resource.getInputStream(), "UTF-8");
            JSONObject jsonObject = JSON.parseObject(json);
            return JsonResult.success(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JsonResult.success();
    }

    @PostMapping("/dataMap.json")
    @ApiOperation(value = "Query pay token type", notes = "")
    @ResponseBody
    public JsonResult dataMap(String keys) {
        try {
            List<DataMap> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(keys)) {
                String[] split = keys.split(",");
                List<String> strings = Arrays.asList(split);
                for (String key : strings) {
                    DataMap oneByKey = dataMapService.findOneByKey(key);
                    if (oneByKey != null) {
                        list.add(oneByKey);
                    }
                }
            }
            return JsonResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.success();
    }
}
