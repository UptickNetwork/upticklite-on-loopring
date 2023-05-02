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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.uptick.market.denum.Action;
import org.uptick.market.entity.User;
import org.uptick.market.service.UserService;
import org.uptick.market.task.AssetSync;
import org.uptick.market.util.JsonResult;
import org.uptick.market.util.StringUtils;

import java.util.HashSet;

@Slf4j
@Component
@RestController
@RequestMapping(org.uptick.market.constant.Api.VERSION + "/user")
@Api(tags = "User info")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private AssetSync assetSync;

    public static HashSet<String> SYNC_ADDRESS = new HashSet();

    @PostMapping("/save.do")
    @ApiOperation(value = "Save or update user info", notes = "")
    @ResponseBody
    public JsonResult save(User user) {
        if (StringUtils.isEmpty(user.getAddress())) {
            return JsonResult.error(Action.PARAM_ERROR);
        }

        User userInfo = userService.getUserByAddress(user.getAddress());
        if (userInfo != null) {
            user.setId(userInfo.getId());
            userService.updateTemplateById(user);
        } else {
            userService.save(user);
        }

        return JsonResult.success();
    }


    @ApiOperation(value = "get user info", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "waller address", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/info.json")
    @ResponseBody
    public JsonResult<User> info(@RequestParam String address) {
        User user = userService.getUserByAddress(address);
        if (user == null) {
            user = new User();
            user.setAddress(address);
            JsonResult.success(user);
        }
        return JsonResult.success(user);
    }

    @ApiOperation(value = "Update user nft info", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "waller address", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/updateData.do")
    @ResponseBody
    public JsonResult<User> updateData(@RequestParam String address) {
       if(StringUtils.isEmpty(address)) {
           return JsonResult.error(Action.PARAM_ERROR);
       }
        User user = userService.getUserByAddress(address);
        if (user != null) {
            if(user.getAccountStatus()==null||user.getAccountStatus()!=3){
                return JsonResult.error(Action.PARAM_ERROR);
            }

            if (!SYNC_ADDRESS.contains(address)) {
                assetSync.syncDataByAddress(user.getAddress(), null);
            }

        }
        return JsonResult.success();
    }

}