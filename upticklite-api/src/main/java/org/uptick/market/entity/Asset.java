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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 2022-04-12
 */

@Table(name = "asset")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class Asset extends CrudEntity {

    private static final long serialVersionUID = 123456789L;


    @ApiModelProperty(hidden = true)
    private Integer level;

    @ApiModelProperty(hidden = true)
    private Integer saleStatus;

    @ApiModelProperty(required = true, value = "NFT token address")
    private String contractAddress;

    @ApiModelProperty(required = false, value = "NFT token id description")
    private String description;

    @ApiModelProperty(hidden = true)
    private String marketOwner;

    @ApiModelProperty(hidden = true)
    private BigDecimal marketPrice;

    @ApiModelProperty(required = true, value = "NFT token id name")
    private String name;

    @ApiModelProperty(required = true, value = "NFT token id owner")
    private String owner;

    @ApiModelProperty(hidden = true)
    private Date marketTime;

    @ApiModelProperty(required = true, value = "NFT token id img url")
    private String imgUrl;

    @ApiModelProperty(required = true, value = "NFT token data")
    private String nftData;

    @ApiModelProperty(required = true, value = "NFT id")
    private String nftId;

    @ApiModelProperty(required = true, value = "account id")
    private Long accountId;


    @ApiModelProperty(hidden = true)
    private Long makerOrderId;

    @ApiModelProperty(required = true, value = "nft token amount")
    private Integer amount;

    @ApiModelProperty(hidden = true)
    private String creator;

    @ApiModelProperty(hidden = true)
    private String marketTokenType;

    @ApiModelProperty(hidden = true)
    private Integer listAmount;

    @ApiModelProperty(hidden = true)
    private Integer makerType;

    @ApiModelProperty(hidden = true)
    private String nftType;

    @ApiModelProperty(hidden = true)
    private String channel;

    @ApiModelProperty(hidden = true)
    private Integer originalRoyaltyPercentage;

    @ApiModelProperty(hidden = true)
    private Integer royaltyPercentage;

    @ApiModelProperty(hidden = true)
    private String metadataUrl;

    @ApiModelProperty(hidden = true)
    private String properties;

    @ApiModelProperty(hidden = true)
    private String lincenseCode;

    @ApiModelProperty
    private Integer assetStatus;

}
