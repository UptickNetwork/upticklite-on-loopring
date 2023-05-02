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
  <div :class="{ contain: true, mobile: isMobile }">
    <div class="infos d-flex flex-column flex-lg-row">
      <div class="main d-flex flex-column">
        <div class="card_box mr-lg-15">
          <img class="mainimg" v-if="!productPic && !videoPic" />
          <img
            v-else-if="!videoPic"
            class="mainimg"
            :src="productPic"
            @click="openLargeImg"
            alt=""
          />
          <video
            v-else
            class="mainimg"
            ref="video"
            autoplay
            loop
            controls
            muted
            controlslist="nodownload"
            :src="videoPic"
            @click="openLargeImg"
          ></video>
        </div>
        <div class="txt_art-mobile" v-if="isMobile">
          <span>{{ marketNftInfo.name }}</span>
          <p class="flex-start-center" v-html="marketNftInfo.description"></p>
        </div>
      </div>
      <div class="info_box d-flex flex-column">
        <div class="ava d-flex flex-row justify-space-between mb-6">
          <div class="d-flex flex-row">
			   <Avatar
			                :size="42"
			                :did="marketNftInfo.owner"
			                :hash="marketNftInfo.ownerPhoto"
			              />
            <div class="ml-2 d-flex flex-center mt-lg-3 ownerName">
              Owned by<span class="name" @click="toOwnerPage">{{
                marketNftInfo.ownerName
              }}</span>
            </div>
          </div>
        </div>
        <div>
          <div
            class="iris"
            style="font-weight: bold"
            v-if="marketNftInfo.marketPrice && marketNftInfo.saleStatus == 1"
          >
            {{ marketNftInfo.marketPrice }} {{ marketNftInfo.marketTokenType }}
          </div>
          <div
            class="iris"
            style="font-weight: bold"
            v-else-if="marketNftInfo.issuerPrice && marketNftInfo.sellout"
          >
            {{ marketNftInfo.issuerPrice }} {{ marketNftInfo.issuerTokenType }}
          </div>
          <div class="iris" v-else>
            <div v-if="owerfrom == 'resale'">Already Sold</div>
            <div v-else>Not Listed</div>
          </div>
        </div>

        <div class="handle d-flex flex-row mb-8">
          <div v-if="owerfrom != 'my'">
            <div v-if="!marketNftInfo.sellout">
              <div class="title" style="font-weight: bold">
                {{ $t("saledetailAvaiable") }} {{ marketNftInfo.listAmount }}
              </div>
              <v-btn
                v-if="
                  marketNftInfo.marketPrice != 0 &&
                  marketNftInfo.marketPrice != null &&
                  marketNftInfo.assetStatus != 2 && isDeposit == '3' &&marketNftInfo.marketOwner != this.$store.state.did
                "
                class="uc bid-btn mt-3 mt-lg-3 nosale_out"
                @click="buybtn"
                >{{ $t("popupBuyPriceBuyNow") }}</v-btn
              >
              <v-btn v-else class="bid-btn mt-3 mt-lg-3 sale_out">
                {{ $t("popupBuyPriceBuyNow") }}
              </v-btn>
            </div>
            <div v-if="marketNftInfo.sellout">
              <div class="title">
                {{ $t("saledetailAvaiable") }} {{ marketNftInfo.listAmount }}
              </div>
              <v-btn
                v-if="marketNftInfo.issuerPrice != 0"
                readonly
                class="uc bid-btn mt-3 mt-lg-3 sale_out"
                >{{ $t("popupBuyPriceBuyNow") }}</v-btn
              >
              <v-btn
                v-else-if="!isMobile"
                class="bid-btn mt-3 mt-lg-3 sale_out"
                readonly
              >
                {{ $t("getForFree") }}</v-btn
              >
            </div>
          </div>
        </div>
        <div
          class="collect"
          v-if="collectionInfo.name"
          @click="collectClick(collectionInfo)"
        >
          <img class="img" :src="getImageIpfs(collectionInfo.imgUrl)" />
          <div class="info">
            <span class="dect">{{ collectionInfo.name }}</span>
            <span class="num">{{ collectionInfo.nftCount }} Items</span>
          </div>
        </div>

        <div class="current-bid d-flex flex-row mt-lg-3 mt-2">
          <img src="@/assets/icons/icon_IPFS.png" alt="" />
          <a class="ml-4 mt-1" :href="this.IpfsHub" target="_blank">{{
            $t("saledetailViewOnIpfs")
          }}</a>
        </div>
        <div class="current-bid mb-2 d-flex flex-row mt-lg-1 mt-1">
          <img src="@/assets/icons/icon_metadata.png" alt="" />
          <a class="ml-4 mt-1" :href="this.IpfsMetadata" target="_blank"
            >View Metadata</a
          >
        </div>
      </div>
    </div>
    <div class="txt_art" v-if="!isMobile">
      <div class="art-title">{{ marketNftInfo.name }}</div>
      <div class="art-description" v-html="marketNftInfo.description"></div>
      <div class="title mt-7" v-if="PropertyList && PropertyList.length > 0">
        Properties
      </div>
      <div
        v-for="(item, index) in PropertyList"
        :key="index"
        class="d-flex flex-column"
      >
        <div class="d-flex flex-row">
          <div class="Properties">{{ item.trait_type }}</div>
          <div class="PropertiesValue">{{ item.value }}</div>
        </div>
      </div>
    </div>
    <div class="history_sale mt-lg-8 mt-8">
      <span>{{ $t("saledetailHistory") }}</span>
      <v-data-iterator
        v-if="desserts.length > 0"
        :items="desserts"
        no-data-text=""
      >
        <template v-slot:header>
          <div
            class="d-flex justify-space-around mb-1 mt-8 py-2 pl-lg-10 elevation-0"
            style="border: solid 1px #cccccc; height: 50px; font-weight: bold"
          >
            <div style="width: 15%">{{ $t("saledetailHistoryEvent") }}</div>
            <div style="width: 15%">{{ $t("saledetailHistoryPrice") }}</div>
            <div style="width: 16%">{{ $t("saledetailHistoryForm") }}</div>
            <div style="width: 14%">{{ $t("saledetailHistoryTo") }}</div>
            <div style="width: 15%">{{ $t("detailIndexTime") }}</div>
          </div>
        </template>
        <template v-slot:default="{ items }">
          <div
            class="table_item_font d-flex justify-space-around mb-1 py-2 px-lg-10 elevation-0"
            v-for="(item, index) in items"
            :key="index"
            style="border: solid 1px #cccccc; height: 50px"
          >
            <div class="overflow item_font" style="width: 15%; font-size: 14px">
              {{ item.event }}
            </div>
            <div
              class="overflow item_font ml-4"
              style="width: 15%; font-size: 14px"
            >
              {{ item.price || "—" }}
            </div>
            <div class="overflow d-flex flex-row" style="width: 16%">
              <v-avatar size="30">
                <v-img
                  v-if="item.formavatarSrc == 'null'"
                  src="@/assets/default.png"
                  :alt="item.form"
                />
                <v-img v-else :src="item.formavatarSrc" :alt="item.form" />
              </v-avatar>
              <span class="ml-3 item_font item_fonts" style="font-size: 14px">{{
                item.form
              }}</span>
            </div>
            <div class="overflow d-flex flex-row" style="width: 14%">
              <v-avatar size="30">
                <v-img
                  v-if="item.toavatarSrc == 'null'"
                  src="@/assets/default.png"
                  :alt="item.form"
                />
                <v-img v-else :src="item.toavatarSrc" :alt="item.form" />
              </v-avatar>
              <span class="ml-3 item_font item_fonts" style="font-size: 14px">{{
                item.to
              }}</span>
            </div>
            <div
              class="overflow item_font item_fonts"
              style="width: 15%; font-size: 14px"
            >
              {{ item.time }}
            </div>
          </div>
        </template>
      </v-data-iterator>
    </div>

    <div class="sale_card" v-if="Nft_skuList.length != 0">
      <div class="sale_tit">
        <Avatar :size="42" :did="Nft_sku.owner" :hash="Nft_sku.profilePhoto" />
        <span style="display: inline-block" class="mb-lg-n9">{{
          Nft_sku.issuerName
        }}</span>
        <img
          class="pointer rgh"
          :src="rightArrow"
          alt=""
          width="31px"
          height="31px"
          @click="toPage"
        />
      </div>
      <div class="cardbox">
        <CardList :list="Nft_skuList" type="other" />
      </div>
    </div>
    <v-dialog fullscreen v-model="openLargemg">
      <large-img
        ref="largeDialog"
        v-if="openLargemg"
        :productPic="productPic"
        :videoPic="videoPic"
        @getOpen="Open"
      >
      </large-img>
    </v-dialog>
    <v-dialog v-model="openpay">
      <pay @getOpen="OpenPay" :address="address" :price="price"></pay>
    </v-dialog>
    <v-dialog v-model="openbuy">
      <buy-price
        v-if="openbuy"
        :marketNftInfo="marketNftInfo"
        :owerfrom="owerfrom"
        :Nft_sku="Nft_sku"
        :assetLevel="assetLevel"
        :buytype="1"
        :userInfo="userInfo"
        @getOpen="openBuy"
      ></buy-price>
    </v-dialog>

    <uComponents ref="ucom"></uComponents>
  </div>
</template>

<script>
import CardList from "@/components/workCard/CardList.vue";
import rightArrow from "@/assets/icons/icon_r.png";
import LargeImg from "../../components/largeImg";
import Pay from "@/components/popup/pay.vue";
import message from "@/components/popup/message.vue";
import Avatar from "@/components/avatar/index";
import api from "@/api";
import { getFileSrc } from "@/utils/file";
import BuyPrice from "../../components/popup/buyPrice";
import { share } from "@/utils";
import logoimg from "@/assets/default.png";
const BANNER_KEY = "BANNER";
const AVATAR_KEY = "AVATAR";
const WORK_KEY = "WORK";

export default {
  components: {
    BuyPrice,
    LargeImg,
    CardList,
    Pay,
    message,
  	Avatar,
  },
  data() {
    return {
      currfriend: {},
      openLargemg: false,
      openpay: false,
      openbuy: false,
      productPic: "",
      videoPic: "",
      sharePath: "",
      rightArrow,
      isSort: true,
      isSaleout: false,
      openShare: false,
      isCopy: false,
      IpfsMetadata: "",
      IpfsHub: "",
      PropertyList: [],
      headers: [
        {
          text: "Event",
          value: "event",
          sortable: false,
        },
        {
          text: "Price (IRIS)",
          value: "price",
          sortable: false,
        },
        {
          text: "Form",
          value: "form",
          sortable: false,
        },
        {
          text: "To",
          value: "to",
          sortable: false,
        },
        {
          text: "Time",
          value: "time",
          sortable: false,
        },
      ],
      creator: "profilePhoto",
      desserts: [],
      cards: [],
      Nft_sku: {},
      Nft_historyList: {},
      Nft_skuList: [],
      Nft_denomid: "",
      address: "",
      price: "",
      assetLevel: 1,
      usd: 0,
      isSelf: false,
      idNotList: false,
      isShow: false,

      userInfo: {},
      marketNftInfo: {},
      addressExploer: "",
      collectionInfo: {},
      owerfrom: "",
      isShowAva: false,
      isShowOwner: false,
      isShowHover: false,
      isDeposit:''
    };
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  filters: {
    emptystr: function (value) {
      if (!value || value.indexOf("NaN") >= 0) return " ";
      return value;
    },
  },
  async mounted() {
   this.isDeposit = sessionStorage.getItem("isDeposit");
    window.addEventListener("click", this.clickOther);
    this.owerfrom = this.$route.query.from;

    let user = await api.home.loopringUserInfo({
      address: this.$store.state.did,
    });
  
    if (user) {
      this.userInfo = user.data;
    }

    let paramsInfo = {
      tokenAddress: this.$route.query.tokenAddress,
      nftId: this.$route.query.nftId,
      owner: this.$route.query.owner,
    };
    let res1 = await api.home.nftTokenInfo(paramsInfo);
    this.marketNftInfo = res1.data;
    if (this.marketNftInfo.makerType == 2) {
      let targetUrl = window.location.href.replace("saledetail", "detail");

      location.href = targetUrl;
    }
    if (res1.data.properties) {
      this.PropertyList = JSON.parse(res1.data.properties);
    }

    if (this.PropertyList)
      this.PropertyList = this.PropertyList.filter(
        (item) => item.trait_type != "" && item.value != ""
      );

    if (this.marketNftInfo.description)
      this.marketNftInfo.description = this.marketNftInfo.description.replace(
        /\n/g,
        "<br/>"
      );
    if (this.marketNftInfo.contractDto) {
      this.collectionInfo = this.marketNftInfo.contractDto;
      this.addressExploer =
        "https://explorer.loopring.io/collections/" +
        this.collectionInfo.address.toLowerCase();
    }
    if (!this.marketNftInfo.imgUrl) {
      this.marketNftInfo.imgUrl =
        "QmPcXBirGiUSyjb8i2FQUpNDTtPHBsgo7tviwwm4YQeJ6p";
    }
    if (!this.marketNftInfo.ownerName) {
      console.log(this.marketNftInfo.owner);
      this.marketNftInfo.ownerName =
        this.marketNftInfo.owner.substr(0, 3) +
        "..." +
        this.marketNftInfo.owner.substr(-3);
    }

    this.IpfsHub = this.getImageIpfs(this.marketNftInfo.imgUrl);
    this.IpfsMetadata = this.getImageIpfs(this.marketNftInfo.metadataUrl);
    this.productPic = this.getImageIpfs(this.marketNftInfo.imgUrl);
    this.isSaleout = true;
    await this.initData();

    // 判断用户是否存在
    let param = {
      address: this.marketNftInfo.creator,
    };
    let ress = await api.home.loopringUserInfo(param);
    if (ress.data.createTime == null) {
      this.isShowHover = false;
    } else {
      this.isShowHover = true;
    }
  },
  methods: {
    toOwnerPage() {
      this.$router.push({
        name: "Otherpage",
        query: { did: this.marketNftInfo.owner },
      });
    },
    async initData() {
      let res2 = await api.home.assetHistory({
        nftId: this.$route.query.nftId,
        tokenAddress: this.$route.query.tokenAddress,
      });
      this.desserts = [];
      res2.data.forEach((v) => {
        let from = logoimg;
        let to = logoimg;
        if (v.senderPhoto) {
          from = this.getImageIpfs(v.senderPhoto);
        }
        if (v.recipientPhoto) {
          to = this.getImageIpfs(v.recipientPhoto);
        }

        let fromName = v.from.substr(0, 5) + "..." + v.from.substr(-5);
        let toName = v.to.substr(0, 5) + "..." + v.to.substr(-5);
        if (v.senderName) {
          fromName = v.senderName;
        }
        if (v.recipientName) {
          toName = v.recipientName;
        }
        let price = v.amount + " " + v.marketTokenType;
        if (v.amount == null) {
          price = "";
        }

        let event = "";
        switch (v.txType) {
          case 1:
            event = "MINT";
            break;
          case 2:
            event = "LIST";
            break;
          case 3:
            event = "DELIST";
            break;
          case 4:
            event = "TRADE";
            break;
          case 5:
            event = "TRANSFER";
            break;
        }
        this.desserts.push({
          event: event,
          price: price,
          form: fromName,
          to: toName,
          time: this.timestampToDateTime(v.createTime),
          formavatarSrc: from,
          toavatarSrc: to,
        });
      });
    },
    share,

    shareBtn() {
      this.openShare = true;
    },
    toPage() {
      if (this.Nft_sku.owner == this.$store.state.did) {
        this.$router.push({
          name: "PersonalPage",
          query: {
            did: this.Nft_sku.owner,
          },
        });
      } else {
        this.$router.push({
          name: "Otherpage",
          query: {
            did: this.Nft_sku.owner,
          },
        });
      }
    },
    collectClick(collectionInfo) {
      let url = "";
      if (collectionInfo.linkUrl) {
        url = collectionInfo.linkUrl;
      } else {
        url = collectionInfo.address;
      }
      this.$router.push({
        path: `/collection/${url}`,
      });
    },

    openLargeImg() {
      this.openLargemg = true;
    },
    Open(e) {
      this.openLargemg = e;
    },
    buyBtn() {
      this.openpay = true;
    },
    OpenPay(e) {
      this.openpay = e;
    },

    mousedCreate() {
      if (this.mousedCreate) this.isShowAva = true;
    },
    mousedOwner() {
      this.isShowOwner = true;
    },
    clickOther() {
      this.isShowAva = false;
      this.isShowOwner = false;
      this.openShare = false;
    },
    async buybtn() {
      if (!this.$store.state.did) {
        window.eventBus.$emit("ShowLogoBox");
        return;
      }

      let isLogged = await this.getLogged();
      if (!isLogged) {
        let accInfo = await this.UnlockDlg.show();
        if (!accInfo || !accInfo.accountId) {
          this.UnlockDlg.hide();
          return;
        }
        this.UnlockDlg.hide();
      }
      this.openbuy = true;
    },
    openBuy(e) {
      this.openbuy = e;
    },
    async getImage(img) {
      if (img !== null) {
        return await getFileSrc(WORK_KEY, img);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.flex-start-center {
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
  margin-top: 20px !important;
}
.explaincontract {
  width: 130px;
  height: 40px;
  background-color: #270645;
  border-radius: 3px;
  position: absolute;
  top: 25px;
  right: 25px;
  z-index: 1;
  span {
    margin: auto;
    font-family: Helvetica;
    font-size: 12px;
    font-weight: normal;
    font-stretch: normal;
    line-height: 15px;
    letter-spacing: 0px;
    color: #ffffff;
    margin-left: 13px;
    margin-right: 13px;
  }
}

.item_fonts {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  width: 100px;
}

.pointer {
  cursor: pointer;
}

.contain {
  width: 100%;
  margin: 0;
  padding: 0;

  .infos {
    .main {
      .txt_art-mobile {
        margin: 20px 0 0;
        white-space: normal;
        word-wrap: break-word;
        word-break: break-all;

        span {
          font-weight: bold;
          font-size: 40px;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #270645;
        }

        p {
          font-family: Helvetica;
          font-size: 16px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #270645;
          margin-top: 22px;
        }
      }

      .card_box {
        display: flex;
        width: 771px;
        height: 611px;
        background-color: #f8f6fd;
        .mainimg {
          max-height: 611px;
          max-width: 100%;
          width: 100%;
          height: 100%;
          margin: auto;
          object-fit: contain;
          align-items: center;
          justify-content: center;
        }
      }
    }

    .info_box {
      width: 100%;
      .ava {
        .ownerName {
          color: #766983;
          font-size: 14px;
          .name {
            cursor: pointer;
            width: 80px;
            height: 30px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            -o-text-overflow: ellipsis;

            font-family: Helvetica;
            font-size: 14px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #1d42ff;
            padding-left: 4px;
          }
        }

        .msg {
          display: inline-block;
          width: 25px;
          height: 24px;
          vertical-align: middle;
        }
      }

      .iris {
        font-weight: bold;
        font-size: 30px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }

      .usd {
        font-family: Helvetica;
        font-size: 13px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #766983;
      }

      .notList {
        height: 25px;
        margin-bottom: 20px;
        font-family: Helvetica;
        font-weight: bold;
        font-size: 30px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }

      .sub {
        font-weight: bold;
        font-size: 15px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #6f58d9;
      }

      .countdown {
        position: relative;

        img {
          cursor: pointer;
          width: 20px;
          height: 20px;
          margin-right: 15px;
        }

        .share {
          width: 166px;
          height: 161px;
          background-image: linear-gradient(#ffffff, #ffffff),
            linear-gradient(#6f58d9, #6f58d9);
          background-blend-mode: normal, normal;
          box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
          border-radius: 5px;
          position: absolute;
          left: -30px;
          top: 30px;
          z-index: 20000;

          img,
          .icon {
            display: inline-block;
            width: 25px;
            height: 25px;
          }

          span {
            font-weight: bold;
            font-size: 13px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
          }
        }

        .showtip {
          display: inline-block;
          position: relative;

          .box {
            position: absolute;
            left: -65px;
            top: 27px;
            margin-top: 0px;
            border-radius: 5px;
            background: #270645;
            width: 85px;
            height: 31px;
            color: #ffffff;
            font-size: 14px;
            line-height: 34px;
            text-align: center;
          }
        }

        .showtip > .box {
          display: none;
        }

        .showtip:hover > .box {
          display: block;
        }
      }

      .current-bid {
        font-size: 15px;
        font-weight: bold;
        font-stretch: normal;
        margin-top: 25px;
        letter-spacing: 0px;
        color: #270645;

        a {
          text-decoration: none;
        }

        img {
          width: 31px !important;
          height: 31px !important;
          vertical-align: middle;
        }

        span {
          display: inline-block;
          font-weight: bold;
          font-size: 13px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #270645;
        }
      }

      .handle {
        width: 343px;
        position: relative;
        .title {
          font-weight: bold;
          font-size: 13px !important;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #270645;
        }

        .bid-btn {
          height: 51px;
          background-blend-mode: normal, normal;
          border-radius: 25px;
          font-weight: bold;
          font-size: 20px;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #ffffff;
        }

        .sale_out {
          width: 294px;
          height: 51px;
          background-image: linear-gradient(#766983, #766983),
            linear-gradient(#270645, #270645);
          background-blend-mode: normal, normal;
          border-radius: 25px;
        }

        .nosale_out {
          width: 294px;
          height: 51px;
          background-blend-mode: normal, normal;
          border-radius: 25px;
          background-color: #1d42ff;
        }

        .phone {
          width: 20px;
          height: 31px;
          display: inline-block;
        }
      }
      .createName {
        color: #270645;
        font-size: 14px;
        .name {
          cursor: pointer;
          height: 30px;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          -o-text-overflow: ellipsis;
          font-family: Helvetica;
          font-size: 14px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #1d42ff;
          padding-left: 4px;
        }
      }

      .collect {
        width: 252px;
        height: 64px;
        display: flex;
        flex-direction: row;
        align-items: center;
        cursor: pointer;

        .img {
          width: 61px;
          height: 61px;
          background-color: #270645;
          border-radius: 5px;
          object-fit: cover;
        }

        .info {
          display: flex;
          flex-direction: column;
          margin-left: 10px;
          background-color: unset !important;

          .name {
            font-weight: bold;
            font-size: 13px;
            line-height: 24px;
            color: #270645;

            font-weight: bold;
          }

          .dect {
            max-width: 195px;
            font-family: Helvetica;
            font-size: 13px;
            line-height: 24px;
            font-weight: bold;
            color: #270645;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
          }

          .num {
            font-family: Helvetica;
            font-size: 13px;
            line-height: 24px;
            color: #766983;
          }
        }
      }
    }
  }

  .txt_art {
    margin: 51px 0 0;
    white-space: normal;
    .lincenseimg {
      position: relative;
      img {
        height: 30px;
      }
    }
    .art-title {
      font-weight: bold;
      font-size: 40px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }

    .art-description {
      width: 748px;
      font-family: Helvetica;
      font-size: 16px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      margin-top: 22px;
    }
    .title {
      font-family: Helvetica;
      font-size: 15px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }
    .Properties {
      width: 170px;
      font-family: Helvetica;
      font-size: 15px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }
    .PropertiesValue {
      font-family: Helvetica;
      font-size: 15px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }
  }

  .history_sale {
    height: auto;
    margin-bottom: 62px;

    .table_item_font {
      .item_font {
        font-weight: normal;
        font-family: Helvetica;
        font-size: 15px;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }
    }

    span {
      font-weight: bold;
      font-size: 25px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      margin-bottom: 25px;
      display: block;
    }

    .tab_img {
      display: inline-block;
      position: relative;
      top: 10px;
      margin-top: 10px;
    }

    .tab-span {
      display: inline-block;
      position: relative;
      top: 0;
      left: 11px;
      font-size: 14px;
      color: #270645;
      font-weight: normal;
    }
  }

  .sale_card {
    height: auto;
    margin-bottom: 100px;

    .sale_tit {
      margin-bottom: 17px;
      position: relative;

      img {
        width: 42px;
        height: 42px;
      }

      span {
        display: inline-block;
        position: relative;
        top: 0px;
        left: 13px;
        font-weight: bold;
        font-size: 15px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }

      .rgh {
        position: absolute;
        right: 0;
      }
    }
  }

  &.mobile {
    padding-bottom: 20px;

    .infos {
      width: 100%;

      .main {
        width: 100%;

        .card_box {
          width: 100%;
          height: unset;
          padding: 0 !important;
        }

        .txt_art {
          span {
            font-size: 38px;
          }
        }
      }

      .info_box {
        .ava {
          display: flex !important;
          flex-direction: column !important;
          .ownerName {
            align-items: center;
            .name {
              display: flex;
              align-items: center;
            }
          }
        }
        .createName {
          margin-top: 15px;
        }
        .countdown {
          margin-top: 15px;
          .share {
            width: 166px;
            height: 161px;
            background-image: linear-gradient(#ffffff, #ffffff),
              linear-gradient(#6f58d9, #6f58d9);
            background-blend-mode: normal, normal;
            box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
            border-radius: 5px;
            position: absolute;
            left: 130px;
            top: 0;
            z-index: 20000;

            img,
            .icon {
              display: inline-block;
              width: 25px;
              height: 25px;
            }

            span {
              font-weight: bold;
              font-size: 13px;
              font-weight: normal;
              font-stretch: normal;
              letter-spacing: 0px;
              color: #270645;
            }
          }
        }

        .msg {
          margin-left: 135px;
        }

        .handle {
          max-width: 100%;

          .bid-btn {
            width: 240px !important;
          }
        }
      }
    }

    .history_sale {
      .table_font {
        font-weight: bold;
        font-family: Helvetica;
        font-size: 14px;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }

      .table_item_font {
        .item_font {
          font-weight: normal;
          font-family: Helvetica;
          font-size: 14px;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #270645;
        }
      }
    }

    .sale_card {
      width: 100%;

      .cardbox {
        width: 100%;
        margin-bottom: 38px;
      }
    }
  }
}

.elevation-1 {
  border-bottom: 1px solid #eee;
}

.history_sale ::v-deep .elevation-1 {
  height: 51px;
  box-shadow: 2px 2px 5px #ccc !important;
  background-blend-mode: normal, normal;
  border-radius: 5px;
}

.overflow {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  -o-text-overflow: ellipsis;
  display: flex;
  align-items: center;

  span {
    margin-top: 25px;
  }
}
</style>
