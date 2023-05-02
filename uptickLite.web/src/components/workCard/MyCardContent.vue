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
   <div style="overflow: hidden;height: 125px;" class="pb-3">
      <v-card-title class="text--title px-5 py-4">{{ value.name }}</v-card-title>
      <v-card-text class="d-flex flex-row align-center justify-space-between px-5 pb-6" style="height: 49px">
         <div class="d-flex flex-row mt-2" v-if="value.makerType != '2'">
            <div>
               <v-img :src="getChainByName(value.marketTokenType).img" alt="logo" width="22px" height="22px" />
            </div>
            <div class="
		   ml-2
		   text--number text--primary-rd
		   font-weight-medium
		 " style="line-height:22px;">
               {{ value.marketPrice }}
            </div>
         </div>
         <v-spacer></v-spacer>

         <div class="d-flex flex-row align-center">

            <img class="ml-5" src="@/assets/icons/icon_burn.png" width="25px" height="25px" @click="burnBtn"
               v-if="value.isBurn == true" alt="" />
            <img class="ml-5" src="@/assets/icons/ban.png" width="25px" height="25px" v-if="value.denomStatus == 3"
               alt="" />
            <img v-if="value.denomStatus != '3' && value.denomStatus != '4' && value.creator == this.$store.state.did" class="ml-5"
               src="@/assets/icons/icon_data.png" width="25px" height="25px" @click="saleTotalBtn" alt="" />
         </div>
      </v-card-text>
      <v-card-text class="py-0 px-5" style="height: 44px">
         <template v-if="value.listAmount > 0">
            <v-btn class="sale" dark rounded depressed width="178" height="28" @click="EndSaleBtn">{{
                  $t("popupSoldoutEndsale")
            }}
            </v-btn>
         </template>
         <template v-else-if="value.listAmount == 0 && value.amount > 0 &&value.soldOut==false">
            <div class="d-flex justify-space-between" style="margin-top:-8px">
               <div class="btnbox">
                  <v-btn class="rd" rounded @click="TransferBtn">{{
                        $t("popupNewTransferTransfer")
                  }}
                  </v-btn>
               </div>
               <template>
                  <v-btn class="satrtsale uc" dark rounded depressed width="91" height="28" @click="putOnSaleBtn">{{
                        $t("popupMarketStartSale")
                  }}
                  </v-btn>
               </template>

            </div>
         </template>
         <template v-if="value.soldOut">
            <div class="sold_out">{{ $t("workCardSoldOut") }}</div>
         </template>

         <template v-else-if="value.denomStatus == '4'">
            <div class="
            d-flex
            justify-center
            align-center
            pt-3
            text--info text--secondary-rd
          ">
               {{ $t("workCardInprogress") }}
            </div>
         </template>
      </v-card-text>

      <v-dialog v-model="Transfer">
         <new-transfer :imgUrl="imgUrl" :address="address" :level="1" :name="name" :contracId="value.id"
            :offSaleTokenIds="offSaleTokenIds" :parentValue="value" @getOpen="opentransfer"
            @promptBoxCommit="promptBoxCommit"></new-transfer>
      </v-dialog>
      <v-dialog v-model="openputonsale">
         <market v-if="openputonsale" :address="address" :onSaleTokenIds="onSaleTokenIds"
            :offSaleTokenIds="offSaleTokenIds" :name="name" :parentValue="value" v-bind="$attrs" v-on="$listeners"
            @getOpen="openPutOnSale"></market>
      </v-dialog>
      <v-dialog v-model="Burning">
         <burn v-if="Burning" :pageValue="value" @getOpen="openBurn"></burn>
      </v-dialog>
      <v-dialog v-model="Sold">
         <soldout :address="address" :onSaleTokenIds="onSaleTokenIds" :offSaleTokenIds="offSaleTokenIds"
            :name="value.name" :parentValue="value" @getOpen="openSold"></soldout>
      </v-dialog>
      <v-dialog v-model="Total">
         <saletotal v-if="Total" :pageData="value" :address="address" :id="value.id" :name="value.name"
            :minPrice="value.minPrice" @getOpen="openTotal"></saletotal>
      </v-dialog>
      <v-dialog v-model="Saleout">
         <soldout @getOpen="openSaleout"></soldout>
      </v-dialog>
   </div>
</template>

<script lang='js'>
import newTransfer from "@/components/popup/newTransfer.vue";
import Burn from "@/components/popup/burn.vue";
import Soldout from "@/components/popup/soldout.vue";
import Saletotal from "@/components/popup/saletotal.vue";
import PutOnSale from "@/components/popup/putOnSale.vue";
import Market from "@/components/popup/market.vue";


export default {
   components: {

      Burn,
      Soldout,
      Saletotal,
      newTransfer,
      PutOnSale,
      Market
   },
   name: "MyCardContent",
   props: {
      value: {
         type: Object
      }
   },
   data: function () {
      return {
         Transfer: false,
         openputonsale: false,
         Burning: false,
         Sold: false,
         Total: false,
         Saleout: false,
         openaban: false,
         openisdeal: false,
         opendeal: "",
         // countLogo,
         now: "",
         start: "",
         end: "",
         insure: "",
         day: 0,
         hour: 0,
         minutes: 0,
         second: 0,
         seconds: "",
         hh: 0,
         mm: 0,
         ss: 0,
         moreseconds: "",
         address: "",
         imgUrl: "",
         name: "",
         level: 1,
         balance: "",
         onSaleTokenIds: "",
         offSaleTokenIds: "",
         supplyLimit: "",
         avliableLenth: 0,
         minPrice: "",
         constructID: "",
         nftType: ""
      };
   },
   mounted() {
      this.init();
   },
   methods: {
      init() {
         this.Time();
         this.address = this.value.contractAddress;
         this.imgUrl = this.value.imgUrl;
         this.name = this.value.name;
         this.constructID = this.value.id;
         this.onSaleTokenIds = this.value.onSaleTokenIds;
         this.offSaleTokenIds = this.value.offSaleTokenIds;
         this.supplyLimit = this.value.supplyLimit;
         this.nftType = this.value.nftType;
         if (this.value.offSaleTokenIds != null) {
            this.avliableLenth = this.value.offSaleTokenIds.length;
         }
         if (this.value.onSaleTokenIds != null) {
            this.avliableLenth += this.value.onSaleTokenIds.length;
         }
      },
      getTime() {
         this.now = Date.parse(new Date());

         this.insure = 1000 * 60 * 60 * 48 + this.end;//48hour

         if (this.now < this.start) {
            this.seconds = (this.start - this.now) / 1000;
         } else if (this.now < this.end) {
            this.seconds = (this.end - this.now) / 1000;
         } else if (this.now < this.insure) {
            this.moreseconds = (this.insure - this.now) / 1000;
         }
      },

      countDown() {
         if (this.seconds > 0) {
            let d = parseInt(this.seconds / (24 * 60 * 60));
            let h = parseInt(((this.seconds / (60 * 60)) % 24) + d * 24);
            this.hour = h < 10 ? "0" + h : h;
            let m = parseInt((this.seconds / 60) % 60);
            this.minutes = m < 10 ? "0" + m : m;
            let s = parseInt(this.seconds % 60);
            this.second = s < 10 ? "0" + s : s;
         }
      },
      moreCountDown() {
         if (this.moreseconds > 0) {
            let d = parseInt(this.moreseconds / (24 * 60 * 60));
            let h = parseInt(((this.moreseconds / (60 * 60)) % 24) + d * 24);
            this.hh = h < 10 ? "0" + h : h;
            let m = parseInt((this.moreseconds / 60) % 60);
            this.mm = m < 10 ? "0" + m : m;
            let s = parseInt(this.moreseconds % 60);
            this.ss = s < 10 ? "0" + s : s;
         }
      },

      Time() {
         setInterval(() => {
            if (this.seconds > 0) {
               this.seconds -= 1;
               this.countDown();
            } else if (this.moreseconds > 0) {
               this.moreseconds -= 1;
               this.moreCountDown();
            } else {
               this.getTime();
            }

         }, 1000);
      },
      AbanBtn() {
         this.openaban = true;
      },
      openAban(e) {
         this.openaban = e;
      },
      iSdealBtn() {
         this.openisdeal = true;
      },
      openisDeal(e) {
         this.openisdeal = e;
      },
      dealBtn() {
         this.opendeal = true;
      },
      openDeal(e) {
         this.opendeal = e;
      },


      TransferBtn() {
         this.Transfer = true;
      },
      opentransfer(e) {
         if (e == 1) {
            this.Transfer = false;
            location.reload();
         } else {
            this.Transfer = e;
         }
      },

      putOnSaleBtn() {
         this.openputonsale = true;
      },
      openPutOnSale(e) {
         if (e == 1) {
            this.openputonsale = false;
         } else {
            this.openputonsale = e;
         }
      },

      burnBtn() {
         this.Burning = true;
      },
      openBurn(e) {
         if (e == 1) {
            this.Burning = false;
           
         }
         else {
            this.Burning = e;
         }
      },

      EndSaleBtn() {
         this.Sold = true;
      },
      openSold(e) {
         if (e == 1) {
            this.Sold = false;
         } else {
            this.Sold = e;
         }
      },

      saleTotalBtn() {
         this.Total = true;
      },
      openTotal(e) {
         this.Total = e;
      },

      saleoutBtn() {
         this.Saleout = true;
      },
      openSaleout(e) {
         this.Saleout = e;
         window.location.reload();
      },
      promptBoxCommit(v) {
         if (v != 3) {
            this.Transfer = false;
         }
      },
      workClick() {
         this.$router.push({ name: "Saledetail", query: { id: this.value.id } });
      }
   },
};

</script>

<style lang='scss' scoped>
.text--primary-rd {
   color: #270645;
}

.text--secondary-rd {
   color: #766983;
}

.text--info {
   font-size: 10px !important;
   line-height: 1;
   letter-spacing: 0px;
}

.text--title {
   display: inline-block;
   font-size: 13px;
   line-height: 24px;
   font-weight: bold;
   overflow: hidden;
   text-overflow: ellipsis;
   white-space: nowrap;
   width: 230px;
   margin-bottom: -17px;
}

.text--sub-title {
   font-size: 12px;
   line-height: 1;
}

.text--number {
   font-size: 15px;
   line-height: 1;
}

.v-divider {
   border-color: #e3e3e3;
}

.satrtsale {
   background-color: #1d42ff !important;
   border-radius: 18px;
   font-family: Helvetica;
   font-weight: bold;
   font-size: 12px !important;
   font-weight: bold;
   font-stretch: normal;
   line-height: 100px;
   letter-spacing: 0px;
   color: #ffffff;
   text-align: center;
}

.btnbox {
   width: 91px;
   height: 28px;
   box-sizing: border-box;
   padding: 2px;
   background-color: #1d42ff;
   border-radius: 17px;
   margin-left: -5px;
   margin-right: 10px;

   .rd {
      width: 100%;
      height: 100%;
      border: none;
      border-radius: 17px;
      background: #fff;
      font-family: Helvetica;
      font-weight: bold;
      font-size: 12px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0;
      color: #270645;
   }
}

.sold_out {
   width: 170px;
   height: 30px;
   margin-top: -10px;
   background-blend-mode: normal, normal;
   border-radius: 18px;
   text-align: center;
   font-family: Helvetica;
   font-weight: bold;
   font-size: 13px;
   font-weight: bold;
   font-stretch: normal;
   line-height: 30px;
   letter-spacing: 0px;
   color: #270645;

}

.dialodup5 {
   width: 281px;
   height: 281px;
   background-color: rgba(255, 255, 255, 0.9);
   position: absolute;
   top: 0;
   left: 0;
   z-index: 10;

   .tip {
      font-family: Helvetica;
      font-size: 13px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      margin-top: 121px;
      margin-bottom: 9px;
      text-align: center;
   }

   .timer {
      font-family: Helvetica;
      font-weight: bold;
      font-size: 15px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      margin-bottom: 32px;
      text-align: center;
   }

   .drawbtn {
      width: 244px;
      height: 36px;
      background-color: #270645;
      border-radius: 17px;
      margin-left: 18px;
      font-family: Helvetica;
      font-weight: bold;
      font-size: 13px;
      font-weight: bold;
      font-stretch: normal;
      line-height: 36px;
      text-align: center;
      letter-spacing: 0px;
      color: #ffffff;
      margin-top: 210px;
   }

   .dealbtn {
      width: 244px;
      height: 36px;
      background-color: #270645;
      border-radius: 17px;
      margin-left: 18px;
      font-family: Helvetica;
      font-weight: bold;
      font-size: 13px;
      font-weight: bold;
      font-stretch: normal;
      line-height: 36px;
      text-align: center;
      letter-spacing: 0px;
      color: #ffffff;
   }
}

.dialodup4 {
   width: 281px;
   height: 281px;
   background-color: rgba(255, 255, 255, 0.9);
   position: absolute;
   top: 0;
   left: 0;
   z-index: 10;

   .tip {
      font-family: Helvetica;
      font-size: 13px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      margin-top: 105px;
      margin-left: 28px;
      margin-bottom: 9px;
   }

   .timer {
      font-family: Helvetica;
      font-weight: bold;
      font-size: 15px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      margin-bottom: 32px;
      text-align: center;
   }

   .btn {
      .box {
         width: 115px;
         height: 36px;
         box-sizing: border-box;
         padding: 2px;
         background-image: linear-gradient(0deg,
               #d202fd 0%,
               #a538fe 26%,
               #776eff 51%,
               #01facd 100%);
         border-radius: 18px;

         .abanbtn {
            width: 100%;
            height: 100%;
            border: none;
            border-radius: 20px;
            background: #fff;
            font-family: Helvetica;
            font-weight: bold;
            font-size: 15px;
            font-weight: bold;
            font-stretch: normal;
            letter-spacing: 0;
            color: #270645;
         }
      }

      .dealbtn {
         width: 116px;
         height: 36px;
         background-color: #270645;
         border-radius: 17px;
         margin-left: 18px;
         font-family: Helvetica;
         font-weight: bold;
         font-size: 13px;
         font-weight: bold;
         font-stretch: normal;
         line-height: 36px;
         text-align: center;
         letter-spacing: 0px;
         color: #ffffff;
      }
   }
}

.sale {
   background-color: #1d42ff !important;
   width: 100%;
   height: 100%;
   border: none;
   border-radius: 17px;
   background: #fff;
   font-family: Helvetica;
   font-weight: bold;
   font-size: 12px;
   font-weight: bold;
   font-stretch: normal;
   letter-spacing: 0;
   margin-top: -10px;

}
</style>
