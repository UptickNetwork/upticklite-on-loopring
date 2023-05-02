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
import WalletConnect from "@walletconnect/client";
import QRCodeModal from "@walletconnect/qrcode-modal";
import { convertUtf8ToHex } from "@walletconnect/utils";
import { apiGetAccountAssets, apiGetGasPrices, apiGetAccountNonce } from "./helpers/api";
import {
    sanitizeHex,
    verifySignature,
    hashTypedDataMessage,
    hashMessage,
} from "./helpers/utilities";

import {
    UserAPI,
    ExchangeAPI,
    NFTAPI,
    GlobalAPI,
    sleep
} from "@loopring-web/loopring-sdk";
var Web3 = require('web3');
const web3Obj = new Web3();

import WalletConnectProvider from "@walletconnect/web3-provider";
import {
    TOKEN_INFO,
    LOOPRING_EXPORTED_SETTING
} from "@/loopring/const";

const sdk = require("@loopring-web/loopring-sdk");

const BigNumber = require('big-number');   
const { toUnitValue ,getMaxFeeBips} = require("../utils/helper");

const { unlockShow, unlockhide } = require("@/components/UnlockDlg");

let appState = {
    connector: null,
    fetching: false,
    connected: false,
    chainId: 1,
    showModal: false,
    pendingRequest: false,
    uri: "",
    accounts: [],
    address: "",
    result: null,
    assets: [],
};

let LoopringAPI;

const AccountStorage = {
    eddsaKey: undefined
};

// let accountStorage = {
//     eddsaKey: undefined
// }

(function () {
    const state = sessionStorage.getItem("walletconnect_appstate");
    let chainId;
    if (state && state.length > 0) {
        appState = JSON.parse(state);
        chainId = appState.chainId;
    }
    
    LoopringAPI = {
        userAPI: new UserAPI({ chainId }),
        exchangeAPI: new ExchangeAPI({ chainId }),
        nftAPI: new NFTAPI({ chainId }),
        globalAPI: new GlobalAPI({ chainId }),
    };
})();

export const events = new Vue();

export async function connect() {
    const bridge = "https://bridge.walletconnect.org";
    // const bridge = "https://wcbridge.loopring.network";

    // create new connector
    const connector = new WalletConnect({ bridge, qrcodeModal: QRCodeModal });

    // await this.setState({ connector });
    appState.connector = connector;

    // check if already connected
    if (!connector.connected) {
        // create new session
        await connector.createSession();
    
    }

    // subscribe to events
    subscribeToEvents();

}

function subscribeToEvents() {
    const { connector } = appState;
    if (!connector) {
        return;
    }

    connector.on("session_update", async (error, payload) => {
        console.log(`connector.on("session_update")`);

        if (error) {
            throw error;
        }

        const { chainId, accounts } = payload.params[0];
        onSessionUpdate(accounts, chainId);
    });

    connector.on("connect", (error, payload) => {
        console.log(`connector.on("connect")`);

        if (error) {
            throw error;
        }

        onConnect(payload);
    });

    connector.on("disconnect", (error, payload) => {
        console.log(`connector.on("disconnect")`);

        if (error) {
            throw error;
        }

        onDisconnect();
    });

}

function resetApp() {
    appState.connector = null;
    appState.fetching = false;
    appState.connected = false;
    appState.chainId = 1;
    appState.showModal = false;
    appState.pendingRequest = false;
    appState.uri = "";
    appState.accounts = [];
    appState.address = "";
    appState.result = null;
    appState.assets = [];

    sessionStorage.removeItem("walletconnect_appstate");
};

async function onConnect(payload) {
    const { chainId, accounts } = payload.params[0];
    const address = accounts[0];

    appState.connected = true;
    appState.chainId = chainId;
    appState.accounts = accounts;
    appState.address = address;

    await getAccountAssets();

    events.$emit("connect", appState);
};

function onDisconnect() {
    resetApp();
killSession();
    events.$emit("disconnect", appState);
	this.$router.push({ name: 'Home' })
	window.location.reload();
   
};

export async function killSession(){
    
    const { connector } = appState;
    await connector.killSession();
}

async function onSessionUpdate(accounts, chainId) {
    const address = accounts[0];
    appState.chainId = chainId;
    appState.accounts = accounts;
    appState.address = address;

    await getAccountAssets();

    events.$emit("session_update", appState);
};

async function getAccountAssets() {
    const { address, chainId } = appState;
    appState.fetching = true;

    try {
        // get account balances
        const assets = await apiGetAccountAssets(address, chainId);

        appState.fetching = false;
        appState.address = assets;

        sessionStorage.setItem("walletconnect_appstate", JSON.stringify(appState));
    } catch (error) {
        console.error(error);
        appState.fetching = false;
    }
};

export async function signPersonalMessage() {
    const { connector, address, chainId } = appState;

    if (!connector) {
        return;
    }

    // test message
    const message = `My email is john@doe.com - ${new Date().toUTCString()}`;

    // encode message (hex)
    const hexMsg = convertUtf8ToHex(message);

    // eth_sign params
    const msgParams = [hexMsg, address];

    try {
        // toggle pending request indicator
        appState.pendingRequest = true;

        // send message
        const result = await connector.signPersonalMessage(msgParams);

        // verify signature
        const hash = hashMessage(message);
        // const valid = await verifySignature(address, result, hash, chainId);

        // format displayed result
        const formattedResult = {
            method: "personal_sign",
            address,
            // valid,
            result,
        };

        // display result
        appState.connector = connector;
        appState.pendingRequest = false;
        appState.result = formattedResult || null;

        return appState;
    } catch (error) {
        console.error(error);
        appState.connector = connector;
        appState.pendingRequest = false;
        appState.result = null;
    }
};


async function getAccountDetail() {
    unlockShow();
    
    try {
        //  Create WalletConnect Provider
        const provider = new WalletConnectProvider({
            // infuraId: "7e31d49d7c8a48f4a4539aff9da768e7", // Required
			infuraId:"b951a403f3d3426fb2008c6923254dbc",//路印的
        });
        await provider.enable();
        //  Create Web3
        const web3 = new Web3(provider);

        let fromAddress = appState.accounts[0];
        let chainId = appState.chainId;
        
        //1 get exchange Info
        const { exchangeInfo } = await LoopringAPI.exchangeAPI.getExchangeInfo()
        console.log("xxl exchangeInfo : ", exchangeInfo);

        //2 get account info
        const { accInfo } = await LoopringAPI.exchangeAPI.getAccount({
            owner: fromAddress
        });
        if (!accInfo) {
            unlockhide();
            return;
        }

        //3 user keySeed generateKeyPair
        const keySeed = accInfo.keySeed && accInfo.keySeed !== ""
            ? accInfo.keySeed
            : sdk.GlobalAPI.KEY_MESSAGE.replace(
                "${exchangeAddress}",
                exchangeInfo.exchangeAddress
            ).replace("${nonce}", (accInfo.nonce - 1).toString());

        let eddsaKey;
        //4 eddsaKey
        try {
            eddsaKey = await sdk.generateKeyPair({
                web3: web3,
                address: accInfo.owner,
                keySeed,
                walletType: sdk.ConnectorNames.WalletConnect,
                chainId: chainId,
            });
        } catch (e) {
            console.log("xxl e ", e);
            return false
        }
        console.log("xxl eddsaKey is :", eddsaKey);
        if (!eddsaKey) {
            unlockhide();
            return;
        }

        //5 apiKey
        const { apiKey } = await LoopringAPI.userAPI.getUserApiKey(
            { accountId: accInfo.accountId },
            eddsaKey.sk);
        if(!apiKey) {
            unlockhide();
            return;
        }

        AccountStorage.eddsaKey = eddsaKey;

        unlockhide();

        return { exchangeInfo, accInfo, eddsaKey, apiKey};
    } catch (error) {
        console.log("WalletConnect getAccountDetail",error);
        unlockhide();
    }
}

export function getApiKeyCache() {
    const key = sessionStorage.getItem("walletconnect_eddsaKey");
    if(key && key.length > 0) {
        const data = JSON.parse(key);
        if(data) {
            return data;
        }
    }
    const detail = getAccountDetail();
    sessionStorage.setItem("walletconnect_eddsaKey", JSON.stringify(detail));
    return detail;
}

export function getAddresss() {
    const fromAddress = appState.accounts[0];
    return fromAddress;
}

export async function getUserBalances(tokenId) {
    let detail = await getAccountDetail();
    if(!detail) return;

    const { exchangeInfo, accInfo, eddsaKey, apiKey } = detail;
    const { userBalances } = await LoopringAPI.userAPI.getUserBalances(
        { accountId: accInfo.accountId, tokens: "" }, apiKey
    );

    let balance = appState.address[tokenId].balance;
    let balanceJson = {
        amountValue: 0,
        lockedValue: 0
    }
    if(tokenId == 0) {
        balanceJson.amountValue = web3Obj.utils.fromWei(balance ,"ether");
    }
    return balanceJson;
}


export async function takerOrder(nftTokenId, nftData, nftAmount, sellTokenId, sellAmount) {
    try{
        sellAmount = BigNumber(toUnitValue(web3Obj,sellTokenId,sellAmount));
        // const fromAddress = appState.accounts[0];

        let detail = await getAccountDetail();
        if(!detail) return;

        const { exchangeInfo, accInfo, eddsaKey, apiKey } = detail;
        const storageId = await LoopringAPI.userAPI.getNextStorageId({
            accountId: accInfo.accountId,
            sellTokenId: sellTokenId,
            },
            apiKey
        );

        const takerOrder = {
            exchange: exchangeInfo.exchangeAddress,
            accountId: accInfo.accountId,
            storageId: storageId.orderId,
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
            maxFeeBips: 1000,
        };
        
        takerOrder.eddsaSignature = sdk.get_EddsaSig_NFT_Order(
            takerOrder,
            eddsaKey.sk
        );
        
        return takerOrder;
    }catch(e){
        console.log("takerOrder error is :",e);
        return null
    }
}

/**
 * @description computeNFTAddress
 * @param fromAddress       from address from calc token Address
 * @param nftBaseUri        nftBaseUri for calculate contract address
 */
export async function computeNFTAddress(fromAddress,nftBaseUri) {
    let chainId = appState.chainId;
    //1 get exchange Info
    // const { exchangeInfo } = await LoopringAPI.exchangeAPI.getExchangeInfo()

    //2 get account info
    const { accInfo } = await LoopringAPI.exchangeAPI.getAccount({
        owner: fromAddress
    });
    if(!accInfo) return;

    //3 nftTokenAddress
    const counterFactualNftInfo = {
        nftOwner: accInfo.owner,
        nftFactory: sdk.NFTFactory[chainId],
        nftBaseUri,
    };
    const nftTokenAddress =
        LoopringAPI.nftAPI.computeNFTAddress(counterFactualNftInfo)
            .tokenAddress || "";
    return nftTokenAddress;

}
