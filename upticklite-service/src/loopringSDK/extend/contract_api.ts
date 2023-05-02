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
import { ChainId } from '../defs/web3_defs'
import { TokenInfo } from '../defs/loopring_defs'
import * as fm from '../utils/formatter'

import BN from 'bn.js';

import {
    genExchangeData,
    ERC20Method
} from '../api/contract_api'


/**
 * deposit
 */
 export async function getDeposit(
    from: string,
    exchangeAddress: string,
    token: TokenInfo,
    value: number,
    fee: number,
    gasPrice: number,
    gasLimit: number,
    chainId: ChainId = ChainId.GOERLI,
    nonce: number
) {

    let valueC = fm.toBig(value).times('1e' + token.decimals)

    const amount = fm.toHex(valueC)

    const data = genExchangeData(ERC20Method.Deposit, {
        tokenAddress: token.address,
        amount,
        from,
        to: from,
        extraData: '',
    })

    if (token.type === 'ETH') {
        valueC = valueC.plus(fee)
    } else {
        valueC = fm.toBig(fee)
    }

    console.log("xxl value : ");
    console.log(valueC.toFixed());
    console.log([
        from, exchangeAddress, valueC.toFixed(), data, chainId, 
        nonce, gasPrice, gasLimit
    ])
    return await getRawTx(from, exchangeAddress, valueC.toFixed(), data, chainId, 
        nonce, gasPrice, gasLimit)

}


/**
 * deposit
 */
 export async function getRawTx(from: string, to: string, value: string, data: any, 
    chainId: ChainId, nonce: number, gasPrice: any, gasLimit: number) {

    gasPrice = fm.fromGWEI(gasPrice).toNumber()
    const _value = new BN(value)

    const rawTx = {
        from,
        to,
        value: _value.toString(),
        data,
        chainId,
        nonce,
        gasPrice,
        gasLimit,
    }

    return rawTx;
    
}
