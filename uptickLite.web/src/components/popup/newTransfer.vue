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
        <h3>{{ $t("popupNewTransferTransfer") }}</h3>
        <div class="content d-flex flex-lg-row flex-column">
          <img :src="Imgsrc" alt="" style="background-color: #fff" />
          <div class="rgh ml-12">
            <h4>{{ transferName }}</h4>
            <div class="ic-sp" v-if="islevel">
              <img src="@/assets/bbb.png" alt="" class="icon" />
              <span>{{ $t("popupNewTransferPyroworld") }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="form">
        <div class="ti-in mt-n3">
          <span class="title">{{ $t("popupTransferReceiver") }}</span>
          <v-text-field v-if="selectValue === 2" v-model="firstname" ref="addressValue"
            :placeholder="this.$t('popupTransferInput')" :rules="[rule.addressRules]" outlined type="input">
          </v-text-field>
        </div>
        <div class="ti-tx mt-n1" v-if="selectValue === 2">
          <span class="title">{{ $t("popupNewTransferRemarks") }}</span>
          <v-textarea label="" v-model="firsttxt" auto-grow outlined rows="2" row-height="18"></v-textarea>
        </div>
      </div>
      <div class="count d-flex flex-column flex-lg-row">
        <div>
          <span class="title">{{ $t("popupNewTransferAmount") }}</span>
          <span>{{ $t("balanceCount") }}:{{ haveAmount }}</span>
        </div>
        <div class="countrgh d-flex flex-row mr-lg-n5 mt-lg-0 mt-5 mb-n5">
          <v-btn class="counticon" small @click="reduceBtn">
            <v-icon dark>mdi-minus</v-icon>
          </v-btn>
          <v-text-field class="countinput" :rules="[rule.editionCount]" ref="countValue" v-model="countValue" oninput="value=value.replace(/\D|^0/g,'')"/>
          <v-btn class="counticon" small @click="increaseBtn">
            <v-icon dark>mdi-plus</v-icon>
          </v-btn>
        </div>
      </div>
      <div class="footer">
        <span class="price">Transfer Fee: {{transferFee}} LRC</span>
        <v-btn class="sub uc" :class="{ 'sub-dis': isPay }" submit @click="subCommit">{{ $t("popupTransferCommit") }}
        </v-btn>
        <span class="price">{{ $t("popupNewTransferBalance") }}：{{ userBalance }}
          {{ $walletUnit }}</span>
      </div>
    </v-card>
    <PromptBox ref="promptBox" @commit="promptBoxCommit"></PromptBox>
    <uComponents ref="ucom"></uComponents>
  </div>
</template>

<script>
import { getFileSrc } from "@/utils/file";
import api from "@/api";
import PromptBox from "@/components/PromptBox.vue";
const WORK_KEY = "WORK";
export default {
  components: { PromptBox },
  name: "newTransfer",

  data: function () {
    return {
      newOpen: true,
      items: [
        {
          text: this.$t("ToMyself"),
          value: 1,
        },
        {
          text: this.$t("ReceiverAddress"),
          value: 2,
        },
      ],
      selectValue: 2,
      firstname: "",
      countValue: "1",
      firsttxt: "",
      transferFee: 0,

      userBalance: "",
      islevel: false,
      transferName: "",
      Imgsrc: "",
      amount: "",
      haveAmount: "",
      commitType: 0,
      isPay: false,
      rule: {
        addressRules: (value) => {
          let flag = (value.substring(0,2)=="0x" && value.length==42) || (value.substring(value.length-12)=="loopring.eth" && value.length>14);
          if(!flag) {
            return "Address format error";
          }
          return true;
        },
        editionCount: (value) => {
          // const pattern=/^(?!0)(?:[0-9]{1,3}|1000)$/;
          if (value <= 0 || value > this.haveAmount) {
            return "Out of amount range";
          }
          return true;
        }
      },
    };
  },

  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  props: {
    address: {
      type: String,
    },
    imgUrl: {
      type: String,
    },
    level: {
      type: Number,
    },
    name: {
      type: String,
    },
    offSaleTokenIds: {
      type: Array,
    },
    contracId: {
      type: Number,
    },
    parentValue: {
      type: Object,
    },
  },
  async mounted() {
    if (this.level == 1) {
      this.islevel = false;
    } else {
      this.islevel = true;
    }
    if (this.imgUrl != null) {
      this.Imgsrc = await getFileSrc(WORK_KEY, this.imgUrl);
    }
    this.transferFee = await this.LRCHandler.getTransferFee();
    
    this.amount = this.parentValue.amount;
    this.haveAmount = this.parentValue.amount;
    this.transferName = this.name;

    let mount = await this.LRCHandler.getUserBalances(1);
    this.userBalance = mount.amountValue;
    this.firsttxt =
      "I sent you【" +
      this.transferName +
      "】，please check it in your collection！";
  },
  methods: {
    verify() {
      let key = localStorage.getItem("KEY_CHAIN");
      let addresslVer = this.$refs.addressValue.validate(true);
      !addresslVer ? this.$refs.addressValue.focus() : "";
      
      let countVer = this.$refs.countValue.validate(true);
      !countVer ? this.$refs.countValue.focus() : "";

      return addresslVer && countVer;
    },

    async subCommit() {
      this.transferFee = await this.LRCHandler.getTransferFee();

      if (this.selectValue == 2) {
        if (!this.verify()) {
          return;
        }
      }
      
      this.isPay = true;
      let toAddress = this.firstname;
      if(toAddress.substring(toAddress.length-12)=="loopring.eth" && toAddress.length>14) {
        let resolve = await api.home.resolveEthereumEns({
          fullName: toAddress,
          contractAddress: "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e"
        });
        toAddress = resolve.value;
      }

      let accInfo = await this.LRCHandler.getAccountInfoOther(toAddress);
      if(accInfo && accInfo.code) {
        this.$toast("error", "L2 Account is not opened at this address. It can be activated at loopring.io");
        this.isPay = false;
        return;
      }

      if(toAddress == this.$store.state.did) {
        this.$toast("error", "You can't transfer it to yourself");
        this.isPay = false;
        return;
      }

      if(!toAddress.startsWith("0x") || toAddress.length!=42) {
          this.$toast("error", "invalid address");
          this.isPay = false;
          return ;
      }
      let resTokenId=await api.home.getNftTokenId({"owner":this.$store.state.did,"tokenAddress": this.parentValue.contractAddress,"nftId": this.parentValue.nftId});
      if(resTokenId==null||resTokenId.data==null){
        this.$toast("error", "invalid NFT");
        this.isPay = false;
        return ;   
      }

      let resTransfer = await this.LRCHandler.submitNFTInTransfer(
        resTokenId.data,
        this.parentValue.nftData,
        this.countValue.toString(),
        toAddress,
        this.parentValue.contractAddress);

        console.log("wxl --- resTransfer",resTransfer)
        if(resTransfer.code == 500006){
             this.$toast("error", "Signature request was rejected");
              this.isPay = false;
             return;
          }
      if (resTransfer.hash) {
          let params = { 
            sender: this.parentValue.owner,
            senderAccountId: this.parentValue.accountId,
            recipient: toAddress,
            recipientAccountId: resTransfer.accountId,
            tokenAddress: this.parentValue.contractAddress,
            nftId: this.parentValue.nftId,
            amount: this.countValue
          }
          let res_transfer = await api.home.nftTransfer(params);
          if(res_transfer) {
            // this.$refs.promptBox.show(this.$t("messageTransferSuccess"), 2);
            this.$toast("success", this.$t("messageTransferSuccess"));
            window.eventBus.$emit("burnCollect");
            this.$emit("getOpen", false);
            this.isPay = false;
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
       this.isPay = false;
    },
    closeDialog() {
      this.newOpen = false;
      this.$emit("getOpen", this.newOpen);
    },
    reduceBtn() {
      if (this.$chainName == "UPTICK EVM") return;
      if (this.countValue > 1) {
        this.countValue--;
      }
    },
    increaseBtn() {
      if (this.$chainName == "UPTICK EVM") return;
      if (this.countValue < this.haveAmount) {
        this.countValue++;
      }
    },

    promptBoxCommit(type) {
      this.$emit("promptBoxCommit", type);
    },

    fixNumber(e) {
      
    }
  },
};
</script>

<style lang="scss" scoped>
.card {
  display: flex;
  flex-direction: column;
  min-height: 590px;
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
      margin: 16px 0 19px 52px;
      font-weight: bold;;
      font-size: 25px;
      color: #270645;
    }

    .content {
      display: flex;
      flex-direction: row;

      .rgh {
        display: flex;
        flex-direction: column;

        h4 {
          width: 300px;
          font-weight: bold;;
          font-size: 20px;
          color: #270645;
          margin-bottom: 10px;
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
  }

  .count {
    margin: -10px 52px 20px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;

    .countrgh {

      .counticon {
        width: 46px;
        height: 45px;
        font-size: 40px;
        margin: 6px 20px;
        background-color: #e3e3e3;
        border-radius: 5px;
        display: inline-block;
      }

      .countinput {
        width: 135px;
        height: 56px;
        text-align: center;
        vertical-align: middle;
        display: inline-block;
        padding: 12px 0;
        background-image: linear-gradient(#ffffff, #ffffff),
          linear-gradient(#f8f6fd, #f8f6fd);
        background-blend-mode: normal, normal;
        border-radius: 5px;
        &::v-deep input {
          text-align: center;
          vertical-align: middle;
        }
      }
    }
  }

  .footer {
    display: flex;
    flex-direction: column;
    text-align: center;
    margin-bottom: 50px;

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
      background-blend-mode: normal, normal;
      border-radius: 25px;
      opacity: 0.9;
      color: #fff;
      font-weight: bold;;
      font-size: 20px;
    }
  }

  &.mobile {
    .count {
      .countrgh {
        .counticon {
          margin: 6px;
        }
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
  font-weight: bold;;
  font-size: 15px !important;
  font-weight: bold !important;
  font-stretch: normal;
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
  margin-left: 130px;
  margin-top: 5px;
  border-radius: 50%;

}

  ::v-deep .v-messages__message {
    color: red;
  }
</style>
