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
package org.uptick.market.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.uptick.market.dao.MakerOrderDao;
import org.uptick.market.dto.ContractDto;
import org.uptick.market.entity.MakerOrder;
import org.uptick.market.service.MakerOrderService;

import java.util.List;


@SuppressWarnings("unchecked")
@Slf4j
@Service
public class MakerOrderServiceImpl extends BaseServiceImpl<MakerOrder, MakerOrderDao> implements MakerOrderService {

    @Override
    public List<MakerOrder> getDealMarkerOrderList(MakerOrder markerOrder) {
        return this.getEntityDao().getDealMarkerOrderList(markerOrder);
    }

}
