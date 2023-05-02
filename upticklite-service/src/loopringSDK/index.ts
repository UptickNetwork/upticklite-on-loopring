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
export * from "./utils";

export * from "./defs";

export * from "./api";

//xxl 00 import for extend						
export * from './extend'

import contractWallet_abi from "./api/config/abis/contractWallet.json";
import erc20_abi from "./api/config/abis/erc20.json";
import exchange_abi from "./api/config/abis/exchange_3_6.json";
import hebao_abi from "./api/config/abis/hebao.json";
import deposit_abi from "./api/config/abis/deposit.json";

export { contractWallet_abi, erc20_abi, exchange_abi, hebao_abi, deposit_abi };
