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
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.uptick.market.dao.ContractDao;
import org.uptick.market.denum.*;
import org.uptick.market.dto.AssetDto;
import org.uptick.market.dto.ContractDto;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.Contract;
import org.uptick.market.entity.User;
import org.uptick.market.service.AssetService;
import org.uptick.market.service.ContractService;
import org.uptick.market.service.UserService;
import org.uptick.market.util.JsonResult;
import org.uptick.market.util.StringUtils;

import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;


@SuppressWarnings("unchecked")
@Slf4j
@Service
public class ContractServiceImpl extends BaseServiceImpl<Contract, ContractDao> implements ContractService {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @Override
    public Contract getContractByTokenAddress(String tokenAddress) {
        if (StringUtils.isEmpty(tokenAddress)) {
            return null;
        }
        Contract param = new Contract();
        param.setAddress(tokenAddress);
        param.setDeleted(false);
        return templateOne(param);
    }

    @Override
    public PageQuery<ContractDto> getContractPage(PageFrame pageFrame, Contract param) {

        PageQuery<Contract> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(param);
        templatePage(pageQuery);
        return this.getEntityDao().contractPage(pageQuery);
    }


    @Override
    public ContractDto collectionInfo(Contract contract) {
        return this.getEntityDao().collectionInfo(contract);
    }

    @Override
    public Contract getByBaseUrl(String linkUrl) {
        Contract param = new Contract();
        param.setLinkUrl(linkUrl);
        return templateOne(param);
    }

    @Override
    public Long creators() {
        return this.getEntityDao().creators();
    }

    @Override
    public PageQuery<ContractDto> getMyContractPage(ContractDto contract, PageFrame pageFrame, String search, Integer filter) {
        contract.setSearch(search);
        contract.setFilter(filter);
        PageQuery pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(contract);
        PageQuery<ContractDto> myContractPage = this.getEntityDao().getMyContractPage(pageQuery);
        for (ContractDto contractDto : myContractPage.getList()) {
            List<Asset> template = new ArrayList<>();
            if (filter == null) {
                if (contractDto.getOwner().equals(contract.getOwner())) {
                    Asset asset = new Asset();
                    asset.setCreator(contract.getOwner());
                    asset.setContractAddress(contractDto.getAddress());
                    template = assetService.template(asset);
                    if (template != null) {
                        Set<String> collect = template.stream().map(v -> v.getNftId()).collect(Collectors.toSet());
                        contractDto.setNftCount(Long.valueOf(collect.size()));
                    }

                } else {
                    Asset asset = new Asset();
                    asset.setOwner(contract.getOwner());
                    asset.setContractAddress(contractDto.getAddress());
                    template = assetService.template(asset);
                    if (!CollectionUtils.isEmpty(template)) {
                        contractDto.setNftCount(Long.valueOf(template.size()));
                    }
                }
            } else if (FilterStatus.CREAT.intValue().equals(filter)) {
                Asset asset = new Asset();
                asset.setCreator(contract.getOwner());
                asset.setContractAddress(contractDto.getAddress());
                template = assetService.template(asset);
                Set<String> collect = template.stream().map(v -> v.getNftId()).collect(Collectors.toSet());
                if (!CollectionUtils.isEmpty(template)) {
                    contractDto.setNftCount(Long.valueOf(collect.size()));
                }
            }
        }
        return myContractPage;
    }

    @Override
    public PageQuery<ContractDto> getOtherCollectionInfo(PageFrame pageFrame, ContractDto contractDto) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(contractDto);
        PageQuery<ContractDto> otherCollectionInfo = this.getEntityDao().getOtherCollectionInfo(pageQuery);
        List<ContractDto> otherCollectionInfoFilter = new ArrayList<>();
        for (ContractDto dto : otherCollectionInfo.getList()) {
            User userByAddress = userService.getUserByAddress(dto.getOwner());
            if (userByAddress != null) {
                dto.setCreatorName(userByAddress.getName());
                dto.setCreatorPhoto(userByAddress.getProfilePhoto());
            }

            Asset assetReq = new Asset();
            assetReq.setContractAddress(dto.getAddress());
            Integer filter = contractDto.getFilter();
            List<Asset> assetByAddressList = new ArrayList<>();
            //Created
            if (filter != null && filter == 1) {
                assetReq.setCreator(contractDto.getOwner());
                assetByAddressList = assetService.template(assetReq);
                Map<String, List<Asset>> assetMap = assetByAddressList.stream().collect(Collectors.groupingBy(v -> v.getNftId()));
                Long nftCount = 0L;
                for (String nftId : assetMap.keySet()) {
                    List<Asset> assets = assetMap.get(nftId);

                    List<Asset> collect2 = assets.stream().filter(v -> v.getLevel() == AssetLevel.FLEA_MARKET.intValue() || AssetStatus.LIST.getStatus().equals(v.getSaleStatus())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect2)) {
                        List<Asset> collect = assets.stream().filter(v -> v.getLevel() == AssetLevel.MARKET.intValue()
                                && AssetStatus.DELIST.getStatus().equals(v.getSaleStatus())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            continue;
                        }
                        nftCount = nftCount + 1;
                    }
                }
                dto.setNftCount(nftCount);
                if (nftCount > 0) {
                    otherCollectionInfoFilter.add(dto);
                }
            } else if (filter != null && filter == 2) {
                assetReq.setSaleStatus(AssetStatus.DELIST.getStatus().intValue());
                assetReq.setOwner(contractDto.getOwner());
                assetByAddressList = assetService.template(assetReq);
            } else {
                //On sale
                assetReq.setSaleStatus(AssetStatus.LIST.getStatus().intValue());
                assetReq.setOwner(contractDto.getOwner());
                assetByAddressList = assetService.template(assetReq);
            }
            if (dto.getNftCount() == null) {
                dto.setNftCount(Long.valueOf(assetByAddressList.size()));
            }
        }
        if (contractDto.getFilter() != null && contractDto.getFilter().equals(FilterStatus.CREAT.intValue())) {
            return pageQuery(pageFrame, otherCollectionInfoFilter);
        }

        return otherCollectionInfo;
    }

    public PageQuery<ContractDto> pageQuery(PageFrame pageFrame, List<ContractDto> assetDtoList) {
        int pageSize = pageFrame.getPageSize();
        int pageNumber = pageFrame.getPageNumber();
        PageQuery<ContractDto> pageQuery = new PageQuery<>();
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setPageSize(pageSize);
        List<ContractDto> assetDtoResult = new ArrayList<>();
        if (pageNumber == 1) {
            for (int i = 0; i < pageSize; i++) {
                int length = assetDtoList.size();
                if (length == i) {
                    break;
                }
                assetDtoResult.add(assetDtoList.get(i));
            }
            pageQuery.setList(assetDtoResult);
        } else {
            Integer s = (pageNumber - 1) * pageSize;
            Integer number = Integer.valueOf(s);
            Integer size = number + pageSize;
            for (int i = number; i < size; i++) {
                int length = assetDtoList.size();
                if (length <= i) {
                    break;
                }
                assetDtoResult.add(assetDtoList.get(i));
            }
        }
        Collections.sort(assetDtoResult, new Comparator<ContractDto>() {
            public int compare(ContractDto a, ContractDto b) {
                return b.getCreateTime().compareTo(a.getCreateTime());
            }
        });
        pageQuery.setList(assetDtoResult);
        pageQuery.setTotalRow(assetDtoList.size());
        return pageQuery;

    }

    @Override
    public PageQuery<ContractDto> getCollectionInfo(PageFrame pageFrame, ContractDto contractDto) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(contractDto);
        PageQuery<ContractDto> otherCollectionInfo = this.getEntityDao().getCollectionInfo(pageQuery);
        for (ContractDto dto : otherCollectionInfo.getList()) {
            User userByAddress = userService.getUserByAddress(dto.getOwner());
            if (userByAddress != null) {
                dto.setCreatorName(userByAddress.getName());
                dto.setCreatorPhoto(userByAddress.getProfilePhoto());
            }

            AssetDto asset = new AssetDto();
            asset.setContractAddress(dto.getAddress());
//            asset.setChannel("Uptick-Loopring");
            List<AssetDto> template = assetService.templateDto(asset);

            if (!CollectionUtils.isEmpty(template)) {
                Map<String, List<AssetDto>> collect = template.stream().collect(Collectors.groupingBy(v -> v.getNftId()));
                Long nftCount = 0l;
                for (String nftId : collect.keySet()) {
                    List<AssetDto> assets = collect.get(nftId);
                    if (!CollectionUtils.isEmpty(assets)) {
                        List<AssetDto> assetListLevel1 = assets.stream().filter(v -> v.getLevel().equals(AssetLevel.MARKET.intValue()) && AssetStatus.LIST.getStatus().equals(v.getSaleStatus())).collect(Collectors.toList());
                        //level1
                        if (!CollectionUtils.isEmpty(assetListLevel1)) {
                            nftCount = nftCount + 1;
                        } else {
                            List<AssetDto> assetLevel2 = assets.stream().filter(v -> v.getLevel().equals(AssetLevel.FLEA_MARKET.intValue())).collect(Collectors.toList());
                            List<Asset> collect2 = assets.stream().filter(v -> v.getLevel().equals(AssetLevel.MARKET.intValue())).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(assetLevel2) && CollectionUtils.isEmpty(collect2)) {
                                nftCount = nftCount + 1;
                            }
                        }
                    }
                    dto.setNftCount(nftCount);
                }
            }

        }
        return otherCollectionInfo;
    }

    @Override
    @Transient
    public Contract saveContract(Contract t) {
        synchronized (t.getAddress()) {
            Contract param = new Contract();
            param.setAddress(t.getAddress());
            param = templateOne(param);
            if (param != null) {
                t.setId(param.getId());
                updateTemplateById(t);
            } else {
                save(t);
            }

        }
        return null;
    }


}
