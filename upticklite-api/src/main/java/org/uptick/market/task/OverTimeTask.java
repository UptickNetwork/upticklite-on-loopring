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


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.uptick.market.denum.AssetStatus;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.MakerOrder;
import org.uptick.market.service.AssetService;
import org.uptick.market.service.MakerOrderService;
import org.uptick.market.util.SpringBeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OverTimeTask {

    @Scheduled(cron = "0 0/60 * * * ?")
    private void ss() {

        try {
            MakerOrderService makerOrderService = SpringBeanUtils.getBean(MakerOrderService.class);
            AssetService assetService = SpringBeanUtils.getBean(AssetService.class);

            MakerOrder makerOrder0 = new MakerOrder();
            makerOrder0.setDeleted(false);
            List<MakerOrder> list = makerOrderService.getDealMarkerOrderList(makerOrder0);
            if (list != null && list.size() > 0) {
                System.out.println("totlapage" + list.size());
                List<Asset> assetList = new ArrayList<>();
                long currentTime = System.currentTimeMillis() / 1000 + (15 * 86400);
                for (MakerOrder makerOrder : list) {
                    JSONObject makerOrderJsonObject = JSON.parseObject(makerOrder.getMakerJson());
                    makerOrder.setValidUntil(makerOrderJsonObject.getLong("validUntil"));
                    if (makerOrder.getValidUntil() < currentTime) {
                        makerOrder.setDeleted(true);
                        Asset asset = new Asset();
                        asset.setContractAddress(makerOrder.getContractAddress());
                        asset.setNftId(makerOrder.getNftId());
                        asset.setSaleStatus(AssetStatus.LIST.getStatus());
                        asset.setMarketOwner(makerOrder.getSigner());
                        asset = assetService.templateOne(asset);
                        if (asset == null) {
                            continue;
                        }

                        if (asset.getListAmount() > 0) {
                            //auction asset
                            asset.setMarketOwner(null);
                            asset.setMarketTime(null);
                            asset.setMarketPrice(null);
                            asset.setUpdateTime(new Date());
                            asset.setMakerOrderId(null);
                            asset.setSaleStatus(AssetStatus.DELIST.getStatus());
                            asset.setListAmount(0);
                            asset.setMakerType(null);
                            asset.setMarketTokenType(null);
                            asset.setUpdateTime(new Date());
                            assetList.add(asset);
                        }

                    }
                }
                makerOrderService.batchUpdate(list);
                assetService.batchUpdate(assetList);
            }
        } catch (Exception e) {
            System.out.println("Error:  " + e.toString());
        }
    }
}
