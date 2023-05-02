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
/* Imports: External */
import { BaseService } from '@eth-optimism/service-base';
import express, { json, Request, Response } from 'express'
import cors from 'cors'

import {
  errResphonse,
  okResphonse,
  ERR_MSG,
} from '../../utils'
import { LRCHandler } from '../loopring/handler';

import Web3 from 'web3';
const PrivateKeyProvider = require("truffle-privatekey-provider")


interface LrcConf {

  adminAddress: string
  adminPRIV: string
  chainId: number
  infroUrl: string
}

export interface DataTransportServiceOptions {

  //host
  serverHost: string
  serverPort: number
  lrcConf: LrcConf

}

export class DataTransportService extends BaseService<DataTransportServiceOptions> {

  protected name = 'Data Transport Service'
  protected cross_id_random_length = 3
  public repeatNum = 0;
  public lastRandom = "0";
  
  private lrcHandler:any;
  

  private state: {
    app: express.Express,
    server: any,
    web3: any,
    lrcConf: LrcConf,
    adminWeb3:any
  } = {} as any

  protected async _init(): Promise<void> {


    //initialize App
    this._initializeApp()
    this.state.lrcConf = this.options.lrcConf;
    this.state.web3 = new Web3(new Web3.providers.HttpProvider(this.state.lrcConf.infroUrl))
     
    const provider = new PrivateKeyProvider(
      this.state.lrcConf.adminPRIV,
      this.state.lrcConf.infroUrl
    );
    const adminWeb3 = new Web3(provider);
    this.state.adminWeb3 = adminWeb3;

    this.lrcHandler = new LRCHandler(this.state.lrcConf.chainId);
    await this.lrcHandler.initAdminCount(this.state.lrcConf.chainId,adminWeb3);

  }

  protected async _start(): Promise<void> {
    console.log("dts come to _start");

    this.state.server = this.state.app.listen(
      this.options.serverPort,
      this.options.serverHost
    )

    this.logger.info('Server started and listening', {
      port: this.options.serverPort,
      host: this.options.serverHost
    })

  }

  protected async _stop(): Promise<void> {
    console.log("dts come to _stop");
  }

  /**
   * Initializes the server application.
   * Do any sort of initialization here that you want. Mostly just important that
   * `_registerAllRoutes` is called at the end.
   */
  private _initializeApp() {
    this.state.app = express()
    this.state.app.use(cors())
    this.state.app.use(json())
    this._registerAllRoutes()
  }


  /**
   * Registers a route on the server.
   * @param method Http method type.
   * @param route Route to register.
   * @param handler Handler called and is expected to return a JSON response.
   */
  private _registerRoute(
    method: string, // Just handle GET for now, but could extend this with whatever.
    route: string,
    handler: (req?: Request, res?: Response) => Promise<any>
  ): void {

    this.state.app[method](route, async (req, res) => {
      const start = Date.now()
      try {

        const json = await handler(req, res)
        const elapsed = Date.now() - start

        this.logger.info('Served HTTP Request', {
          method: req.method,
          url: req.url,
          elapsed,
        })

        return res.json(json)
      } catch (e) {
        const elapsed = Date.now() - start
        this.logger.info('Failed HTTP Request', {
          method: req.method,
          url: req.url,
          elapsed,
          msg: e.toString(),
        })
        return res.status(400).json({
          error: e.toString(),
        })
      }
    })
  }

  /** 
   * get the deposit raw transaction
   */
  private _registerAllRoutes(): void {

      /**
       * @description             get the deposit raw transaction
       * @param from              from address to deposit       
       * @param value             depoist value unit ETH
       * @param fee               deposit fee unit Wei
       * @param gasPrice          gasPrice unit GWei
       * @param gasLimit          gasLimit for evm contract run
       * @param nonce             account nonce 
      **/
      this._registerRoute(
        'post',
        '/lrc/packDepositTx',
        async (req): Promise<any> => {

          let params = req.body;
          let fromAddress = params.fromAddress;
          let value = params.value;
          let fee = params.fee;
          let gasPrice = params.gasPrice;
          let gasLimit = params.gasLimit;
          let nonce = params.nonce;

          try {
              //1.0 get exchange Info
              console.log("****1. get exchange Info");
              const exchangeInfo  = await this.lrcHandler.getExchangeInfo();

              //2.0 get get DepositTx
              console.log("****2. get DepositTx");
              let tx = await this.lrcHandler.getDeposit(
                fromAddress,
                exchangeInfo.exchangeAddress,
                value,    
                fee,      
                gasPrice, 
                gasLimit, 
                this.state.lrcConf.chainId, 
                nonce
              )
              return okResphonse(tx);

          } catch (reason) {

            console.log(reason)
            return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);

          }

        }),

        /**
         * @description             broadcast the deposit transaction signed by client private key
         * @param signTx            signed transaction to broadcast
         */
        this._registerRoute(
          'post',
          '/lrc/broadcastTx',
          async (req): Promise<any> => {
  
            try {

              const params = req.body
              let tx = await this.state.web3.eth
                .sendSignedTransaction(params.signTx.rawTransaction)

              return okResphonse(tx.transactionHash);
            } catch (reason) {
  
              console.log(reason)
              return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
            }
  
        }),
  
        /**
         * @description             get key pair message from address
         * @param fromAddress       address to get message
         */
        this._registerRoute(
          'post',
          '/lrc/getKeyPairMsg',
          async (req): Promise<any> => {
  
            let params = req.body;
            let address = params.address;
            try {

              const keySeed = await this.lrcHandler.getKeyPairMsg(address);
              return okResphonse(keySeed);

            } catch (reason) {
              return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
            }

          }),

          /**
           * @description             get update account message
           * @param keyPairSignature  keypair signature from getKeyPairMsg api
           * @param fromAddress       address to get account message
           */
          this._registerRoute(
            'post',
            '/lrc/getUpdateAccountMsg',
            async (req): Promise<any> => {

              let params = req.body;
              let keyPairSignature = params.keyPairSignature;
              let fromAddress = params.fromAddress;

              try {
                let msg = await this.lrcHandler.getUpdateAccountMsg(
                  fromAddress, 
                  keyPairSignature,
                  this.state.lrcConf.chainId
                );
                return okResphonse(msg);

              } catch (reason) {

                console.log(reason)
                return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);

              }

            })

          /**
           * @description             send update account to loorping layer2
           * @param ecdsaSignature    ecdsaSignature for update account 
           * @param keyPairSignature  key pair signature
           * @param fromAddress       from address update account
           */
          this._registerRoute(
            'post',
            '/lrc/sendUpdateAccount',
            async (req): Promise<any> => {

              let params = req.body;
          
              let fromAddress = params.fromAddress;
              let keyPairSignature = params.keyPairSignature;
              let ecdsaSignature = params.ecdsaSignature;

              try {
                let resultTx = await this.lrcHandler.sendUpdateAccount(
                  fromAddress, 
                  keyPairSignature,
                  ecdsaSignature,
                  this.state.lrcConf.chainId);
                return okResphonse(resultTx);

              } catch (reason) {

                console.log(reason)
                return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);

              }
            }),

          /**
           * @description             get user apiKey  
           * @param fromAddress       address to get use apiKey
           * @param keyPairSignature  keypair signature
           */
            this._registerRoute(
              'post',
              '/lrc/userApiKey',
              async (req): Promise<any> => {

                let params = req.body;
                let keyPairSignature = params.keyPairSignature;
                let fromAddress = params.fromAddress;
                  
                try {

                  const apiKey = await this.lrcHandler.getUserApiKey(fromAddress,keyPairSignature);
                  return okResphonse(apiKey);

                } catch (reason) {
                  console.log(reason)
                  return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                }

            }),
        
            /**
              * @description            get nft Balances
              * @param fromAddress      nft owner
              * @param apiKey           api key of the account
              * @param offset         number of records to skip
              * @param limit          number of records to return
             */
            this._registerRoute(
              'post',
              '/lrc/getNftBalances',
              async (req): Promise<any> => {

                console.log("lrc/getNftBalances 1");
                let params = req.body;
                let fromAddress = params.fromAddress;

                console.log("lrc/getNftBalances 2");
                try {

                  let offset = params.hasOwnProperty("offset") ? params.offset : 0;
                  let limit = params.hasOwnProperty("limit") ? params.limit : 20;

                  let quiry = {
                    offset,
                    limit,
                    metadata:true,
                    ...params
                  }

                 delete quiry.fromAddress;
                 let response = await this.lrcHandler.getNftBalances(fromAddress,quiry);

                  return okResphonse(response.data);

                } catch (reason) {
                  console.log(reason)
                  return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                }
            }),

            /**
              * @description            get Transfer NftTx
              * @param fromAddress      nft owner
              * @param toAddress        to address of nft
              * @param amount           amount of 1155
              * @param nftId            nftId to send
              * @param keyPairSignature keyPair sig
             */
            this._registerRoute(
              'post',
              '/lrc/getTransferNftTx',
              async (req): Promise<any> => {

                let params = req.body;
                let fromAddress = params.fromAddress;
                let toAddress = params.toAddress;
                let amount = params.amount;
                let nftId = params.nftId;
                let keyPairSignature = params.keyPairSignature;

                try {

                  let response = await this.lrcHandler.getTransferNftTx(
                    fromAddress,
                    toAddress,
                    nftId,
                    amount,
                    keyPairSignature,
                    this.state.lrcConf.chainId
                  );
                  return okResphonse(response);

                } catch (reason) {

                  console.log(reason)
                  return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);

                }

              })

            /**
              * @description transfer nft
              * @param fromAddress      nft owner
              * @param toAddress        to address of nft
              * @param amount           amount of 1155
              * @param nftId            nftId to send
              * @param keyPairSignature keyPair sig
              * @param ecdsaSignature   ecdsa Signature
             */
            this._registerRoute(
              'post',
              '/lrc/transferNFT',
              async (req): Promise<any> => {

                let params = req.body;
                let fromAddress = params.fromAddress;
                let toAddress = params.toAddress;
                let amount = params.amount;
                let nftId = params.nftId;
                let keyPairSignature = params.keyPairSignature;
                let ecdsaSignature = params.ecdsaSignature;
      
                try {

                  console.log("xxl service transferNFT");
                  let response = await this.lrcHandler.sendTransferNFT(
                    fromAddress,
                    toAddress,
                    nftId,
                    amount,
                    keyPairSignature,
                    ecdsaSignature,
                    this.state.lrcConf.chainId
                  );
                  return okResphonse(response);
      
                } catch (reason) {
                  console.log(reason)
                  return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                }
              })

              /**
               * @description             get nft trade order
               * @param fromAddress       address of nft
               * @param keyPairSignature  keypair signature
               * @param isSell            true:is sell order;order buy order
               * @param nftId             nftId to send
               * @param nftAmount         nft amount for trade
               * @param tokenId           tokenId for trade
               *                          "0": "ETH",
               *                          "1": "LRC",
               *                          "2": "USDT",
               *                          "4": "LP-LRC-ETH",
               *                          "6": "DAI",
               *                          "7": "LP-ETH-USDT",
               *                          "8": "USDC",
               *                          "9": "LP-USDC-ETH",
               * @param tokenAmount       token amount for trade
               */
               this._registerRoute(
                'post',
                '/lrc/getNftOrder',
                async (req): Promise<any> => {
  
                  let params = req.body;
                  let address = params.address;
                  let keyPairSignature = params.keyPairSignature;
                  let isSell = params.isSell;
                  let nftId = params.nftId;
                  let nftAmount = params.nftAmount;
                  let tokenId = params.tokenId;
                  let tokenAmount = params.tokenAmount;

                  try {
  
                    console.log("xxl service getNftOrder");
                    let response = await this.lrcHandler.getNftOrder(
                      address,
                      keyPairSignature,
                      isSell,
                      nftId,
                      nftAmount,
                      tokenId,
                      tokenAmount,
                      this.state.lrcConf.chainId
                    );

                    return okResphonse(response);
        
                  } catch (reason) {
                    console.log(reason)
                    return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                  }
                })


              /**
               * @description                           submit the nft trade to platform
               * @param makerOrder                      maker Order for trade
               * @param makerOrderEddsaSignature        maker Order EddsaSignature for trade
               * @param makerFeeBips                    makerFeeBips for trade
               * @param takerOrder                      taker Order for trade
               * @param takerOrderEddsaSignature        taker Order EddsaSignature for trade
               * @param takerFeeBips                    takerFeeBips for trade
               */
               this._registerRoute(
                'post',
                '/lrc/tradeNFT',
                async (req): Promise<any> => {
  
                  let params = req.body;

                  let makerOrder = params.makerOrder;
                  let makerOrderEddsaSignature = params.makerOrderEddsaSignature;
                  let makerFeeBips = params.makerFeeBips;
                  let takerOrder = params.takerOrder;
                  let takerOrderEddsaSignature = params.takerOrderEddsaSignature;
                  let takerFeeBips = params.takerFeeBips;
                
                  try {

                    console.log("lrc/tradeNFT param : ",[
                      makerOrder,
                      makerOrderEddsaSignature,
                      makerFeeBips,
                      takerOrder,
                      takerOrderEddsaSignature,
                      takerFeeBips,
                      this.state.lrcConf.chainId
                    ]);
  
                    let response = await this.lrcHandler.tradeNFT(
                      makerOrder,
                      makerOrderEddsaSignature,
                      makerFeeBips,
                      takerOrder,
                      takerOrderEddsaSignature,
                      takerFeeBips,
                      this.state.lrcConf.chainId
                    );

                    return okResphonse(response);
        
                  } catch (reason) {
                    console.log(reason)
                    return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                  }
                  
                })

              /**
               * @description  getNftInfoFromId
               * @param nftId  nftId for the nftInfo
               */
               this._registerRoute(
                'post',
                '/lrc/getNftDetailFromNftId',
                async (req): Promise<any> => {
  
                  let params = req.body;
                  let nftId = params.nftId;
                  let fromAddress = params.fromAddress;
                  try {
  
                    console.log("xxl service getNftDetailFromNftId");
                    let response = await this.lrcHandler.getNftDetailFromNftId(
                      fromAddress,
                      nftId
                    );

                    return okResphonse(response);
        
                  } catch (reason) {
                    console.log(reason)
                    return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                  }
                  
                })

              /**
               * @description  getNftInfoFromId
               * @param nftId  nftId for the nftInfo
               */
               this._registerRoute(
                'post',
                '/lrc/getContractNFTMeta',
                async (req): Promise<any> => {
  
                  let params = req.body;
                  let nftId = params.nftId;

                  try {
  
                    console.log("xxl service getContractNFTMeta");
                    let response = await this.lrcHandler.getContractNFTMeta(
                      nftId
                    );

                    return okResphonse(response);
        
                  } catch (reason) {
                    console.log(reason)
                    return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                  }
                  
                })

              /**
               * @description  get users nft trade list
               * @param nftId  nftId for the nftInfo
               */
               this._registerRoute(
                'post',
                '/lrc/getTradesList',
                async (req): Promise<any> => {
  
                  let params = req.body;
                 
                  try {

                    let response = await this.lrcHandler.getTradesList(
                      params
                    );
                    return okResphonse(response);
        
                  } catch (reason) {
                    console.log(reason)
                    return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                  }
                  
                })

              /**
               * @description  get l2 block info by block id to anylse nft data
               * @param nftId  nftId for the nftInfo
               */
               this._registerRoute(
                'post',
                '/lrc/getBlock',
                async (req): Promise<any> => {
  
                  let params = req.body;
                 
                  try {
                    let response = await this.lrcHandler.getBlock(
                      params
                    );
                    return okResphonse(response);
        
                  } catch (reason) {
                    console.log(reason)
                    return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                  }
                  
                }),

                /**
                * @description post     query nft holders by looprings nftData
                * @param nftData        the Loopring's NFT token data identifier which is a hash string of NFT token address and NFT_ID
                * @param offset         number of records to skip
                * @param limit          number of records to return
                */
              this._registerRoute(
                'post',
                '/lrc/getNftHolders',
                async (req): Promise<any> => {

                  console.log("lrc/getNftHolders ");
                  let params = req.body;

                  try {

                    let offset = params.hasOwnProperty("offset") ? params.offset : 0;
                    let limit = params.hasOwnProperty("limit") ? params.limit : 100;

                    let quiry = {
                      offset,
                      limit,
                      ...params
                    }

                    delete quiry.fromAddress;
                    let response = await this.lrcHandler.getNftHolders(quiry);

                    return okResphonse(response);

                  } catch (reason) {
                    console.log(reason)
                    return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                  }
              }),

                /**
                * @description post      get account detail information
                * @param accountId       accountID, if owner is presented, it must be align with the owners accountId, otherwise an error occurs.
                */
                this._registerRoute(
                  'post',
                  '/lrc/getNfts',
                  async (req): Promise<any> => {
  
                    console.log("lrc/getNfts ");
                    let params = req.body;
  
                    try {
  
                      let quiry = {
                        ...params
                      }
  
  
                      delete quiry.fromAddress;
                      let response = await this.lrcHandler.getNfts(quiry);
  
                      return okResphonse(response);
  
                    } catch (reason) {
                      console.log(reason)
                      return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                    }
                })


                  /**
                  * @description post      get account detail information
                  * @param accountId       accountID, if owner is presented, it must be align with the owners accountId, otherwise an error occurs.
                  */
                  this._registerRoute(
                  'post',
                  '/lrc/getAccount',
                  async (req): Promise<any> => {
  
                    console.log("lrc/getNftHolders ");
                    let params = req.body;
  
                    try {

                      let quiry = {
                        ...params
                      }

                      let response = await this.lrcHandler.getAccountInfo(quiry);
  
                      return okResphonse(response);
  
                    } catch (reason) {
                      console.log(reason)
                      return errResphonse(ERR_MSG.MINT_ERROR.NO, ERR_MSG.MINT_ERROR.MSG);
                    }
                })


  }



}




