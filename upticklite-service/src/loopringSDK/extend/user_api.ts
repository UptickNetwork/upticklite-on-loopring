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
import { BaseAPI } from '../api/base_api'
import * as sign_tools from './sign/sign_tools'
import * as loopring_defs from '../defs/loopring_defs'
import { LOOPRING_URLs} from '../defs/url_defs'
import { ReqMethod, SIG_FLAG } from '../defs/loopring_enums'


export class UserExtendAPI extends BaseAPI {

    public async getUpdateAccountMsg(
        request:any,
        chainId:number,

    ) {
        const result = await sign_tools.getSignUpdateAccountWithoutDataStructure(request, chainId)
        return result;
    }

    public async getTransferNftMsg(
        request:any,
        chainId:number,

    ) {
        const result = await sign_tools.getNFTTransferWithoutDataStructure(request, chainId)
        return result;
    }

    public async sendUpdateAccount(request:any,ecdsaSignature:string){

        const reqParams: loopring_defs.ReqParams = {
            url: LOOPRING_URLs.ACCOUNT_ACTION,
            bodyParams: request,
            method: ReqMethod.POST,
            sigFlag: SIG_FLAG.NO_SIG,
            ecdsaSignature,
        }

        const raw_data = (await this.makeReq().request(reqParams)).data
        return {
            ...this.returnTxHash(raw_data)
        }

    }

    public async sendTransferNFT(request:any,ecdsaSignature:string,apiKey:any){

        const reqParams: loopring_defs.ReqParams = {
            url: LOOPRING_URLs.POST_NFT_INTERNAL_TRANSFER,
            bodyParams: request,
            apiKey,
            method: ReqMethod.POST,
            sigFlag: SIG_FLAG.NO_SIG,
            ecdsaSignature,
        }

        const raw_data = (await this.makeReq().request(reqParams)).data
        return {
            ...this.returnTxHash(raw_data)
        }

    }

}