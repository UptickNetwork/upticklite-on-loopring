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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;


@Table(name = "contract")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class Contract extends CrudEntity {

    private static final long serialVersionUID = 123456789L;


    @ApiModelProperty(required = true, value = "NFT token address")
    private String address;

    @ApiModelProperty(required = true, value = "NFT img url")
    private String imgUrl;


    @ApiModelProperty(required = true, value = "NFT name")
    private String name;

    @ApiModelProperty(required = true, value = "NFT  metadate")
    private String collectionMetadata;

    @ApiModelProperty(required = true, value = "NFT base url")
    private String baseUrl;

    @ApiModelProperty(required = true, value = "NFT owner")
    private String owner;

    @ApiModelProperty(required = false, value = "NFT description")
    private String description;

    @ApiModelProperty(hidden = true)
    private Integer amount;

    @ApiModelProperty(hidden = true)
    private String linkUrl;

    private Boolean recommendDisabled;
    private Date recommendStartTime;
    private Date recommendStopTime;

    private Integer contractStatus;


}
