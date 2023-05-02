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
      <div class="top d-flex flex-lg-column flex-column">
        <h3>{{ $t("popupNewTransferTransfer") }}</h3>
        <div class="content d-flex flex-lg-row flex-column">
          <img :src="pageData.imgUrl" alt="" />
          <div class="rgh">
            <h4>{{ pageData.name }}</h4>
          </div>
        </div>
      </div>
      <div class="form">
        <div class="ti-in">
          <span class="title">{{ $t("popupTransferReceiver") }}</span>
          <v-text-field ref="addressValue" v-model="recipient" :placeholder="this.$t('popupTransferInput')"
            :rules="[rule.addressRules]" outlined type="input"></v-text-field>
        </div>
        <div class="ti-tx">
          <span class="title">{{ $t("popupNewTransferRemarks") }}</span>
          <v-textarea label="" v-model="firsttxt" auto-grow outlined rows="2" row-height="22"></v-textarea>
        </div>
      </div>
      <div class="footer">
        <span class="price">Transfer Fee: {{transferFee}} LRC</span>
        <v-btn class="sub" :class="{ 'sub-dis': isPay }" submit @click="subCommit">{{ $t("popupTransferCommit") }}
        </v-btn>
        <span class="price">{{ $t("popupNewTransferBalance") }}：{{ userBalance }}
          {{ pageData.marketTokenType }}</span>
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
export default {
  name: "transfer",
  data: function () {
    return {
      open: true,
      recipient: "",
      firsttxt:
        "Jinxing sent you【A pair of amber pupils】，please check it in your collection！",
      nameRules: [(v) => !!v || "address is required"],
      userBalance: "",
      falseTitle: "this is a error",
      successTitle: "create success",
      isShowFalse: false,
      isShowSuccess: false,
      isPay: false,
      transferFee: 0,
      rule: {
        addressRules: (value) => {
          let flag = (value.substring(0,2)=="0x" && value.length==42) || (value.substring(value.length-12)=="loopring.eth" && value.length>14);
          if(!flag) {
            return "Address format error";
          }
          return true;
        },
      },
      chainToken: ""
    };
  },
  props: {
    pageData: {
      type: Object,
    },
  },
  async mounted() {
    this.firsttxt =
      "I sent you【" +
      this.pageData.name +
      "】，please check it in your collection！";
    this.chainToken = this.getChainTokenId(this.pageData.marketTokenType);
    let mount = await this.LRCHandler.getUserBalances(this.chainToken);
    this.userBalance = mount;
    
    this.transferFee = await this.LRCHandler.getTransferFee();
    
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  methods: {
    verify() {
      let addresslVer = this.$refs.addressValue.validate(true);
      !addresslVer ? this.$refs.addressValue.focus() : "";
      return addresslVer;
    },

    async subCommit() {
      this.transferFee = await this.LRCHandler.getTransferFee();
      
      if (!this.verify()) {
        return;
      }

      this.isPay = true;
      let toAddress = this.recipient;
      if(toAddress.substring(toAddress.length-12)=="loopring.eth" && toAddress.length>14) {
        let resolve = await api.home.resolveEthereumEns({
          fullName: toAddress,
          contractAddress: "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e"
        });
        toAddress = resolve.value;
      }

      if (toAddress == this.$store.state.did) {
        this.$toast("error", "You can't transfer it to yourself");
        return;
      }

      toAddress = encodeURIComponent((toAddress).replace(/(^\s*)|(\s*$)/g, ""));
      if (!toAddress.startsWith("0x") || toAddress.length != 42) {
        this.$toast("error", "invalid address");
        return;
      }
      let resTokenId = await api.home.getNftTokenId({ "owner": this.$store.state.did, "tokenAddress": this.pageData.contractAddress, "nftId": this.pageData.nftId });
      if (resTokenId == null || resTokenId.data == null) {
        this.$toast("error", "invalid NFT");
        return;
      }

      let resTransfer = await this.LRCHandler.submitNFTInTransfer(
        resTokenId.data,
        this.pageData.nftData,
        "1",
        toAddress,
        this.pageData.contractAddress);
      if (resTransfer.hash) {
        let params = {
          assetId: this.pageData.id,
          sender: this.pageData.owner,
          senderAccountId: this.pageData.accountId,
          recipient: toAddress,
          recipientAccountId: resTransfer.accountId
        }
        let res_transfer = await api.home.nftSingleTransfer(params);
        if (res_transfer.success) {
          this.$toast("success", this.$t("messageTransferSuccess"));
          this.$emit("getOpen", false);
          this.isPay = false;

          this.$router.push({ name: 'PersonalPage' });
        }
        else {
          this.$toast("error", this.$t("messageTransferFailed"));
          this.isPay = false
        }
      }
      else {
        this.$toast("error", this.$t("messageTransferFailed"));
        this.isPay = false
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
  margin-left: 110px;
  border-radius: 50%;
}

.card {
  display: flex;
  flex-direction: column;
  min-height: 600px;
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
        background: none;
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
      }
    }

    .ti-tx {
      &::v-deep .v-input__slot .v-text-field__slot {
        height: 75px !important;

      }

      .title {
        font-weight: bold;
        margin-top: -18px;

      }
    }
  }

  .footer {
    display: flex;
    flex-direction: column;
    text-align: center;

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
      font-weight: bold;
    }
  }

  &.mobile {
    .top {
      .content {
        width: 100%;
      }
    }

    .footer {
      .sub {
        width: 70%;
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
  display: flex;
  align-items: center;
  font-family: Helvetica;
  color: #270645;
}

.title {
  font-weight: bold;
  ;
  font-size: 15px;
  font-weight: normal;
  font-stretch: normal;
  color: #270645;
}


  ::v-deep .v-messages__message {
    color: red;
  }
</style>
