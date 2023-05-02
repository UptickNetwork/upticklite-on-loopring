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
import org.uptick.market.dto.AssetDto;
import org.uptick.market.dto.ContractDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;

import java.math.BigDecimal;
import java.util.List;

public interface AssetService extends BaseService<Asset> {


    /**
     * Query the list of tokens I created
     *
     * @param asset     token param
     * @param pageFrame page param
     * @param search    search
     * @return
     */
    PageQuery<AssetDto> getMyCreatedListPage(Asset asset, PageFrame pageFrame, String search);


    /**
     * Query the list of tokens I own
     *
     * @param asset     token param
     * @param pageFrame page param
     * @param sort      Sort by creation time：1，Sort by update time：2
     * @return
     */
    PageQuery<AssetDto> getMyOwnListPage(Asset asset, PageFrame pageFrame, Integer sort);


    /**
     * Query the list of tokens I own,sort by creator
     *
     * @param asset
     * @param pageFrame
     * @return
     */
    PageQuery<AssetDto> getMyOwnByCreatorListPage(Asset asset, PageFrame pageFrame);


    /**
     * Query token market info
     *
     * @param asset token param
     * @return
     */
    AssetDto getMarketInfo(Asset asset);


    /**
     * Query token info by nft id
     *
     * @param param
     * @return
     */
    AssetDto getTokenInfo(Asset param);


    /**
     * NFT market list
     *
     * @param asset
     * @param pageFrame
     * @param search
     * @param sort
     * @return
     */
    PageQuery<AssetDto> getMarketListPage(Asset asset, PageFrame pageFrame, String search, Integer sort);


    /**
     * NFT flea market list
     *
     * @param asset
     * @param pageFrame
     * @param search
     * @param sort
     * @return
     */
    PageQuery<AssetDto> getFleaMarketListPage(Asset asset, PageFrame pageFrame, String search, Integer sort);


    /**
     * Homepage List
     *
     * @param asset
     * @param pageFrame
     * @return
     */
    PageQuery<AssetDto> getHomepageListPage(Asset asset, PageFrame pageFrame);


    /**
     * Burn nft
     *
     * @param tokenAddress
     * @param nftId
     */
    void burnNft(String tokenAddress, String nftId);


    /**
     * Query nft number
     *
     * @return
     */
    Long getNftNumber();

    /**
     * Query my  token
     *
     * @param asset
     * @param pageFrame
     * @param search
     * @param filter
     * @return
     */
    PageQuery<AssetDto> getMyTokenPage(AssetDto asset, PageFrame pageFrame, String search, Integer filter);

    /**
     * Query for others' tokens
     *
     * @param asset
     * @param pageFrame
     * @param search
     * @param filter
     * @return
     */
    PageQuery<AssetDto> getOtherTokenPage(Asset asset, PageFrame pageFrame, String search, Integer filter);

    /**
     * Query asset info
     *
     * @param asset
     * @return
     */
    List<AssetDto> templateDto(AssetDto asset);

    /**
     * Query marketPlace
     *
     * @param pageRequest
     * @param search
     * @param marketTokenType
     * @param sortType
     * @param nftType
     * @param addrsss
     * @return
     */
    PageQuery<AssetDto> getmarketPlace(PageFrame pageRequest, String search, String marketTokenType, Integer sortType,
                                       String nftType,
                                       String addrsss);

    Asset saveAsset(Asset t);
}
