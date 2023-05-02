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
  <div>
    <div class="title">{{ $t('headerUserCenterNavMyOrders') }}
      <img :class="[rotate?'go':'']" class="refresh" src="@/assets/icons/icon_refresh.png" @click="refresh"/>
    </div>
    <div class="group" ref="group" @scroll="groupScroll" @mouseover="groupMouseover">
      <div class="item" v-for="(item,index) in orders" :key="index">
        <img class="imgSrc" :src="item.nftImg"/>
        <div class="name" style="width:20%">
          <span class="s1 contentleft">{{ item.nftName }}</span>
          <v-card-text class="d-flex flex-row  pb-2 px-5 pt-2 pl-0 text-avatar">
            <Avatar size="25" :did="item.sender" :hash="item.senderPhoto" />
            <div class="ml-2 text-truncate" style="max-width: 100%;">{{ item.senderName ? item.senderName:item.sender.substr(0, 6) + "..." + item.sender.substr(-6) }}</div>
          </v-card-text>
        </div>
        <div class="price" style="width:20%">
          <span class="s1">{{ $t("popupBuyPricePrice") }}</span>
          <div class="d1">
            <img class="img" :src="getChainByName(item.tokenType).img"/>
            <span class="s2">{{ item.orderPrice }}</span>
          </div>
        </div>
        <div class="time" style="width:30%">
          <span class="s1">{{ $t("popupBuyPriceOrderTime") }}</span>
          <span class="s2">{{ timestampToDateTime(item.createTime) }}</span>
        </div>
        <div class="state ml-lg-n8" style="width:30%;text-align:right">
          <div v-if="item.orderStatus==5">
            <div class="box ml-lg-16">
              <v-btn class="s1" @click="cancelBtn(item.no)">{{ $t("orderActionCancel") }}</v-btn>
            </div>
            <v-btn class="s2" @click="payBtn(item.no)">{{ $t("orderActionPay") }}</v-btn>
          </div>
	
          <div v-else-if="item.orderStatus==0">
            <span class="s3">{{ $t("orderStateCancel") }}</span>
          </div>
        
          <div v-else-if="item.orderStatus==2">
            <span class="s3">{{ $t("orderStateSuccess") }}</span>
          </div>
          <div v-else-if="item.orderStatus==3">
            <span class="s3">{{ $t("orderStateFail") }}</span>
          </div>
          <div v-else-if="item.orderStatus==1">
            <!-- 进行中 -->
            <span class="s3">{{ $t("workCardInprogress") }}</span>
          </div>

        </div>
      </div>
      <loading :isShowLoading="isShowLoading"></loading>
    </div>
    <uComponents ref="ucom"></uComponents>
  </div>
</template>

<script>
import Avatar from '@/components/avatar/index.vue';
import api from "@/api";
import {getFileSrc} from "@/utils/file";
import Loading from "@/components/loading.vue";
import {orderList} from "@/api/modules/home";

const AVATAR_KEY = "AVATAR";

export default {
  components: {Loading, Avatar},
  data() {
    return {
      pageNumber: 1,
      pageSize: 10,
      totalPage: 0,

      orders: [],
      isShowLoading: false,
      rotate: false
    };
  },
  mounted() {
    this.getData();
  },

  methods: {
    refresh() {
      this.rotate = true;
      setTimeout(() => {
        this.rotate = false
      }, 2000);
      this.pageNumber = 1;
      this.orders = [];
      this.getData();
    },
    async getData() {
      this.isShowLoading = true;
      let bscOwner;
      let json_user = window.localStorage.getItem("key_user");

      if (json_user != null && JSON.stringify(json_user.length) != 0) {
        let data = JSON.parse(json_user);

        bscOwner = data.bscAddress;
      }

      let params = {
        owner: this.$store.state.did,
        pageNumber: this.pageNumber,
        pageSize: this.pageSize,
      };
      let res = await api.home.orderList(params);
      this.totalPage = res.data.totalPage;
      this.getImage(res.data.list, "nftImg");
      if (this.pageNumber == 1) {
        this.orders = []
      }
      this.orders = [...this.orders, ...res.data.list];


      this.isShowLoading = false;

    },
    async getImage(list, field) {
      list.forEach(async v => {
        let src = await getFileSrc(AVATAR_KEY, v[field]);
        v[field] = src;
      });
    },
    async cancelBtn(order_No) {
      let res = await api.home.orderCancel({
        orderNo: order_No
      });
      if (res != null && res.success) {
        this.pageNumber = 1;
        this.orders = [];
        this.getData();
      }

    },
    async payBtn(order_No) {

      try {
        let preOrderRes = await api.home.orderByOrderNo({
          "orderNo": order_No
        });
        if (!preOrderRes.data || !preOrderRes.data.tokenId) {
          return;
        }
        let orderNo = preOrderRes.data.orderNo;

        let memo = "购买NFT[" + preOrderRes.data.name + "]";

        let assetLevel = preOrderRes.data.assetLevel;
        let orderPrice = "";


        if (preOrderRes.success != "" && preOrderRes.success == true) {
          orderPrice = preOrderRes.data.orderPrice;
        }

        // get fee
        let res = await api.home.getFee();
        let recipient = res.data.irisMerchantAddress;
        let IrisPlatformMallFee = res.data.IrisPlatformMallFee;
        let IrisCreatorMallFee = 0;
        let IrisAddress = res.data.IrisAddress;
        if (!res.data.IrisCreatorMallFee) {
          IrisCreatorMallFee = 0;

        } else {
          IrisCreatorMallFee = res.data.IrisCreatorMallFee;

        }
        if (assetLevel == 2) {
          IrisPlatformMallFee = res.data.IrisPlatformMarketFee;
          IrisCreatorMallFee = res.data.IrisCreatorMarketFee;

        }

        let hash;
        if (this.$chainName == "UPTICK EVM") {
          let assetId = "";
          if (preOrderRes.data.orderDetailsList && preOrderRes.data.orderDetailsList.length > 0) {
            assetId = preOrderRes.data.orderDetailsList[0].assetId;
          }
          this.$wallet.setContractAddress(preOrderRes.data.nftType, {
            address: preOrderRes.data.contractAddress,
            platformAddress: preOrderRes.data.platformAddress,
          });
          recipient = preOrderRes.data.owner;
          let tokenId = preOrderRes.data.tokenId;
          let payResult = await this.$wallet.orderPay(preOrderRes.data.nftType, recipient, tokenId, orderPrice, assetId);
          hash = payResult.hash;
        } else if (orderPrice != "0") {
          let payResult = await this.$wallet.orderPay(orderPrice, recipient, memo);
          hash = payResult.hash;
        }

        let denom = preOrderRes.data.address;
        let toOwner = this.$store.state.did;
        let owner = preOrderRes.data.assetOwner;
        let tokenId = preOrderRes.data.tokenId;

        // merchant
        if (this.$chainName != "UPTICK EVM" && (orderPrice == "0" || hash != "")) {
          if (orderPrice == "0") {
            hash = null;
          }
          let params = {
            callBack:
                `${this.$env.VUE_APP_API_URL}` +
                "/api/1.0/orderInfo/wallet/pay.do?orderNo=" +
                orderNo,
            denom: denom,
            memo: memo,
            recipien: toOwner,
            shares: [
              {rate: IrisPlatformMallFee, received: IrisAddress},
              {rate: IrisCreatorMallFee, received: owner}
            ],
            hash: hash,
            tokenIds: [tokenId]
          };
          let BuyNow = await api.home.BuyNow(params);
        }

        //payDo
        let payDoParam = {
          "orderNo": orderNo
        };
        let payDo = await api.home.payDo(payDoParam);

        this.$toast("success", this.$t("buySuccess"));
        window.location.reload();

      } catch (e) {
        this.$toast("error", this.$t("buyFailed"));
      }

    },

    groupScroll(e) {
      let ele = e.srcElement ? e.srcElement : e.target;
      if ((ele.scrollTop + ele.offsetHeight > ele.scrollHeight - 1)
          && this.pageNumber < this.totalPage) {
        ++this.pageNumber;
        this.getData();
      }
    },
    groupMouseover() {
      this.$refs.group.focus();
    }
  }
};
</script>

<style lang="scss">
.contentleft {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  width: 250px;
}

.title {
  font-size: 25px;
  color: #270645;
  position: relative;

  .refresh {
    height: 25px;
    width: 25px;
    position: absolute;
    right: 0px;
  }

  .go {
    transform: rotate(360deg);
    transition: all 2s;
    pointer-events: none;
    user-select: none;
  }
}

.group {
  height: 73vh;
  max-height: 73vh;
  overflow-y: auto;
  padding: 0 5px;

  .item {
    height: 106px;
    background-image: linear-gradient(
            #ffffff,
            #ffffff),
    linear-gradient(
            #6f58d9,
            #6f58d9);
    background-blend-mode: normal,
    normal;
    border-radius: 5px;

    display: flex;
    align-items: center;
    margin: 20px 0;
    border: 1px solid #cccccc;

    .imgSrc {
      width: 76px;
      height: 76px;
      border-radius: 5px;
      margin: 15px;
      object-fit: cover;
    }

    .name {
      width: 25%;
      display: flex;
      flex-direction: column;
      font-size: 11px;

      .s1 {
        font-size: 15px;
        font-weight: bold;
        line-height: 30px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
      }
    }

    .price {
      font-size: 14px;
      color: #270645;
      display: flex;
      flex-direction: column;
      // align-items: center;
      flex: 1;
      position: relative;
      padding-left: 50px;

      .d1 {
        display: flex;
        align-items: center;
        margin-left: -5px;

        .s2 {

          font-weight: bold;
          margin-left: 5px;
        }

        .img {
          width: 22px;
          height: 22px;
          margin: 0;
        }
      }

    }

    .time {
      font-size: 14px;
      color: #270645;
      display: flex;
      flex-direction: column;
      flex: 1;

      .s2 {
        margin-top: 8px;
      }
    }

    .state {
      margin-right: 30px;

      .box {
        display: inline-flex;
        width: 115px;
        height: 36px;
        box-sizing: border-box;
        padding: 2px;
        background-image: linear-gradient(
                0deg,
                #d202fd 0%,
                #a538fe 26%,
                #776eff 51%,
                #01facd 100%
        );
        border-radius: 20px;

        .s1 {
          width: 100%;
          height: 100%;
          border: none;
          border-radius: 20px;
          background: #fff;
          font-weight: bold;;
          font-size: 15px;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0;
          color: #270645;
        }
      }

      .s2 {
        display: inline-flex;
        justify-content: center;
        align-items: center;
        margin-left: 20px;
        width: 115px;
        height: 36px;
        background-image: linear-gradient(90deg,
            #d300fd 0%,
            #a439fe 26%,
            #7471ff 52%,
            #00fdcf 100%),
        linear-gradient(
                #270645,
                #270645);
        background-blend-mode: normal,
        normal;
        box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
        border-radius: 18px;
        color: #fff !important;
        font-weight: bold;
      }
    }
  }
}

</style>
