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
package org.uptick.market.task;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.uptick.market.denum.AssetLevel;
import org.uptick.market.denum.AssetStatus;
import org.uptick.market.denum.ContractAndNftStatus;
import org.uptick.market.dto.AssetDB;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.AssetExtension;
import org.uptick.market.entity.Contract;
import org.uptick.market.entity.User;
import org.uptick.market.middleware.MiddlewareService;
import org.uptick.market.middleware.entity.*;
import org.uptick.market.service.*;
import org.uptick.market.util.StringUtils;


import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
@EnableAsync
public class AssetSync {

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetExtensionService assetExtensionService;

    @Autowired
    private UserService userService;

    @Autowired
    private MakerOrderService makerOrderService;

    @Autowired
    private DBService assetDBService;

    @Autowired
    private ContractService contractService;


    @Autowired
    private MiddlewareService middlewareService;

    public static HashSet<String> SYNC_ADDRESS = new HashSet();

    @Async("threadPoolTaskExecutor")
    public void syncDataByAddress(String address, String tokenAddress) {
        try {
            if (SYNC_ADDRESS.contains(address.toLowerCase())) {
                return;
            }
            SYNC_ADDRESS.add(address.toLowerCase());
            List<NFTData> nftDataList = middlewareService.getNftBalances(address, tokenAddress);

            List<String> nftDatas = new ArrayList<>();
            if (nftDataList != null && nftDataList.size() > 0) {
                for (NFTData var : nftDataList) {
                    nftDatas.add(var.getNftData());
                    info(var);
                }
            }

            //删除没有的NFT数据
            Asset param = new Asset();
            param.setOwner(address);
            param.setContractAddress(tokenAddress);
            List<Asset> assetList = assetService.template(param);
            if (assetList != null && assetList.size() > 0) {
                for (Asset asset : assetList) {
                    if (!nftDatas.contains(asset.getNftData())) {
                        if (asset.getMakerOrderId() != null) {//删除上架信息
                            makerOrderService.deleteById(asset.getMakerOrderId());
                        }
                        assetService.forceDeleteById(asset.getId());
                    }
                }


            }
        } catch (Exception e) {
            log.error("Error:", e);
        } finally {
            SYNC_ADDRESS.remove(address.toLowerCase());
        }


    }


    public void sync(String tokenAddress, String nftId) {

        Asset param = new Asset();
        param.setContractAddress(tokenAddress);
        param.setNftId(nftId);
        List<Asset> assetList = assetService.template(param);

        if (assetList != null && assetList.size() > 0) {

            String nftData = assetList.get(0).getNftData();

            List<NftHolder> nftHolderList = middlewareService.getNftHolders(nftData);
            if (nftHolderList != null && nftHolderList.size() > 0) {

                Map<Long, List<Asset>> map = new HashMap<>();
                for (Asset var : assetList) {
                    List<Asset> assets = map.get(var.getAccountId());
                    if (assets == null) {
                        assets = new ArrayList<>();
                    }
                    assets.add(var);
                    map.put(var.getAccountId(), assets);
                }


                Map<Long, Long> add = new HashMap<>();
                for (NftHolder var : nftHolderList) {
                    Long accountId = var.getAccountId();
                    Long amount = var.getAmount();
                    List<Asset> ats = map.get(accountId);
                    if (ats == null || ats.size() == 0) {
                        add.put(accountId, amount);
                    } else {
                        Long size = Long.valueOf(ats.size());
                        if (amount > size) {
                            add.put(accountId, amount - size);
                            assetList.removeAll(ats);
                        } else if ((amount < size)) {
                            sort(ats);
                            List<Asset> del = ats.subList(0, amount.intValue());
                            assetList.removeAll(del);
                        } else {
                            assetList.removeAll(ats);
                        }
                    }
                }

                if (add.size() == assetList.size()) {
                    int i = 0;
                    for (Long var : add.keySet()) {

                        Integer amount = add.get(var).intValue();

                        String owner = getAddressByAccountId(var);
                        for (int j = i; j < amount + i; j++) {
                            Asset asset = assetList.get(j);
                            asset.setLevel(AssetLevel.FLEA_MARKET.intValue());
                            asset.setSaleStatus(AssetStatus.DELIST.getStatus());
                            asset.setAccountId(var);
                            asset.setOwner(owner);
                            asset.setUpdateTime(new Date());
                            asset.setMarketTokenType(null);
                            asset.setMarketPrice(null);
                            asset.setMarketTime(null);
                            asset.setMakerOrderId(null);
                            asset.setMarketOwner(null);
                            assetService.updateById(asset);

                        }
                        i = i + amount;

                    }


                } else {
                    log.error("nftId: {} sync error", nftId);
                }

            }

        }

    }


    private String getAddressByAccountId(Long accountId) {

        User user = userService.getUserByAccountId(accountId);
        if (user != null) {
            return user.getAddress();
        } else {
            Account account = middlewareService.getAccount(accountId);
            if (account != null) {
                return account.getOwner();
            }
        }
        return null;
    }

    private void sort(List<Asset> assets) {
        Collections.sort(assets, new Comparator<Asset>() {
            public int compare(Asset a, Asset b) {
                if (a.getSaleStatus() < b.getSaleStatus()) {

                    return 1;

                } else if (a.getSaleStatus() > b.getSaleStatus()) {

                    return -1;
                } else {
                    if (a.getLevel() < b.getLevel()) {
                        return -1;
                    }
                    if (a.getLevel() > b.getLevel()) {
                        return 1;
                    }
                }
                return 0;
            }
        });
    }

    public void info(NFTData var) {

        try {


            String tokenAddress = var.getTokenAddress();
            String nftData = var.getNftData();
            String nftId = var.getNftId();
            String nftTpye = var.getNftType();


            Map<String, Object> map = new HashMap<>();
            map.put("nftData", nftData);
            AssetDB baseInfo = assetDBService.findOne(map, AssetDB.class);
            if (baseInfo == null) {
                Asset param = new Asset();
                param.setNftData(nftData);
                param = assetService.templateOne(param);
                baseInfo = new AssetDB();
                if (param != null) {
                    BeanUtils.copyProperties(param, baseInfo);
                    assetDBService.save(baseInfo);
                }

            }

            if (baseInfo == null || StringUtils.isEmpty(baseInfo.getName()) || StringUtils.isEmpty(baseInfo.getImgUrl())) {
                baseInfo = new AssetDB();

                baseInfo.setContractAddress(tokenAddress);
                //  baseInfo.setCreator("");

                baseInfo.setNftData(nftData);
                baseInfo.setNftId(nftId);
                baseInfo.setNftType(nftTpye);

                Metadata metadata = var.getMetadata();
                String collectionMetadataUri = null;
                if (metadata != null) {
                    String uri = metadata.getUri();
                    if (StringUtils.isNotEmpty(uri)) {
                        String[] split = uri.split("//");
                        if (split.length > 1) {
                            baseInfo.setMetadataUrl(split[1]);
                        }
                        NftMetadata nftMetadata = middlewareService.getNftMetadataByUri(uri);
                        if (nftMetadata != null) {
                            baseInfo.setName(nftMetadata.getName());
                            baseInfo.setDescription(nftMetadata.getDescription());
                            baseInfo.setProperties(nftMetadata.getAttributes());
                            if (nftMetadata.getLincense_code() != null && !nftMetadata.getLincense_code().equals("ULC0001")) {
                                baseInfo.setLincenseCode(nftMetadata.getLincense_code());
                            }
//update Lincense_code License_code
                            if (nftMetadata.getLicense_code() != null && !nftMetadata.getLicense_code().equals("ULC0001")) {
                                baseInfo.setLincenseCode(nftMetadata.getLicense_code());
                            }
                            if (StringUtils.isNotEmpty(nftMetadata.getImage()) && nftMetadata.getImage().startsWith("ipfs://")) {
                                String img = nftMetadata.getImage().replace("ipfs://", "");
                                baseInfo.setImgUrl(img);
                            } else {
                                baseInfo.setImgUrl(nftMetadata.getImage());
                            }
                            baseInfo.setCreator(nftMetadata.getMinter());
                            baseInfo.setChannel(nftMetadata.getMint_channel());
                            collectionMetadataUri = nftMetadata.getCollection_metadata();

                        } else {

                            return;
                        }
                    }

                } else {
                    return;
                }

                NftInfo nftInfo = middlewareService.getNftInfo(nftData);
                if (nftInfo != null) {

                    if (nftInfo.getCreatedAt() != null) {
                        baseInfo.setCreateTime(new Date(nftInfo.getCreatedAt()));
                    }
                    baseInfo.setCreator(nftInfo.getMinter());
                    baseInfo.setRoyaltyPercentage(nftInfo.getRoyaltyPercentage());
                    baseInfo.setOriginalRoyaltyPercentage(nftInfo.getOriginalRoyaltyPercentage());

                    Contract contract = new Contract();
                    contract.setAddress(nftInfo.getTokenAddress());
                    contract = contractService.templateOne(contract);

                    if (contract == null) {
                        contract = new Contract();
                        contract.setAddress(nftInfo.getTokenAddress());
                        contract.setAmount(1);
                    } else {
                        contract.setAmount(contract.getAmount() + 1);
                    }

                    contract.setAddress(nftInfo.getTokenAddress());
                    contract.setOwner(nftInfo.getMinter());
                    contract.setBaseUrl(nftInfo.getNftBaseUri());

                    if (StringUtils.isNotEmpty(collectionMetadataUri)) {
                        CollectionMetadata base = middlewareService.getCollectionMetadataByUri(collectionMetadataUri);
                        if (base != null) {
                            contract.setName(base.getName());
                            contract.setDescription(base.getDescription());
                            String contract_status = base.getContract_status();
                            if (StringUtils.isNotEmpty(contract_status)) {
                                contract.setContractStatus(Integer.valueOf(base.getContract_status()));
                            } else {
                                contract.setContractStatus(ContractAndNftStatus.normal.getStatus());
                            }

                            if (StringUtils.isNotEmpty(base.getBanner_uri())) {
                                if (base.getBanner_uri().startsWith("ipfs://")) {
                                    String img = base.getBanner_uri().replace("ipfs://", "");
                                    contract.setImgUrl(img);
                                } else {
                                    contract.setImgUrl(base.getBanner_uri());
                                }
                            }
                            contract.setCollectionMetadata(collectionMetadataUri);

                        }
                    }
                    if (contract.getId() == null) {
                        contract.setDeleted(false);
                        contract.setCreateTime(new Date());
                        contract.setContractStatus(0);
                        if(StringUtils.isEmpty(contract.getLinkUrl())){
                            contract.setLinkUrl(contract.getAddress());
                        }
                        contractService.saveContract(contract);
                    } else {
                        if(StringUtils.isEmpty(contract.getLinkUrl())){
                            contract.setLinkUrl(contract.getAddress());
                        }
                        contractService.updateTemplateById(contract);
                    }

                } else {
                    log.warn("获取NftInfo失败:{}", JSONObject.toJSONString(var));
                    return;
                }
                assetDBService.save(baseInfo);
            }

            holderByOwner(var, baseInfo);
        } catch (Exception e) {
            log.error("error:{}", e);
        }


    }

    public void holderByOwner(NFTData nftData, AssetDB baseInfo) {
        try {


            //获取数据库中nft支持者信息
            Asset param = new Asset();
            param.setNftData(nftData.getNftData());
            param.setAccountId(nftData.getAccountId());
            Asset asset = assetService.templateOne(param);
            if (asset != null) {
                if (!asset.getAmount().equals(nftData.getTotal())) {//持有nft数量不一致，修改持有数量
                    if (nftData.getTotal() == 0) {
                        log.info("nft amount is 0: {}", JSONObject.toJSONString(nftData));
                        assetService.forceDeleteById(asset.getId());
                    }

                    Asset update = new Asset();
                    update.setId(asset.getId());
                    update.setAmount(nftData.getTotal());
                    if (asset.getListAmount() != null && asset.getListAmount() > nftData.getTotal()) {//修改上架数量
                        update.setListAmount(nftData.getTotal());
                    }
                    if (asset.getListAmount() != null && asset.getListAmount() > 0) {
                        update.setSaleStatus(AssetStatus.LIST.getStatus());
                    } else {
                        update.setSaleStatus(AssetStatus.DELIST.getStatus());
                    }
                    assetService.updateTemplateById(update);


                }
            } else {
                if (nftData.getTotal() == null || nftData.getTotal() < 1) {
                    return;
                }
                Asset newAsset = new Asset();
                String owner = getAddressByAccountId(nftData.getAccountId());
                newAsset.setContractAddress(baseInfo.getContractAddress().toLowerCase());
                newAsset.setName(baseInfo.getName());
                newAsset.setDescription(baseInfo.getDescription());
                newAsset.setCreator(baseInfo.getCreator().toLowerCase());
                newAsset.setImgUrl(baseInfo.getImgUrl());
                newAsset.setNftData(baseInfo.getNftData());
                newAsset.setNftId(baseInfo.getNftId());
                newAsset.setNftType(baseInfo.getNftType());
                newAsset.setOwner(owner.toLowerCase());
                newAsset.setAccountId(nftData.getAccountId());
                newAsset.setAmount(nftData.getTotal());
                newAsset.setListAmount(0);
                newAsset.setChannel(baseInfo.getChannel());
                newAsset.setRoyaltyPercentage(baseInfo.getRoyaltyPercentage());
                newAsset.setOriginalRoyaltyPercentage(baseInfo.getOriginalRoyaltyPercentage());
                newAsset.setSaleStatus(AssetStatus.DELIST.getStatus());
                newAsset.setProperties(baseInfo.getProperties());
                newAsset.setMetadataUrl(baseInfo.getMetadataUrl());
                if (baseInfo.getCreator().toLowerCase().equals(owner.toLowerCase())) {
                    newAsset.setLevel(AssetLevel.MARKET.intValue());
                } else {
                    newAsset.setLevel(AssetLevel.FLEA_MARKET.intValue());
                }
                newAsset.setCreateTime(baseInfo.getCreateTime());
                newAsset.setDeleted(false);
                Contract contractByTokenAddress = contractService.getContractByTokenAddress(newAsset.getContractAddress());
                if (contractByTokenAddress != null && ContractAndNftStatus.black.getStatus().equals(contractByTokenAddress.getContractStatus())) {
                    newAsset.setAssetStatus(ContractAndNftStatus.black.getStatus());
                }
                if (newAsset.getAssetStatus() == null) {
                    newAsset.setAssetStatus(ContractAndNftStatus.normal.getStatus());
                }
                assetService.saveAsset(newAsset);

                if (newAsset.getCreator().equals(newAsset.getOwner())){
                    AssetExtension searchAssetExtension = new AssetExtension();
                    searchAssetExtension.setNftId(newAsset.getNftId());
                    searchAssetExtension = assetExtensionService.templateOne(searchAssetExtension);
                    if (searchAssetExtension == null){
                        AssetExtension assetExtension = new AssetExtension();
                        assetExtension.setCreateTime(new Date());
                        assetExtension.setNftId(newAsset.getNftId());
                        assetExtension.setDeleted(false);
                        assetExtension.setTotalInventory(Long.valueOf(newAsset.getAmount()));
                        assetExtension.setIssuerPrice(new BigDecimal(0));
                        assetExtension.setDaiTotalPrice(new BigDecimal(0));
                        assetExtension.setEthTotalPrice(new BigDecimal(0));
                        assetExtension.setLrcTotalPrice(new BigDecimal(0));
                        assetExtension.setUsdcTotalPrice(new BigDecimal(0));
                        assetExtension.setUsdtTotalPrice(new BigDecimal(0));
                        assetExtension.setWeight(100);
                        assetExtension.setLikeCount(0L);
                        assetExtension.setViewCount(0L);
                        assetExtension.setCreateOwner(newAsset.getCreator());
                        assetExtensionService.save(assetExtension);
                    }

                }


            }


        } catch (Exception e) {
            log.error("error:{}", e);
        }

    }
}
