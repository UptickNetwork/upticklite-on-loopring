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
      <img class="clo" @click="closeDialog" src="@/assets/btn_close.png" alt />
      <div class="top">
        <h3>{{ $t("popupWalletConnect") }}</h3>
        <p>{{ $t("popupWalletConnectWallet") }}</p>
      </div>
      <div class="link">
        <ul>
          <li class="fox d-flex flex-row" @click="metaMaskBtn">
            <img src="@/assets/icons/logo_metamask.png" alt />
            <span>{{ $t("popupWalletMetaMask") }}</span>
          </li>
          <li class="wallet  d-flex flex-row" @click="wallentBtn">
            <img   src="@/assets/icons/logo_walletconnect.png" alt />
            <span>WalletConnect</span>
          </li>
        </ul>
      </div>
      <div class="store">
        <a target="_blank" href="https://chrome.google.com/webstore/detail/metamask/nkbihfbeogaeaoehlefnkodbefgpgknn" >If you have not installed the MetaMask wallet plugin, click here to install it.</a>
      </div>
    </v-card>
    <uComponents ref="ucom"></uComponents>
  </div>
</template>

<script>
import { connect as walletConnect, events } from "@/walletConnect";
export default {
  data: function() {
    return {
      open: true,
      openWallet: false,
      walletDownLoadUrl: (this.$chainName == "UPTICK EVM")
        ? "https://chrome.google.com/webstore/detail/metamask/nkbihfbeogaeaoehlefnkodbefgpgknn"
        : "https://chrome.google.com/webstore/detail/keplr/dmkamcknogkgcdfhhbddcghachkejeap?utm_source=chrome-ntp-icon"
    }
  },
  props: {
    keyChain: String,
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
    walletDownLoadNotice: function() {
      if(this.$chainName == "UPTICK EVM") {
        return this.$t("walletDownLoadNotice").replace("Keplr","MetaMask");
      }
      return this.$t("walletDownLoadNotice");
    }
  },
  methods: {
    closeDialog() {
      this.open = false;
      this.$emit("getOpen", this.openWallet);
    },
    async metaMaskBtn() {
      if(!window.ethereum) {
        this.$toast("error", "You have not installed the MetaMask wallet plugin");
        return;
      }
      let res = await this.LRCHandler.getApiKeyCache()
      console.log("wxl -- ewewew",res)
      if(!res){
         this.$toast("error", "Signature request was rejected");
         return
      }
      let did = await this.LRCHandler.getAddresss();
      this.$store.commit("SET_DID", did);
      if (did.length > 0) {
        this.$emit("getOpen", 1);
          this.$emit("getStatus", true, did, 2);
      } else {
        this.$emit("getStatus", false);
      }
    },
    wallentBtn() {
      walletConnect();
      events.$on("connect", async (res) => {
        console.log("connect",res);
        let did = res.accounts[0];
        this.$store.commit("SET_DID", did);
        
        this.$emit("getStatus", true, did, 3);
        this.$emit("getOpen", 1);
      });
    },

  },
};
</script>

<style lang="scss" scoped>
.card {
  width: 500px;
  min-height: 400px;
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
  .chormdetail {
    margin: 0 52px;
    display: flex;
    flex-direction: column;
    color: #766983;
    font-size: 15px;
  }
  .top {
    margin: 0 52px;
    display: flex;
    flex-direction: column;
    h3 {
      margin: 41px 0 15px;
      font-weight: bold;;
      font-size: 25px;
      color: #270645;
    }
    p {
      // width: 367px;
      height: 41px;
      font-family: Helvetica;
      font-size: 15px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #766983;
    }
  }
  .link {
    margin: 26px 52px;
    ul {
      list-style: none;
      padding: 0;
      li {
        cursor: pointer;
        // width: 400px;
        height: 61px;
        background-image: linear-gradient(#ffffff, #ffffff),
          linear-gradient(#f8f6fd, #f8f6fd);
        background-blend-mode: normal, normal;
        box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
        border-radius: 5px;
        display: block;
        margin-bottom: 9px;
        img {
          display: inline-block;
          margin: 10px 28px;
        }
        span {
          display: inline-block;
          //   width: 80px;
          position: relative;
          top: 3px;
          left: 0;
          height: 13px;
          font-weight: bold;;
          font-size: 15px;
          font-weight: bold;
          font-stretch: normal;
          line-height: 60px;
          letter-spacing: 0px;
          color: #270645;
        }
      }
      li:hover {
        box-shadow: 0px 0px 16px 0px rgba(0, 0, 0, 0.36);
      }
      .fox {
        img {
          width: 40px;
          height: 40px;
        }
      }
      .wallet {
        img {
          margin-top: 16px;
          width: 40px;
          height: 40px;
        }
        span {
          position: relative;
          top: 5px;
          left: 0;
        }
      }
      .chain {
        img {
          width: 40px;
          height: 40px;
        }
      }
    }
  }

  .store {
    margin: 26px 52px;
  }

  &.mobile {
    .top {
      h3 {
        font-size: 20px;
      }
      p {
        height: auto;
      }
    }
    .link {
      ul {
        li {
          img {
            margin: 10px;
          }
        }
      }
    }
  }
}
</style>
