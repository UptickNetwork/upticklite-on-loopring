<h1 align="center">Uptick Lite on Loopring Api</h1>
<p align="center">Uptick Lite on Loopring Api for the applicaitons built in loopring layer2.</p>
<div align="center">

[![License: GPL v3.0](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.en.html)
[![Version](https://img.shields.io/twitter/url?url=https%3A%2F%2Ftwitter.com%2FUptickproject)]()
[![discord](https://img.shields.io/badge/discord-active-brightgreen)](https://discord.com/channels/781005936260939818/981431815841669120)
![java 1.8](https://img.shields.io/badge/java-1.8-blue)
</div>

## Config

1、 Create database:

  Database version：mysql 5.7

  Database file:market.sql

2、edit config

[edit config](https://github.com/UptickNetwork/upticklite-api/blob/main/src/main/resources/application-dev.properties)
```
#port
server.port=<port>
# mysql config
mysql.ip=<mysql.ip>
mysql.database=<database>
mysql.username=<username>
mysql.password=<password>
mysql.port=3306

# MongoDB
spring.data.mongodb.host=127.0.0.1
spring.data.mongodb.port=27017
spring.data.mongodb.database=loopring_lite

# log info
logging.config=classpath:logback.xml
logging.level.root=info
logging.level.org.uptick.market.interceptor.DebugRewriteInterceptor=info
#Open or not swagger-ui
swagger.enable=true
#middleware url
node.config.middleware-url=<middleware.url>
```
[edit config] (https://github.com/UptickNetwork/upticklite-api/blob/main/src/main/resources/logback.xml)

```
#log
    <root level="info">
<!--        <appender-ref ref="STDOUT"/>--> Log in ConsoleAppender
        <appender-ref ref="syslog"/> Log in  log/sys.log
    </root>

```
## Run
```
#If the compilation is wrong, you need to modify the JDK path in the pom.xml
mvn clean  install

java -jar ./target/uptick-lite-1.0.jar
```

## api detail
Uptick Lite on Loopring Api implements business logic related to NFT management and marketplace management Including the modules of 
Data api,Market api,flea market api,homepage api,NFT api and so on.

- [Config](#config)
- [Run](#run)
- [api detail](#api-detail)
- [Data api](#data-api)
  - [Query pay token type](#query-pay-token-type)
- [Market api](#market-api)
  - [Query launchPad](#query-launchPad)
  - [Query marketPlace](#query-marketPlace)
  - [Query nft market info](#query-nft-market-info)
- [Flea market api](#flea-market-api)
  - [Query nft flea market list](#query-nft-flea-market-list)
  - [Query nft market info](#query-nft-market-info-1)
- [Homepage api](#homepage-api)
  - [Query collection info](#query-collection-info-1)
  - [Query nft market list](#query-nft-market-list-1)
- [NFT  api](#nft--api)
  - [Burn nft](#burn-nft)
  - [Nft collection info](#nft-collection-info)
  - [Contract list](#contract-list)
  - [Nft deList](#nft-delist)
  - [Deploy contract](#deploy-contract)
  - [Query the list of tokens I created](#query-the-list-of-tokens-i-created)
  - [Query the list of tokens I own](#query-the-list-of-tokens-i-own)
  - [Query the list of tokens I own,sort by creator](#query-the-list-of-tokens-i-ownsort-by-creator)
  - [Query token id](#query-token-id)
  - [Nft history](#nft-history)
  - [Nft list](#nft-list)
  - [Mint nft](#mint-nft)
  - [Single nft deList](#single-nft-delist)
  - [Single Nft list](#single-nft-list)
  - [Single trade](#single-trade)
  - [Nft transfer](#nft-transfer)
  - [Query token info](#query-token-info)
  - [Query token info by asset id](#query-token-info-by-asset-id)
  - [Nft trade](#nft-trade)
  - [Nft transfer](#nft-transfer-1)
- [Order  api](#order--api)
  - [Order list](#order-list)
  - [Order statements](#order-statements)
- [User info](#user-info)
  - [get user info](#get-user-info)
  - [Save or update user info](#save-or-update-user-info)
- [Contribution](#contribution)
- [License](#license)

**Interface list**

**HOST**:13.213.149.227:6091

**Version**:1.0

## Data api

### Query pay token type

**Interface address**:`/api/1.0/data/token.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**： Null

**Response example**:

```json
{
  "code": 0,
  "data": {},
  "map": {},
  "msg": "",
  "success": true
}
```

### sysndata

**Interface address**:`/api/1.0/user/updateData.do`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**： address

**Response example**:

```json
{
  "code": 0,
  "data": {},
  "map": {},
  "msg": "",
  "success": true
}
```

## Market api

### Query LaunchPad

**Interface address**:`/api/1.0/market/getMarketplace.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|pageNumber| Page number  | query | true |integer  |    
|pageSize| Page size  | query | true |integer  |    
|marketTokenType| Pay token type(ETH、USDT、DAI、LRC、USDC)  | query | false |string  |    
|search| Search  | query | false |string  |    
|address| Search  | query | false |string  |    
|nftShowType| Collection type  | query | true |3  |    
|sort| sort(1:create time,2:random)  | query | true |integer  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "firstPage": true,
    "lastPage": true,
    "list": [
      {
        "address": "",
        "baseUrl": "",
        "collectionMetadata": "",
        "createTimeLong": 0,
        "creatorName": "",
        "creatorPhoto": "",
        "description": "",
        "imgUrl": "",
        "listAmount": 0,
        "name": "",
        "owner": "",
        "ownerName": "",
        "ownerPhoto": ""
      }
    ],
    "orderBy": "",
    "pageNumber": 0,
    "pageSize": 0,
    "paras": {},
    "totalPage": 0,
    "totalRow": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```

### Query marketPlace

**Interface address**:`/api/1.0/market/getMarketplace.json `

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

|  Parameter name | Parameter description     |     in |  Required      |  Type  |  
|-----------------| -------------------------------- |-----------|--------|----|
| pageNumber      | Page number  | query | true |integer  |    
| pageSize        | Page size  | query | true |integer  |    
| marketTokenType | Pay token type(ETH、USDT、DAI、LRC、USDC)  | query | false |string  |    
| search          | Search  | query | false |string  |       
| sort            | sort(1:create time,2:random)  | query | true |integer  |     

**Response example**:

```json
{
  "code": 0,
  "data": {
    "firstPage": true,
    "lastPage": true,
    "list": [
      {
        "accountId": 0,
        "amount": 0,
        "contractAddress": "",
        "createTimeLong": 0,
        "creatorName": "",
        "creatorPhoto": "",
        "description": "",
        "haveAmount": 0,
        "imgUrl": "",
        "isBurn": true,
        "listAmount": 0,
        "name": "",
        "nftData": "",
        "nftId": "",
        "owner": "",
        "ownerName": "",
        "ownerPhoto": "",
        "soldOut": true,
        "tokenImgUrl": "",
        "tokenName": ""
      }
    ],
    "orderBy": "",
    "pageNumber": 0,
    "pageSize": 0,
    "paras": {},
    "totalPage": 0,
    "totalRow": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```



### Query collection list

**Interface address**:`/api/1.0/market/list.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|tokenAddress| Nft token address  | query | true |string  |    
|sourceType| 1:launch pad,2:marketPlace,3:on sale,4:off sale,5:owner| query | true |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "accountId": 0,
    "amount": 0,
    "contractAddress": "",
    "createTimeLong": 0,
    "creatorName": "",
    "creatorPhoto": "",
    "description": "",
    "haveAmount": 0,
    "imgUrl": "",
    "isBurn": true,
    "listAmount": 0,
    "name": "",
    "nftData": "",
    "nftId": "",
    "owner": "",
    "ownerName": "",
    "ownerPhoto": "",
    "soldOut": true,
    "tokenImgUrl": "",
    "tokenName": ""
  },
  "map": {},
  "msg": "",
  "success": true
}
```


## Homepage api

### Query collection info

**Interface address**:`/api/1.0/homepage/collection.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|pageNumber| Page number  | query | true |integer  |    
|pageSize| Page size  | query | true |integer  |    
|creator| Creator wallet address  | query | true |string  |    
|search| search  | query | true |string  |    
|filter| filter(1:creator)  | query | false |string  |    


**Response example**:

```json
{
  "code": 0,
  "data": {
    "firstPage": true,
    "lastPage": true,
    "list": [
      {
        "address": "",
        "baseUrl": "",
        "collectionMetadata": "",
        "createTimeLong": 0,
        "creatorName": "",
        "creatorPhoto": "",
        "description": "",
        "imgUrl": "",
        "listAmount": 0,
        "name": "",
        "owner": "",
        "ownerName": "",
        "ownerPhoto": ""
      }
    ],
    "orderBy": "",
    "pageNumber": 0,
    "pageSize": 0,
    "paras": {},
    "totalPage": 0,
    "totalRow": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```

### Query nft market list

**Interface address**:`/api/1.0/homepage/token.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|pageNumber| Page number  | query | true |integer  |    
|pageSize| Page size  | query | true |integer  |    
|creator| Creator wallet address  | query | true |string  |   

**Response example**:

```json
{
  "code": 0,
  "data": {
    "firstPage": true,
    "lastPage": true,
    "list": [
      {
        "accountId": 0,
        "amount": 0,
        "contractAddress": "",
        "createTimeLong": 0,
        "creatorName": "",
        "creatorPhoto": "",
        "description": "",
        "haveAmount": 0,
        "imgUrl": "",
        "isBurn": true,
        "listAmount": 0,
        "name": "",
        "nftData": "",
        "nftId": "",
        "owner": "",
        "ownerName": "",
        "ownerPhoto": "",
        "soldOut": true,
        "tokenImgUrl": "",
        "tokenName": ""
      }
    ],
    "orderBy": "",
    "pageNumber": 0,
    "pageSize": 0,
    "paras": {},
    "totalPage": 0,
    "totalRow": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```

## NFT  api

### Burn nft

**Interface address**:`/api/1.0/nft/burn.do`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|tokenAddress| Token address  | query | true |string  |    
|nftId| Nft id  | query | false |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {},
  "map": {},
  "msg": "",
  "success": true
}
```

### Nft collection info

**Interface address**:`/api/1.0/nft/collectionInfo.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|linkUrl| short url  | query | false |string  |    
|tokenAddress| Token address  | query | false |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "address": "",
    "baseUrl": "",
    "collectionMetadata": "",
    "createTimeLong": 0,
    "creatorName": "",
    "creatorPhoto": "",
    "description": "",
    "imgUrl": "",
    "listAmount": 0,
    "name": "",
    "owner": "",
    "ownerName": "",
    "ownerPhoto": ""
  },
  "map": {},
  "msg": "",
  "success": true
}
```

### Nft deList

## **Interface address**:`/api/1.0/nft/deList.do`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|owner| User wallet address  | query | true |string  |    
|nftAddress| nft token address  | query | true |string  |    
|nftId| nft id   | query | true |integer  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "firstPage": true,
    "lastPage": true,
    "list": [
      {
        "accountId": 0,
        "amount": 0,
        "contractAddress": "",
        "createTimeLong": 0,
        "description": "",
        "imgUrl": "",
        "name": "",
        "nftData": "",
        "nftId": "",
        "owner": ""
      }
    ],
    "orderBy": "",
    "pageNumber": 0,
    "pageSize": 0,
    "paras": {},
    "totalPage": 0,
    "totalRow": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```

### Deploy contract

**Interface address**:`/api/1.0/nft/deploy.do`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|address| NFT token address  | query | true |string  |    
|imgUrl| NFT img url  | query | true |string  |    
|name| NFT name  | query | true |string  |    
|collectionMetadata| NFT  metadate  | query | true |string  |    
|baseUrl| NFT base url  | query | true |string  |    
|owner| NFT owner  | query | true |string  |    
|description| NFT description  | query | false |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {},
  "map": {},
  "msg": "",
  "success": true
}
```


### Query the list of tokens I own,sort by creator

**Interface address**:`/api/1.0/nft/getOwnByCreator.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|pageNumber| Page number  | query | true |integer  |    
|pageSize| Page size  | query | true |integer  |    
|owner| User wallet address  | query | true |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "firstPage": true,
    "lastPage": true,
    "list": [
      {
        "accountId": 0,
        "amount": 0,
        "contractAddress": "",
        "createTimeLong": 0,
        "creatorName": "",
        "creatorPhoto": "",
        "description": "",
        "haveAmount": 0,
        "imgUrl": "",
        "isBurn": true,
        "listAmount": 0,
        "name": "",
        "nftData": "",
        "nftId": "",
        "owner": "",
        "ownerName": "",
        "ownerPhoto": "",
        "soldOut": true,
        "tokenImgUrl": "",
        "tokenName": ""
      }
    ],
    "orderBy": "",
    "pageNumber": 0,
    "pageSize": 0,
    "paras": {},
    "totalPage": 0,
    "totalRow": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```

### Query token id

**Interface address**:`/api/1.0/nft/getMyTokenId.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|owner| Wallet address  | query | true |string  |    
|tokenAddress| Token address  | query | true |integer  |    
|nftId| Nft id  | query | true |string  |    

**Response example**:

```json
{
	"code": 0,
	"data": {
		"firstPage": true,
		"lastPage": true,
		"list": [
			{
				"accountId": 0,
				"amount": 0,
				"assetExtension": {
					"createOwner": "",
					"daiTotalPrice": 0,
					"ethTotalPrice": 0,
					"issuerCoinDenom": "",
					"issuerPrice": 0,
					"issuerTokenType": "",
					"likeCount": 0,
					"lrcTotalPrice": 0,
					"nftId": "",
					"totalInventory": 0,
					"usdcTotalPrice": 0,
					"usdtTotalPrice": 0,
					"viewCount": 0
				},
				"assetStatus": 0,
				"contractAddress": "",
				"contractDto": {
					"address": "",
					"baseUrl": "",
					"collectionMetadata": "",
					"contractStatus": 0,
					"creatorName": "",
					"creatorPhoto": "",
					"description": "",
					"filter": 0,
					"imgUrl": "",
					"listAmount": 0,
					"name": "",
					"nftCount": 0,
					"owner": "",
					"ownerName": "",
					"ownerPhoto": "",
					"recommendDisabled": true,
					"recommendStartTime": "",
					"recommendStopTime": "",
					"search": "",
					"sortType": 0
				},
				"copyright": 0,
				"creatorName": "",
				"creatorPhoto": "",
				"description": "",
				"exNftId": "",
				"filter": 0,
				"haveAmount": 0,
				"imgUrl": "",
				"isBurn": true,
				"listAmount": 0,
				"lockAssetHash": "",
				"maxPrice": 0,
				"minPrice": 0,
				"name": "",
				"nftData": "",
				"nftId": "",
				"notInNftId": [],
				"orderMarketType": 0,
				"orderPrice": 0,
				"orderTokenType": "",
				"owner": "",
				"ownerName": "",
				"ownerPhoto": "",
				"qualification": 0,
				"saleModel": 0,
				"search": "",
				"soldOut": true,
				"sortType": 0,
				"tokenImgUrl": "",
				"tokenName": "",
				"tokenType": ""
			}
		],
		"orderBy": "",
		"pageNumber": 0,
		"pageSize": 0,
		"paras": {},
		"totalPage": 0,
		"totalRow": 0
	},
	"map": {},
	"msg": "",
	"success": true
}
```


### Query Contract list

**Interface address**:`/api/1.0/nft/getMyContract.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|owner| Wallet address  | query | true |string  |    
|search| search | query | true |string  |    
|filter| filter(1:creator) | query | false |string  |    

**Response example**:

```json
{
	"code": 0,
	"data": {
		"firstPage": true,
		"lastPage": true,
		"list": [
			{
				"address": "",
				"baseUrl": "",
				"collectionMetadata": "",
				"contractStatus": 0,
				"creatorName": "",
				"creatorPhoto": "",
				"description": "",
				"filter": 0,
				"imgUrl": "",
				"listAmount": 0,
				"name": "",
				"nftCount": 0,
				"owner": "",
				"ownerName": "",
				"ownerPhoto": "",
				"recommendDisabled": true,
				"recommendStartTime": "",
				"recommendStopTime": "",
				"search": "",
				"sortType": 0
			}
		],
		"orderBy": "",
		"pageNumber": 0,
		"pageSize": 0,
		"paras": {},
		"totalPage": 0,
		"totalRow": 0
	},
	"map": {},
	"msg": "",
	"success": true
}
```


### Nft history

**Interface address**:`/api/1.0/nft/history.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |
| ------------ | -------------------------------- |-----------|--------|----|
|pageNumber| Page number  | query | true |integer  |    
|pageSize| Page size  | query | true |integer  |    
|tokenAddress| Token address  | query | true |string  |    
|nftId| Nft id  | query | true |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "firstPage": true,
    "lastPage": true,
    "list": [
      {
        "amount": 0,
        "assetId": 0,
        "contractAddress": "",
        "createTimeLong": 0,
        "from": "",
        "nftId": "",
        "recipientName": "",
        "recipientPhoto": "",
        "senderName": "",
        "senderPhoto": "",
        "serialNo": "",
        "to": "",
        "txId": "",
        "txType": 0
      }
    ],
    "orderBy": "",
    "pageNumber": 0,
    "pageSize": 0,
    "paras": {},
    "totalPage": 0,
    "totalRow": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```

### Nft list

**Interface address**:`/api/1.0/nft/list.do`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  | 
| ------------ | -------------------------------- |-----------|--------|----|
|owner| User wallet address  | query | true |string  |    
|nftAddress| nft token address  | query | true |string  |    
|nftId| nft id   | query | true |string  |    
|marketPrice| market price  | query | true |string  |    
|tokenType| Pay token type(ETH、USDT、DAI、LRC、USDC)  | query | true |string  |    
|makerOrderJson| Maker order json  | query | true |string  |    
|signature| Maker signature  | query | true |string  |    
|limit| Bug limit  | query | false |integer  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "firstPage": true,
    "lastPage": true,
    "list": [
      {
        "accountId": 0,
        "amount": 0,
        "contractAddress": "",
        "createTimeLong": 0,
        "description": "",
        "imgUrl": "",
        "name": "",
        "nftData": "",
        "nftId": "",
        "owner": ""
      }
    ],
    "orderBy": "",
    "pageNumber": 0,
    "pageSize": 0,
    "paras": {},
    "totalPage": 0,
    "totalRow": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```

### Mint nft

**Interface address**:`/api/1.0/nft/mint.do`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  | 
| ------------ | -------------------------------- |-----------|--------|----|
|contractAddress| NFT token address  | query | true |string  |    
|description| NFT token id description  | query | false |string  |    
|name| NFT token id name  | query | true |string  |    
|owner| NFT token id owner  | query | true |string  |    
|imgUrl| NFT token id img url  | query | true |string  |    
|nftData| NFT token data  | query | true |string  |    
|nftId| NFT id  | query | true |string  |    
|accountId| account id  | query | true |integer  |    
|amount| nft token amount  | query | true |integer  |    

**Response example**:

```json
{
  "code": 0,
  "data": {},
  "map": {},
  "msg": "",
  "success": true
}
```


### Query token info

**Interface address**:`/api/1.0/nft/tokenInfo.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  | 
| ------------ | -------------------------------- |-----------|--------|----|
|tokenAddress| Token address  | query | true |string  |    
|nftId| Nft id  | query | true |string  |    
|owner| Nft owner wallet address  | query | false |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "accountId": 0,
    "amount": 0,
    "contractAddress": "",
    "createTimeLong": 0,
    "creatorName": "",
    "creatorPhoto": "",
    "description": "",
    "haveAmount": 0,
    "imgUrl": "",
    "isBurn": true,
    "listAmount": 0,
    "name": "",
    "nftData": "",
    "nftId": "",
    "owner": "",
    "ownerName": "",
    "ownerPhoto": "",
    "soldOut": true,
    "tokenImgUrl": "",
    "tokenName": ""
  },
  "map": {},
  "msg": "",
  "success": true
}
```


### Nft trade

**Interface address**:`/api/1.0/nft/trade.do`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|owner| User wallet address  | query | true |string  |    
|makerOwner| maker Owner wallet address  | query | true |string  |    
|accountId| Loopring account id  | query | true |integer  |    
|nftAddress| nft token address  | query | true |string  |    
|nftId| nft id   | query | true |string  |    
|takerOrderJson| Maker order json  | query | true |string  |    
|signature| Maker signature  | query | true |string  |    
|amount| Bug amount  | query | true |integer  |    

**Response example**:

```json
{
  "code": 0,
  "data": {},
  "map": {},
  "msg": "",
  "success": true
}
```

### Nft transfer

**Interface address**:`/api/1.0/nft/transfer.do`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|sender| Sender wallet address  | query | true |string  |    
|senderAccountId| Sender account id  | query | true |integer  |    
|recipient| Recipient wallet address  | query | true |string  |    
|recipientAccountId| Recipient account id  | query | true |integer  |    
|tokenAddress| Nft token address  | query | true |string  |    
|nftId| Nft id  | query | true |string  |    
|amount| Transfer amount  | query | true |integer  |    

**Response example**:

```json
{
  "code": 0,
  "data": {},
  "map": {},
  "msg": "",
  "success": true
}
```

## Order  api

### Order list

**Interface address**:`/api/1.0/order/list.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|pageNumber| Page number  | query | true |integer  |    
|pageSize| Page size  | query | true |integer  |   
|owner| User wallet address  | query | true |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "firstPage": true,
    "lastPage": true,
    "list": [
      {
        "contractAddress": "",
        "createTimeLong": 0,
        "deleted": true,
        "description": "",
        "id": 0,
        "json": "",
        "level": 0,
        "nftId": "",
        "nftImg": "",
        "nftName": "",
        "orderNo": "",
        "orderPrice": 0,
        "orderStatus": 0,
        "quantity": 0,
        "recipient": "",
        "recipientName": "",
        "recipientPhoto": "",
        "sender": "",
        "senderName": "",
        "senderPhoto": "",
        "tokenType": "",
        "txId": "",
        "weight": 0
      }
    ],
    "orderBy": "",
    "pageNumber": 0,
    "pageSize": 0,
    "paras": {},
    "totalPage": 0,
    "totalRow": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```

### Order statements

**Interface address**:`/api/1.0/order/statements.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|pageNumber| Page number  | query | true |integer  |    
|pageSize| Page size  | query | true |integer  |    
|nftId| nftId  | query | false |string  |    
|tokenAddress| Token address  | query | true |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "amount": 0,
    "listAmount": 0,
    "orderList": [
      {
        "contractAddress": "",
        "createTimeLong": 0,
        "deleted": true,
        "description": "",
        "id": 0,
        "json": "",
        "level": 0,
        "nftId": "",
        "nftImg": "",
        "nftName": "",
        "orderNo": "",
        "orderPrice": 0,
        "orderStatus": 0,
        "quantity": 0,
        "recipient": "",
        "recipientName": "",
        "recipientPhoto": "",
        "sender": "",
        "senderName": "",
        "senderPhoto": "",
        "tokenType": "",
        "txId": "",
        "weight": 0
      }
    ],
    "saleAmount": 0
  },
  "map": {},
  "msg": "",
  "success": true
}
```

## User info

### get user info

**Interface address**:`/api/1.0/user/info.json`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  |  
| ------------ | -------------------------------- |-----------|--------|----|
|address| waller address  | query | true |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {
    "accountId": 12542,
    "accountStatus": 1,
    "address": "0xb731a264d86714adcfc5841ba58db518b8bc142a",
    "background": "",
    "createTimeLong": 0,
    "description": "",
    "email": "",
    "name": "",
    "profilePhoto": ""
  },
  "map": {},
  "msg": "",
  "success": true
}
```

### Save or update user info

**Interface address**:`/api/1.0/user/save.do`

**Request type**：`POST`

**produces**:`["*/*"]`

**Request parameters**：

| Parameter name        | Parameter description     |     in |  Required      |  Type  | 
| ------------ | -------------------------------- |-----------|--------|----|
|accountStatus| Loopring account status(1:unregistered；2：Registered, not activated；3：activated)  | query | true |integer  |    
|accountId| Loopring account status  | query | false |integer  |    
|address| Loopring wallet address  | query | true |string  |    
|description| User description  | query | false |string  |    
|email| User e-mail  | query | false |string  |    
|name| User name  | query | false |string  |    
|profilePhoto| User profile photo  | query | false |string  |    
|background| User background image  | query | false |string  |    

**Response example**:

```json
{
  "code": 0,
  "data": {},
  "map": {},
  "msg": "",
  "success": true
}
```

## Contribution
Thank you for considering to help out with the source code! We welcome contributions from anyone on the internet, and are grateful for even the smallest of fixes!

If you'd like to contribute to Uptick Lite on Loopring Api, please fork, fix, commit and send a pull request for the maintainers to review and merge into the main code base. 


## License  
[![License: GPL v3.0](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.en.html)

Released under the GPL v3.0 open source agreement, if you want to use commercial, be re-developed, or for technical support, please contact 
[our technical team](mailto:tech@uptickproject.com>) .

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for details.
