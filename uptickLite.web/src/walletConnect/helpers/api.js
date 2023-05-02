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
import axios from "axios";

const api = axios.create({
    baseURL: "https://ethereum-api.xyz",
    timeout: 30000, // 30 secs
    headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
    },
});

export async function apiGetAccountAssets(address, chainId) {
    const response = await api.get(`/account-assets?address=${address}&chainId=${chainId}`);
    const { result } = response.data;
    return result;
}

export async function apiGetAccountTransactions(address, chainId) {
    const response = await api.get(`/account-transactions?address=${address}&chainId=${chainId}`);
    const { result } = response.data;
    return result;
}

export const apiGetAccountNonce = async (address, chainId) => {
    const response = await api.get(`/account-nonce?address=${address}&chainId=${chainId}`);
    const { result } = response.data;
    return result;
};

export const apiGetGasPrices = async () => {
    const response = await api.get(`/gas-prices`);
    const { result } = response.data;
    return result;
};
