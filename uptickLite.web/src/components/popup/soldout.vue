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
        <h3 class="end">{{ $t("popupSoldoutEndsale") }}</h3>
        <h3 class="content">{{ nftName }}</h3>
      </div>
      <div class="footer">
        <button
          class="sub v-btn uc"
          :class="{ 'sub-dis': isPay }"
          submit
          @click="subCommit"
        >
          {{ $t("popupTransferCommit") }}
        </button>
      </div>
    </v-card>
    <v-snackbar
      v-model="isShowFalse"
      color="error"
      centered
      timeout="2000"
      style="opacity: 0.8"
      ><span style="color: white; font-size: 20px">{{
        falseTitle
      }}</span></v-snackbar
    >
    <v-snackbar
      v-model="isShowSuccess"
      color="success"
      centered
      timeout="2000"
      style="opacity: 0.8"
      ><span style="color: white; font-size: 20px">{{
        successTitle
      }}</span></v-snackbar
    >
    <uComponents ref="ucom"></uComponents>
  </div>
</template>
<script>
import api from "@/api";
export default {
  data: () => ({
    newOpen: true,
    nftName: "",
    isShowFalse: false,
    isPay: false,
    isShowSuccess: false,
    falseTitle: "this is a error",
    successTitle: "create success",
  }),
  props: {
    address: {
      type: String,
    },
    name: {
      type: String,
    },
    onSaleTokenIds: {
      type: Array,
    },
    offSaleTokenIds: {
      type: Array,
    },
    parentValue: {
      type: Object
    }
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  mounted() {
    this.nftName = this.name;
  },
  methods: {
    async subCommit() {
      let param = {
        "owner": this.$store.state.did,
        "nftAddress": this.parentValue.contractAddress,
        "nftId": this.parentValue.nftId,
      };

      this.isPay = true;
      let nftDeList_res = await api.home.nftDeList(param);

      if (nftDeList_res.success == true) {
         window.eventBus.$emit("burnCollect");
          this.$emit("getOpen", false);
          this.isPay = false;
          // window.location.reload();
        } else {
          this.$toast("error", this.$t("transactionInprogressError"));
          this.isPay = false;
        }
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
  min-height: 239px;
  width: 600px;

  .top {
    display: flex;
    flex-direction: column;
    margin: 30px auto 0;

    .end {
      margin: 0 auto 13px;
      width: 100%;
      height: 21px;
      text-align: center;
      font-weight: bold;;
      font-size: 25px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      white-space: nowrap;
    }

    .content {
      margin: 19px auto 25px;
      width: 226px;
      min-height: 22px;
      font-family: Helvetica;
      font-size: 20px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      text-align: center;
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
      border-radius: 25px;
      opacity: 0.9;
      font-weight: bold;;
      font-size: 20px;
      color: #ffffff;
    }
  }
  &.mobile {
    .footer {
      width: 100%;
      margin: 0;
      .sub {
        margin-left: 10%;
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
.sub-dis {
  position: relative;
  pointer-events: none;
  background-image: linear-gradient(#766983, #766983),
    linear-gradient(#270645, #270645) !important;
  background-blend-mode: normal, normal;
  border-radius: 25px;
  opacity: 0.9;
}
.sub-dis::after {
  content: "";
  background-image: url(../../assets/loading.gif);
  background-size: 100%;
  display: inline-block;
  position: absolute;
  width: 20px;
  height: 20px;
  margin-left: 110px;

}
</style>

