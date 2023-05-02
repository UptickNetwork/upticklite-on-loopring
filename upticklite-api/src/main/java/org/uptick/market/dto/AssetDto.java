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
package org.uptick.market.dto;

import lombok.Getter;
import lombok.Setter;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.AssetExtension;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AssetDto extends Asset implements Serializable {

    private Integer listAmount;

    private Integer haveAmount;

    private String ownerName;

    private String ownerPhoto;

    private String creatorName;

    private String creatorPhoto;

    private Boolean soldOut = false;

    private Boolean isBurn;

    private String tokenName;

    private String tokenImgUrl;

    private String search;

    private Integer filter;

    private AssetExtension assetExtension;

    private String lockAssetHash;

    private List<String> notInNftId;

    private ContractDto contractDto;

    private Integer sortType;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer saleModel;

    private Integer qualification;

    private Integer copyright;

    private String tokenType;

    private Integer orderMarketType;

    private BigDecimal orderPrice;

    private String orderTokenType;

    private String exNftId;

}
