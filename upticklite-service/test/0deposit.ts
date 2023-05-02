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
var assert = require('assert');
var axios = require('axios')
var Web3 = require('web3')

describe('loopring transfer', function() {

    let baseURL = "http://localhost:8901/";
    //let baseURL = "http://13.213.149.227:8901/";

    let fromAddress = "0x9e01042a54466C3ddeB6E1e4512AF239d1184330";
    let fromPriv = "0x1ddff530cd1f5346ff1afd367a1ebba47755f45a2a786f127ddbc7a859c67962";


    let web3Http = "https://goerli.infura.io/v3/a06ed9c6b5424b61beafff27ecc3abf3"

    it('transfer looping', async function() {

      let fullURL = baseURL + "lrc/packDepositTx"
      let value = 0.003;
      let fee = 0;
      let gasPrice = 10;
      let gasLimit = 0x7a1200;
      let nonce = 0;

      // 1.get Transaction Count
      const web3 = new Web3(new Web3.providers.HttpProvider(web3Http));
      nonce = await web3.eth.getTransactionCount(fromAddress)
      let jsonData = {
        fromAddress,
        value,
        fee,
        gasPrice,
        gasLimit,
        nonce
      }
      console.log(jsonData);
      
      //2.post pack deposit tx
      axios
      .post(fullURL,jsonData)
      .then( async res => {

        console.log(res.data);

        //3.sign Tx 
        let signedTx = await web3.eth.accounts.signTransaction(
          res.data.data,
          fromPriv
        );
        console.log(signedTx);

        //4.brodcastTx
        const jsonBroadcastTx = { 
          signTx:  signedTx
        };

        console.log(jsonBroadcastTx);

        fullURL = baseURL + "lrc/broadcastTx"
        axios
          .post(fullURL,jsonBroadcastTx)
          .then( async res => {            
            console.log(res.data);
        })

      })
    })
  

});


