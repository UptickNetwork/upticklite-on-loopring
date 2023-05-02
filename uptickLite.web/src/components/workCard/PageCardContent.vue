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
   <div>
      <v-card-title class="text--title px-5 py-4">{{value.name}}</v-card-title>
      <v-card-text v-if="value.useType == 1" class="d-flex flex-row justify-space-between py-0 px-5">
         <template>
            <div class="d-flex justify-space-between status-7">
               <div>
                  <div class="text--sub-title text--primary-rd">
                     {{ $t("workCardCurrentBid") }}
                  </div>
                  <div class="d-flex flex-row mt-2">
                     <div>
                        <v-img
                           v-if="value.marketPrice" :src="getChainByName(value.marketTokenType).img"
                            alt="logo"
                            width="22px"
                            height="22px"
                        />
                     </div>
                     <div
                         class="
                     ml-2
                     text--number text--primary-rd
                     font-weight-medium
                   " style="line-height:22px;"
                     >
                        {{ value.minPrice }}
                     </div>
                  </div>
               </div>
            </div>
            <div class="sold_out">{{$t("workCardSoldOut")}}</div>
         </template>
      </v-card-text>
      <v-card-text  v-else  class="py-0 px-5" style="height: 40px; margin-top: 13px;">
         <template>
            <template v-if="value.soldOut == true">
               <div class="d-flex justify-space-between mt-n2">
                  <div class="d-flex justify-space-between mt-n2">
                   <div>
                      <div class="d-flex flex-row mt-n2">
                         <div >
                           <v-img v-if="value.marketPrice" :src="getChainByName(value.marketTokenType).img" alt="logo" width="22px" height="22px"/>
                        </div>
                         <div
                             class="ml-2 text--number text--primary-rd font-weight-medium" style="line-height:22px;"
                         >
                            {{value.marketPrice}}
                         </div>
                      </div>
                   </div>
               </div>
               <div class="sold_out">{{$t("workCardSoldOut")}}</div>
               </div>
            </template>
            <template v-else>
                 <div>
                     <!-- <div class="text--sub-title text--primary-rd">Current Bid</div> -->
                     <div class="d-flex flex-row mt-n2" style="">
                        <div >
                           <v-img v-if="value.marketPrice" :src="getChainByName(value.marketTokenType).img" alt="logo" width="22px" height="22px"/>
                        </div>
                        <div
                            class="ml-2 text--number text--primary-rd font-weight-medium" style="line-height:22px;"
                        >
                           {{value.marketPrice}}
                        </div>
                     </div>
                  </div>
               
            </template>
         </template>
      </v-card-text>
   </div>
</template>

<script lang='js'>

  export default {
    name: "pageCardContent",
    props: {
      value: {
        type: Object,
        default: () => ({
          denomType: 8,
          onsale: true
        })
      }
    },
    data: function() {
      return {
        // countLogo,
        isStart: false,
        now: "",
        start: "",
        end: "",
        day: 0,
        hour: 0,
        minutes: 0,
        second: 0,
        seconds: "",
      };
    },
    mounted() {
      this.Time(); 
    },
    methods: {
      getTime() {
        this.now = Date.parse(new Date());
      },

      countDown() {
        let d = parseInt(this.seconds / (24 * 60 * 60));
        this.day = d < 10 ? "0" + d : d;
        let h = parseInt(((this.seconds / (60 * 60)) % 24) + d * 24);
        this.hour = h < 10 ? "0" + h : h;
        let m = parseInt((this.seconds / 60) % 60);
        this.minutes = m < 10 ? "0" + m : m;
        let s = parseInt(this.seconds % 60);
        this.second = s < 10 ? "0" + s : s;
      },
      Time() {
        setInterval(() => {
          if (this.seconds > 0) {
            this.seconds -= 1;
            this.countDown();
          }else{
            this.getTime();
          }
        }, 1000);
      },
    },
  };

</script>

<style lang='scss' scoped>
   .status-7{
      padding-bottom:10px;
   }
   .text--primary-rd {
      color: #270645;
   }

   .text--secondary-rd {
      color: #766983;
   }

   .text--info {
      font-size: 10px;
      line-height: 1;
      letter-spacing: 0px;
   }

   .text--title {
      font-size: 13px;
      line-height: 24px;
      width: 217px;
      font-weight: bold;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      display: inline-block;
   }

   .text--sub-title {
      font-size: 12px;
      line-height: 1;
   }

   .text--number {
      font-size: 13px;
      line-height: 1;
      font-weight: bold!important;
   }

   .v-divider {
      border-color: #e3e3e3;
   }

   .sold_out {
      width: 115px;
      height: 36px;
      background-image: linear-gradient(
              91deg,
          
              #1d42ff 58%,
              #9969fc 100%
      ),
      linear-gradient(#ffffff, #ffffff);
      background-blend-mode: normal, normal;
      border-radius: 18px;
      text-align: center;
      font-weight: bold;;
      font-size: 13px;
      font-weight: bold;
      font-stretch: normal;
      line-height: 36px;
      letter-spacing: 0px;
      color: #ffffff;
      position: absolute;
      right: -15px;
      bottom: 14px;
   }

   .btnbox {
      width: 119px;
      height: 35px;
      box-sizing: border-box;
      padding: 2px;
      background-image: linear-gradient(
              0deg,
              #d202fd 0%,
              #a538fe 26%,
              #776eff 51%,
              #01facd 100%
      );
      border-radius: 17px;

      .rd {
         width: 100%;
         height: 100%;
         border: none;
         border-radius: 17px;
         background: #fff;
         font-weight: bold;;
         font-size: 13px;
         font-weight: bold;
         font-stretch: normal;
         letter-spacing: 0;
         color: #270645;
      }
   }
</style>
