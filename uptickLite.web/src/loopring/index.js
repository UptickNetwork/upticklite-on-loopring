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
import Vue from "vue";
import $store from "../store";

import { LRCHandler } from '@/loopring/handler.js';
let handler;

import { getApiKeyCache } from "@/walletConnect/index";

(function () {
    let json = window.localStorage.getItem("key_user");
    if(json) {
        let data = JSON.parse(json);
        if (window.ethereum && data.walletType == 2) {
            addNetwork();
            window.ethereum.on('accountsChanged', handleAccountsChanged);
        }
    }
})();

async function handleAccountsChanged(accounts) {
    if (accounts.length == 0) return;
    localStorage.clear();
   	sessionStorage.clear();

    let address = accounts[0];
    let did = address;
    $store.commit('SET_DID', did);
    
    localStorage.setItem("key_user", JSON.stringify({ user: true, did: $store.state.did, bscAddress: "", walletType: 2 }));
    location.reload();
}

async function addNetwork() {
    try {
        let env = process.env.VUE_APP_ENVIRONMENT;
        let chainId = '0x5';
        if(env == "testNet") {
            chainId = '0x5';
        } else if(env == "pro") {
            chainId = '0x1';
        }
        await window.ethereum.request({
            method: "wallet_switchEthereumChain",
            params: [{ chainId: chainId }]
        });
    }
    catch (error) {
        console.log(error);
    }
}

export const getLogged = async () => {
    console.log("wxl --- getLogged")

    let accountInfo = await handler.getAccountInfoSession();
    console.log("wxl --- 1111",accountInfo)
    if(accountInfo.accountId > 0) {
        return true;
    }
    return false;
}

export function create() {
    return new Promise((resolve) => {
        handler = new LRCHandler(res => {
            resolve(res);
        });
        Vue.prototype.LRCHandler = handler;
    });
}

export function init() {
 
    let json = window.localStorage.getItem("key_user");
    if(json) {
      
        let data = JSON.parse(json);
        window.walletType = data.walletType;
        
        handler = new LRCHandler();
        Vue.prototype.LRCHandler = handler;
        Vue.prototype.getLogged = getLogged;

    }else{
        handler = new LRCHandler();
        Vue.prototype.LRCHandler = handler;
        Vue.prototype.getLogged = getLogged;
    }
}

