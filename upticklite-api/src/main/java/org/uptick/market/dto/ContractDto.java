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

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.uptick.market.entity.Contract;

import java.io.Serializable;


@Setter
@Getter
public class ContractDto extends Contract implements Serializable {

    private Integer listAmount;

    private String ownerName;

    private String ownerPhoto;

    private String creatorName;

    private String creatorPhoto;

    private String search;

    private Integer filter;

    private Long nftCount;

    private Integer contractStatus;

    private Integer sortType;

}