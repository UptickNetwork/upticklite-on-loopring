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
   <div class="load">
      <div class="dlg">
        <div class="img">
          <img src="@/assets/loading.gif" alt=""/>
        </div>
        <div class="title">
            {{ text }}
        </div>
      </div>
   </div>
</template>

<script>
  export default {
    name: "UnlockDlg",
    props: {
      
    },
    data: function () {
      return {
        text: "Waiting for signature..."
      };
    },
    computed: {
      isMobile: function() {
        return this.$vuetify.breakpoint.mobile;
      }
    },

    async mounted() {
        sessionStorage.removeItem("KEY_exchangeInfo");
        sessionStorage.removeItem("KEY_accInfo");
        sessionStorage.removeItem("KEY_keySeed");
        sessionStorage.removeItem("KEY_eddsaKey");
        sessionStorage.removeItem("KEY_apiKey");
        sessionStorage.removeItem("KEY_activeFee");
        sessionStorage.removeItem("KEY_LRCAccount");
    },
    methods: {
      setText(txt) {
        this.text = txt;
      },
      async sign() {
        let accInfo = await this.LRCHandler.getAccountInfo();
        return accInfo;
      }
    }
  };
</script>

<style lang="scss" scoped>
    .load {
        position: fixed;
        left: 0px;
        top: 0px;
        right: 0;
        bottom: 0;
        z-index: 9999;

        background-color: rgba(0,0,0,0.5);
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        user-select: none;
        
        .dlg {
          width: 500px;
          height: 300px;
          background-color: #fff;
          border-radius: 10px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;

          .img { 
            img{
                width: 60px;
                height: 60px; 
            }
          }   

          .title {
              margin-top: 20px;
              font-size: 26px;
          }
        }
    }   
   

</style>
