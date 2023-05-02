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
package org.uptick.market.service;


import org.beetl.sql.core.engine.PageQuery;
import org.uptick.market.dto.ContractDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.Contract;

import java.util.List;

public interface ContractService extends BaseService<Contract> {


    /**
     * query contract by nft address
     *
     * @param tokenAddress
     * @return
     */
    Contract getContractByTokenAddress(String tokenAddress);


    /**
     * @param pageFrame
     * @param param
     * @return
     */
    PageQuery<ContractDto> getContractPage(PageFrame pageFrame, Contract param);


    /**
     * query collection info
     *
     * @param contract
     * @return
     */
    ContractDto collectionInfo(Contract contract);

    /**
     * query collection by baseUrl
     *
     * @param baseUrl
     * @return
     */
    Contract getByBaseUrl(String baseUrl);


    /**
     * Query nft creator number
     *
     * @return
     */
    Long creators();

    /**
     * Query my contracts
     *
     * @param contract
     * @param pageFrame
     * @param search
     * @param filter
     * @return
     */
    PageQuery<ContractDto> getMyContractPage(ContractDto contract, PageFrame pageFrame, String search, Integer filter);

    /**
     * Query for others' contracts
     *
     * @param pageFrame
     * @param contractDto
     * @return
     */
    PageQuery<ContractDto> getOtherCollectionInfo(PageFrame pageFrame, ContractDto contractDto);

    /**
     * Query launchPad
     *
     * @param pageFrame
     * @param contractDto
     * @return
     */
    PageQuery<ContractDto> getCollectionInfo(PageFrame pageFrame, ContractDto contractDto);

    Contract saveContract(Contract t) ;
}
