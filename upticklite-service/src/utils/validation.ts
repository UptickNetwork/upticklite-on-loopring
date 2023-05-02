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
import { fromHexString } from '@eth-optimism/core-utils'
import * as url from 'url'

export const validators = {

  isNumericStr:(val :string): boolean => {
    if (typeof val != "string") return false // we only process strings!  

    // use type coercion to parse the _entirety_ of the string (`parseFloat` alone does not do this)...
    return !isNaN(parseInt(val)) && 
           !isNaN(parseFloat(val)) // ...and ensure strings of whitespace fail
  },

  isNumber: (val: any): boolean => {

    console.log(typeof val);

    return typeof val === 'number'
  },

  isBoolean: (val: any): boolean => {
    return typeof val === 'boolean'
  },
  isString: (val: any): boolean => {
    return typeof val === 'string'
  },
  isHexString: (val: any): boolean => {
    return (
      validators.isString(val) &&
      val.startsWith('0x') &&
      fromHexString(val).length === (val.length - 2) / 2
    )
  },
  isAddress: (val: any): boolean => {
    return validators.isHexString(val) && val.length === 42
  },
  isInteger: (val: any): boolean => {
    return Number.isInteger(val)
  },
  isUrl: (val: any): boolean => {
    try {
      const parsed = new url.URL(val)
      return (
        parsed.protocol === 'ws:' ||
        parsed.protocol === 'http:' ||
        parsed.protocol === 'https:'
      )
    } catch (err) {
      return false
    }
  },
  isJsonRpcProvider: (val: any): boolean => {
    return val.ready !== undefined
  },
  isLevelUP: (val: any): boolean => {
    // TODO: Fix?
    return val && val.db
  },
}
