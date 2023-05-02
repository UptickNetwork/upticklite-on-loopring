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
var Web3 = require('web3');
const web3Obj = new Web3();
export const getRanHex = size => {
    let result = [];
    let hexRef = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'];

    for (let n = 0; n < size; n++) {
        result.push(hexRef[Math.floor(Math.random() * 16)]);
    }
    return result.join('');
}


export const timestampToDate = (time) => {
	let date = new Date(time);
	let Y = date.getFullYear() + "-";
	let M = (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1) + "-";
	let D = (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + " ";
	// let h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) + ':';
	// let m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) + ':';
	// let s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds());
	let strDate = Y + M + D;
	return strDate;
}

export const timestampToDateTime = (time) => {
	let date = new Date(time)
	let Y = date.getFullYear() + '-';
	let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	let D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate()) + ' ';
	let h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) + ':';
	let m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) + ':';
	let s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds());
	let strDate = Y+M+D+h+m+s;
	return strDate;
}
export const timestampToTimeHour = (time) => {
	let date = new Date(time)
	// let Y = date.getFullYear() + '-';
	// let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	// let D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate()) + ' ';
	let h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) ;
	//let m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) + ':';
	//let s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds());
	let strDate = h;
	return strDate;
}

export const timestampToTimeMinute = (time) => {
	let date = new Date(time)
	// let Y = date.getFullYear() + '-';
	// let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	// let D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate()) + ' ';
	//let h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) + ':';
	let m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) ;
	//let s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds());
	let strDate = m;
	return strDate;
}
export const timestampToTimeSecond = (time) => {
	let date = new Date(time)
	// let Y = date.getFullYear() + '-';
	// let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	// let D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate()) + ' ';
	//let h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) + ':';
	//let m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) + ':';
	let s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds());
	let strDate = s;
	return strDate;
}

export const addDate = (date, days)=> {
	let timestamp = date + days * 86400000;
	//let timestamp = date + days * 600000;
    return timestamp;
}

export const toIpfsLink = (ipsfHash)=>{
	return "ipfs://" + ipsfHash;
}
export const toS3Link = (ipsfHash)=>{
	return "https://dpx9zbsum7s83.cloudfront.net/" + ipsfHash +"";
}
export const toBaseUri = (subUri)=>{
	
	let prePrefixBaseUrl = process.env.VUE_APP_PREFIX_BASE_URL; 
	return prePrefixBaseUrl + subUri;
}


const BigNumber = require('big-number');
export const toUnitValue = (web3Obj,tokenId,OrgValue)=>{
	console.log("toUnitValue  ----")
	let env = process.env.VUE_APP_ENVIRONMENT;
	let retValue = 0;
	if(env == "testNet") {
		if(tokenId == 2 || tokenId == 8  ){
			retValue = (parseFloat(OrgValue) * 1000000);
		}else{
			retValue = web3Obj.utils.toWei(OrgValue,"ether");
		}

	}else{
		if(tokenId == 3 || tokenId == 6  ){
			retValue = (parseFloat(OrgValue) * 1000000);
		}else{
			retValue = web3Obj.utils.toWei(OrgValue,"ether");
		}

	}

	return retValue;
}

export const toShowValue = (web3Obj,tokenId,OrgValue)=>{	
	let env = process.env.VUE_APP_ENVIRONMENT;
	let retValue = 0;
	if(env == "testNet") {

	if(tokenId == 2 || tokenId == 8  ){
		retValue = parseFloat(OrgValue)/1000000;
	}else{
		retValue = web3Obj.utils.fromWei(OrgValue);
	}
	} else if(env == "pro") {
		
	if(tokenId == 3 || tokenId == 6  ){
		retValue = parseFloat(OrgValue)/1000000;
	}else{
		console.log("wxl -- tokenId",tokenId,OrgValue)
		retValue = web3Obj.utils.fromWei(OrgValue);
	}
	}
	return retValue.toString();
}

let tokenId2IndexMapDev = {
	0:0,1:1,2:2,6:3,8:2
}
let tokenId2IndexMapPro = {
	0:2,1:3,3:4,5:1,6:0
}
export const tokenId2Index = (tokenId)=>{
	let env = process.env.VUE_APP_ENVIRONMENT;
	if(env == "testNet") {
		return tokenId2IndexMapDev[parseInt(tokenId)]
	}else{
		console.log("tokenId2IndexMapPro",tokenId,tokenId2IndexMapPro[parseInt(tokenId)])
		return tokenId2IndexMapPro[parseInt(tokenId)]
	}

}
let maxFeeBips =1000
export const getMaxFeeBips = (totalRoyalty) => {
	return totalRoyalty*100;
}

export const getFeePrice = (orgPrice,tokenId,tradeCost) => {

	if(!orgPrice){
		orgPrice="1"
	}
	let price=parseFloat(orgPrice) * 0.01;
	price=number_format(price.toFixed(4))
	let bigOrgPrice = toUnitValue(web3Obj,tokenId,orgPrice+"")
	let tradeCostPrice ;
	
	let tradeCostToWei;
	let finalPrice ;
	// 计算成本价开始

	tradeCostToWei = toShowValue(web3Obj,tokenId,tradeCost.toString())
	tradeCostToWei  = parseFloat(Math.ceil(( tradeCostToWei )*10000))/10000;
	tradeCostPrice = Math.ceil(tradeCost/bigOrgPrice*100)*100
	
		// 计算成本价结束
	
// 比较成本价和单价的1%，取两者的较大值
	if(tradeCostToWei >price ){
		finalPrice = tradeCostToWei
		
	}else{
		finalPrice = price
		tradeCostPrice=100  //成本价比价格低，就取单价的1%，万分比是100
	}

	let feeJson = {
		tradeCostPrice:tradeCostPrice,
		feePrice:finalPrice

	}
	// 计算1%和成本费tradeCost 取大值 toUnitValue(web3Obj,tokenId,volume.toString())
	return feeJson;
}
var number_format = function(number, n){
	n = n ? parseInt(n) : 8;
	if (n <= 0) return Math.round(number);
	number = Math.round(number * Math.pow(10000, n)) / Math.pow(10000, n);
	return number;
}