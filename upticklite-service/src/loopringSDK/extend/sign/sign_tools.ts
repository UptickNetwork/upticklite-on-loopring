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

import * as fm from "../../utils/formatter";
import * as sigUtil from "eth-sig-util";

import {
    UpdateAccountRequestV3,
    OriginNFTTransferRequestV3
} from '../../defs/loopring_defs'

import { ChainId } from '../../defs/web3_defs'

import{
    GetEcDSASigType,
    getUpdateAccountEcdsaTypedData,
    getNFTTransferTypedData
} from '../../api/sign/sign_tools'

export async function getSignUpdateAccountWithoutDataStructure(
    bodyParams: UpdateAccountRequestV3,chainId: ChainId) {

    const typedData: any = getUpdateAccountEcdsaTypedData(bodyParams, chainId)
    const result = await getEcDSASigMsg(typedData, GetEcDSASigType.WithoutDataStruct)
    return result
}

export async function getNFTTransferWithoutDataStructure(
    bodyParams: OriginNFTTransferRequestV3,chainId: ChainId) {
    
    console.log("xxl getNFTTransferWithoutDataStructure 1 ",bodyParams);
    const typedData: any = getNFTTransferTypedData(bodyParams, chainId)
    console.log("xxl getNFTTransferWithoutDataStructure 2 ",typedData);

    const result = await getEcDSASigMsg(typedData, GetEcDSASigType.WithoutDataStruct)
    return result
}

export async function getEcDSASigMsg(typedData: any,type: GetEcDSASigType) {
    
    switch (type) {

        case GetEcDSASigType.WithoutDataStruct:

            let hash: any = sigUtil.TypedDataUtils.sign(typedData)
            hash = fm.toHex(hash)
            return hash

        default:
            break
    }
    throw Error('getEcDSASig unsupported switch case:' + type)
}


