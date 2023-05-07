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
        <h3>{{ $t("popupMarketStartSale") }}</h3>
        <h3 class="pupils">{{ NftName }}</h3>
      </div>
      <div class="form">
        <div class="title mb-lg-3 mb-3">Token</div>
        <ChainSelectInput @change="chainChange"></ChainSelectInput>
      </div>
      <div class="form">
        <div class="title mb-lg-3 mb-3">{{ $t("popupMarketSalesPrice") }} </div>
        <v-text-field
          ref="SalePrice"
          v-model="SalePrice"
          outlined
          :suffix="chain.name"
          type="input"
          :rules="[ rules.errorPrice ]"
        ></v-text-field>
      </div>

      <div class="showLimit">
         <span class="minTxt">Minimum price <span class="limintContent">{{currentMinPrice}}</span></span> 
         <span class="maxTxt">Maximum price <span class="limintContent">{{currentMaxPrice}}</span></span> 
      </div>
        <div class="form mt-lg-3">
        <div class="ti-in">
          <span class="title" >Expiration</span>
          <v-select
          class="mt-3"
            :items="list"
            label="6 Month"
            v-model="validUntil"
            item-text="text"
            item-value="value"
            solo
            any
          ></v-select>
        </div>
      </div>

      <div class="footer">
        <button class="sub" :class="{ 'sub-dis': isPay }" @click="subCommit">
          {{ $t("popupTransferCommit") }}
        </button>
      </div>
    </v-card>
    <uComponents ref="ucom"></uComponents>
  </div>
</template>

<script>
import api from "@/api";
import ChainSelectInput from "@/components/ChainSelectInput.vue"

import Web3 from "web3";
const web3Obj = new Web3();
const { toShowValue,tokenId2Index} = require("../../utils/helper");

export default {
  components: { ChainSelectInput },
  name: "transfer",
  data: () => ({
     validUntil:5,
    priceLimit:[],
    currentMinPrice:"0",
    currentMaxPrice:"0",
    open: true,
    SalePrice: "",
    amount: "",
    switch1: "false",
    NftName: "",
    isShow: true,
    isPay: false,
    rules: {
      errorPrice: (value) => {
        return false;
      }      
    },
    chain: { },
  }),

  props: {
    address: {
      type: String,
    },
    onSaleTokenIds: {
      type: Array,
    },
    offSaleTokenIds: {
      type: Array,
    },
    name: {
      type: String,
    },
    parentValue: {
      type: Object,
    },
  },
  computed: {

    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
     list:function () {
      return  [
          {
            text: "1 Day",
            value: 1,
          },
          {
            text: "1 Week ",
            value: 2,
          },
          {
            text: "1 Month",
            value: 3,
          },
          {
            text: "3 Month",
            value: 4,
          },
          {
            text: "6 Month",
            value: 5,
          },
          {
            text: "1 Year ",
            value: 6,
          },
          {
            text: "Long term (5 years) ",
            value: 7,
          },
        ];
      
    },
    handler: function () {
      return {
        switch: this.switch1,
      };
    },
  },
  watch: {
    handler: {
      handler: function () {
        if (this.switch1 == true) {
          this.amount = 1;
          this.isShow = true;
        } else {
          this.amount = 0;
          this.isShow = false;
        }
      },
      deep: true,
    },
  },
  async mounted() {

    try{
    // init
      this.chain.name = this.$walletUnit;
      this.chain.tokenId = 1;
      
      this.NftName = this.name;
      let amount = await this.LRCHandler.getUserBalances(1);
      this.amount = 1;
      this.switch1 =false

      this.priceLimit = await this.LRCHandler.getPriceLimit(
          this.parentValue.contractAddress
      );

      this.currentMinPrice =  toShowValue(web3Obj,1,this.priceLimit[1].marketOrderInfo["minimum"]);
      this.currentMaxPrice =  toShowValue(web3Obj,1,this.priceLimit[1].marketOrderInfo["maximum"]);

      this.rules.errorPrice = (value) => {
        if(value<parseFloat(this.currentMinPrice) || value>parseFloat(this.currentMaxPrice)){
          return "Out of price range"; 
        }
        const pattern = /^\d*(?:\.\d{0,5})?$/;
        return pattern.test(value) || "Out of price range";
       }
    }catch(e){
      console.log("market mounted error",e);
    }
 

  },
  methods: {

    async chainChange(chain) {

      let amount = await this.LRCHandler.getUserBalances(chain.tokenId);
      this.chain = chain;
      
      let index = tokenId2Index(chain.tokenId)
      
      this.currentMinPrice =  toShowValue(web3Obj,chain.tokenId,this.priceLimit[index].marketOrderInfo["minimum"]);
      this.currentMaxPrice =  toShowValue(web3Obj,chain.tokenId,this.priceLimit[index].marketOrderInfo["maximum"]);

    },
    verify() {
      let saleVer = this.$refs.SalePrice.validate(true);
      let amountVer = true;
      if (this.switch1 == true) {
        amountVer = this.$refs.amount.validate(true);
      }

      !saleVer ? this.$refs.SalePrice.focus() : "";
      !amountVer ? this.$refs.amount.focus() : "";
      return saleVer && amountVer;
    },


    async subCommit() {
      try {
         if (!this.verify()) {
          return;
      }
      if(parseFloat(this.SalePrice) < parseFloat(this.currentMinPrice)){
        this.$toast("error", this.$t("salePriceTooSmall") );
        return;
      }

      if(parseFloat(this.SalePrice) > parseFloat(this.currentMaxPrice)){
        this.$toast("error", this.$t("salePriceTooBig"));
        return;
      }
    
      this.isPay = true;
      let resTokenId=await api.home.getNftTokenId({"owner":this.$store.state.did,"tokenAddress": this.parentValue.contractAddress,"nftId": this.parentValue.nftId});
		  if(resTokenId==null||resTokenId.data==null){
        this.$toast("error", "invalid NFT");
        return ;   
      }
      let validUntil ;
      if(this.validUntil == 1){
        validUntil = Math.round(Date.now() / 1000) + 16 * 86400
      }else if(this.validUntil == 2){
         validUntil = Math.round(Date.now() / 1000) + 22 * 86400
      }else if(this.validUntil == 3){
         validUntil = Math.round(Date.now() / 1000) + 45 * 86400
      }else if(this.validUntil == 4){
         validUntil = Math.round(Date.now() / 1000) + 105 * 86400
      }else if(this.validUntil == 5){
         validUntil = Math.round(Date.now() / 1000) + 195 * 86400
      }else if(this.validUntil == 6){
         validUntil = Math.round(Date.now() / 1000) + 380 * 86400
      }else if(this.validUntil == 7){
         validUntil = Math.round(Date.now() / 1000) +  1840 * 86400
      }
      //nftTokenId, nftData, nftAmount, buyTokenId, buyAmount,totalRoyalty,validUntil
      let signature = await this.LRCHandler.makerOrder(
        resTokenId.data,
        this.parentValue.nftData,
        this.parentValue.amount,
        this.chain.tokenId,
        this.SalePrice,
        this.parentValue.royaltyPercentage,
        validUntil
      );
      console.log();
      if(!signature){
          this.$toast("error", "Signature request was rejected");
               this.isPay = false;
         return
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
      let buyLimit = "";
      if (this.switch1 == true) {
          buyLimit = this.amount;
      } else {
          buyLimit = 0;
      }
	
      let param = {
        "owner": this.$store.state.did,
        "nftAddress": this.parentValue.contractAddress,
        "nftId": this.parentValue.nftId,
        "marketPrice": this.SalePrice,
        "tokenType": this.chain.name,
		    "limit": buyLimit,
        "makerOrderJson": JSON.stringify(makerOrderJson),
        "signature": signature.eddsaSignature,
        "listAmount": this.parentValue.amount
      };
      let nftlist_res = await api.home.nftList(param);

      if(nftlist_res.success) {

         let t = this.$t("onSaleSuccess");
                  let reg = new RegExp("【N】", "g"); //g代表全部
                  let regN = new RegExp("<br/>", "g"); //g代表全部
                  let name = "【" + this.name + "】";
                  let title = t.replace(reg, name).replace(regN, " ");
                  this.$mtip({
                    title: title,
          });
        window.eventBus.$emit("burnCollect");
        this.$emit("getOpen", false);
        this.isPay = false;
      
      } 
      else {
        this.$toast("error", this.$t("transactionInprogressError"));
        this.isPay = false;
      }
        
      } catch (error) {
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
  min-height: 531px;
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
    margin-left: 52px;

    h3 {
      margin: 16px 0 19px;
      font-weight: bold;;
      font-size: 25px;
      color: #270645;
    }

    .pupils {
      margin: 0 0 22px;
  
      font-size: 20px;
      color: #270645;
    }
  }

  .form {
    margin: 7px 52px;
      &::v-deep .v-messages__message{
     color: red;
   }
     &::v-deep .v-input__slot {
      height: 56px;
     
	  
	}
   &::v-deep .v-text-field.v-text-field--solo .v-label {
      color: black;
    }

    .title {

      font-size: 15px;
      font-weight: bold;
      font-stretch: normal;
      color: #270645;
    }
  }

  .maxamount {
    margin: 0 52px;

    .amount {
      margin-right: 18px;
      font-weight: bold;
      font-size: 15px;
      width: 212px;
      height: 17px;
      color: #270645;
      display: inline-block;
    }

    .txt {
      width: 98px;
      height: 55px;
      background-image: linear-gradient(#ffffff, #ffffff),
        linear-gradient(#f8f6fd, #f8f6fd);
      background-blend-mode: normal, normal;
      border-radius: 5px;
      display: inline-block;
    }

    .sth {
      margin-left: 108px;
      width: 60px;
      height: 35px;
      display: inline-block;
      position: relative;
      right: 0px;
      bottom: 0px;
      border-radius: 18px;
    }
  }

  .footer {
    display: flex;
    flex-direction: column;
    text-align: center;
    margin: 25px auto;

    .price {
      display: block;
      font-family: Helvetica;
      font-size: 13px;
      font-weight: normal;
      font-stretch: normal;
      line-height: 40px;
      color: #766983;
    }

    .sub {
      margin: 0 auto;
      width: 450px;
      height: 51px;
      background-color: #1d42ff;
      background-blend-mode: normal, normal;
      border-radius: 25px;
      opacity: 0.9;
      font-size: 20px;
      color: #ffffff;
    }
  }
  &.mobile {
    .maxamount {
      .sth {
        margin-left: 50px;
      }
    }
    .footer {
      margin: 10px 10%;
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

span {
  align-items: center;
  font-family: Helvetica;
  color: #270645;
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
  margin-left: 10px;
  margin-top: 5px;
}

.showLimit{
  margin-left: 52px;
  margin-top:-15px;
  margin-bottom: 10px;
}

.maxTxt{
  margin-left:20px;
}

.limintContent{
  font-weight:bold;
}

</style>
