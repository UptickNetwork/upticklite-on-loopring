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
      <img class="clo" @click="closeDialog" src="@/assets/btn_close.png" alt="" />
      <div class="top">
        <h3>{{ $t("popupSaletotalResale") }}</h3>
        <div class="content">
          <img :src="pageData.imgUrl" alt="" />
          <div class="rgh">
            <h4>{{ pageData.name }}</h4>
          </div>
        </div>
      </div>
      <div></div>
      <div class="form">
        <div class="ti-in">
          <span class="title">Token</span>
          <ChainSelectInput @change="chainChange"></ChainSelectInput>
        </div>
        <div class="ti-in mt-lg-2">
          <span class="title">{{ $t("popupResaleResalePrice") }}</span>
          <p class="introduce">
            {{ $t("popupResaleTheSecondary") }}
          </p>
          <v-text-field class="resale" ref="SalePrice" v-model="resaleprice" :rules="[rules.errorPrice]" outlined
            :suffix="chain.name" type="input" placeholder="Please input the resale price "></v-text-field>
        </div>

        <div class="showLimit">
          <!-- <span class="txt">{{ item.name }}</span> -->
          <span class="minTxt">Minimum price <span class="limintContent">{{ currentMinPrice }}</span></span>
          <span class="maxTxt">Maximum price <span class="limintContent">{{ currentMaxPrice }}</span></span>
        </div>
      </div>

      <div class="footer">
        <button class="sub uc" :class="{ 'sub-dis': isPay }" submit @click="subCommit">
          {{ $t("popupTransferCommit") }}
        </button>

      </div>
    </v-card>
    <v-snackbar v-model="isShowFalse" color="error" centered timeout="2000" style="opacity: 0.8"><span
        style="color: white; font-size: 20px">{{
            falseTitle
        }}</span></v-snackbar>
    <v-snackbar v-model="isShowSuccess" color="success" centered timeout="2000" style="opacity: 0.8"><span
        style="color: white; font-size: 20px">{{
            successTitle
        }}</span></v-snackbar>
    <uComponents ref="ucom"></uComponents>
  </div>
</template>


<script>
import api from "@/api";
import ChainSelectInput from "@/components/ChainSelectInput.vue"

//add web3 convert import
import Web3 from "web3";
const web3Obj = new Web3();
const { toShowValue, tokenId2Index } = require("../../utils/helper");

export default {
  components: { ChainSelectInput },
  data: () => ({
    priceLimit: [],
    currentMinPrice: "0",
    currentMaxPrice: "0",
    newOpen: true,
    resaleprice: "",
    falseTitle: "this is a error",
    successTitle: "create success",
    isShowFalse: false,
    isShowSuccess: false,
    isPay: false,
    rules: {
      errorPrice: (value) => {

        if (value == 0) {
          return "Out of price range";
        }
        const pattern = /^((\+?[0-9][0-9]{0,8})\.([0-9]{1,4})$)|^(\+?[0-9][0-9]{0,8})$/;
        return pattern.test(value) || "Out of price range";
      },
    },
    chain: {},
    amount: "",
  }),
  props: {
    pageData: {
      type: Object,
    },
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  async mounted() {

    // init
    this.chain.name = this.$walletUnit;
    this.chain.tokenId = 1;

    let mount = await this.LRCHandler.getUserBalances(1);
    this.amount = 1;

    this.priceLimit = await this.LRCHandler.getPriceLimit(
      this.pageData.contractAddress
    );

    this.currentMinPrice = toShowValue(web3Obj, 1, this.priceLimit[1].marketOrderInfo["minimum"]);
    this.currentMaxPrice = toShowValue(web3Obj, 1, this.priceLimit[1].marketOrderInfo["maximum"]);

    this.rules.errorPrice = (value) => {
      if (value == 0 || value < parseFloat(this.currentMinPrice) || value > parseFloat(this.currentMaxPrice)) {
        return "Out of price range";
      }

      const pattern = /^((\+?[0-9][0-9]{0,8})\.([0-9]{1,4})$)|^(\+?[0-9][0-9]{0,8})$/;
      return pattern.test(value) || "Out of price range";
    }
  },
  methods: {
    async chainChange(chain) {
      let mount = await this.LRCHandler.getUserBalances(chain.tokenId);
      this.chain = chain;

      let index = tokenId2Index(chain.tokenId)
      this.currentMinPrice = toShowValue(web3Obj, chain.tokenId, this.priceLimit[index].marketOrderInfo["minimum"]);
      this.currentMaxPrice = toShowValue(web3Obj, chain.tokenId, this.priceLimit[index].marketOrderInfo["maximum"]);

    },
    verify() {
      let saleVer = this.$refs.SalePrice.validate(true);
      !saleVer ? this.$refs.SalePrice.focus() : "";
      return saleVer;
    },
    async subCommit() {

      if (!this.verify()) {
        return;
      }

      if (parseFloat(this.resaleprice) < parseFloat(this.currentMinPrice)) {
        this.$toast("error", this.$t("salePriceTooSmall"));
        return;
      }

      if (parseFloat(this.resaleprice) > parseFloat(this.currentMaxPrice)) {
        this.$toast("error", this.$t("salePriceTooBig"));
        return;
      }

      this.isPay = true;
      let resTokenId = await api.home.getNftTokenId({ "owner": this.$store.state.did, "tokenAddress": this.pageData.contractAddress, "nftId": this.pageData.nftId });
      if (resTokenId == null || resTokenId.data == null) {
        this.$toast("error", "invalid NFT");
        return;
      }

      let signature = await this.LRCHandler.makerOrder(
        resTokenId.data,
        this.pageData.nftData,
        this.amount.toString(),
        this.chain.tokenId,
        this.resaleprice
      );
      if(!signature) {
        this.isPay = false;
        return;
      }
      let makerOrderJson = {
        "exchange": signature.exchange,
        "accountId": signature.accountId,
        "storageId": signature.storageId,
        "sellToken": signature.sellToken,
        "buyToken": signature.buyToken,
        "allOrNone": signature.allOrNone,
        "fillAmountBOrS": signature.fillAmountBOrS,
        "validUntil": signature.validUntil,
        "maxFeeBips": signature.maxFeeBips
      };
      let param = {
        "owner": this.$store.state.did,
        "assetId": this.pageData.id,
        "marketPrice": this.resaleprice,
        "tokenType": this.chain.name,
        "makerOrderJson": JSON.stringify(makerOrderJson),
        "signature": signature.eddsaSignature
      };
      let nftlist_res = await api.home.nftSingleList(param);

      if (nftlist_res.success) {
        this.$emit("getOpen", 1);
        this.isPay = false;
      }
      else {
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
  margin-left: 30px;
  margin-top: 5px;

}

.card {
  display: flex;
  flex-direction: column;
  min-height: 620px;
  width: 600px;

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

  .top {
    display: flex;
    flex-direction: column;

    h3 {
      margin: 41px 0 17px 52px;
      font-weight: bold;
      ;
      font-size: 25px;
      color: #270645;
    }

    .content {
      display: flex;
      flex-direction: row;

      img {
        display: inline-block;
        width: 126px;
        height: 126px;
        border-radius: 5px;
        margin: 1px 22px 10px 52px;
      }

      .rgh {
        display: flex;
        flex-direction: column;

        h4 {
          font-weight: bold;
          ;
          font-size: 20px;
          color: #270645;
          margin-bottom: 10px;
          margin-right: 21px;
        }

        .ic-sp {
          display: flex;

          .icon {
            display: flex;
            align-items: flex-end;
            width: 41px;
            height: 40px;
            background-color: #6f58d9;
            border-radius: 50%;
            margin-left: 0;
            margin-top: 10px;
          }

          span {
            display: flex;
            align-items: center;
            font-family: Helvetica;
            color: #270645;
          }
        }
      }
    }
  }

  .form {
    margin: 10px 52px;
    display: flex;
    flex-direction: column;

    .ti-in {
      .title {
        font-weight: bold;
        ;
        font-size: 15px !important;
        font-weight: bold;
        font-stretch: normal;
        color: #270645;
      }

      .introduce {
        width: 491px;
        font-family: Helvetica;
        font-size: 15px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }
    }
  }

  .footer {
    display: flex;
    flex-direction: column;
    text-align: center;
    margin-bottom: 25px;

    .price {
      display: block;
      font-family: Helvetica;
      font-size: 13px;
      font-weight: normal;
      font-stretch: normal;
      color: #766983;
    }

    .sub {
      margin: 14px auto;
      width: 450px;
      height: 50px;
      background-color: #1d42ff;
      background-blend-mode: normal, normal;
      border-radius: 25px;
      opacity: 0.9;
      color: white;
      font-weight: bold;
      ;
      font-size: 20px;
      font-weight: bold
    }
  }

  &.mobile {
    .top {
      .content {
        display: flex;
        flex-direction: column;

        img {
          margin: 0 auto;
        }

        .rgh {
          text-align: center;

          .ic-sp {
            .icon {
              display: flex;
              align-items: center;
              margin-left: 70px;
              margin-top: 0;
            }

            span {
              margin-right: 70px;
            }
          }
        }
      }
    }

    .form {
      .ti-in {
        .introduce {
          width: 100%;
          height: 100px;
          overflow: hidden;
        }
      }
    }

    .footer {
      .sub {
        width: 90%;
      }
    }
  }
}

.showLimit {
  margin-left: 0px;
  margin-top: -15px;
  margin-bottom: 10px;
}

.maxTxt {
  margin-left: 20px;
}

.limintContent {
  font-weight: bold;
}
</style>
