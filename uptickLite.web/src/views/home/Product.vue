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
  <div :class="{ home: true, mobile: isMobile }">
    <div class="home-work d-flex align-center">
      <Work
        style="cursor: pointer"
        contain
        :aspectRatio="isMobile ? null : -1"
        :hash="sku.imgUrl"
        @imgClick="workClick"
      />
      <v-icon v-if="sku.denomType == 3" class="icon" size="30" color="#fff"
        >mdi-video
      </v-icon>
    </div>
    <div class="d-flex flex-column product">
      <div class="d-flex align-center mt-9 mt-lg-11"
          :style="isMobile ? 'margin-top:13px !important' : ''">
        <Avatar
          :did="sku.owner"
          :size="isMobile ? 42 : 40"
          :hash="sku.profilePhoto"
          showAvatarInfo
        />
        <div class="ml-7 ml-lg-4 fs-15 name"
            :style="isMobile ? 'margin-left:15px !important' : ''"
        >{{ sku.issuerName }}</div>
      </div>
      <div class="mt-4 mt-lg-6 fs-60 font-weight-bold work-name text-truncate">
        {{ sku.name }}
      </div>
      <template v-if="sku.useType == '1'">
        <div v-if="!isStart">
          <div class="mt-3 mt-lg-7 fs-15 font-weight-bold t1">
          </div>
          <div class="mt-4 mt-lg-3 fs-30 font-weight-bold t2">
            {{ hour }}h {{ minutes }}m {{ second }}s
          </div>
          <div class="mt-3 mt-lg-6 fs-15 font-weight-bold t1">
            {{ $t("workCardStartBid") }}
          </div>
        </div>
        <div v-else>
          <div class="mt-3 mt-lg-7 fs-15 font-weight-bold t1">
          </div>
          <div class="mt-4 mt-lg-3 fs-30 font-weight-bold t2">
            {{ hour }}h {{ minutes }}m {{ second }}s
          </div>
      </template>
      <div class="mt-4 mt-lg-2 fs-30 font-weight-bold t2 work-price">
        {{ sku.minPrice }} {{$walletUnit}}
      </div>
      <div class="mt-5 mt-lg-2 fs-13" style="color: #766983;display: none;">USD {{ usd }}</div>
      <div class="d-flex align-center mt-10 btn">

        <div v-if="sku.useType != '1' || sku.denomType == '3'">
          <div v-if="sku.minPrice != 0">
            <v-btn
              v-if="isPrice"
              class="rd"
              rounded
              width="225px"
              height="51px"
              @click="buybtn"
              >{{ $t("popupBuyPriceBuyNow") }}
            </v-btn>
          </div>
          <div v-else>
            <v-btn
              v-if="isPrice"
              class="rd"
              rounded
              width="225px"
              height="51px"
              @click="buybtn"
              >{{ $t("getForFree") }}
            </v-btn>
          </div>
        </div>
      </div>
    </div>
    <v-dialog v-model="openbid">
    </v-dialog>
    <v-dialog v-model="openpay">
      <pay @getOpen="Open" :address="address" :price="price"></pay>
    </v-dialog>
    <v-dialog v-model="openbuy">
      <buy-price
        v-if="openbuy"
        :minprice="minprice"
        :buytype="0"
        :Nft_sku="sku"
        :assetLevel="1"
        @getOpen="openBuy"
      ></buy-price>
    </v-dialog>
  </div>
</template>

<script>
import Pay from "@/components/popup/pay.vue";
import BuyPrice from "../../components/popup/buyPrice";

import Work from "@/components/work/index.vue";
import Avatar from "@/components/avatar/index.vue";

//import phoneBtn from "@/assets/icons/icon_pay.png";
import productPic from "@/assets/test.png";
// import api from "@/api";

export default {
  name: "Product",
  components: { Pay,Work, Avatar, BuyPrice },
  props: {
    sku: {
      type: Object,
      default: () => {},
    },
    address: {
      type: String,
    },
    price: {
      type: String,
    },
  },
  data() {
    return {
      openbid: false,
      openpay: false,
      openbuy: false,
      // phoneBtn,
      productPic,
      showAvatarInfo: true,
      minprice: "",
      usd: "",
      isStart: false,
      now: "",
      start: "",
      end: "",
      day: 0,
      hour: 0,
      minutes: 0,
      second: 0,
      seconds: "",
      buyType: "",
      isClick: false,
      // isPrice: false,
      isPrice: true,
    };
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  async mounted() {
    this.Time();

    let _self = this;
    let count = 60;
    this.buyType = 0;
    (function fn() {
      count--;
      setTimeout(() => {
        let usd_price = sessionStorage.getItem("KEY_USD_PRICE");

        _self.usd = (_self.sku.minPrice * usd_price).toFixed(4);
        if (_self.usd == "NaN") {
          _self.usd = "";
        }
        if (count > 0 && !!usd_price) {
          fn();
        }
      }, 1500);
    })();
  },
  methods: {
    getTime() {
      this.now = Date.parse(new Date());

      if (this.sku.useType == "1") {
        if (this.now < this.start) {
          this.isStart = false;
          this.isClick = true;
          this.seconds = (this.start - this.now) / 1000;
        } else if (this.now < this.end) {
          this.isStart = true;
          this.isClick = false;
          this.seconds = (this.end - this.now) / 1000;
        } else {
          this.isClick = true;
        }
      }
    },
    // 天 时 分 秒 格式化函数
    countDown() {
      let d = parseInt(this.seconds / (24 * 60 * 60));
      this.day = d < 10 ? "0" + d : d;
      let h = parseInt(((this.seconds / (60 * 60)) % 24) + d * 24);
      this.hour = h < 10 ? "0" + h : h;
      let m = parseInt((this.seconds / 60) % 60);
      this.minutes = m < 10 ? "0" + m : m;
      let s = parseInt(this.seconds % 60);
      this.second = s < 10 ? "0" + s : s;
    },
    //定时器没过1秒参数减1
    Time() {
      setInterval(() => {
        if (this.seconds > 0) {
          this.seconds -= 1;
          this.countDown();
        } else {
          this.getTime();
        }
      }, 1000);
    },
    buyBtn() {
      this.openpay = true;
    },
    //弹框关闭后获取open值
    Open(e) {
      this.openpay = e;
    },
    bidBtn() {
      if (!this.$vuetify.breakpoint.mobile) {
        if (!this.$store.state.did) {
          window.eventBus.$emit("ShowLogoBox");
          return;
        }
      }
      if (this.$vuetify.breakpoint.mobile) {
        this.openpay = true;
        return;
      }
      this.openbid = true;
    },
    OpenBid(e) {
      this.openbid = e;
    },
    buybtn() {
      if (!this.$vuetify.breakpoint.mobile) {
        if (!this.$store.state.did) {
          window.eventBus.$emit("ShowLogoBox");
          return;
        }
      }

      if (this.$vuetify.breakpoint.mobile) {
        this.openpay = true;
        return;
      }
      this.openbuy = true;
      this.minprice = this.price;
    },
    openBuy(e) {
      this.openbuy = e;
    },
    workClick() {
      if (this.sku.useType == "1" ) {
        this.$router.push({
          name: "Detail",
        });
      } else {
        this.$router.push({ name: "Saledetail", query: { id: this.sku.id } });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
v-dialog {
  width: auto;
  height: auto;
}

.name {
  font-size: 15px;
  font-weight: bold;;
}

.rd {
  width: 107px;
  height: 17px;
  font-weight: bold;;
  font-size: 20px;
  font-weight: bold;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #ffffff;
}

.nord {
  width: 294px;
  height: 51px;
  background-image: linear-gradient(#766983, #766983),
    linear-gradient(#270645, #270645);
  background-blend-mode: normal, normal;
  border-radius: 25px;
  opacity: 0.9;
  font-weight: bold;;
  font-size: 20px;
  font-weight: bold;
  font-stretch: normal;
  /*line-height: 100px;*/
  letter-spacing: 0px;
  color: #ffffff !important;
}

.phone {
  width: 20px;
  height: 31px;
  display: inline-block;
}

.home {
  display: flex;
  letter-spacing: 0px;
  line-height: 1;

  .fs-13 {
    font-size: 13px;
  }

  // .fs-15 {
  //   font-size: 24px;
  // }

  .fs-60 {
    font-size: 50px;
  }

  .fs-30 {
    font-size: 40px;
  }

  .home-work {
    // margin-right: 100px;
    width: 650px;
    height: 575px;
    max-width: 650px;
    max-height: 575px;
    margin-left: 45px;
    margin-top:50px;

    .work {
      width: 100%;
      height: 100%;
    }
  }

  &.mobile {
    flex-direction: column;

    .home-work {
      margin-right: 0;
      width: 100%;
      height: unset;
      max-width: unset;
      max-height: unset;
      margin-top: unset;
      margin-left: unset;
    }

    .name {
      font-size: 15px;
      font-weight: bold;
    }

    .work-name {
      font-size: 30px !important;
    }
    .work-price {
      font-size: 30px !important;
    }
    .t1 {
      font-size: 15px;
    }

    .t2 {
      font-size: 30px;
    }

    .btn {
      margin-top: 69px;
    }
  }
}
</style>

<style lang="scss">
.work {
  position: relative;

  .icon {
    position: absolute;
    left: 15px;
    bottom: 110px;
  }
}

.product {
  width: 400px;
  height: 575px;
  max-width: 400px;
  max-height: 575px;
  flex: 1;
  justify-content: center;
  overflow: hidden;
  margin-right: 45px;
  margin-left: auto;
}

.mobile {
  .product {
    width: 80%;
    height: unset;
    max-width: 80%;
    .v-avatar {
      .v-image {
        max-width: 90% !important;
        max-height: 90% !important;
      }
    }

    .work-name {
      font-size: 30px !important;
      width: 250px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      display: inline-block;
    }

    .work-price {
      font-size: 30px !important;
    }
  }
}
</style>
