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
    <div class="banner">
      <v-img class="img" v-if="ban" :src="ban" cover alt=""/>
    </div>
    <div class="infocard">
      <div class="d-flex flex-lg-row flex-column infoAvat">
        <div class="d-flex flex-lg-column align-start avat">
          <v-avatar
              size="191"
              style="background-color: #6f58d9; border: solid 8px #ffffff"
          >
            <v-img :src="src" alt=""/>
          </v-avatar>

        </div>
        <div class="d-flex flex-column infos ml-lg-8 mt-lg-12">
          <div class="showtip d-flex flex-lg-row justify-space-between">
            <span class="name" style="font-weight: bold">{{
                pageInfo.name
              }}</span>

          </div>
          <div class="no_copy">
            <span v-if="!isMobile" class="No">{{ bscAddress }}</span>
            <span v-if="isMobile" class="No">{{
                pageInfo.bscAddress | didfilter
              }}</span>
            <v-img
                class="copy"
                :class="{ click: isCopy }"
                src="@/assets/icons/icon_copy.png"
                @click="onCopyClick"
            />
          </div>
          <div class="d-flex flex-column">
            <div
                class="desc pading-gap mb-lg-3 mt-lg-3"
                v-html="pageInfo.description"
            ></div>
          </div>
        </div>
      </div>
    </div>
    <v-dialog fullscreen v-model="openLargemg">
      <large-img
          ref="largeDialog"
          v-if="openLargemg"
          :productPic="productPic"
          @getOpen="Open"
      >
      </large-img>
    </v-dialog>
    <uComponents ref="ucom"></uComponents>
  </div>
</template>

<script>
import {getFileSrc} from "@/utils/file";
import api from "@/api";
import LargeImg from "@/components/largeImg.vue";
import ScrollX from "@/components/scroll/ScrollX.vue";
import logoimg from "@/assets/default.png";
//
const AVATAR_KEY = "AVATAR";
const BANNER_KEY = "BANNER";
const SYNOPSIS_KEY = "SYNOPSIS";

export default {
  name: "headerPage",
  components: {LargeImg, ScrollX},
  data: () => ({
    src: "",
    ban: "",
    avatars: [],
    pageInfo: {},

    isCopy: false,
    isSelf: false,
    Twitter: "",
    Youtube: "",
    Homepage: "",
    imgList: [
    ],
    openLargemg: false,
    productPic: "",
    joinTime: "",
    bscAddress: "",
    logoimg
  }),
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  filters: {
    didfilter: function (value) {
      if (value && value.length > 24) {
        return value.substr(0, 12) + "..." + value.substr(-12);
      } else {
        return "";
      }
    },
  },
  mounted() {
    window.eventBus.$on("LangChange", this.onLangChange);
    this.getFans();
  },
  methods: {
    onLangChange() {
      this.avatars = [];
      this.imgList = [];

      this.getFans();
    },

    async getFans() {
      let params = {
        address: this.$route.query.did,
      };
      let res = await api.home.loopringUserInfo(params);
      this.pageInfo = res.data;
      
      if (res.data.address != null) {
        this.bscAddress = res.data.address;
      }

      if (this.pageInfo.joinTime != null) {
        let t = this.$t("otherpage_join");
        let reg = new RegExp("【N】", "g");
        this.joinTime = t.replace(reg, this.pageInfo.joinTime);
      }
      if (this.pageInfo.twitter != null) {
        let newtwitter = this.pageInfo.twitter.indexOf("http");
        if (newtwitter == 0) {
          this.Twitter = `${this.pageInfo.twitter}`;
        }
        if (newtwitter == -1) {
          this.Twitter = `https://${this.pageInfo.twitter}`;
        }
      }
      if (this.pageInfo.youtube != null) {
        let newyoutube = this.pageInfo.youtube.indexOf("http");
        if (newyoutube == 0) {
          this.Youtube = `${this.pageInfo.youtube}`;
        }
        if (newyoutube == -1) {
          this.Youtube = `https://${this.pageInfo.youtube}`;
        }
      }
      if (this.pageInfo.homepage != null) {
        let newhomepage = this.pageInfo.homepage.indexOf("http");
        if (newhomepage == 0) {
          this.Homepage = `${this.pageInfo.homepage}`;
        }
        if (newhomepage == -1) {
          this.Homepage = `https://${this.pageInfo.homepage}`;
        }
      }
      if (this.pageInfo.description)
        this.pageInfo.description = this.pageInfo.description.replace(/\n/g, "<br/>");
      this.$emit("getName", this.pageInfo.userName);
      if (this.pageInfo.profilePhoto != null){
          this.src = await getFileSrc(AVATAR_KEY, this.pageInfo.profilePhoto);
      }   
       else {
          this.src = logoimg;
        }
      if (this.pageInfo.background != null){
          this.ban = await getFileSrc(BANNER_KEY, this.pageInfo.background);

      }else {
          this.ban = logoimg;
        }
      
      this.isSelf = this.$store.state.did == this.pageInfo.did;
    },
    async unflowBtn() {
      let params = {
        chainType: this.$store.state.chainType,
        userAddress: this.$route.query.did,
        fansAddress: this.$store.state.did,
      };
      let res = await api.home.addDo(params);
      if (res.success === true) this.pageInfo.followed = true;
    },
    async flowedBtn() {
      let params = {
        chainType: this.$store.state.chainType,
        userAddress: this.$route.query.did,
        fansAddress: this.$store.state.did,
      };
      let res = await api.home.removeDo(params);
      if (res.success === true) this.pageInfo.followed = false;
    },
   
    openLargeImg(item) {
      this.productPic = item.src;
      this.openLargemg = true;
    },
    Open(e) {
      this.openLargemg = e;
    },
    onCopyClick() {
      var input = document.createElement("input");
      input.value =
          this.$chainName == "UPTICK EVM"
              ? this.bscAddress
              : this.$route.query.did;
      document.body.appendChild(input);
      input.select();
      document.execCommand("copy");
      document.body.removeChild(input);
      this.isCopy = true;
      setTimeout(() => (this.isCopy = false), 100);
    },
    onMediaClick(val) {
      window.open(val, "_blank");
    },
    avatarClick() {
      if (this.pageInfo.did == this.$store.state.did) {
        this.$router.push({name: "Fans"});
      }
    },
  },
};
</script>

<style lang="scss" scoped>
a {
  text-decoration: none;
  vertical-align: middle;

  img,
  span {
    display: inline-block;
  }

  span {
    color: #270645 !important;
  }
}

.flex-between-center {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.flex-center-start {
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.flex-start-center {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.flex-column {
  flex-direction: column;
}

.flex-row {
  display: flex;
  flex-direction: row;
}

.pading-gap {
  padding: 0;
}

.contain {
  padding: 0;
  margin: 0;

  .banner {
    height: 301px !important;
    background-color: #f8f6fd;
    // margin-top: 31px;
    .img {
      height: 100%;
      width: 100%;
    }
  }

  .infocard {
    position: relative;
    top: -50px;
    padding: 14px 0;

    .infoAvat {
      .avat {
        .unflowbox {
          width: 86px;
          height: 41px;
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
          position: relative;
          top: 50px;
          left: 50px;

          .unflow {
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

        .flowed {
          width: 121px;
          height: 41px;
          background-color: #270645;
          border-radius: 20px;
          font-weight: bold;;
          font-size: 15px;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #ffffff;
        }

        .isPcBtn {
          position: relative;
          top: 50px;
          left: 35px;
        }
      }

      .infos {
        .showtip {
          min-width: 997px;
        }

        .unflowbox1 {
          width: 86px;
          height: 41px;
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
          margin: 0 auto;

          .unflow {
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

        .flowed {
          width: 121px;
          height: 41px;
          background-color: #270645;
          border-radius: 20px;
          font-weight: bold;;
          font-size: 15px;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #ffffff;
        }
      }
    }

    .name {
      font-weight: bold;;
      font-size: 40px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }

    .no_copy {
      .No {
        font-family: Helvetica;
        font-size: 11px;
        color: #270645;
        padding-top: 4px;
      }

      .copy {
        width: 13px;
        height: 15px;
        display: inline-block;
        vertical-align: middle;
        margin-left: 8px;
        cursor: pointer;
      }

      .click {
        margin-top: 5px;
      }
    }

    .icon_url {
      margin: 25px 0 -5px;

      .icon-box {
        display: inline-block;
        word-wrap: break-word;
        word-break: break-all;
        vertical-align: middle;
        margin: 0 24px 0 0;

        span {
          display: inline;
          font-family: Helvetica;
          font-size: 12px;
          font-weight: normal;
          font-stretch: normal;
          line-height: 24px;
          letter-spacing: 0px;
          color: #000000;
        }

        img {
          display: inline;
          width: 22px;
          height: 22px;
          margin: 0 8px 0 0;
        }
      }

      .media {
        cursor: pointer;
      }
    }

    .desc {
      // display: -webkit-box;
      overflow: hidden;
      font-size: 13px;
      white-space: normal !important;
      text-overflow: ellipsis;
      word-wrap: break-word;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
    }

    .ingbox {
      max-width: 996px;
      overflow-x: auto;
      overflow-y: hidden;
      white-space: nowrap;

      .box {
        height: 300px;
        margin-right: 5px;

        img {
          width: auto;
          height: 300px;
          display: block;
        }
      }
    }

    .followers {
      padding: 0 46px;
    }

    .num {
      font-weight: bold;;
      font-size: 15px;
      color: #270645;
      font-weight: bold;
    }

    .sub {
      font-family: Helvetica;
      font-size: 14px;
      color: #766983;
    }

    .line {
      border-bottom: solid 1px #e3e3e3;
      margin: 18px 0;
    }

    .follow-btn {
      width: auto;
      margin: 0 auto;
    }

    .footer {
      .avatar-name {
        font-weight: bold;;
        font-size: 14px;
        color: #270645;
        font-weight: bold;
        padding-right: 14px;
      }

      .avatar {
        position: absolute;
        left: 0;
        top: 0;
        background: white;
        padding: 2px;
        border-radius: 50%;
      }
    }
  }

  &.mobile {
    width: 100%;

    .banner {
      height: 180px;
      background-color: #c9c9c9;

      .img {
        height: 100%;
      }
    }

    .infocard {
      .infoAvat {
        width: 100%;

        .avat {
          margin: -30px auto 0;
        }

        .infos {
          text-align: center;

          .showtip {
            min-width: 100%;
          }

          span {
            text-align: center;
          }
        }
      }

      .desc {
        width: 100%;
      }

      .ingbox {
        margin-top: 25px;
        margin-bottom: 30px;
        overflow-x: auto;
        overflow-y: hidden;
        white-space: nowrap;

        .box {
          height: 350px;
          img {
            width: auto;
            height: 100%;
          }
        }
      }

      .icon_url {
        margin: 25px 0 24px;
        text-align: left;

        .icon-box {
          display: inline-block;
          word-wrap: break-word;
          word-break: break-all;
          vertical-align: middle;
          margin: 0 24px 0 0;

          span {
            display: inline;
            font-family: Helvetica;
            font-size: 12px;
            font-weight: normal;
            font-stretch: normal;
            line-height: 24px;
            letter-spacing: 0px;
            color: #000000;
          }

          img {
            display: inline;
            width: 22px;
            height: 22px;
            margin: 0 8px 0 0;
          }
        }

        .media {
          cursor: pointer;
        }
      }
    }
  }
}
</style>
