# Uptick Lite on Loopring


项目预览链接
https://upticklite.uptick.network/#/index

### 概述
本项目是一个基于Loopring平台的基础版NFT市场平台，包含围绕NFT资产的管理功能，包括NFT的发布、铸造、上架、交易、下架、卡包、转移等。目的不是直接成为一个商业化运营的NFT平台，而是通过这个NFT市场的基本功能，提供给业界一个基于Loopring的NFT平台的参考设计，使有计划在Loopring上建立自身的NFT平台的项目可以快速上手。

本项目由Uptick Network团队负责搭建和维护，Loopring团队同步参与并提供和Loopring技术相关的支持。

本项目将以开源的形式对外发布，凡是愿意遵守本项目的开源协议的开发者均可使用。

### 主要功能
作为一个基础版的NFT Marketplace，包含所有针对NFT操作的功能，以便有自身业务场景的开发商快速开发自己的专用NFT交易市场。本通用市场不包含涉及具体业务场景和日常运营的逻辑和功能。
#### 核心NFT功能：
  - Mint：铸造NFT资产
  - List: 交易市场上架
  - De-list：从市场下架
  - Trade：市场内交易达成
  - Transfer：资产转移
  - Market：一手市场和二手市场
  - Wallet：NFT资产卡包及账号管理
  - Storage：数据存储管理接口

### 系统设计
整个系统分为三个主要技术组成部分：
  - 中间件：实现和Loopring底层的交互
  - 服务端：实现NFT管理相关的业务逻辑和Marketplace管理
  - 前端：基于Web browser的Web端，实现和用户交互的功能
除了通用的Marketplace功能开发，技术层面的定制化要点在于和Loopring接口和NFT协议的结合、账户体系的结合、在loopring Layer2上实现NFT 交易以及基于Loopring撮合交易平台的订单管理。

### 代码仓库
前端
https://github.com/UptickNetwork/upticklite-web

中间件
https://github.com/UptickNetwork/upticklite-service

服务端
https://github.com/UptickNetwork/upticklite-api








### 参考文档
https://loopring.github.io/loopring_sdk/


