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
const { utils } = require('ethers')

describe('nft trade ', function() {


    let baseURL = "http://localhost:8901/";
    
    let fromAddress = "0x46B3C2641B7493dfE155b0E4034AaD7D1b273B60";
    let fromPriv = "0xb6387a6127026159b7ea5927536b68e517db82c806b1849839f8ff13b97c7be8";

    let toAddress = "0x79A475E22c347206Fb774A7681f3b664B1673e79";
    let toPriv = "3fc1287909ea6237674748ba7d160e4f3f7cd68ee48065b3dc65124b6ac489de";

    let web3Http = "https://goerli.infura.io/v3/a06ed9c6b5424b61beafff27ecc3abf3"

    it('nft trade ', async function() {


      let fullURL = baseURL + "lrc/getKeyPairMsg"
      const web3 = new Web3(new Web3.providers.HttpProvider(web3Http));
      
      //1.post make getKey Pair Msg
      let fromJsonData = {address:fromAddress}      
      let makerKeyPairResult = await axios.post(fullURL,fromJsonData);
      let makerKeyPairData = makerKeyPairResult.data.data;
      console.log("1.0 maker KeyPair Result data->",makerKeyPairData);

      let makerKeyPairSignature = await web3.eth.accounts.sign(makerKeyPairData,fromPriv).signature
      console.log("1.1 maker KeyPair Signature->",makerKeyPairSignature);

      //2.get maker order
      let makeTokenAmount = utils.parseEther("0.4").toString();
      //"0":"ETH", "1":"LRC", "2":"USDT", "4":"LP-LRC-ETH", "6": "DAI", "7":"LP-ETH-USDT", "8":"USDC", "9":"LP-USDC-ETH"
      fullURL = baseURL + "lrc/getNftOrder"
      let makerOrderRequest = {
        address:fromAddress,
        keyPairSignature:makerKeyPairSignature,
        isSell:true,
        nftId:224,
        nftAmount:2,
        tokenId:1,
        tokenAmount:makeTokenAmount
      }

      let makerOrderResult = await axios.post(fullURL,makerOrderRequest);
      let makerOrderData = makerOrderResult.data.data;
      console.log("2.0 maker Order Data->",makerOrderData);

      //3.post taker getKey Pair Msg
      fullURL = baseURL + "lrc/getKeyPairMsg"
      let toJsonData = {address:toAddress}      
      let takerKeyPairResult = await axios.post(fullURL,toJsonData);
      let takerKeyPairData = takerKeyPairResult.data.data;
      console.log("3.1 taker KeyPair Result data->",takerKeyPairData);

      let takerKeyPairSignature = await web3.eth.accounts.sign(takerKeyPairData,toPriv).signature
      console.log("3.2 taker KeyPair Signature->",takerKeyPairSignature);

      //4.get taker order
      let takerTokenAmount1 = utils.parseEther("0.2").toString();
      fullURL = baseURL + "lrc/getNftOrder"
      let taker1OrderRequest = {
        address:toAddress,
        keyPairSignature:takerKeyPairSignature,
        isSell:false,
        nftId:224,
        nftAmount:1,
        tokenId:1,
        tokenAmount:takerTokenAmount1
      }

      let taker1OrderResult = await axios.post(fullURL,taker1OrderRequest);
      let taker1OrderData = taker1OrderResult.data.data;
      console.log("4.0 taker1 Order Data->",taker1OrderData);


      //5.nft trade
      fullURL = baseURL + "lrc/tradeNFT"
      let tradeRequest = {
        makerOrder:makerOrderData.order,
        makerOrderEddsaSignature:makerOrderData.orderEddsaSignature,
        makerFeeBips:1000,
        takerOrder:taker1OrderData.order,
        takerOrderEddsaSignature:taker1OrderData.orderEddsaSignature,
        takerFeeBips:100  
      }


      console.log("xxl tradeRequest ",tradeRequest);

      let tradeResult = await axios.post(fullURL,tradeRequest);
      let tradeData = tradeResult.data.data;
      console.log("5.0 taker1 Order Data->",tradeData);

    })




});


