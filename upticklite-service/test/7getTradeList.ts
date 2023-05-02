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

describe('loopring get api key', function() {


    let baseURL = "http://localhost:8901/";


    it('transfer nft balances', async function() {

      let fullURL = baseURL + "lrc/getTradesList"
     
      let jsonData = {
        offset:1,
        limit:50
      }
      // other optional params
      // market "LRC-ETH"
      // orderHash "0x9d114267e8b261457d567093c13cf3deea5f14c9235be26c6fa833dba12a9632"
      // fromId The begin id of the query
      // fillTypes request.getUserTxs.fillTypes Allowable : ['dex', 'amm']

      //1.post getKey Pair Msg
      axios
      .post(fullURL,jsonData)
      .then( async res => {

        console.log(res.data.data);
        
      })
    })

});


