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
<template>
   <div class="top" style="z-index: 201;">
      <v-expand-transition>
         <v-main tag="div" class="drawer-nav" :elevation="3" v-if="value" absolute>

            <template v-if="navOpen>0">
               <v-container class="d-flex flex-column py-0 pb-12 pb-lg-0" style="height: 50%">
                  <DropSelectNav :type="navType" @input="close" />
               </v-container>
            </template>
         </v-main>
      </v-expand-transition>
      <v-app-bar app absolute hide-on-scroll :scroll-target="`#${containerId}`" :fixed="value" class="pa-0" color="#fff"
         :elevation="0" :height="height">
         <v-container>
            <div class="d-flex align-center py-0  px-lg-0">
               <Logo height="43px" :simple="isMobile" style="margin-right: 15px;" :class="{ mobileLogo: isMobile }" />
               <v-spacer></v-spacer>
               <template>
                  <Nav v-if="!isMobile" />
                  <v-btn class="uc creatbtn ml-10" v-if="showCreate && isDeposit == 3 && !isMobile" @click="toCreateBtn" rounded
                     depressed>{{ $t('headerBtnCreate') }}</v-btn>
                  
                  <v-btn icon class="primary ml-2" v-if="navOpen==2" @click="close">
                     <v-icon color="#fff">mdi-close</v-icon>
                  </v-btn>
                  <UserCenterSelect class="ml-4" v-show="navOpen!=2" v-if="user && isDeposit == 3" @click="UserCenterClick" />

                  <v-btn v-if="isLoadComplete && !showCreate" class="ml-4 " rounded depressed
                     style="background-color: #270645 ; color: #ffffff; " @click="connectWallet">
                     {{ $t('headerBtnConnectWallet') }}
                  </v-btn>
                  <v-btn v-if="isLoadComplete && showCreate && isDeposit == 1" class="ml-4 " rounded depressed
                     style="background-color: #270645 ; color: #ffffff;" @click="showDeposit1">
                     Deposit & Activate Layer 2 
                  </v-btn>
                  <v-btn v-if="isLoadComplete && showCreate && isDeposit == 2" class="ml-4 " rounded depressed
                     style="background-color: #270645 ; color: #ffffff;" @click="showActivate">
                     Activate Layer 2
                  </v-btn>
               </template>
               <template v-if="isMobile">
                  <div v-if="user" class="mr-7">
                     <v-btn icon class="primary ml-2" v-if="userCenterOpen" @click="close()">
                        <v-icon color="#fff">mdi-close</v-icon>
                     </v-btn>
                  </div>
                  <template>
                     <v-btn icon class="primary ml-2" v-if="navOpen==1" @click="close()">
                        <v-icon color="#fff">mdi-close</v-icon>
                     </v-btn>
                     <v-btn icon class="primary ml-2" v-else @click="open()">
                        <v-icon color="#fff">mdi-menu</v-icon>
                     </v-btn>
                  </template>
               </template>
            </div>
         </v-container>
      </v-app-bar>
      <v-dialog v-model="openWallet">
         <wallet :keyChain="keyChain" @getOpen="Open" @getStatus="Status"></wallet>
      </v-dialog>
    
      <v-snackbar v-model="isShow" color="error" centered timeout="2000" style="opacity: 0.8;"><span
            style="color:white;font-size:20px;">{{ title }}</span></v-snackbar>

      <ActivateLoopring ref="deposit" @deposit="onDeposit" @activate="onActivate"></ActivateLoopring>
   </div>
</template>

<script lang="js">
import Logo from '@/components/logo/index.vue';
import Nav from './components/Nav.vue';
import ChainSelect from './components/ChainSelect.vue';
import LangSelect from './components/LangSelect.vue';
import UserCenterSelect from './components/UserCenterSelect.vue';
import DropSelectNav from './components/DropSelectNav.vue';
import Wallet from "../components/popup/wallet";
import api from "@/api";

import SearchInput from '@/components/SearchInput.vue'
import ChainInput from '@/components/ChainInput.vue';
import ActivateLoopring from '@/components/popup/ActivateLoopring.vue';


import { connect as walletConnect, events } from "@/walletConnect";

export default {
   name: 'Header',
   components: { Wallet, Logo, Nav, ChainSelect, LangSelect, UserCenterSelect, DropSelectNav, SearchInput, ChainInput, ActivateLoopring },
   props: {
      value: Boolean,
      containerId: String
   },
   data: function () {
      return {
   
         openWallet: false,
         navOpen: 0,
         navType: "head",
         userCenterOpen: false,
         isShow: false,
         title: '',
         user: null,
         keyChain: "IRIS",
         showCreate: false,
         isDeposit: 1,
         depositValue: "",

         coinData: [{ name: "LRC", img: require("@/assets/icons/chain_lrc_w.png") },
         { name: "ETH", img: require("@/assets/icons/chain_eth_w.png") },
         { name: "USDC", img: require("@/assets/icons/chain_usdc_w.png") },
         { name: "USDT", img: require("@/assets/icons/chain_usdt_w.png") },
         { name: "DAI", img: require("@/assets/icons/chain_dai_w.png") },],
         coinOpen: "none",
         coinIndex: 1,

         isLoadComplete: false,
         userInfo:{}
      };
   },
   computed: {
      isMobile: function () {
         return this.$vuetify.breakpoint.mobile;
      },
      height: function () {
         if ((this.$route.name === 'Auctionlist') && this.isMobile) {
            return 180
         }
         return 100;
      },
   },
   filters: {
      addressEllipsis(val) {
         if (val && val.length > 10) {
            return val.substring(0, 7) + "..." + val.substring(val.length - 6);
         }
      }
   },
   async mounted() {
      try {
         debugger
         window.eventBus.$on('ShowLogoBox', this.toCreateBtn);
         this.getChainToken();
         let json = window.localStorage.getItem("key_user");
         let fromAddress = "";
         let data = "";
         if (json != null && json.length != 0) {
            data = JSON.parse(json);
            window.walletType = data.walletType;

            this.user = data.user;
            this.$store.commit('SET_DID', data.did);
            console.log("wxl -- 111 set_did",this.$store.state.did)
            fromAddress = data.did;
            this.showCreate = true;
            sessionStorage.setItem("showCreate", this.showCreate);
            if(data.walletType == 2 || data.walletType == undefined) {
               let dep = await this.loopringUserInfo();
               if(dep) {
                  this.isDeposit = dep.accountStatus || 1;
                   sessionStorage.setItem("isDeposit", this.isDeposit);
               }
            }
            else if(data.walletType == 3) {
               this.isDeposit = 3;
                sessionStorage.setItem("isDeposit", this.isDeposit);
            }
			  await  this.updateDate();
         } else {
            this.showCreate = false;
             sessionStorage.setItem("showCreate", this.showCreate);
         }

         if(data.walletType == 2) {
            if(this.isDeposit <= 1 && this.$store.state.did) {
               let accountInfo = await this.LRCHandler.getAccountInfo();
               
               this.isDeposit = accountInfo.deposit;
               sessionStorage.setItem("isDeposit", this.isDeposit);
               this.loopringUserSave(this.isDeposit, accountInfo.accountId);
            }
            let isApyKeyExist = await this.LRCHandler.checkAndSetApiKeySession();
            if( isApyKeyExist == true ){
               this.isDeposit = 3;
                sessionStorage.setItem("isDeposit", this.isDeposit);
               await this.setShowCreate();
            }else if(isApyKeyExist == false){
               this.isDeposit = 2;
               sessionStorage.setItem("isDeposit", this.isDeposit);
            }
         }
      } catch (error) {
         console.log("Header mounted",error);
      }
      
      this.isLoadComplete = true;

      this.walletEvents();
   },
   methods: {
	   async updateDate(){
	            let updateParams = {
	                  address:this.$store.state.did
	               }
	              await api.home.updateDate(updateParams)
	         },
      walletEvents() {
         const data = localStorage.getItem("walletconnect");
         if(!data) return;
         walletConnect();
         events.$on("disconnect", res => {
            console.log("wxl --- disconnect")
            console.log("disconnect",res);

            localStorage.clear();
            sessionStorage.clear();
            
            location.reload();
         });
         events.$on("session_update", res => console.log("session_update",res));
      },
      getChainToken() {
         api.home.chainToken().then(res => {
            let list = [];
            for (const key in res.data) {
                  list.push({ 
                     name: res.data[key].symbol,
                     img: res.data[key].logourl,
                     tokenId: res.data[key].tokenId
                  });
            }
            list.sort((a,b)=>{ return a.tokenId-b.tokenId});
            localStorage.setItem("ChainToken", JSON.stringify(list));
         });
      },
      connectWallet: function () {
         let key = localStorage.getItem("KEY_CHAIN");
         if (key) this.keyChain = key;

         this.openWallet = true
         this.user = false
		     this.updateDate();

      },
      UserCenterClick() {
         this.navOpen = 2;
         this.navType = "user";
         this.$emit('input', true);
      },
      open: function () {
         this.navOpen = 1;
         this.navType = "head";
         this.$emit('input', true);
      },
      close: function () {
         this.navOpen = 0;
         this.$emit('input', false);
      },
      Open(e) {
         if(e == 1) {
            this.openWallet = false;
            location.reload();
         } else {
            this.openWallet = e;
         }
      },
      Status(s, account, type) {
         // this.user = s;
         if(type == 3) {
            this.isDeposit = 3;
            sessionStorage.setItem("isDeposit", this.isDeposit);
         }
         this.setShowCreate();
         localStorage.setItem("key_user", JSON.stringify({ user: s, did: this.$store.state.did, bscAddress: "", walletType: type}));
      },
     
      coinSelect() {
         if (this.coinOpen == "none")
            this.coinOpen = "unset";
         else
            this.coinOpen = "none";
         this.$nextTick(() => {
            this.$refs.listBlur.focus();
         });
      },
      coinListBlur() {
         this.coinOpen = "none";
      },
      chainChange(index) {
         this.coinIndex = index;
         // this.coinOpen = "none";
      },
      chainInput(val) {
         this.depositValue = val;
      },
      async toCreateBtn() {
         if (this.$store.state.did) {
      
                this.$router.push({ name: 'Create' })
            
           
         } else {
            let key = localStorage.getItem("KEY_CHAIN");
            if (key) this.keyChain = key;

            this.openWallet = true
            this.user = false

         }

      },
      async setShowCreate() {
         if (this.$store.state.did) {
            let param = { address: this.$store.state.did };
            
            let myInfo = await api.home.loopringUserInfo(param);
            if (!myInfo.data) {
               this.showCreate = true;
                sessionStorage.setItem("showCreate", this.showCreate);
             } 
            //else {
            //    let grade = myInfo.data.grade;
            //    if (grade == 2 ) {
            //       this.showCreate = true;
            //        sessionStorage.setItem("showCreate", this.showCreate);
            //    } else {
            //       this.showCreate = false;
            //        sessionStorage.setItem("showCreate", this.showCreate);
            //    }
            // }
            let dep = await this.loopringUserInfo();

            this.isDeposit = dep.accountStatus || 1;
            sessionStorage.setItem("isDeposit", this.isDeposit);
			    this.updateDate();
         }

      },
      onDeposit(value) {
         if (value) {
            this.isDeposit = 2;
            sessionStorage.setItem("isDeposit", this.isDeposit);
         } else {
            this.isDeposit = 1;
            sessionStorage.setItem("isDeposit", this.isDeposit);
         }
         this.loopringUserSave(this.isDeposit, 0);
      },
      onActivate(value) {
         this.isDeposit = 3;
         this.loopringUserSave(this.isDeposit, value.accountId);
      },
      showDeposit1() {
         this.$refs.deposit.show();
      },
      showActivate() {
         this.$refs.deposit.show();
      },
      async loopringUserSave(status, accountId) {
         let did = this.$store.state.did;
         // this.isDeposit = status;
		
			 let accountParam={
			 			 address: did,
			 			 accountStatus: status, 
			 			 accountId:accountId,
			 			 } 
			 	 if(status==3){
			 let resolve = await api.home.reverseAddrEns(did);
			 if(resolve.data&&resolve.data.length>0){
			 		let ens = resolve.data[0].ens;
			 		accountParam.domain=ens
				} 
			 }
			  console.log("377===========")
			 await api.home.loopringUserSave(accountParam);
			  if(status==3){
			   window.location.reload();
			 }
			 

      },
      async loopringUserInfo() {
         let did = this.$store.state.did;
         let res = await api.home.loopringUserInfo({ address: did });
         this.userInfo = res
         localStorage.setItem("key_user_grade", JSON.stringify({  grade: res.data.grade}));
         if (res.code == 0) {
            return res.data;
         } else {
            return { accountStatus: 0 };
         }
      },
   }
};
</script>

<style lang="scss" scoped>
.top {
   letter-spacing: 0px;

   &::v-deep .pa-0 {
      height: 78px !important;
   }
   &::v-deep .drawer-nav {
      padding: 73px 0px 0px !important;
   }
}

.creatbtn {
   font-weight: bold;;
   font-size: 15px;
   font-weight: bold;
   font-stretch: normal;
   line-height: 100px;
   letter-spacing: 0px;
   color: #ffffff;
}

.mobileLogo {
   margin-left: 10px;
}

.mobileChain {
   // border: solid 1px red;
   margin-left: -12px;
   margin-right: 3px;
}

.deposit-btn {
   position: relative;

}
.deposit-btn::after {
   content: "";
   background: url(../assets/loading.gif) no-repeat;
   background-size: 30px 30px;
   width: 30px;
   height: 30px;
   position: absolute;
   right: 180px;
   top: 11px;
   border-radius: 50%;
}


.connected-with {
   display: flex;
   // justify-content: center;
   align-items: center;
   user-select: none;

   .close {
      width: 30px;
      height: 31px;
      position: absolute;
      right: 10px;
      top: 11px;
      cursor: pointer;
   }

   .tit {
      font-family: Helvetica;
      font-size: 15px;
      line-height: 48px;
      color: #766983;
   }

   .addr {
      font-weight: bold;;
      font-size: 25px;
      line-height: 50px;
      color: #270645;
   }

   .mid {
      display: flex;
      justify-content: space-between;
      width: 60%;

      .left,
      .right {
         display: flex;
         align-items: center;
         cursor: pointer;

         .img {
            width: 13px;
            height: 15px;
         }

         .txt {
            font-family: Helvetica;
            font-size: 15px;
            line-height: 48px;
            color: #270645;
            margin-left: 10px;
         }
      }
   }

   .remark {
      font-family: Helvetica;
      font-size: 15px;
      line-height: 48px;
      color: #766983;
   }

   .uc {
      width: 450px;
      height: 50px;
      font-family: Helvetica;font-weight: bold;
      font-size: 20px;
      line-height: 100px;
      color: #ffffff;
   }
}

.create-account {
   display: flex;
   // justify-content: center;
   // align-items: center;
   user-select: none;
   width: 600px;
   height: 400px;
   overflow: hidden;

   .close {
      width: 30px;
      height: 31px;
      position: absolute;
      right: 10px;
      top: 11px;
      cursor: pointer;
   }

   .tit {
      font-family: Helvetica;font-weight: bold;
      font-size: 25px;
      line-height: 50px;
      color: #270645;
   }

   .subtit {
      font-family: Helvetica;font-weight: bold;
      font-size: 15px;
      line-height: 50px;
      color: #270645;
   }

   .remark {
      font-family: Helvetica;
      font-size: 15px;
      line-height: 20px;
      color: #766983;
      margin: 30px 0 0;
   }
   .patient{
     font-family: Helvetica;
      font-size: 15px;
      line-height: 20px;
      color: #766983;
      margin: 10px 0 30px; 
   }

   .uc {
      font-family: Helvetica;font-weight: bold;
      font-size: 20px;
      height: 51px;
      color: #ffffff;
   }

   .middle {
      border: solid 2px #e3e3e3;
      border-radius: 5px;
      display: flex;
      align-items: center;
      padding: 10px;
      position: relative;

      .select {
         display: flex;
         align-items: center;

         img {
            width: 40px;
            height: 40px;
         }

         .txt {
            font-family: Helvetica;
            font-size: 20px;
            color: #270645;
            margin-left: 10px;
         }

         .icon {
            margin-left: 20px;
         }
      }

      .list {
         position: absolute;
         left: 14px;
         top: 50px;
         width: 112px;
         background-color: #ffffff;
         box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
         border-radius: 5px;
         z-index: 9;

         .item {
            display: flex;
            align-items: center;
            margin: 5px;
            padding: 5px 10px;

            img {
               width: 27px;
               height: 27px;
            }

            .txt {
               font-size: 13px;
               color: #270645;
               margin-left: 10px;
               width: 360px;
            }

            &:hover {
               background-color: #1d42ff;
               border-radius: 5px;
            }
         }
      }

      input {
         font-family: Helvetica;font-weight: bold;
         font-size: 24px;
         color: #766983;
         text-align: right;
         width: 350px;
         margin-left: auto;
         margin-right: 10px;

         &:focus {
            outline: none;
         }
      }
   }
}

.activate-account {
   display: flex;
   // justify-content: center;
   align-items: center;
   user-select: none;
   width: 600px;
   height: 290px;
   overflow: hidden;

   .close {
      width: 30px;
      height: 31px;
      position: absolute;
      right: 10px;
      top: 11px;
      cursor: pointer;
   }

   .tit {
      font-family: Helvetica;font-weight: bold;
      font-size: 25px;
      line-height: 50px;
      color: #270645;
   }

   .remark {
      font-family: Helvetica;
      font-size: 15px;
      line-height: 25px;
      color: #270645;
      margin-bottom: 10px;
   }

   .tip {
      font-family: Helvetica;
      font-size: 15px;
      color: #270645;
      align-self: flex-start;
      margin-top: 20px;
      color: #ff0033;
   }

   .amount {
      font-family: Helvetica;
      font-size: 15px;
      color: #270645;
      align-self: flex-start;
      margin-bottom: 20px;
   }

   .uc {
      width: 450px;
      height: 51px;
   }
   
}
</style>
