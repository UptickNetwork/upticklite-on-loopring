<h1 align="center">Uptick Lite On Loopring Service</h1>
<p align="center">Uptick Lite On Loopring Service is a software middleware for the applicaitons built in loopring layer2.</p>
<div align="center">

[![License: GPL v3.0](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.en.html)
[![Version](https://img.shields.io/twitter/url?url=https%3A%2F%2Ftwitter.com%2FUptickproject)]()
[![discord](https://img.shields.io/badge/discord-active-brightgreen)](https://discord.com/channels/781005936260939818/981431815841669120)
[![type-badge](https://img.shields.io/npm/types/react-data-grid)](https://www.npmjs.com/package/react-data-grid)

</div>


## Config
[edit config](https://github.com/UptickNetwork/upticklite-service/blob/main/.env.sample)

Set the following configuration file(.env) 
```shell
UPTICK_LRC__SERVER_HOST=<SERVER_HOST>
UPTICK_LRC__SERVER_IP=<SERVER_IP>
UPTICK_LRC__ADMIN_ADDRESS=<ADMIN_ADDRESS>
UPTICK_LRC__ADMIN_PRIV=<ADMIN_PRIV>
UPTICK_LRC__CHAIN_ID=<CHAIN_ID>
UPTICK_LRC__INFRO_URL=<INFRO_URL>
```

The fields of the configuration file are described as follows

| field |explanation  |
|-----|------|
| SERVER_HOST | deployment service domain name |
| SERVER_IP | IP address of the service |
| ADMIN_ADDRESS | admin address of the service |
| ADMIN_PRIV | admin private key of the service |
| INFRO_URL | ethereum infura url|


## Quick Start
```shell
# set .env file 
mv .env.sample mv .env
# install package 
yarn install
# start service
yarn start
# or start servie using pm2
yarn pm2
```
> Make sure you are using the original npm registry.  
> `npm config set registry http://registry.npmjs.org`

## Dependencies
Either make sure you're running a version of node compliant with the `engines` requirement in `package.json`, or install Node Version Manager [`nvm`](https://github.com/creationix/nvm) and run `nvm use` to use the correct version of node.

Requires `nodejs` ,`yarn` and `npm`.

```shell
# node -v 
v16.0.0
# yarn version
yarn version v1.22.17 
# npm -v
8.5.3
```

## Client Usage
The following code segment demonstrate basic client usage.

- Account
- - Deposit
```typescript
//get the deposit raw transaction
let jsonData = {
    fromAddress,        //from address to deposit
    value,              //depoist value unit ETH
    fee,                //deposit fee unit Wei
    gasPrice,           //gasPrice unit GWei
    gasLimit,           //gasLimit for evm contract run
    nonce               //account nonce 
}
let fullURL = baseURL + "lrc/packDepositTx"
axios.post(fullURL,jsonData)

//broadcast the deposit transaction signed by client private key
const jsonBroadcastTx = { 
    signTx:  signedTx   //signed transaction to broadcast
};
fullURL = baseURL + "lrc/broadcastTx"
axios.post(fullURL,jsonBroadcastTx)
```

- - updateAccount
```typescript
//get key pair message from address
let jsonData = {
    address:fromAddress //address to get message
}
let fullURL = baseURL + "lrc/getKeyPairMsg"
axios.post(fullURL,jsonData)

//get update account message
let jsonUpdateAccountMsg = {
    keyPairSignature,   //keypair signature from getKeyPairMsg api
    fromAddress         //address to get account message
}
fullURL = baseURL + "lrc/getUpdateAccountMsg"
axios.post(fullURL,jsonData)

//send update account to loorping layer2
let jsonSendUpdateAccount = {
    keyPairSignature,   //keypair signature for update account
    fromAddress,        //address to update account
    ecdsaSignature      //ecdsa signature for update account
}
fullURL = baseURL + "lrc/sendUpdateAccount"
axios.post(fullURL,jsonSendUpdateAccount)
```

- - userApiKey
```typescript
//get user apiKey  
let jsonUpdateAccountMsg = {
    keyPairSignature,   //address to get use apiKey
    fromAddress         //keypair signature
}
fullURL = baseURL + "lrc/userApiKey"
axios.post(fullURL,jsonUpdateAccountMsg)
```

- - getAccount
```typescript
//get account detail information
let jsonData = {
    accountId           //account id
}
let fullURL = baseURL + "lrc/getAccount"
axios.post(fullURL,jsonData)
```

- NFT
- - trade
```typescript
//get nft trade order
let makerOrderRequest = {
    address:fromAddress,                    //address of nft
    keyPairSignature:makerKeyPairSignature, //keypair signature
    isSell:true,                            //true:is sell order;order buy order
    nftId:224,                              //nftId to send
    nftAmount:2,                            //nft amount for trade
    tokenId:1,                              //tokenId for trade
    tokenAmount:makeTokenAmount             //token amount for trade
}
fullURL = baseURL + "lrc/getNftOrder"
let makerOrderResult = await axios.post(fullURL,makerOrderRequest);

//submit the nft trade to platform
let tradeRequest = {
    makerOrder:makerOrderData.order,                             //maker Order for trade
    makerOrderEddsaSignature:makerOrderData.orderEddsaSignature, //maker Order EddsaSignature for trade
    makerFeeBips:1000,                                           //makerFeeBips for trade
    takerOrder:taker1OrderData.order,                            //taker Order EddsaSignature for trade
    takerOrderEddsaSignature:taker1OrderData.orderEddsaSignature,//taker Order EddsaSignature for trade
    takerFeeBips:100                                             //takerFeeBips for trade
}
fullURL = baseURL + "lrc/tradeNFT"
await axios.post(fullURL,tradeRequest);
```
- - getTradesList
```typescript
//get users nft trade list
let jsonData = {
    offset:1,   //the begin id of the query.
    limit:50    //number of records to return
}
let fullURL = baseURL + "lrc/getTradesList"
axios.post(fullURL,jsonData)
```
- - getBlock
```typescript
//get l2 block info by block id to anylse nft data
let jsonData = {
    id:1       //block id
}
let fullURL = baseURL + "lrc/getBlock"
axios.post(fullURL,jsonData)
```
- - getNftHolders
```typescript
//query nft holders by looprings nftData
let jsonData = {
    nftData,    //the Loopring's nft token data 
    offset,     //number of records to skip
    limit       //number of records to return
}
let fullURL = baseURL + "lrc/getNftHolders"
axios.post(fullURL,jsonData)
```

## Contribution
Thank you for considering to help out with the source code! We welcome contributions from anyone on the internet, and are grateful for even the smallest of fixes!

If you'd like to contribute to Uptick Lite On Loopring Service, please fork, fix, commit and send a pull request for the maintainers to review and merge into the main code base. 


## License  

Uptick Lite On Loopring is an GPL v3.0-licensed open source project with its ongoing development made possible entirely by the support of the uptick team. 
If you'd like to join us. please contact 
[our technical team](mailto:tech@uptickproject.com>) .


[![License: GPL v3.0](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.en.html)

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for details.
