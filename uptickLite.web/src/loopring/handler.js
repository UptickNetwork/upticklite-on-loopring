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
var Web3 = require('web3');

let baseURL = "/uptickservice";
// let rlcURL = "https://uat2.loopring.io";
var axios = require('axios')
var bigInt = require("big-integer");
const BigNumber = require('big-number');

const { toUnitValue ,getMaxFeeBips} = require("../utils/helper");

const { unlockShow, unlockhide } = require("@/components/UnlockDlg");
// import unlockDlg from "@/components/UnlockDlg";
// unlockDlg();

import {
    TOKEN_INFO,
    LOOPRING_EXPORTED_SETTING
} from "./const";

import {
    UserAPI,
    ExchangeAPI,
    NFTAPI,
    GlobalAPI,
    sleep,
} from "@loopring-web/loopring-sdk";
import {
    LockApi  
} from "@uptsmart/loopring-sdk";

import {
    UserAPI as OrgUserApi
} from "@loopring-web/loopring-sdk";


import api from "@/api";

// wallet connect
import WalletConnectProvider from "@walletconnect/web3-provider";

// const sdk = require("@uptsmart/loopring-sdk");
const sdk = require("@loopring-web/loopring-sdk");
const web3Obj = new Web3();

let LoopringAPI,LoopringOrgApi;
let ChainId = sdk.ChainId.GOERLI;

let lrcAccount = {};
let getApiKeyCount = 0;

let last_time = 0;

let fun_back;


const getSessionObj = function(key) {
    let val = sessionStorage.getItem(key);
    if(val) {
      let obj = JSON.parse(val);
      return obj;
    }
    return null;
}

let walletconnect_appstate;

export class LRCHandler {

    /**
     * @description             init
     * @param address           chaiId of the service
     */
    constructor(fun) {
        fun_back = fun;
        try {
            let env = process.env.VUE_APP_ENVIRONMENT;
            if(env == "testNet") {
                ChainId = sdk.ChainId.GOERLI;
            } else if(env == "pro") {
                ChainId = sdk.ChainId.MAINNET;
            }
            LoopringAPI = {
                userAPI: new UserAPI({ chainId: ChainId },{ChainId}),
                exchangeAPI: new ExchangeAPI({ chainId: ChainId },{ChainId}),
                nftAPI: new NFTAPI({ chainId: ChainId },{ChainId}),
                globalAPI: new GlobalAPI({ chainId: ChainId },{ChainId}),
                lockAPI:new LockApi({ chainId: ChainId })
              
            };
  // ,lockAPI:new LockApi({ chainId: ChainId })
            LoopringOrgApi = {
                orgUserApi:new OrgUserApi({ chainId: ChainId})
            }

            if(window.walletType == 3) {
                const json = sessionStorage.getItem("walletconnect_appstate");
                walletconnect_appstate = JSON.parse(json);
            }          
            // this.getApiKeyCache();
        } catch (error) {
            console.log("LRCHandler constructor",error);
        }
    }

    async getPriceLimit(nftTokenAddress) {
        try {
            let fullURL = process.env.VUE_APP_LRCURL + "/api/v3/nft/info/orderUserRateAmount"
            const { apiKey,accInfo} = lrcAccount;
            let params = { 
                accountId: accInfo.accountId,
                nftTokenAddress
            };
            let res = await axios({
                method: 'get', //you can set what request you want to be
                url: fullURL,
                params: params,
                headers: {
                    "X-API-KEY": apiKey
                }
            })
            return res.data.amounts;

        }catch(e){
            console.log("checkMinPrice error",e)
        }

    }

    async getApiKeyCache() {
        try {
            let acc = sessionStorage.getItem("KEY_LRCAccount");
            if (acc && acc.length > 0) {
                let obj = JSON.parse(acc);
                if (obj.time && obj.accountId) {
                    await this.getSession();
                    return true;
                }
            }
           let res =  await this.getApiKeyDetail();
           if(res){
            return true
           }else{
            return false 
           }
        } catch (e) {
            console.log(e);
            if(e.message.indexOf("timeout")>=0) {
                getApiKeyCount = 0;
                unlockhide();
                unlockShow({message:"Request timeout"});
                setTimeout(() => {
                    unlockhide();
                }, 3000);
            }
        }
    }

    async getApiKeyDetail() {
        if(getApiKeyCount > 0) {  // Prevent multiple calls
            setTimeout(() => {
                getApiKeyCount = 0;
            }, 2000);
            return;
        } 
        getApiKeyCount++;
        
        unlockShow();
        
        const fromAddress = await this.getAccountAddress();
        let info = await this.getAccountDetail(fromAddress, ChainId);
        if(!info) {
            getApiKeyCount = 0;
            window.eventBus.$emit("SignatureEvent", 1);
            unlockhide();
            if(fun_back) {
                fun_back(true);
            }
            return false;
        }
        if(info == "account not exit"){
            return true
        }
        if(!info.apiKey){
            return true
        }
        const { exchangeInfo, accInfo, keySeed, eddsaKey, apiKey, activeFee, storageId, nftTokenAddress }  = info;
        this.setSession(exchangeInfo, accInfo, keySeed, eddsaKey, info.apiKey, activeFee, storageId, nftTokenAddress);
        getApiKeyCount = 0;  // Set to 0 for subsequent calls

        window.eventBus.$emit("SignatureEvent", 2);
        unlockhide();
       
        let bscOwner;
        if (accInfo != null && accInfo.owner!=null) {
         
          bscOwner = accInfo.owner;
		
		  if(accInfo.nonce>0){
			let accountParam={ 
				address: bscOwner,
			 accountStatus: 3, 
			 accountId: accInfo.accountId,
			 } 
	
		  let resolve = await api.home.reverseAddrEns(bscOwner);
		  if(resolve.data&&resolve.data.length>0){
		  		let ens = resolve.data[0].ens;
		  		accountParam.domain=ens
		  }
          await api.home.loopringUserSave(accountParam);
          return true
		
		 
		  
		  }
     
        }
       
        if(fun_back) {
            fun_back(true);
        }
       
    }

    async checkAndSetApiKeySession(){
        let eddsaKey = getSessionObj("KEY_eddsaKey");
        let accInfo = getSessionObj("KEY_accInfo");
        if(!accInfo) return;

        const { apiKey } = await LoopringAPI.userAPI.getUserApiKey({ accountId: accInfo.accountId },eddsaKey.sk);
        if(typeof(apiKey) == "undefined"){
            return false;
        }else{
            sessionStorage.setItem("KEY_apiKey", apiKey);
            return true;
        }
    }

    async getSession() {
        let exchangeInfo = getSessionObj("KEY_exchangeInfo");
        let accInfo = getSessionObj("KEY_accInfo");
        let keySeed = sessionStorage.getItem("KEY_keySeed");
        let eddsaKey = getSessionObj("KEY_eddsaKey");
        let apiKey = sessionStorage.getItem("KEY_apiKey");
        let activeFee = getSessionObj("KEY_activeFee");
        
        lrcAccount = { exchangeInfo, accInfo, keySeed, eddsaKey, apiKey, activeFee };
      
    }

    setSession(exchangeInfo, accInfo, keySeed, eddsaKey, apiKey, activeFee, storageId, nftTokenAddress) {

        // cache info
        lrcAccount = { exchangeInfo, accInfo, keySeed, eddsaKey, apiKey, activeFee, storageId, nftTokenAddress };
        sessionStorage.setItem("KEY_exchangeInfo", JSON.stringify(exchangeInfo));
        sessionStorage.setItem("KEY_accInfo", JSON.stringify(accInfo));
        sessionStorage.setItem("KEY_keySeed", keySeed);
        sessionStorage.setItem("KEY_eddsaKey", JSON.stringify(eddsaKey));
        sessionStorage.setItem("KEY_apiKey", apiKey);
        sessionStorage.setItem("KEY_activeFee", JSON.stringify(activeFee));
        sessionStorage.setItem("KEY_LRCAccount", JSON.stringify({ time: new Date().getTime(), accountId: accInfo.accountId }));
    }

    async getAccount() {
        const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
        
        window.web3 = await this.generateWeb3();
        return accounts;
    }

    async getAddresss() {
        // const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
        // if(accounts && accounts.length>0) {
        //     const fromAddress = accounts[0];
        //     return fromAddress;
        // }
        return await this.getAccountAddress();
    }


    async getUserBalances(tokenId) {
 
        await this.getApiKeyCache();
        if(!lrcAccount.accInfo.accountId){
            return
        }
        
        const { userBalances } = await LoopringAPI.userAPI.getUserBalances(
            { accountId: lrcAccount.accInfo.accountId, tokens: "" }, lrcAccount.apiKey
        );
       
        let amountValue = 0;
        let lockedValue = 0;
        let balanceJson = {
            amountValue:amountValue,
            lockedValue:lockedValue

        }
        if(userBalances[tokenId]) {
            let env = process.env.VUE_APP_ENVIRONMENT;
            if(env == "testNet"){
                if(tokenId == 2 || tokenId == 8){       //USDT and USDC
                    amountValue = userBalances[tokenId].total  / 1000000;
                     lockedValue =userBalances[tokenId].locked  / 1000000;
                }else{
                    amountValue = web3Obj.utils.fromWei(userBalances[tokenId].total ,"ether");
                    lockedValue = web3Obj.utils.fromWei(userBalances[tokenId].locked ,"ether");
                } 
            }else{
                if(tokenId == 3 || tokenId == 6){       //USDT and USDC
                    amountValue = userBalances[tokenId].total  / 1000000;
                     lockedValue =userBalances[tokenId].locked  / 1000000;
                }else{
                    amountValue = web3Obj.utils.fromWei(userBalances[tokenId].total ,"ether");
                    lockedValue = web3Obj.utils.fromWei(userBalances[tokenId].locked ,"ether");
                } 
            }
            
           
			 balanceJson.amountValue = amountValue
            balanceJson.lockedValue = lockedValue
               
            return balanceJson
        }

        return balanceJson;

    }

    async getUserEthLayer2Balances() {
        try {
            await this.getApiKeyCache();
            
            let accInfo;
            if(typeof(lrcAccount.accInfo) == "undefined"){
                return;
                // await this.getSession();
                // accInfo = getSessionObj("KEY_accInfo");
            }else{
                accInfo = lrcAccount.accInfo;
            }

            const { userBalances } = await LoopringAPI.userAPI.getUserBalances(
                { accountId: accInfo.accountId, tokens: "0" }, ""
            );
            if(userBalances.length > 0) return;
            return web3Obj.utils.fromWei(userBalances[0].total,"ether")
        } catch (error) {
            return;
        }
    }

    async getAllUserBalances() {
		const fromAddress = await this.getAccountAddress();
		let info = await this.getAccountDetail(fromAddress, ChainId);
		if(!info) {
		   lrcAccount.accInfo= info;
		}
		
		
        if(!lrcAccount.accInfo.accountId){
            return;
        }
        await this.getApiKeyCache();
        
        const { userBalances } = await LoopringAPI.userAPI.getUserBalances(
            { accountId: lrcAccount.accInfo.accountId, tokens: "" }, lrcAccount.apiKey
        );

        for (const key in userBalances) {
            if (Object.hasOwnProperty.call(userBalances, key)) {
                const ele = userBalances[key];
                let env = process.env.VUE_APP_ENVIRONMENT;
                if(env == "testNet"){
                    if(ele.tokenId == 2 || ele.tokenId == 8){       //USDT and USDC
                        ele.format = ele.total / 1000000;
                    }else{
                        ele.format = web3Obj.utils.fromWei(ele.total ,"ether");
                    }

                }else{
                    if(ele.tokenId == 3 || ele.tokenId == 6){       //USDT and USDC
                        ele.format = ele.total / 1000000;
                    }else{
                        ele.format = web3Obj.utils.fromWei(ele.total ,"ether");
                    }

                }
               
            }
        }

        return userBalances;
        // let amountValue = 0;
        // let lockedValue = 0;
        // let balanceJson = {
        //     amountValue:amountValue,
        //     lockedValue:lockedValue

        // }
        // if(userBalances[tokenId]) {
            
        //     if(tokenId == 2 || tokenId == 8){       //USDT and USDC
        //         amountValue = userBalances[tokenId].total  / 1000000;
		// 		 lockedValue =userBalances[tokenId].locked  / 1000000;
        //     }else{
        //         amountValue = web3Obj.utils.fromWei(userBalances[tokenId].total ,"ether");
        //         lockedValue = web3Obj.utils.fromWei(userBalances[tokenId].locked ,"ether");
        //     } 
		// 	 balanceJson.amountValue = amountValue
        //     balanceJson.lockedValue = lockedValue
               
        //     return balanceJson
        // }

        // return balanceJson;
    }

    async deposit(token, value, fee, gasPrice, gasLimit) {
        const acc = await window.ethereum.request({ method: 'eth_requestAccounts' });
        const fromAddress = acc[0];
        
        const web3 = await this.generateWeb3();
        
        const nonce = await web3.eth.getTransactionCount(fromAddress);

        const { exchangeInfo } = await LoopringAPI.exchangeAPI.getExchangeInfo();

        if(token != "ETH") {
            await sdk.approveMax(
                web3,
                fromAddress,
                TOKEN_INFO.tokenMap[token].address, // LRC address  {tokenIdMap} = getTokens();  tokenIdMap['LRC']
                exchangeInfo.depositAddress, //{exchangeInfo} = getExchangeInfo()  exchangeInfo.depositAddress
                gasPrice,
                gasLimit,
                ChainId,
                nonce,
                true
            );
        }

        const response = await sdk.deposit(
            web3,
            fromAddress,
            exchangeInfo.exchangeAddress,
            TOKEN_INFO.tokenMap[token],
            value,
            fee,
            gasPrice,
            gasLimit,
            ChainId,
            nonce,
            true
        );
        console.log("deposit response", response);
        return response;
    }

    async getFee(fromAddress,baseUrl){
      let accInfo = getSessionObj("KEY_accInfo");
      let apiKey = sessionStorage.getItem("KEY_apiKey");

      let nftTokenAddress = await this.computeNFTAddress(fromAddress,baseUrl);
      const nftOffchainFee = await LoopringAPI.userAPI.getNFTOffchainFeeAmt(
        {
            accountId: accInfo.accountId,
            tokenAddress: nftTokenAddress,
            requestType: sdk.OffchainNFTFeeReqType.NFT_MINT,
        },
        apiKey
      );

      let fee = web3Obj.utils.fromWei(nftOffchainFee.fees["LRC"].fee,"ether");
      return fee;

    }

    async getTransferFee() {
		await this.getApiKeyCache();
        let accInfo = getSessionObj("KEY_accInfo");
        let apiKey = sessionStorage.getItem("KEY_apiKey");
			
        const nftOffchainFee = await LoopringAPI.userAPI.getOffchainFeeAmt({
            accountId: accInfo.accountId,
            requestType: sdk.OffchainNFTFeeReqType.NFT_TRANSFER,
        }, apiKey);
        let fee = web3Obj.utils.fromWei(nftOffchainFee.fees["LRC"].fee,"ether");
        return fee;
    }

    async getAccountInfo(){

        try {
            const fromAddress = await this.getAccountAddress();
            
            const detail = await this.getAccountDetail(fromAddress, ChainId);
            if(!detail) {
                return { deposit: 1, accountId: 0 };
            }

            const { accInfo, apiKey } = detail;
            if(!accInfo) {
                return { deposit: 1, accountId: 0 };
            }
            else if(accInfo && accInfo.accountId && apiKey == undefined) {
               
                return { deposit: 2, accountId:accInfo.accountId };
            }
            else if(accInfo && accInfo.accountId && apiKey != undefined) {
                return { deposit: 3 ,accountId:accInfo.accountId};
            }
        } catch (error) {
            console.log("getAccountInfo",error);
        }
        return { deposit: 1, accountId: 0 };
    }

    async getAccountInfoSession(){
        try {
            await this.getApiKeyCache(); 
            const { accInfo } = lrcAccount;

            return { accountId: accInfo.accountId };
        } catch (error) {
            console.log("getAccountInfo",error);
        }
        return { deposit: 1, accountId: 0 };
    }

    async getAccountInfoOther(fromAddress){

        try {
            const account = await LoopringAPI.exchangeAPI.getAccount({
                owner: fromAddress
            });
            return account;
        } catch (error) {
            console.log("getAccountInfo",error);
        }
        return { deposit: 1, accountId: 0 };
    }

    /**
     * @description updateAccount update the account
     */
    async updateAccount(token) {
        try {
            
            window.web3 = await this.generateWeb3();
            await this.getApiKeyCache(); 
            const { exchangeInfo, accInfo, eddsaKey, activeFee, keySeed ,apiKey} = lrcAccount;

            if(accInfo && accInfo.accountId && apiKey != "undefined") {
                return { activate: true ,accountId:accInfo.accountId};
            }

            //6 updateAccount (active or rest）
            let nonce = accInfo.nonce;
            //if(window.walletType == 3) nonce = accInfo.nonce-1;
            const request = {
                exchange: exchangeInfo.exchangeAddress,
                owner: accInfo.owner,
                accountId: accInfo.accountId,
                publicKey: { x: eddsaKey.formatedPx, y: eddsaKey.formatedPy },
                maxFee: {
                    tokenId: TOKEN_INFO.tokenMap[token].tokenId,
                    volume: activeFee.fees[token].fee,
                },
                keySeed,
                validUntil: LOOPRING_EXPORTED_SETTING.validUntil,
                nonce: nonce,
            };
            const resultTx = await LoopringAPI.userAPI.updateAccount({
                request: request,
                web3: window.web3,
                chainId: ChainId,
                walletType: this.getWalletType(),
                isHWAddr: false,
            });
            return resultTx;

        } catch (e) {
            console.log("updateAccount error : ", e);
        }

    }

    /**
     * @description tranferNft
     */
    async mintNft(
        nftBaseUri,
        nftTokenAddress,
        ipfs,
        amount,
        royaltyPercentage
    ) {
        //nft ID
        let nftId = LoopringAPI.nftAPI.ipfsCid0ToNftID(ipfs)
        //get account
        window.web3 = await this.generateWeb3();
        
        await this.getApiKeyCache();
        const { exchangeInfo, accInfo, eddsaKey, apiKey } = lrcAccount; 
        const storageId = await LoopringAPI.userAPI.getNextStorageId(
            {
                accountId: accInfo.accountId,
                sellTokenId: TOKEN_INFO.tokenMap["LRC"].tokenId,
            },
            apiKey
        );
        //7 nft off chain fee
        const nftOffchainFee = await LoopringAPI.userAPI.getNFTOffchainFeeAmt(
            {
                accountId: accInfo.accountId,
                tokenAddress: nftTokenAddress,
                requestType: sdk.OffchainNFTFeeReqType.NFT_MINT,
            },
            apiKey
        );

        let reqs = {
            counterFactualNftInfo: {
                nftFactory: sdk.NFTFactory[ChainId],
                nftOwner: accInfo.owner,
                nftBaseUri,
            },
            exchange: exchangeInfo.exchangeAddress,
            minterId: accInfo.accountId,
            minterAddress: accInfo.owner,
            toAccountId: accInfo.accountId,
            toAddress: accInfo.owner,
            nftType: 0,
            tokenAddress: nftTokenAddress,
            nftId, //nftId.toString(16),
            amount,
            validUntil: LOOPRING_EXPORTED_SETTING.validUntil,
            storageId: storageId.offchainId,
            maxFee: {
                tokenId: TOKEN_INFO.tokenMap["LRC"].tokenId,
                amount: nftOffchainFee.fees["LRC"].fee,
                //amount: "6810000000000000000"
            },
            royaltyPercentage,
            forceToMint: true, // suggest use as false, for here is just for run test
        };

        // change to loopring org sdk of 2.0.22
        // Step 7. Mint
        // const response = await LoopringAPI.userAPI.submitNFTMint({
        //     request: reqs,
        //     web3: window.web3,
        //     chainId: ChainId,
        //     walletType: this.getWalletType(),
        //     eddsaKey: eddsaKey.sk,
        //     apiKey: apiKey,
        // });
        const response = await LoopringOrgApi.orgUserApi.submitNFTMint({
            request: reqs,
            web3: window.web3,
            chainId: ChainId,
            walletType: this.getWalletType(),
            eddsaKey: eddsaKey.sk,
            apiKey: apiKey
        },{
            accountId:accInfo.accountId,
            counterFactualInfo: {
                nftFactory: sdk.NFTFactory[ChainId],
                nftOwner: accInfo.owner,
                nftBaseUri,
            }
        });

        return { nftTokenAddress, nftId, response };
    }

    async getTokenAddress() {
        const fromAddress = await this.getAccountAddress();

        const { nftTokenAddress } = await this.getAccountDetail(fromAddress, ChainId, "");
        return nftTokenAddress;
    }


    /**
     * @description tranferNft
     * @param tokenId           tokenId from trade
     *                          "0": "ETH",
     *                          "1": "LRC",
     *                          "2": "USDT",
     *                          "4": "LP-LRC-ETH",
     *                          "6": "DAI",
     *                          "7": "LP-ETH-USDT",
     *                          "8": "USDC",
     *                          "9": "LP-USDC-ETH",
     */
    
    async submitNFTInTransfer(tokenId, nftData, nftAmount, toAddress, nftTokenAddress,nftBaseUri ) {
        const fromAddress = await this.getAccountAddress();
        window.web3 = await this.generateWeb3();
        await this.getApiKeyCache();

        const { exchangeInfo, accInfo, eddsaKey, apiKey } = lrcAccount;
        const storageId = await LoopringAPI.userAPI.getNextStorageId(
            {
              accountId: accInfo.accountId,
              sellTokenId: tokenId,
            },
            apiKey
          );
        //7 nft off chain fee
        const nftOffchainFee = await LoopringAPI.userAPI.getOffchainFeeAmt({
            accountId: accInfo.accountId,
            requestType: sdk.OffchainNFTFeeReqType.NFT_TRANSFER,
        }, apiKey);

        let request = {
            exchange: exchangeInfo.exchangeAddress,
            fromAccountId: accInfo.accountId,
            fromAddress: fromAddress,
            toAccountId: 0, // toAccountId is not required, input 0 as default
            toAddress: toAddress,
            token: {
                tokenId: tokenId,
                nftData: nftData,
                amount: nftAmount,
            },
            maxFee: {
                tokenId: TOKEN_INFO.tokenMap["LRC"].tokenId,
                amount: nftOffchainFee.fees["LRC"].fee,
            },
            storageId: storageId.offchainId,
            validUntil: LOOPRING_EXPORTED_SETTING.validUntil,
            // memo: '',
        };
		let getCodeRes=await window.web3.eth.getCode(fromAddress)
		if(getCodeRes!=null&&getCodeRes.length>10){
            try {
                // deployed contract wallet
			let transferResult = await LoopringOrgApi.orgUserApi.submitNFTInTransfer({
			    request: request,
			    web3: window.web3,
			    chainId: ChainId,
			    walletType: this.getWalletType(),
			    eddsaKey: eddsaKey.sk,
			    apiKey,
            });
          
			 return transferResult;
            } catch (error) {
                let resTransfer ={};
                resTransfer.code =500006
             
                return resTransfer
                
            }
			
		}else{
			let fullURL = process.env.VUE_APP_LRCURL + "/api/v3/counterFactualInfo"
			let params = {
			    address:accInfo.owner
			}
			let transferResult;
			try{
			    let res = await axios({
			        method: 'get', //you can set what request you want to be
			        url: fullURL,
			        params: params
			    })
			    if(res.data["resultInfo"] != undefined){
			    
			        transferResult = await LoopringOrgApi.orgUserApi.submitNFTInTransfer({
			            request: request,
			            web3: window.web3,
			            chainId: ChainId,
			            walletType: this.getWalletType(),
			            eddsaKey: eddsaKey.sk,
			            apiKey,
			        });
			    }else{
					
			        transferResult = await LoopringOrgApi.orgUserApi.submitNFTInTransfer({
			            request: request,
			            web3: window.web3,
			            chainId: ChainId,
			            walletType: this.getWalletType(),
			            eddsaKey: eddsaKey.sk,
			            apiKey,
			        },{
			            accountId:accInfo.accountId,
			            counterFactualInfo: res.data
			        });
			    }
			    return transferResult;
			    
			}catch(e){
			    console.log("xxl e ",e);
			    transferResult = await LoopringOrgApi.orgUserApi.submitNFTInTransfer({
			        request: request,
			        web3: window.web3,
			        chainId: ChainId,
			        walletType: this.getWalletType(),
			        eddsaKey: eddsaKey.sk,
			        apiKey,
			    });
			
			    return transferResult
			}
		}
       
    
      
        

    }
    async makerOrder(nftTokenId, nftData, nftAmount, buyTokenId, buyAmount,totalRoyalty,validUntil) {
        buyAmount = BigNumber(toUnitValue(web3Obj,buyTokenId,buyAmount.toString())).multiply(nftAmount);   
          
        const fromAddress = await this.getAccountAddress();
   
        let detail = await this.getAccountDetail(fromAddress, ChainId);
        if(!detail) return;
     
        const { exchangeInfo, accInfo, eddsaKey,apiKey } = detail;
        const storageId = await LoopringAPI.userAPI.getNextStorageId({
              accountId: accInfo.accountId,
              sellTokenId: nftTokenId,
            },
            apiKey
        );
        
        const makerOrder = {
            exchange: exchangeInfo.exchangeAddress,
            accountId: accInfo.accountId,
            storageId: storageId.orderId,
            sellToken: {
                tokenId: nftTokenId,
                nftData: nftData,
                amount: nftAmount,
            },
            buyToken: {
                tokenId: buyTokenId.toString(),
                amount: buyAmount.toString(),
            },
            allOrNone: false,
            fillAmountBOrS:false,
            validUntil: validUntil,
            maxFeeBips: getMaxFeeBips(totalRoyalty),
        };
       

        let eddsaSignature= await sdk.get_EddsaSig_NFT_Order(
            makerOrder,
            eddsaKey.sk
        );
        makerOrder.eddsaSignature  = eddsaSignature.result
        console.log( makerOrder.eddsaSignature);
        debugger
        return makerOrder;
    }

    async takerOrder(nftTokenId, nftData, nftAmount, sellTokenId, sellAmount,tradeCost,storageid) {
        try{
            console.log("wxl  takerOrder ---",tradeCost,storageid)
            sellAmount = BigNumber(toUnitValue(web3Obj,sellTokenId,sellAmount));
            
            const fromAddress = await this.getAccountAddress();
            
            let detail = await this.getAccountDetail(fromAddress, ChainId);
            if(!detail) return;
            
            const { exchangeInfo, accInfo, eddsaKey,apiKey } = detail;
            // const storageId = await LoopringAPI.userAPI.getNextStorageId({
            //     accountId: accInfo.accountId,
            //     sellTokenId: sellTokenId,
            //     },
            //     apiKey
            // );
			//链上获取最新的
            
            const takerOrder = {
                exchange: exchangeInfo.exchangeAddress,
                accountId: accInfo.accountId,
                storageId: storageid,
                sellToken: {
                    tokenId: sellTokenId.toString(),
                    amount: sellAmount.toString(),
                },
                buyToken: {
                    tokenId: nftTokenId,
                    nftData: nftData,
                    amount: nftAmount,
                },
                allOrNone: false,
                fillAmountBOrS:true,
                validUntil: LOOPRING_EXPORTED_SETTING.validUntil,
                maxFeeBips: tradeCost,
            };
            
  
            let eddsaSignature = sdk.get_EddsaSig_NFT_Order(
                takerOrder,
                eddsaKey.sk
            );
            takerOrder.eddsaSignature = eddsaSignature.result
            
            return takerOrder;
        }catch(e){
            console.log("takerOrder error is :",e);
            return null
        }
    }


    async getAccountStatus(fromAddress){

        //1 get exchange Info
        const { exchangeInfo } = await LoopringAPI.exchangeAPI.getExchangeInfo()

        //2 get account info
        const { accInfo } = await LoopringAPI.exchangeAPI.getAccount({
            owner: fromAddress
        });
        if(!accInfo ||  accInfo.nonce == 0 ) return 1;

        //3 user keySeed generateKeyPair        
        let nonce = accInfo.nonce-1;
        //if(window.walletType == 3) nonce = accInfo.nonce-1;
        const keySeed = accInfo.keySeed && accInfo.keySeed !== ""
            ? accInfo.keySeed
            : sdk.GlobalAPI.KEY_MESSAGE.replace(
                "${exchangeAddress}",
                exchangeInfo.exchangeAddress
            ).replace("${nonce}", nonce.toString());

        //4 eddsaKey
        const eddsaKey = await sdk.generateKeyPair({
            web3: window.web3,
            address: accInfo.owner,
            keySeed,
            walletType: this.getWalletType(),
            chainId: ChainId,
        });

        console.log("xxl eddsaKey ",eddsaKey);

        //5 apiKey
        const { apiKey } = await LoopringAPI.userAPI.getUserApiKey(
            { accountId: accInfo.accountId },
            eddsaKey.sk);

        if(apiKey == undefined){
            return 2
        }else{
            return 3;
        }

    }

    async getAccountDetail(fromAddress, chainId) {
        
        window.web3 = await this.generateWeb3();
        //1 get exchange Info
        const { exchangeInfo } = await LoopringAPI.exchangeAPI.getExchangeInfo()

        //2 get account info
        const { accInfo } = await LoopringAPI.exchangeAPI.getAccount({
            owner: fromAddress
        });
        if(!accInfo ) return 'account not exit';

        let nonce = accInfo.nonce ? accInfo.nonce-1:0;
        // if(window.walletType == 3) nonce = accInfo.nonce-1;
        //3 user keySeed generateKeyPair
        const keySeed = accInfo.keySeed && accInfo.keySeed !== ""
            ? accInfo.keySeed
            : sdk.GlobalAPI.KEY_MESSAGE.replace(
                "${exchangeAddress}",
                exchangeInfo.exchangeAddress
            ).replace("${nonce}", nonce.toString());

        let eddsaKey;
        //4 eddsaKey
        try{
            eddsaKey = await sdk.generateKeyPair({
                web3: window.web3,
                address: accInfo.owner,
                keySeed,
                walletType: this.getWalletType(),
                chainId: chainId,
                accountId: accInfo.accountId
            });
        }catch(e){
            console.log("xxl e ",e);
        }
        if(!eddsaKey) return;

        //5 apiKey
        // const { apiKey } = await LoopringAPI.userAPI.getUserApiKey(
        //     { accountId: accInfo.accountId },
        //     eddsaKey.sk);
			
						    let apiKeyInfo;
							let apiKey;
						try{
			 apiKeyInfo= await LoopringAPI.userAPI.getUserApiKey(
						        { accountId: accInfo.accountId },
						        eddsaKey.sk);
						    	 apiKey=apiKeyInfo.apiKey
						}catch(e){
							
						    console.log("xxl e ",e);
						}
			
			

        //6 get Active FeeInfo
        const activeFee = await LoopringAPI.globalAPI.getActiveFeeInfo({
            accountId: accInfo.accountId,
        });

        //7 storageId
        //TODO apiKey is exist or not

        // lrcAccount = {
        //     exchangeInfo, accInfo, keySeed, eddsaKey, apiKey, activeFee, storageId
        // }
        this.setSession(exchangeInfo, accInfo, keySeed, eddsaKey, apiKey, activeFee);
        return {
            exchangeInfo, accInfo, keySeed, eddsaKey, apiKey, activeFee
        }

    }

    /**
     * @description computeNFTAddress
     * @param fromAddress       from address from calc token Address
     * @param nftBaseUri        nftBaseUri for calculate contract address
     */
    async computeNFTAddress(fromAddress,nftBaseUri) {
        
        //1 get exchange Info
        const { exchangeInfo } = await LoopringAPI.exchangeAPI.getExchangeInfo()

        //2 get account info
        const { accInfo } = await LoopringAPI.exchangeAPI.getAccount({
            owner: fromAddress
        });
        if(!accInfo) return;

        await this.getApiKeyCache();
        //3 nftTokenAddress
        const counterFactualNftInfo = {
            nftOwner: accInfo.owner,
            nftFactory: sdk.NFTFactory[ChainId],
            nftBaseUri,
        };
        const nftTokenAddress =
            LoopringAPI.nftAPI.computeNFTAddress(counterFactualNftInfo)
                .tokenAddress || "";
        return nftTokenAddress;

    }


    async getStorageId(sellTokenId) {  
        let accInfo = getSessionObj("KEY_accInfo");
        const storageId = await LoopringAPI.userAPI.getNextStorageId({
            accountId: accInfo.accountId,
            sellTokenId: sellTokenId,
            maxNext:true
            },
            lrcAccount.apiKey,
           
        );
        // const storageId = await LoopringAPI.userAPI.getNextStorageId(
        //     {
        //         accountId: lrcAccount.accInfo.accountId,
        //         sellTokenId: TOKEN_INFO.tokenMap["LRC"].tokenId, // same as maxFee tokenId
        //     },
        //     lrcAccount.apiKey
        // );
        return storageId;
    }

    sdkNFTFactory(nftBaseUri) {
        const counterFactualNftInfo = {
            nftOwner: lrcAccount.accInfo.owner,
            nftFactory: sdk.NFTFactory[ChainId],
            nftBaseUri,
        };
        return counterFactualNftInfo;
    }

    sdkComputeNFTAddress(nftInfo) {
        const nftTokenAddress = LoopringAPI.nftAPI.computeNFTAddress(nftInfo)
                                .tokenAddress || "";
        return nftTokenAddress;
    }
	getIpfsFromNftId(nftId){
        try {
            let ipfs ="https://loopring.mypinata.cloud/ipfs/"+LoopringAPI.nftAPI.ipfsNftIDToCid(nftId);
		    return ipfs;
        } catch (error) {
            console.log("getIpfsFromNftId", error);
            return "";
        }
		
	}

    
    async generateWeb3() {
        if(window.walletType == 3) {
            //  Create WalletConnect Provider
            const provider = new WalletConnectProvider({
                infuraId: "b951a403f3d3426fb2008c6923254dbc", // Required
            });
            await provider.enable();
            //  Create Web3
            const web3 = new Web3(provider);
            return web3;
        }
        return new Web3(window.ethereum);
    }


    async getAccountAddress() {
        let account;
        if(window.walletType == 3) {
            account = walletconnect_appstate.accounts[0];
        }
        else {
            const acc = await window.ethereum.request({ method: 'eth_requestAccounts' });
            account = acc[0];
        }
        return account;
    }

    getWalletType() {
        if(window.walletType == 3) {
            return sdk.ConnectorNames.WalletConnect;
        }
        return sdk.ConnectorNames.MetaMask;
    }

    async Lock(accountId,tokenId,lockedUntil,tag,volume,lockType)
    {
        try {
            console.log("wxl ===== ",lockedUntil,volume     )
            let fullURL = baseURL + "/lockAsset";
            let eddsaKey = getSessionObj("KEY_eddsaKey");
            let timestamp = Date.parse(new Date())/1000
            // let apiKey = sessionStorage.getItem("KEY_apiKey");
            let volumeToBigNumber
          
           
            if(lockType == 'lockNft'){
                volumeToBigNumber=volume
            }else{
            console.log("wxl --- 44444")
             volumeToBigNumber = toUnitValue(web3Obj,tokenId,volume.toString())
             console.log("wxl --- 22222",volumeToBigNumber)
            }
            
            const { exchangeInfo} = lrcAccount;
            let loopingApiData = {
                accountId:Number(accountId), 
                exchange: exchangeInfo.exchangeAddress,
                timestamp:timestamp ,
                token:{
                    tokenId:tokenId,
                    volume:volumeToBigNumber,
                }
    
            }
       
            //  调用loopingSDK服务
            let res = await LoopringAPI.lockAPI.getLockHashAndEddsaSignature(loopingApiData,eddsaKey.sk)

  
            let jsonData = {    
                eddsaSignature:res.sig,
                accountId:Number(accountId),  
                tokenId:tokenId,
                lockedUntil:lockedUntil,
                timestamp: timestamp,
                tag:tag,  
                volume:volumeToBigNumber,   
              }

              let resLockAsset= await axios.post(fullURL, jsonData)
 
              if(resLockAsset.data.hash!=null){
                  return resLockAsset.data;
              }else{
                 return resLockAsset.data.resultInfo;  
              }
            
        } catch (error) {
            console.log("wwwwwww",error)
            
        }
      
		   
    }
	// 获取开户所需要的费用
	async getActiveAccountFee() {
	    try {
            let fullURL = process.env.VUE_APP_LRCURL + "/api/v3/user/offchainFee"
            let env = process.env.VUE_APP_ENVIRONMENT;
	        // const { apiKey,accInfo} = lrcAccount;
	        let params = { 
	            accountId: 10110,
	            requestType: 2
	        };
	        let res = await axios({
	            method: 'get', //you can set what request you want to be
	            url: fullURL,
	            params: params,
	            // headers: {
	            //     "X-API-KEY": apiKey
	            // }
	        })
            const fees = res.data.fees;
            const resp = { };
            fees.forEach(ele => {
                if(env == 'pro'){
                    if(ele.tokenId == 3 || ele.tokenId == 6){       //USDT and USDC
                        ele.format = (ele.fee  / 1000000 * 2).toString();
                    }else{
                        ele.format = (web3Obj.utils.fromWei(ele.fee ,"ether") * 2).toString();
                    } 
                }else{
                    if(ele.tokenId == 2 || ele.tokenId == 8){       //USDT and USDC
                        ele.format = (ele.fee  / 1000000 * 2).toString();
                    }else{
                        ele.format = (web3Obj.utils.fromWei(ele.fee ,"ether") * 2).toString();
                    } 
                }
             
                resp[ele.token] = ele;
            });
            resp.group = fees;
	        return resp;
            // return res.data.fees;
	
	    }catch(e){
	        console.log("offchainFee error",e)
	    }
	
	}
	// 获取交易成本费
	async getOrderFee(nftTokenAddress,feeTokenSymbol) {
	    try {
	        let fullURL = process.env.VUE_APP_LRCURL + "/api/v3/nft/info/orderUserRateAmount"
	        const { apiKey,accInfo} = lrcAccount;
	        let params = { 
	            accountId: accInfo.accountId,
	            nftTokenAddress:nftTokenAddress,
				feeTokenSymbol:feeTokenSymbol
	        };
	        let res = await axios({
	            method: 'get', //you can set what request you want to be
	            url: fullURL,
	            params: params,
	            headers: {
	                "X-API-KEY": apiKey
	            }
	        })
			console.log(res)
	        return res;
	
	    }catch(e){
	        console.log("offchainFee error",e)
	    }
	
	}

    
}

