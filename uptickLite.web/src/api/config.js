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

(function() {
    let env = process.env.VUE_APP_ENVIRONMENT;
	let envs = process.env;
	let result = { };
	for (const key in envs) {
		if(key.search(/_dev|_uat|_pro/i) && key.length-key.search(/_dev|_uat|_pro/i)==4) {
			if(key.lastIndexOf(env) > 0) {
				let nkey = key.substring(0, key.length-4);
				result[nkey] = envs[key];
			}
		}
		else {
			result[key] = envs[key];
		}
	}
	window.$env = result;
	Vue.prototype.$env = result;
})();

// VUE_APP_API_URL +  api
export const BASE_URL = `/uptickapi`;
export const BASE_URL_S3 = `/uptickS3`;
