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
package org.uptick.market.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.FilterType;
import org.springframework.util.CollectionUtils;
import org.uptick.market.dao.AssetDao;
import org.uptick.market.denum.*;
import org.uptick.market.dto.AssetDto;
import org.uptick.market.dto.ContractDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.AssetExtension;
import org.uptick.market.entity.MakerOrder;
import org.uptick.market.entity.OrderDetails;
import org.uptick.market.service.AssetExtensionService;
import org.uptick.market.service.AssetService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.uptick.market.service.MakerOrderService;
import org.uptick.market.service.OrderDetailsService;
import org.uptick.market.util.PageUtils;
import org.uptick.market.util.StringUtils;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SuppressWarnings("unchecked")
@Slf4j
@Service
public class AssetServiceImpl extends BaseServiceImpl<Asset, AssetDao> implements AssetService {

    @Autowired
    private AssetExtensionService assetExtensionService;

    @Autowired
    private MakerOrderService makerOrderService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Override
    public PageQuery<AssetDto> getMyCreatedListPage(Asset asset, PageFrame pageFrame, String search) {

        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(asset));
        jsonObject.put("search", search);
        PageQuery<Asset> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(jsonObject);

        return this.getEntityDao().getMyCreatedListPage(pageQuery);
    }

    @Override
    public PageQuery<AssetDto> getMyOwnListPage(Asset asset, PageFrame pageFrame, Integer sort) {
        JSONObject object = JSON.parseObject(JSONObject.toJSONString(asset));
        object.put("sort", sort);
        PageQuery pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(object);
        return this.getEntityDao().getMyOwnListPage(pageQuery);
    }

    @Override
    public PageQuery<AssetDto> getMyOwnByCreatorListPage(Asset asset, PageFrame pageFrame) {
        PageQuery<Asset> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(asset);
        return this.getEntityDao().getMyOwnByCreatorListPage(pageQuery);
    }

    @Override
    public AssetDto getMarketInfo(Asset asset) {
        return this.getEntityDao().getMarketInfo(asset);
    }

    @Override
    public AssetDto getTokenInfo(Asset param) {
        return this.getEntityDao().getTokenInfo(param);
    }

    @Override
    public PageQuery<AssetDto> getMarketListPage(Asset asset, PageFrame pageFrame, String search, Integer sort) {
        JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(asset));
        jsonObject.put("search", search);
        jsonObject.put("sort", sort);
        PageQuery<Asset> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(jsonObject);
        return this.getEntityDao().getMarketListPage(pageQuery);
    }

    @Override
    public PageQuery<AssetDto> getFleaMarketListPage(Asset asset, PageFrame pageFrame, String search, Integer sort) {
        JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(asset));
        jsonObject.put("search", search);
        jsonObject.put("sort", sort);
        PageQuery<Asset> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(jsonObject);
        return this.getEntityDao().getFleaMarketListPage(pageQuery);
    }

    @Override
    public PageQuery<AssetDto> getHomepageListPage(Asset asset, PageFrame pageFrame) {
        PageQuery<Asset> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(asset);
        return this.getEntityDao().getHomepageListPage(pageQuery);
    }

    @Override
    public void burnNft(String tokenAddress, String nftId) {
        this.getEntityDao().delNftByNftId(tokenAddress, nftId);
    }

    @Override
    public Long getNftNumber() {
        return this.getEntityDao().getNftNumber();
    }

    @Override
    public PageQuery<AssetDto> getMyTokenPage(AssetDto asset, PageFrame pageFrame, String search, Integer filter) {
        asset.setSearch(search);
        asset.setFilter(filter);
        PageQuery<AssetDto> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(asset);
        PageQuery<AssetDto> myCreatedTokenPage = new PageQuery<>();
        if (asset.getCreator() != null) {
            myCreatedTokenPage = this.getEntityDao().getMyTokenCreatPage(pageQuery);
        } else {
            myCreatedTokenPage = this.getEntityDao().getMyCreatedTokenPage(pageQuery);
        }

        List<AssetDto> assetDtos = myCreatedTokenPage.getList();
        for (AssetDto assetDto : assetDtos) {
            if (AssetLevel.MARKET.intValue() == assetDto.getLevel()) {
                getAssetExtensionInfo(assetDto);
            }

            MakerOrder makerOrder = new MakerOrder();
            makerOrder.setId(assetDto.getMakerOrderId());
            makerOrder = makerOrderService.templateOne(makerOrder);
            assetDto.setLockAssetHash(makerOrder == null ? null : makerOrder.getLockAssetHash());

            //售空
            AssetDto assetDtoReq = new AssetDto();
            assetDtoReq.setContractAddress(assetDto.getContractAddress());
            assetDtoReq.setNftId(assetDto.getNftId());
            assetDtoReq.setCreator(assetDto.getCreator());
            List<AssetDto> assetDtoList = this.getEntityDao().templateDto(assetDtoReq);
            List<AssetDto> collect1 = assetDtoList.stream().filter(v -> v.getLevel() == 1).collect(Collectors.toList());
            List<AssetDto> collect2 = assetDtoList.stream().filter(v -> v.getLevel() == 2).filter(v -> v.getOwner().equals(asset.getOwner())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect1) && CollectionUtils.isEmpty(collect2)) {
                assetDto.setAmount(0);
                assetDto.setListAmount(0);
                assetDto.setSaleStatus(AssetStatus.DELIST.getStatus());
                assetDto.setSoldOut(true);
                assetDto.setOwner(assetDto.getCreator());
                getAssetExtensionInfo(assetDto);
            }

            Boolean isBurn = false;
            if (assetDto.getAssetExtension() != null && Long.valueOf(assetDto.getAmount()).equals(assetDto.getAssetExtension().getTotalInventory())
                    && AssetLevel.MARKET.intValue().equals(assetDto.getLevel())
                    && AssetStatus.DELIST.getStatus().equals(assetDto.getSaleStatus())
            ) {
                isBurn = true;
            }
            assetDto.setIsBurn(isBurn);
        }
        return myCreatedTokenPage;
    }

    private AssetDto getAssetExtensionInfo(AssetDto assetDto) {
        AssetExtension assetExtension = new AssetExtension();
        assetExtension.setNftId(assetDto.getNftId());
        assetExtension = assetExtensionService.templateOne(assetExtension);
        if (assetExtension != null) {
            assetDto.setAssetExtension(assetExtension);
        }
        return assetDto;
    }

    public PageQuery<AssetDto> getOtherTokenPage(Asset asset, PageFrame pageFrame, String search, Integer filter) {

        AssetDto assetReq = new AssetDto();
        BeanUtils.copyProperties(asset, assetReq);
        assetReq.setSearch(search);
        assetReq.setFilter(filter);
        PageQuery<AssetDto> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(assetReq);
        PageQuery<AssetDto> myCreatedTokenPage = this.getEntityDao().getOtherTokenPage(pageQuery);
        for (AssetDto assetDto : myCreatedTokenPage.getList()) {
            if (AssetLevel.MARKET.intValue().equals(assetDto.getLevel())) {
                getAssetExtensionInfo(assetDto);
            }

            if (assetDto.getId() == null) {
                Asset soldOutAsset = new Asset();
                soldOutAsset.setNftId(assetDto.getExNftId());
                soldOutAsset.setDeleted(false);
                soldOutAsset = this.getEntityDao().templateOne(soldOutAsset);
                if (soldOutAsset != null) {
                    BeanUtils.copyProperties(soldOutAsset, assetDto);
                    assetDto.setSoldOut(true);
                }
            } else {
                Boolean isBurn = false;
                if (assetDto.getAssetExtension() != null && Long.valueOf(assetDto.getAmount()).equals(assetDto.getAssetExtension().getTotalInventory())
                        && AssetLevel.MARKET.intValue().equals(assetDto.getLevel())
                        && AssetStatus.DELIST.getStatus().equals(assetDto.getSaleStatus())
                ) {
                    isBurn = true;
                }
                assetDto.setIsBurn(isBurn);
            }
        }

        return myCreatedTokenPage;
    }

    @Override
    public List<AssetDto> templateDto(AssetDto asset) {
        return this.getEntityDao().templateDto(asset);
    }

    @Override
    public PageQuery<AssetDto> getmarketPlace(PageFrame pageFrame, String search, String marketTokenType, Integer sortType,
                                              String nftType,
                                              String address) {
        AssetDto assetDto = new AssetDto();
        assetDto.setSearch(search);
        assetDto.setSortType(sortType);
        assetDto.setMarketTokenType(marketTokenType);
        assetDto.setNftType(nftType);
        assetDto.setContractAddress(address);

        PageQuery pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        PageQuery<AssetDto> marketDtoList = new PageQuery<>();
        if (SortType.Sold.intValue().equals(sortType)) {
            if (!StringUtils.isEmpty(assetDto.getMarketTokenType())) {
                assetDto.setTokenType(assetDto.getMarketTokenType());
                assetDto.setMarketTokenType(null);
            }
            if (assetDto.getSaleModel() != null) {
                assetDto.setOrderMarketType(assetDto.getSaleModel());
                assetDto.setSaleModel(null);
            }
        }
        pageQuery.setParas(assetDto);
        marketDtoList = this.getEntityDao().getmarketPlace(pageQuery);

        for (AssetDto marketDto : marketDtoList.getList()) {
            if (sortType != null && sortType == 3) {
                marketDto.setMarketPrice(marketDto.getOrderPrice());
                marketDto.setMarketTokenType(marketDto.getOrderTokenType());
            } else {
                if (AssetLevel.MARKET.intValue() == marketDto.getLevel()) {
                    getAssetExtensionInfo(marketDto);
                }
            }
        }
        if (sortType != null && SortType.PriceLowToHigh.intValue().equals(sortType)) {
            Collections.sort(marketDtoList.getList(), new Comparator<AssetDto>() {
                public int compare(AssetDto a, AssetDto b) {
                    return a.getMarketPrice().compareTo(b.getMarketPrice());
                }
            });
        }
        if (sortType != null && SortType.PriceHighToLow.intValue().equals(sortType)) {
            Collections.sort(marketDtoList.getList(), new Comparator<AssetDto>() {
                public int compare(AssetDto a, AssetDto b) {
                    return b.getMarketPrice().compareTo(a.getMarketPrice());
                }
            });
        }
        return marketDtoList;
    }

    @Override
    @Transient
    public Asset saveAsset(Asset t) {
        String lock = t.getNftData() + t.getOwner();
        synchronized (lock) {
            Asset param = new Asset();
            param.setNftData(t.getNftData());
            param.setOwner(t.getOwner());
            param = templateOne(param);
            if (param != null) {
                t.setId(param.getId());
                updateTemplateById(t);
                return t;
            } else {
                return save(t);
            }

        }
    }


}
