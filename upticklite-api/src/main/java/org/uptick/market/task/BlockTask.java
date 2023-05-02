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
package org.uptick.market.task;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.uptick.market.denum.AssetLevel;
import org.uptick.market.denum.AssetStatus;
import org.uptick.market.denum.TxType;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.DataMap;
import org.uptick.market.middleware.MiddlewareService;
import org.uptick.market.service.AssetService;
import org.uptick.market.service.DataMapService;
import org.uptick.market.service.TransferLogService;

import java.util.*;

import static org.uptick.market.constant.Key.BLOCK_ID;

@Component
public class BlockTask {


    @Autowired
    private MiddlewareService middlewareService;


    @Autowired
    private DataMapService dataMapService;

    @Autowired
    private AssetService assetService;


    @Autowired
    private AssetSync assetSync;

    @Scheduled(cron = "0 0/1 * * * ?")
    private void ss() {

        DataMap dataMap = dataMapService.findOneByKey(BLOCK_ID);
        if (dataMap != null) {
            String id = dataMap.getValue();
            JSONObject jsonObject = middlewareService.getBlock(id);
            if (jsonObject == null) {
                return;
            }

            if (jsonObject.getJSONObject("data") != null) {

                JSONArray transactions = jsonObject.getJSONObject("data").getJSONArray("transactions");
                if (transactions != null) {
                    for (int i = 0; i < transactions.size(); i++) {
                        JSONObject object = transactions.getJSONObject(i);
                        String type = object.getString("txType");
                        if (type.equals("Transfer")) {

                            JSONObject token = object.getJSONObject("token");
                            String nftData = token.getString("nftData");

                            Asset param = new Asset();
                            param.setNftData(nftData);
                            param = assetService.templateOne(param);
                            if (param != null) {
                                assetSync.sync(param.getContractAddress(), param.getNftId());

                            }
                        }
                    }
                }
                Integer blockId = Integer.valueOf(id);
                blockId = blockId + 1;
                dataMapService.updateValueByKey(BLOCK_ID, blockId.toString());

            }


        }

    }
}
