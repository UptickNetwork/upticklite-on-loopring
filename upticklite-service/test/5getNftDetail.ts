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
import { generateKeyPair } from "crypto";

var assert = require('assert');
var axios = require('axios')
var Web3 = require('web3')

describe('get nft detail from nftId', function() {


    let baseURL = "http://localhost:8901/";
    // let fromAddress = "0x79A475E22c347206Fb774A7681f3b664B1673e79";
    let fromAddress = "0x8a86bacE2089204e8eBf25f55dBb9582e8556302";
    it('get nftId ', async function() {

      let fullURL = baseURL + "lrc/getNftDetailFromNftId"
     
      let jsonData = {
        fromAddress,
        nftId:224
      }
  
      //1.post getKey Pair Msg
      axios
      .post(fullURL,jsonData)
      .then( async res => {

        console.log(res.data.data);
        
      })
    })

});


