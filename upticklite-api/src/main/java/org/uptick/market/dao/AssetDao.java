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


import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.stereotype.Component;
import org.uptick.market.dto.AssetDto;

import org.uptick.market.dto.ContractDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;

import java.util.List;


/**
 * mapper 2022-04-12 14:28:17
 */
@Component
public interface AssetDao extends BaseDao<Asset> {


    PageQuery<AssetDto> getMyCreatedListPage(PageQuery<Asset> pageQuery);

    PageQuery<AssetDto> getMyOwnListPage(PageQuery pageQuery);

    PageQuery<AssetDto> getMyOwnByCreatorListPage(PageQuery pageQuery);

    AssetDto getMarketInfo(Asset asset);

    ContractDto getContractList(Asset param);

    AssetDto getTokenInfo(Asset param);

    PageQuery<AssetDto> getMarketListPage(PageQuery<Asset> pageQuery);

    PageQuery<AssetDto> getFleaMarketListPage(PageQuery<Asset> pageQuery);

    PageQuery<AssetDto> getHomepageListPage(PageQuery<Asset> pageQuery);

    int delNftByNftId(@Param("contractAddress") String contractAddress, @Param("nftId") String nftId);

    Long getNftNumber();

    PageQuery<AssetDto> getMyCreatedTokenPage(PageQuery<AssetDto> pageQuery);

    PageQuery<AssetDto> getMyTokenCreatPage(PageQuery<AssetDto> pageQuery);

    List<AssetDto> templateDto(AssetDto asset);

    PageQuery<AssetDto> getOtherTokenPage(PageQuery<AssetDto> pageQuery);

    PageQuery<AssetDto> getmarketPlace(PageQuery pageQuery);
}
