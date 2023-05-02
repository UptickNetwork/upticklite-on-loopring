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

describe('loopring 1updateAccount', function() {


    let baseURL = "http://localhost:8901/";
    
    let fromAddress = "0x9e01042a54466C3ddeB6E1e4512AF239d1184330";
    let fromPriv = "0x1ddff530cd1f5346ff1afd367a1ebba47755f45a2a786f127ddbc7a859c67962";

    let web3Http = "https://goerli.infura.io/v3/a06ed9c6b5424b61beafff27ecc3abf3"

    it('transfer looping ...', async function() {


      let fullURL = baseURL + "lrc/getKeyPairMsg"
      const web3 = new Web3(new Web3.providers.HttpProvider(web3Http));
      let jsonData = {address:fromAddress}
      console.log(jsonData);
      
      //1.post getKey Pair Msg
      axios
      .post(fullURL,jsonData)
      .then( async res => {

        console.log(res.data);

        let signMsg = res.data.data;
       
        let keyPairSignature = await web3.eth.accounts.sign(signMsg,fromPriv).signature
        console.log(keyPairSignature);

        //2.pack Update Account
        fullURL = baseURL + "lrc/getUpdateAccountMsg"
        let jsonUpdateAccountMsg = {
          keyPairSignature,
          fromAddress
        }

        console.log("xxl getUpdateAccountMsg ");
        console.log(jsonUpdateAccountMsg);

        axios
          .post(fullURL,jsonUpdateAccountMsg)
          .then( async res => {            
            console.log(res.data);

            //3.uptickAccontMsg Tx 
            let ecdsaSignature = await web3.eth.accounts.sign(res.data.data,fromPriv).signature;
            console.log("ecdsaSignature",ecdsaSignature);
            
            fullURL = baseURL + "lrc/sendUpdateAccount"
            let jsonSendUpdateAccount = {
              keyPairSignature,
              fromAddress,
              ecdsaSignature
            }

            console.log("xxl sendUpdateAccount ",jsonSendUpdateAccount);

            axios
              .post(fullURL,jsonSendUpdateAccount)
              .then( async res => {            
                console.log(res.data);
            })

        })

      })
    })

});


