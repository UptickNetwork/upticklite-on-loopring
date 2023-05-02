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
import
service, { $postBodyJson }
    from '@/utils/request';
import rsa from "@/utils/RSAEncrypt";
import { BASE_URL,BASE_URL_S3 } from '../config';

export function loopringUserInfo(params) {
    return service.post(`${BASE_URL}/user/info.json`,
        transObjToParamStr2(params)
    );
}

export function loopringUserSave(params) {
    return service.post(`${BASE_URL}/user/save.do`,
        transObjToParamStr2(params,true)
    );
}

export function nftDeploy(params) {
    return service.post(`${BASE_URL}/nft/deploy.do`,
        transObjToParamStr2(params,true)
    );
}

export function nftMint(params) {
    return service.post(`${BASE_URL}/nft/mint.do`,
        transObjToParamStr2(params,true)
    );
}


export function nftList(params) {
    return service.post(`${BASE_URL}/nft/list.do`,
        transObjToParamStr2(params)
    );
}

export function nftDeList(params) {
    return service.post(`${BASE_URL}/nft/deList.do`,
        transObjToParamStr2(params)
    );
}

export function nftMyNFTList(params) {
    return service.post(`${BASE_URL}/nft/myNFTList.json`,
        transObjToParamStr2(params)
    );
}
// Query nft market info
export function marketNftInfo(params) {
    return service.post(`${BASE_URL}/market/nftInfo.json`,
        transObjToParamStr2(params)
    );
}

export function nftTrade(params) {
    return service.post(`${BASE_URL}/nft/trade.do`,
        transObjToParamStr2(params)
    );
}

function transObjToParamStr2(object,isSign) {
    if(isSign){
        let sign =  rsa.encrypt(object)
        object = {sign:sign}
    }
    let arr = [];
    for (const key in object) {
        if (Object.hasOwnProperty.call(object, key)) {
            const element = object[key];
            arr.push(`${key}=${element}`);
        }
    }
    return arr.join('&');
}

function setEvmParams(object) {
    let key = localStorage.getItem('KEY_CHAIN');
    if (key == "UPTICK EVM") {
        object.chainType = "EVM_UPTICK";
    }
}


export function getMarketnftList(params) {
    return service.post(`${BASE_URL}/market/getMarketplace.json`,
        transObjToParamStr2(params)
    )
}

export function updateDate(params) {
    return service.post(`${BASE_URL}/user/updateData.do`,
        transObjToParamStr2(params)
    )
}
export function getCollectnftList(params) {
    return service.post(`${BASE_URL}/market/list.json`,
        transObjToParamStr2(params)
    )
}
export function getLaunchList(params) {
    return service.post(`${BASE_URL}/market/getFirstPublish.json`,
        transObjToParamStr2(params)
    )
}


export function getPersonnftList(params) {
    return service.post(`${BASE_URL}/homepage/token.json`,
        transObjToParamStr2(params)
    )
}

export function getPersoncollectionList(params) {
    return service.post(`${BASE_URL}/homepage/collection.json`,
        transObjToParamStr2(params)
    )
}

//resale  
export function getResaleList(params) {
    return service.post(`${BASE_URL}/fleaMarket/list.json`,
        transObjToParamStr2(params)
    )
}

export function getResaleNftInfo(params) {
    return service.post(`${BASE_URL}/fleaMarket/nftInfo.json`,
        transObjToParamStr2(params)
    )
}

export function NftSingleTrade(params) {
    return service.post(`${BASE_URL}/nft/singleTrade.do`,
        transObjToParamStr2(params)
    )
}

export function getNftOwn(params) {
    return service.post(`${BASE_URL}/nft/getMyTokenId.json`,
        transObjToParamStr2(params)
    )
}

export function getOwnByCollection(params) {
    return service.post(`${BASE_URL}/nft/getMyContract.json`,
        transObjToParamStr2(params)
    )
}

export function getOwnByCreator(params) {
    return service.post(`${BASE_URL}/nft/getOwnByCreator.json`,
        transObjToParamStr2(params)
    )
}

export function nftTokenInfo(params) {
    return service.post(`${BASE_URL}/nft/tokenInfo.json`,
        transObjToParamStr2(params)
    )
}

export function nftTokenInfoByassetId(params) {
    return service.post(`${BASE_URL}/nft/tokenInfoByAssetId.json`,
        transObjToParamStr2(params)
    )
}

export function assetHistory(params) {
    return service.post(`${BASE_URL}/nft/history.json`,
        transObjToParamStr2(params)
    )
}
export function nftSingleList(params) {
    return service.post(`${BASE_URL}/nft/singleList.do`,
        transObjToParamStr2(params)
    )
}

export function nftSingleDeList(params) {
    return service.post(`${BASE_URL}/nft/singleDeList.do`,
        transObjToParamStr2(params)
    )
}

export function orderList(params) {
    return service.post(`${BASE_URL}/order/list.json`,
        transObjToParamStr2(params)
    )
}

export function collectionInfo(params) {
    return service.post(`${BASE_URL}/nft/collectionInfo.json`,
        transObjToParamStr2(params)
    )
}

export function chainToken(params) {
    return service.post(`${BASE_URL}/data/token.json`,
        transObjToParamStr2(params)
    )
}

export function nftBurn(params) {
    return service.post(`${BASE_URL}/nft/burn.do`,
        transObjToParamStr2(params)
    )
}

export function nftTransfer(params) {
    return service.post(`${BASE_URL}/nft/transfer.do`,
        transObjToParamStr2(params)
    )
}

export function nftSingleTransfer(params) {
    return service.post(`${BASE_URL}/nft/singleTransfer.do`,
        transObjToParamStr2(params)
    )
}
export function getNftTokenId(params) {
    return service.post(`${BASE_URL}/nft/getTokenId.json`,
        transObjToParamStr2(params)
    )
}

export function orderStatements(params) {
    return service.post(`${BASE_URL}/order/statements.json`,
        transObjToParamStr2(params)
    )
}
export function getSystemParams(params) {
    return service.post(`${BASE_URL}/data/dataMap.json`, 
        transObjToParamStr2(params)
    )
}
export function updateCollection(params) {
    return service.post(`${BASE_URL_S3}/file/upload/json`, 
        transObjToParamStr2(params)
    )
}


export function resolveEthereumEns(params) {
    return service.post("https://api3.loopring.io/api/v2/delegator/resolveEthereumEns", params);
}
export function reverseAddrEns(address) {
    return service.get("https://api.loopring.network/api/wallet/v3/reverseAddrEns?address="+address);
}