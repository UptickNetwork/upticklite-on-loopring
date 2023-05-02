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
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.stereotype.Service;
import org.uptick.market.dao.OrderInfoDao;
import org.uptick.market.dto.OrderDataDto;
import org.uptick.market.dto.OrderInfoDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.OrderInfo;
import org.uptick.market.service.OrderInfoService;

import java.util.List;


@SuppressWarnings("unchecked")
@Slf4j
@Service
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfo, OrderInfoDao> implements OrderInfoService {


    @Override
    public PageQuery<OrderInfoDto> getOrderPage(OrderInfo info, PageFrame pageFrame) {
        PageQuery<OrderInfo> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(info);
        return this.getEntityDao().getOrderPage(pageQuery);
    }

    @Override
    public List<OrderDataDto> getOrderData(OrderInfo orderInfo) {
        return this.getEntityDao().getOrderData(orderInfo);
    }
}
