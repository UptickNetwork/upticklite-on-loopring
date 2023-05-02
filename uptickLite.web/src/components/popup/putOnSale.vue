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
        <h3>{{$t('popupMarketStartSale')}}</h3>
        <p>{{$t('onsaleChooseSaleType')}}</p>
      </div>
      <div class="link">
        <ul class="d-flex flex-lg-row flex-column">
          <li class="mr-lg-4" @click="fixedPriceBtn">
            <img
              src="@/assets/icons/sale_fixed.png"
              width="69px"
              height="69px"
              alt
            />
            <div class="infotit">{{$t('onsaleFixedPrice')}}</div>
            <div class="infos">{{$t('onsaleModifiabledPrice')}}</div>
          </li>
          <li v-if="supplyLimit === 1 && $chainName!='UPTICK EVM'" class="" @click="StartSaleBtn">
            <div class="infos">{{$t('onSaleIssues')}}</div>
          </li>
          <li v-if="supplyLimit !== 1 || $chainName=='UPTICK EVM'" class="onlyread">
            <div class="infos">{{$t('onSaleIssues')}}</div>
          </li>
        </ul>
      </div>
    </v-card>
    <v-dialog v-model="Sale" persistent>
    </v-dialog>
    <v-dialog v-model="openmarket" persistent>
      <market
        :address="address"
        :onSaleTokenIds="onSaleTokenIds"
        :offSaleTokenIds="offSaleTokenIds"
        :name="name"
        :parentValue="parentValue"
        v-bind="$attrs"
        v-on="$listeners"
        @getOpen="openMarket"
      ></market>
    </v-dialog>
  </div>
</template>

<script>
import Market from "@/components/popup/market.vue";
export default {
  components: { Market },
  props: {
    supplyLimit: {
      type: Number,
    },
    address: {
      type: String,
    },
    onSaleTokenIds: {
      type: Array,
    },
    offSaleTokenIds: {
      type: Array,
    },
    name:{
      type: String,
    },
    parentValue:{
      type: Object,
    },
  },
  data: () => ({
    open: false,
    sum: 1,
    openmarket:false,
    Sale:false
  }),
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  methods: {
    closeDialog() {
      this.$emit("getOpen", this.open);
    },
    StartSaleBtn() {
      this.Sale = true;
    },
    openSale(e) {
      this.Sale = e;
    },
    fixedPriceBtn() {
      this.openmarket = true;
    },
    openMarket(e) {
      this.openmarket = e;
    },
  },
};
</script>

<style lang="scss" scoped>
.card {
  width: 600px;
  height: 396px;
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
    margin: 0 52px;
    display: flex;
    flex-direction: column;
    h3 {
      margin: 41px 0 15px;
      font-weight: bold;;
      font-size: 25px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }
    p {
      font-family: Helvetica;
      font-size: 20px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      margin-bottom: 29px;
    }
  }
  .link {
    margin: 0 52px 53px;
    ul {
      list-style: none;
      padding: 0;
      li {
        text-align: center;
        cursor: pointer;
        width: 240px;
        height: 200px;
        background-image: linear-gradient(#ffffff, #ffffff),
          linear-gradient(#f8f6fd, #f8f6fd);
        background-blend-mode: normal, normal;
        box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
        border-radius: 5px;
        display: block;
        margin-bottom: 9px;
        img {
          display: block;
          margin: 38px auto 30px;
        }
        .infotit,
        .infotitauc {
          height: 22px;
          font-weight: bold;;
          font-size: 20px;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0px;
          line-height: 22px;
          color: #270645;
          margin: 0 auto 7px;
        }
        .infos {
          height: 12px;
          font-family: Helvetica;
          font-size: 13px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          line-height: 12px;
          color: #270645;
          margin-bottom: 17px;
        }
      }
      .onlyread {
        .infotitauc,
        .infos {
          color: #ccc;
        }
      }
      li:hover {
        box-shadow: 0px 0px 16px 0px rgba(0, 0, 0, 0.36);
      }
    }
  }
  &.mobile {
    height: auto;
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
            margin: 35px auto;
          }
        }
      }
    }
  }
}
</style>
