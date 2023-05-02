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
package org.uptick.market.middleware.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uptick.market.configuration.NodeConfiguration;
import org.uptick.market.middleware.MiddlewareService;
import org.uptick.market.middleware.entity.*;
import org.uptick.market.service.DBService;
import org.uptick.market.util.HttpClientUtil;
import org.uptick.market.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MiddlewareServiceImpl implements MiddlewareService {

    @Autowired
    private NodeConfiguration nodeConfiguration;

    @Autowired
    private DBService dbService;


    @Override
    public String tradeNFT(Object makerOrder, String makerOrderEddsaSignature, Object takerOrder, String takerOrderEddsaSignature) {
        Map<String, Object> map = new HashMap<>();

        map.put("makerOrder", makerOrder);
        map.put("makerOrderEddsaSignature", makerOrderEddsaSignature);
        JSONObject jsonObject = JSONObject.parseObject(makerOrder.toString());
        map.put("makerFeeBips", jsonObject.get("maxFeeBips") == null ? 1000 : jsonObject.get("maxFeeBips"));
        map.put("takerOrder", takerOrder);
        map.put("matchByTaker", true);
        JSONObject jsonObject1 = JSONObject.parseObject(takerOrder.toString());
        map.put("takerOrderEddsaSignature", takerOrderEddsaSignature);
        map.put("takerFeeBips", jsonObject1.get("maxFeeBips") == null ? 100 : jsonObject1.get("maxFeeBips"));
        String result = HttpClientUtil.post(nodeConfiguration.getMiddlewareUrl() + "/lrc/tradeNFT", JSONObject.toJSONString(map));
        return result;
    }

    @Override
    public JSONObject getBlock(String blockId) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", blockId);
        String result = HttpClientUtil.post(nodeConfiguration.getMiddlewareUrl() + "/lrc/getBlock", JSONObject.toJSONString(map));
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSON.parseObject(result);
            return jsonObject;
        }
        return null;
    }

    @Override
    public List<NFTData> getNftBalances(String owner, String tokenAddress) {
        Map<String, Object> map = new HashMap<>();
        map.put("fromAddress", owner);
        map.put("tokenAddrs", tokenAddress);
        Integer offset = 0;
        Integer limit = 50;
        map.put("limit", limit);

        List<NFTData> nftDataList = new ArrayList<>();
        while (true) {
            map.put("offset", offset);
           String result = HttpClientUtil.post(nodeConfiguration.getMiddlewareUrl() + "/lrc/getNftBalances", JSONObject.toJSONString(map));
            if (StringUtils.isNotEmpty(result)) {

                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject != null && jsonObject.get("data") != null) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data != null && data.size() > 0) {
                        List<NFTData> nftDatas = data.toJavaList(NFTData.class);
                        nftDataList.addAll(nftDatas);
                        if (nftDatas.size() != limit) {
                            break;
                        } else {
                            offset = offset + limit;
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }

            }
        }

        return nftDataList;
    }

    @Override
    public List<NftHolder> getNftHolders(String nftData) {
        Map<String, Object> map = new HashMap<>();
        map.put("nftData", nftData);
        Integer offset = 0;
        Integer limit = 500;
        map.put("limit", limit);
        List<NftHolder> nftHolderList = new ArrayList<>();
        while (true) {

            map.put("offset", offset);

            String result = HttpClientUtil.post(nodeConfiguration.getMiddlewareUrl() + "/lrc/getNftHolders", JSONObject.toJSONString(map));
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getJSONObject("data") != null) {
                    JSONArray data = jsonObject.getJSONObject("data").getJSONArray("nftHolders");
                    if (data != null && data.size() > 0) {
                        List<NftHolder> nftHolders = data.toJavaList(NftHolder.class);
                        nftHolderList.addAll(nftHolders);
                        if (nftHolders.size() != limit) {
                            break;
                        } else {
                            offset = offset + limit;
                        }
                    }
                } else {
                    break;
                }
            }
        }


        return nftHolderList;
    }

    @Override
    public Account getAccount(Long accountId) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", accountId);
        String result = HttpClientUtil.post(nodeConfiguration.getMiddlewareUrl() + "/lrc/getAccount", JSONObject.toJSONString(map));
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getJSONObject("data") != null) {
                JSONObject data = jsonObject.getJSONObject("data");
                return data.toJavaObject(Account.class);

            }
        }
        return null;
    }

    @Override
    public NftMetadata getNftMetadataByUri(String uri) {

        try {
//            https://loopring.mypinata.cloud/ipfs

            Map<String, Object> param = new HashMap<>();
            param.put("metadata_uri", uri);
            NftMetadata nftMetadata = dbService.findOne(param, NftMetadata.class);
            String metadata_uri = uri;

            if (nftMetadata == null) {
                if (uri.startsWith("ipfs://")) {
                    uri = uri.replace("ipfs://", "");
                    uri = nodeConfiguration.getIpfsPath() + uri;
                } else if (uri.startsWith("http")) {

                } else {
                    return null;
                }

                String result = HttpClientUtil.getInstance().get(uri);
                if (StringUtils.isNotEmpty(result)) {
                    String encode = URLEncoder.encode(result, "UTF-8");
                    String decode = URLDecoder.decode(encode, "UTF-8");
                    nftMetadata = JSONObject.parseObject(decode, NftMetadata.class);
                    nftMetadata.setMetadata_uri(metadata_uri);
                    dbService.save(nftMetadata);
                }
            }
            return nftMetadata;

        } catch (Exception e) {
            log.error("json error:{}", e);
        }
        return null;
    }

    @Override
    public NftInfo getNftInfo(String nftData) {
        Map<String, Object> param = new HashMap<>();
        param.put("nftData", nftData);
        NftInfo nftInfo = dbService.findOne(param, NftInfo.class);
        if (nftInfo == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("nftDatas", nftData);
            String result = HttpClientUtil.post(nodeConfiguration.getMiddlewareUrl() + "/lrc/getNfts", JSONObject.toJSONString(map));
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getJSONArray("data") != null) {
                    JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                    nftInfo = data.toJavaObject(NftInfo.class);
                    dbService.save(nftInfo);
                }
            }
        }


        return nftInfo;
    }

    @Override
    public CollectionMetadata getCollectionMetadataByUri(String uri) {

        try {

            Map<String, Object> param = new HashMap<>();
            param.put("collection_metadata_uri", uri);
            CollectionMetadata collectionMetadata = dbService.findOne(param, CollectionMetadata.class);
            String collection_metadata_uri = uri;
            if (collectionMetadata == null) {
                if (uri.startsWith("ipfs://")) {
                    uri = uri.replace("ipfs://", "");
                    uri = nodeConfiguration.getIpfsPath() + uri;
                } else if (uri.startsWith("http")) {

                } else {
                    return null;
                }

                String result = HttpClientUtil.getInstance().get(uri);

                if (StringUtils.isNotEmpty(result)) {
                    String encode = URLEncoder.encode(result, "UTF-8");
                    String decode = URLDecoder.decode(encode, "UTF-8");
                    collectionMetadata = JSONObject.parseObject(decode, CollectionMetadata.class);
                    collectionMetadata.setCollection_metadata_uri(collection_metadata_uri);
                    dbService.save(collectionMetadata);
                }
            }
            return collectionMetadata;

        } catch (Exception e) {
            log.error("json error:{}", e);
            return null;
        }
    }
}
