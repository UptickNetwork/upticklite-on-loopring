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
import logoimg from "@/assets/default.png"

export const getImageIpfs = (ipfs, type) => {
    if(ipfs && ipfs!="null" && ipfs!="undefined") {
		let url=`https://loopring.mypinata.cloud/ipfs/${ipfs}`
        return url;
    } else {
        if(type == "avatar") {
            return logoimg;
        }
        return logoimg;
    }
}

const getChainTokenId = (name) => {
    let json = localStorage.getItem("ChainToken");
    if(json) {
        let tokens = JSON.parse(json);
        for (const key in tokens) {
            if (Object.hasOwnProperty.call(tokens, key)) {
                const ele = tokens[key];
                if(ele.name == name) {
                    return ele.tokenId;
                }
            }
        }
    }
    return 0;
}

const getChainByName = (name) => {
    let json = localStorage.getItem("ChainToken");
    if(json) {
        let tokens = JSON.parse(json);
        for (const key in tokens) {
            if (Object.hasOwnProperty.call(tokens, key)) {
                const ele = tokens[key];
                if(ele.name == name) {
                    return ele;
                }
            }
        }
    }
    return { 
        name: "",
        img: "",
        tokenId: ""
    };
}

const addressEllipsis = (addr) => {
    if(addr && addr.length > 10) {
        return addr.substr(0, 6) + "..." + addr.substr(-6);
    }
    return "";
}

export function init() {

    Vue.prototype.$walletUnit = "LRC";

    Vue.prototype.getImageIpfs = getImageIpfs;
    Vue.prototype.getChainTokenId = getChainTokenId;
    Vue.prototype.getChainByName = getChainByName;
    Vue.prototype.addressEllipsis = addressEllipsis;

}