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
import { toHexString } from '@eth-optimism/core-utils'
import {
  CROSS_TYPE,
  CHAIN_IDS
} from './constants'
var crypto = require('crypto');

/**
 * Basic timeout-based async sleep function.
 * @param ms Number of milliseconds to sleep.
 */
export const sleep = async (ms: number): Promise<void> => {
  return new Promise<void>((resolve) => {
    setTimeout(resolve, ms)
  })
}

export const assert = (condition: () => boolean, reason?: string) => {
  try {
    if (condition() === false) {
      throw new Error(`Assertion failed: ${reason}`)
    }
  } catch (err) {
    throw new Error(`Assertion failed: ${reason}\n${err}`)
  }
}

export const toRpcHexString = (n: number): string => {
  if (n === 0) {
    return '0x0'
  } else {
    return '0x' + toHexString(n).slice(2).replace(/^0+/, '')
  }
}

export const padHexString = (str: string, length: number): string => {
  if (str.length === 2 + length * 2) {
    return str
  } else {
    return '0x' + str.slice(2).padStart(length * 2, '0')
  }
}


export const randomStr = (length: number): string => {
  let random = '';
  let lexicon = 'abcdefghijklmnopqrstuvwxyz'
  for (let i=0; i<length; i++) {
    let randomIndex = Math.floor(Math.random()*1000)%lexicon.length;
    random += lexicon.substr(randomIndex,1);
  }
  return random;

}

// old nftid iris to evm 
// export const  NFTIDIris2Evm = (irisNFDID) :string =>{

//   let lIrisNFDID = irisNFDID.toLowerCase();
//   let nLen = lIrisNFDID.length;
//   let HEXString = "0123456789abcdef";
//   let ret = "";
//   for(var i = 0 ; i < nLen ; i ++){
//       let eachData = lIrisNFDID[i];
//       let isExist = HEXString.includes(eachData);
//       if(isExist){
//           ret += eachData;
//       }else{
//           ret += eachData.charCodeAt(0).toString(16)
//       }
//   }

//   if(ret.length % 2 != 0){
//     ret = ret + "0"
//   }
//   ret = "0x" + ret;

//   return ret;
// }

export const  NFTIDIris2Evm = (irisNFDID,denomID) :string =>{

  let orgKey = denomID + irisNFDID ;
  let md5 = crypto.createHash('md5');
  let result = md5.update(orgKey).digest('hex').substr(8,16);

  return "0x" + result
}

export const NFTIDEvm2Iris = (evmNFDID) :string =>{
 
  return parseInt(evmNFDID).toString(16);
}


 //for not have problem of ios and android
//type 0: iris => bsc 1: bsc=>iris 
export const serializeVrfData = (json :any) :string =>{

  let sData = json.fromRange + json.toRange;
  return sData;

}

export const getCrossType = (fromChainID,toChainID) :number=>{

  if(
    (fromChainID == CHAIN_IDS.IRIS_MAINNET ||  fromChainID == CHAIN_IDS.IRIS_TETNET)
    &&
    (toChainID == CHAIN_IDS.BSC_MAINNET ||  toChainID == CHAIN_IDS.BSC_TETNET)
  ){

    return CROSS_TYPE.IRIS2BSC
  
  }else if(
    (fromChainID == CHAIN_IDS.BSC_MAINNET ||  fromChainID == CHAIN_IDS.BSC_TETNET)
    &&
    (toChainID == CHAIN_IDS.IRIS_MAINNET ||  toChainID == CHAIN_IDS.IRIS_TETNET)
  ){
  
    return CROSS_TYPE.BSC2IRIS

  }else{
  
    return CROSS_TYPE.NO_CROSS

  }
}

export const isNull = (exp) :boolean =>{

  return !exp && typeof(exp) != 'undefined' && exp!=0;
} 



export const getContractTx = async(abi,addresss,funcName,params,eth) : Promise<any> =>{

  const callContract = new eth.Contract(
    abi,
    addresss
  );

  const tx = await callContract.methods[funcName](
    ...params
  );

  return tx;
}



export const getUnsignTx = async(tx,from,to,value,chainID,gasLime,eth) : Promise<any> =>{

  let gasPrice  = 0;
  let data ;
  let nonce ;
  try{

      gasPrice = await eth.getGasPrice();

  }catch(e){
     gasPrice = 1000000000;
    console.log("xxl service getUnsignTx  getGasPrice exception");
  }

  try{

      data = tx.encodeABI();
      nonce = await eth.getTransactionCount(from);
      //TODO
      const unsignedTx = {
        from:from,
        to:to, 
        value:value,
        data:data,
        gasPrice:gasPrice,
        gasLimit:gasLime,
        nonce:nonce, 
        chainId:chainID
    };

    return unsignedTx;

  }catch(e){
    
    console.log("xxl service getUnsignTx exception ...");
    console.log(e);

    return null;
  }



}

export const  stringToHex = (str):string =>{

  let bufStr = Buffer.from(str, 'utf8');
  return bufStr.toString('hex');

}


export const dec2hex = (str):string => { 
  var dec = str.toString().split(''), sum = [], hex = [], i, s
  while(dec.length){
      s = 1 * dec.shift()
      for(i = 0; s || i < sum.length; i++){
          s += (sum[i] || 0) * 10
          sum[i] = s % 16
          s = (s - sum[i]) / 16
      }
  }
  while(sum.length){
      hex.push(sum.pop().toString(16))
  }
  return hex.join('')
}

