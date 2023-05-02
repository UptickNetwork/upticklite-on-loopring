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


    let baseURL = "http://13.213.149.227:8901/";
    //let baseURL = "http://localhost:8901/";
    let nftData = "0x27a1cea8489669a3a0dbaaf8d3dfade1f156e2ecdc175424031f5c131de7a345";

    it('transfer nft balances', async function() {

      let fullURL = baseURL + "lrc/getNftHolders"
     
      let jsonData = {
        nftData
      }
  
      //1.post getKey Pair Msg
      axios
      .post(fullURL,jsonData)
      .then( async res => {

        console.log(res.data.data);
        
      })
    })

});


