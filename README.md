# Uptick Lite on Loopring


Project Preview Link:
https://upticklite.uptick.network/#/index

### Overview
Uptick Lite on Loopring is the basic version of the Uptick NFT Marketplace; based on Loopring, and providing NFT asset management functions (some of which include minting, listing, trading, and NFT transfer capabilities). The purpose is not to directly become a commercially operated NFT platform, but to provide the industry with a design reference of a Loopring-based NFT marketplace through the basic NFT management functions we provide. This helps the projects that originally planned to build their own NFT platform on Loopring get started as quickly as possible.

Uptick Lite on Loopring is developed and maintained by Uptick Network, with Loopring participating and simultaneously providing technical support.

This project will be open-sourced and become available to all developers who are willing to abide by the open-source agreement of this project.

### Main Function
As a general NFT Marketplace, this version only includes the basic functions for NFT operation, so that developers can quickly develop their own dedicated NFT marketplace to meet their own customized and specific business scenarios. This open-source NFT marketplace does not contain logic or functions related to the specific business scenarios and daily operations.

#### Core NFT Functions：
  - Mint：Create NFT assets
  -  List: Publish NFTs to the marketplace to start selling
  -  De-list: Remove NFTs from the marketplace to stop selling
  -  Trade: Buy and sell NFTs in the marketplace
  - Transfer: Transfer NFTs to another address
  - Market: First-hand and second-hand market
  - Wallet: Non-custodial NFT asset and account management
  - Storage: Data storage management interface

### System Design
The whole system is divided into three main technical components:
  - Middleware
    Realize the interaction via the bottom layer of Loopring

  - Server 
  Realize business logic related to NFT management and marketplace management
  - Front-End ：Through the web browser (network browser), the interaction between the system and the user is realized. In addition to the general marketplace functions, the system realizes NFT transactions on the Loopring Layer 2 network at the technical level by combining with the Loopring interface protocol, NFT protocol, and account management protocol; realizing the order management of the platform through the Loopring matching protocol

### Code Repository

WEB
https://github.com/UptickNetwork/upticklite-on-loopring/tree/main/upticklite-web

Middleware
https://github.com/UptickNetwork/upticklite-on-loopring/tree/main/upticklite-service

API Server
https://github.com/UptickNetwork/upticklite-on-loopring/tree/main/upticklite-api








### Reference Documents
https://loopring.github.io/loopring_sdk/


