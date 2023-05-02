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
package org.uptick.market.dao;


import org.beetl.sql.core.engine.PageQuery;
import org.springframework.stereotype.Component;
import org.uptick.market.dto.OrderDataDto;
import org.uptick.market.dto.OrderInfoDto;
import org.uptick.market.entity.OrderInfo;

import java.util.List;


/**
 * mapper 2022-04-12 14:28:17
 */
@Component
public interface OrderInfoDao extends BaseDao<OrderInfo> {

    PageQuery<OrderInfoDto> getOrderPage(PageQuery<OrderInfo> pageQuery);

    List<OrderDataDto> getOrderData(OrderInfo orderInfo);

}
