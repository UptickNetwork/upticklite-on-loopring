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

import java.util.Date;

/**
 * 2022-04-25
 */

@Table(name = "taker_order")
@Data
@EqualsAndHashCode(callSuper = true)
public class TakerOrder extends CrudEntity {

    private static final long serialVersionUID = 123456789L;

    private Long id;
    private Boolean deleted;
    private Integer weight;
    private String contractAddress;
    private String json;
    private String makerJson;
    private Long makerOrderId;
    private String signature;
    private String signer;
    private String nftId;
    private Long validUntil;
    private Date createTime;
    private Date updateTime;


}
