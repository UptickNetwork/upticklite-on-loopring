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
   <div class="contain">
      <v-card id="card" :class="{ card: true, mobile: isMobile }">
         <img
             class="clo"
             width="31px"
             height="31px"
             @click="closeDialog"
             src="@/assets/btn_close.png"
             alt=""
         />
         <vue-qr :text="addressUrl" :size="260" class="code"></vue-qr>
         <v-dialog v-model="show">
            <vue-qr :text="addressUrl" :size="260" class="codecopy"></vue-qr>
         </v-dialog>

         <div class="reminder">
            <img class="codeicon" src="@/assets/icons/icon_scan.png" alt=""/>
            <span class="QR">{{$t('popupPayScanQR')}}</span>
         </div>
         <img class="upticklogo" src="@/assets/icons/Uptick_IRIS3o.png" alt=""/>
      
         <div class="download">
            <img class="downicon" src="@/assets/icons/icon_download.png" alt=""/>
            <a class="downloadText" href="https://www.uptickproject.com/download.html" target="_blank">{{$t('popupPayDownload')}}</a>
            <br/>
            <span class="buttomtext"
            >{{$t('popupPaySearch')}}</span
            >
         </div>
      </v-card>
   </div>
</template>
<script>
  import vueQr from "vue-qr";

  export default {
    data: () => ({
      open: true,
      show: false
    }),
    components: {
      vueQr
    },
    props: {
      address: {
        type: String
      },
      price:{
        type:String
      }
    },
    computed: {
      addressUrl() {
        let address = this.address;
        return `{"source":"web","denomType":"2","target":"buy","sumPrice":"1","contractAddress":"${address}"}`;
      },
      isMobile: function() {
        return this.$vuetify.breakpoint.mobile;
      }
    },
    methods: {
      subCommit() {
      },
      closeDialog() {
        this.open = false;
        this.$emit("getOpen", this.open);
      },

    }
  };
</script>
<style lang="scss" scoped>
   .card {
      display: flex;
      flex-direction: column;
      .clo {
         display: block;
         position: absolute;
         right: 10px;
         top: 10px;
         background-color: #fff;
         border-radius: 50%;
         cursor: pointer;
      }

      .code {
         margin: 56px 200px 0 200px;
         width: 200px;
         height: 200px;
      }

      .reminder {
         margin-top: 15px;

         .QR {
            margin-left: 10px;
            text-align: center;
            width: 280px;
            height: 17px;
            font-weight: bold;;
            font-size: 15px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
         }

         .codeicon {
            margin-left: 147px;
            margin-right: 0;
            vertical-align: middle;
            margin-bottom: 8px;
            display: inline-block;
            width: 20px;
            height: 20px;
            background-color: white;
         }
      }

      .upticklogo {
         margin: 46px 275px 0 275px;
         width: 50px;
         height: 50px;
         background-color: white;
      }

      .downlink {
         display: none;
      }

      .download {
         margin-top: 15px;
         margin-bottom: 21px;

         .downicon {
            vertical-align: middle;
            display: inline-block;
            margin: 0 0 6px 210px;
            width: 19px;
            height: 19px;
            background-color: white;
         }

         .downloadText {
            margin-left: 10px;
            width: 156px;
            height: 17px;
            font-family: Helvetica;
            font-size: 15px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
         }

         .buttomtext {
            margin-left: 153px;
            width: 294px;
            height: 12px;
            font-family: Helvetica;
            font-size: 10px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
         }
      }

      &.mobile {
         overflow-x: hidden;
         width: 300px !important;
         height: auto;
         padding: 0 10px;

         .code {
            display: none !important;
            margin: 30px auto;
         }

         .codecopy {
            width: 100px;
            height: 100px;
            margin: 60px auto;
         }

         .reminder {
            display: none;
         }

         .upticklogo {
            margin: 50px auto;
         }

         .downlink {
            display: block;
            font-weight: bold;;
            text-decoration: underline;
            font-size: 20px;
            font-weight: normal;
            text-decoration: underline;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
            margin: 0 auto 35px;
         }

         .download {
            margin: 0 auto 20px;

            .downicon {
               margin: 0;
            }

            .downloadText {
               margin-bottom: 45px;
            }

            .buttomtext {
               display: none;
            }
         }
      }
   }


</style>




