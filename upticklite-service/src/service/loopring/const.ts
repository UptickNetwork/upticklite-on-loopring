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
import {
    AmmpoolAPI,
    ExchangeAPI,
    UserAPI,
    WalletAPI,
    WsAPI,
    NFTAPI,
    DelegateAPI,
    GlobalAPI,
    WhitelistedUserAPI,
} from "../../loopringSDK/api";

import * as sdk from "../../loopringSDK/index";
import * as ContractAPI from "../../loopringSDK/api/contract_api";

const chainId = sdk.ChainId.GOERLI;

export const LoopringAPI = {
    userAPI: new UserAPI({ chainId }),
    exchangeAPI: new ExchangeAPI({ chainId }),
    globalAPI: new GlobalAPI({ chainId }),
    ammpoolAPI: new AmmpoolAPI({ chainId }),
    walletAPI: new WalletAPI({ chainId }),
    wsAPI: new WsAPI({ chainId }),
    whitelistedUserAPI: new WhitelistedUserAPI({ chainId }),
    nftAPI: new NFTAPI({ chainId }),
    delegate: new DelegateAPI({ chainId }),
    __chainId__: chainId,
    contractAPI: ContractAPI,
  };

export let TOKEN_INFO = {
  addressIndex: {
    "0x0000000000000000000000000000000000000000": "ETH",
    "0xfc28028d9b1f6966fe74710653232972f50673be": "LRC",
    "0xd4e71c4bb48850f5971ce40aa428b09f242d3e8a": "USDT",
    "0xfeb069407df0e1e4b365c10992f1bc16c078e34b": "LP-LRC-ETH",
    "0x049a02fa9bc6bd54a2937e67d174cc69a9194f8e": "LP-ETH-USDT",
    "0xcd2c81b322a5b530b5fa3432e57da6803b0317f7": "DAI",
    "0x47525e6a5def04c9a56706e93f54cc70c2e8f165": "USDC",
    "0xf37cf4ced77b985708d591acc6bfd08586ab3409": "LP-USDC-ETH",
  },
  tokenMap: {
    ETH: {
      type: "ETH",
      tokenId: 0,
      symbol: "ETH",
      name: "Ethereum",
      address: "0x0000000000000000000000000000000000000000",
      decimals: 18,
      precision: 7,
      precisionForOrder: 3,
      orderAmounts: {
        minimum: "5000000000000000",
        maximum: "1000000000000000000000",
        dust: "200000000000000",
      },
      luckyTokenAmounts: {
        minimum: "50000000000000",
        maximum: "1000000000000000000000",
        dust: "50000000000000",
      },
      fastWithdrawLimit: "100000000000000000000",
      gasAmounts: {
        distribution: "85000",
        deposit: "100000",
      },
      enabled: true,
      isLpToken: false,
      tradePairs: ["LRC", "USDT", "USDC"],
    },
    LRC: {
      type: "erc20Trade",
      tokenId: 1,
      symbol: "LRC",
      name: "Loopring",
      address: "0xfc28028d9b1f6966fe74710653232972f50673be",
      decimals: 18,
      precision: 3,
      precisionForOrder: 3,
      orderAmounts: {
        minimum: "5000000000000000000",
        maximum: "5000000000000000000000000",
        dust: "5000000000000000000",
      },
      luckyTokenAmounts: {
        minimum: "50000000000000000",
        maximum: "5000000000000000000000000",
        dust: "50000000000000000",
      },
      fastWithdrawLimit: "750000000000000000000000",
      gasAmounts: {
        distribution: "101827",
        deposit: "200000",
      },
      enabled: true,
      isLpToken: false,
      tradePairs: ["ETH"],
    },
    USDT: {
      type: "erc20Trade",
      tokenId: 2,
      symbol: "USDT",
      name: "USDT",
      address: "0xd4e71c4bb48850f5971ce40aa428b09f242d3e8a",
      decimals: 6,
      precision: 2,
      precisionForOrder: 3,
      orderAmounts: {
        minimum: "5000000",
        maximum: "2000000000000",
        dust: "250000",
      },
      luckyTokenAmounts: {
        minimum: "50000",
        maximum: "200000000000",
        dust: "50000",
      },
      fastWithdrawLimit: "250000000000",
      gasAmounts: {
        distribution: "106233",
        deposit: "200000",
      },
      enabled: true,
      isLpToken: false,
      tradePairs: ["ETH", "DAI"],
    },
    "LP-LRC-ETH": {
      type: "erc20Trade",
      tokenId: 4,
      symbol: "LP-LRC-ETH",
      name: "AMM-LRC-ETH",
      address: "0xfeb069407df0e1e4b365c10992f1bc16c078e34b",
      decimals: 8,
      precision: 6,
      precisionForOrder: 3,
      orderAmounts: {
        minimum: "100000000",
        maximum: "10000000000000000000",
        dust: "100000000",
      },
      luckyTokenAmounts: {
        minimum: "100000000",
        maximum: "10000000000000000000",
        dust: "100000000",
      },
      fastWithdrawLimit: "20000000000",
      gasAmounts: {
        distribution: "150000",
        deposit: "200000",
      },
      enabled: true,
      isLpToken: true,
    },
    "LP-ETH-USDT": {
      type: "erc20Trade",
      tokenId: 7,
      symbol: "LP-ETH-USDT",
      name: "LP-ETH-USDT",
      address: "0x049a02fa9bc6bd54a2937e67d174cc69a9194f8e",
      decimals: 8,
      precision: 6,
      precisionForOrder: 3,
      orderAmounts: {
        minimum: "100000000",
        maximum: "10000000000000",
        dust: "100000000",
      },
      luckyTokenAmounts: {
        minimum: "100000000",
        maximum: "10000000000000",
        dust: "100000000",
      },
      fastWithdrawLimit: "20000000000",
      gasAmounts: {
        distribution: "150000",
        deposit: "200000",
      },
      enabled: true,
      isLpToken: true,
    },
    DAI: {
      type: "erc20Trade",
      tokenId: 6,
      symbol: "DAI",
      name: "dai",
      address: "0xcd2c81b322a5b530b5fa3432e57da6803b0317f7",
      decimals: 18,
      precision: 6,
      precisionForOrder: 3,
      orderAmounts: {
        minimum: "10000000000000000000",
        maximum: "100000000000000000000000",
        dust: "10000000000000000",
      },
      luckyTokenAmounts: {
        minimum: "10000000000000000000",
        maximum: "100000000000000000000000",
        dust: "10000000000000000000",
      },
      fastWithdrawLimit: "10000000000000000000000",
      gasAmounts: {
        distribution: "150000",
        deposit: "200000",
      },
      enabled: true,
      isLpToken: false,
      tradePairs: ["USDT"],
    },
    USDC: {
      type: "USDC",
      tokenId: 8,
      symbol: "USDC",
      name: "USDC",
      address: "0x47525e6a5def04c9a56706e93f54cc70c2e8f165",
      decimals: 6,
      precision: 6,
      precisionForOrder: 3,
      orderAmounts: {
        minimum: "1000",
        maximum: "10000000000000000000",
        dust: "100",
      },
      luckyTokenAmounts: {
        minimum: "1000000",
        maximum: "10000000000",
        dust: "1000000",
      },
      fastWithdrawLimit: "20000000000000000000",
      gasAmounts: {
        distribution: "150000",
        deposit: "200000",
      },
      enabled: true,
      isLpToken: false,
      tradePairs: ["ETH"],
    },
    "LP-USDC-ETH": {
      type: "LP-USDC-ETH",
      tokenId: 9,
      symbol: "LP-USDC-ETH",
      name: "LP-USDC-ETH",
      address: "0xf37cf4ced77b985708d591acc6bfd08586ab3409",
      decimals: 8,
      precision: 7,
      precisionForOrder: 3,
      orderAmounts: {
        minimum: "100000",
        maximum: "1000000000000000000000000000000000000000",
        dust: "10000",
      },
      luckyTokenAmounts: {
        minimum: "1000000000000000",
        maximum: "10000000000000000000",
        dust: "1000000000000000",
      },
      fastWithdrawLimit: "20000000000000000000",
      gasAmounts: {
        distribution: "150000",
        deposit: "200000",
      },
      enabled: true,
      isLpToken: true,
    },
  },
  idIndex: {
    "0": "ETH",
    "1": "LRC",
    "2": "USDT",
    "4": "LP-LRC-ETH",
    "6": "DAI",
    "7": "LP-ETH-USDT",
    "8": "USDC",
    "9": "LP-USDC-ETH",
  },
  marketMap: {
    "LRC-ETH": {
      baseTokenId: 1,
      enabled: true,
      market: "LRC-ETH",
      orderbookAggLevels: 5,
      precisionForPrice: 6,
      quoteTokenId: 0,
      status: 3,
      isSwapEnabled: true,
      createdAt: 1617967800000,
    },
    "ETH-USDT": {
      baseTokenId: 0,
      enabled: true,
      market: "ETH-USDT",
      orderbookAggLevels: 3,
      precisionForPrice: 3,
      quoteTokenId: 2,
      status: 3,
      isSwapEnabled: true,
      createdAt: 1617972300000,
    },
    "DAI-USDT": {
      baseTokenId: 6,
      enabled: true,
      market: "DAI-USDT",
      orderbookAggLevels: 2,
      precisionForPrice: 4,
      quoteTokenId: 2,
      status: 3,
      isSwapEnabled: true,
      createdAt: 0,
    },
    "USDC-ETH": {
      baseTokenId: 8,
      enabled: true,
      market: "USDC-ETH",
      orderbookAggLevels: 3,
      precisionForPrice: 3,
      quoteTokenId: 0,
      status: 3,
      isSwapEnabled: true,
      createdAt: 1636974420000,
    },
  },
};

export const LOOPRING_EXPORTED_SETTING = {

  validUntil: Math.round(Date.now() / 1000) + 30 * 86400,
  nftTokenAddress: "0x8394cB7e768070217592572582228f62CdDE4FCE",
  tradeLRCValue: 1000000000000000000,

}