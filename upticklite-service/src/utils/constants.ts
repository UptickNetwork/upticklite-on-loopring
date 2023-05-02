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
export const CROSS_TYPE = {
    NO_CROSS: -1,
    IRIS2BSC: 0,
    BSC2IRIS: 1
}

export const ERR_MSG = {
    MINT_ERROR:{
        NO:-1001,
        MSG:"Mint error"
    },
    

    
}

export const CHAIN_IDS = {
    IRIS_TETNET:  "nyancat-8",
    IRIS_MAINNET: "irishub-1",
    BSC_TETNET:  97,
    BSC_MAINNET: 56,
}

export const IRIS_PARAMS = {
    TIMEOUT:10000,
}

export const CROSS_STATUS = {

    START_DATA:             0,
    PACK_OK:                1,
    
    FROM_CHAIN_PENNDING:    2,
    FROM_CHAIN_OK:          3,

    CHAIN_CROSSING:         4,
   
    TO_CHAIN_PENNDING:      5,
    TO_CHAIN_OK:            6,
   
    FROM_CHAIN_ERROR:       7,
    TO_CHAIN_ERROR:         8,
    
    POST_SERVER_OK:         9
    
}