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

package org.uptick.market.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beetl.sql.core.annotatoin.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 2022-04-12
 */

@Table(name = "order_details")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderDetails extends CrudEntity {

    private static final long serialVersionUID = 123456789L;


    private Long id;
    private Boolean deleted;
    private Integer level;
    private Integer orderMarketType;
    private Integer orderStatus;
    private Boolean reconciliationCreator;
    private Boolean reconciliationLoopring;
    private Integer weight;
    private Long assetId;
    private BigDecimal commissionCharge;
    private String contractAddress;
    private String json;
    private String nftId;
    private String orderNo;
    private BigDecimal originalRoyaltyPercentage;
    private BigDecimal payPrice;
    private BigDecimal platformFee;
    private String recipient;
    private BigDecimal royaltyPercentage;
    private String sender;
    private String tokenType;
    private String txId;
    private Date createTime;
    private Date reconciliationCreatorTime;
    private Date reconciliationLoopringTime;
    private Date updateTime;


}
