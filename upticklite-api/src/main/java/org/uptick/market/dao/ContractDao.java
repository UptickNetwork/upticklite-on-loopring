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
import org.uptick.market.dto.ContractDto;
import org.uptick.market.entity.Contract;


/**
 * mapper 2022-04-19 16:28:10
 */
@Component
public interface ContractDao extends BaseDao<Contract> {


    PageQuery<ContractDto> collection(PageQuery pageQuery);


    ContractDto collectionInfo(Contract contract);


    PageQuery<ContractDto> contractPage(PageQuery pageQuery);


    Long creators();

    PageQuery<ContractDto> getMyContractPage(PageQuery pageQuery);

    PageQuery<ContractDto> getOtherCollectionInfo(PageQuery pageQuery);

    PageQuery<ContractDto> getCollectionInfo(PageQuery pageQuery);

}
