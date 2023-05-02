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
  <div id="container">
    <v-card :class="{ card: true, mobile: isMobile }">
      <img
        class="clo"
        @click="closeDialog"
        src="@/assets/btn_close.png"
        alt=""
      />
      <div class="top">
        <h3 class="content">
          {{ $t("popupBurnYourMinted") }}
        </h3>
      </div>
      <div class="footer">
        <button class="sub v-btn uc" submit @click="subCommit">
          {{ $t("popupTransferCommit") }}
        </button>
      </div>
    </v-card>
    <uComponents  ref="ucom"></uComponents>
  </div>
</template>
<script>
import api from '@/api'
export default {
  data: () => ({
    open: true,
  }),
  props: {
    pageValue: {
      type: Object,
    },
    ownerFrom:{
      type:String
    }
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  methods: {
    
    async subCommit() {
      console.log(this.pageValue);
      let params
      if(this.ownerFrom=="burnCollect"){
          params = {
        tokenAddress: this.pageValue.address,
      }

      }else{
       params = {
        tokenAddress: this.pageValue.contractAddress,
        nftId: this.pageValue.nftId
      }
      }
      
      
        let res = await api.home.nftBurn(params);
        if(res.success == true)
        {
           this.$toast("success", this.$t("success")).then(() => {
              window.eventBus.$emit("burnCollect");
              this.$emit("getOpen", 1)
              
          })
        }else{
           this.$toast("error", this.$t(res.msg));

        }
        // this.$refs.promptBox.show(res.msg);
        
    },
    closeDialog() {
      this.open = false;
      this.$emit("getOpen", this.open);
    },
  },
};
</script>
<style lang="scss" scoped>
.card {
  display: flex;
  flex-direction: column;
  min-height: 274px;
  width: 600px;
  .top {
    display: flex;
    flex-direction: column;
    margin: 42px 70px 30px;
    .end {
      margin: 0 auto 23px;
      font-weight: bold;;
      font-size: 25px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }

    .content {
      height: 72px;
      font-weight: bold;;
      font-size: 20px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      text-align: center;
      color: #270645;
    }
  }
  .clo {
    position: absolute;
    right: 10px;
    top: 10px;
    width: 30px;
    height: 31px;
    background-color: #fff;
    border-radius: 50%;
    margin-right: 0;
    cursor: pointer;
  }
  .footer {
    margin: 0 auto;
    .sub {
      margin: 30px auto;
      width: 450px;
      height: 51px;
      background-blend-mode: normal, normal;
      background-color: #1d42ff;
      border-radius: 25px;
      opacity: 0.9;
      font-weight: bold;;
      font-size: 20px;
      color: #ffffff;
    }
  }
  &.mobile {
    .top {
      width: 100%;
      margin: 30px 10% 0;

      .content {
        width: 80%;
        height: auto;
        word-wrap: break-word;
      }
    }
    .footer {
      width: 100%;
      margin-left: 10%;
      .sub {
        width: 80%;
      }
    }
  }
}

img {
  display: inline-block;
  width: 126px;
  height: 126px;
  background-color: #8a73ff;
  border-radius: 5px;
  margin: 1px 22px 10px 52px;
}
</style>




