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
    <div class="d-flex flex-column justify-center align-center banner" style="width: 100%">
      <FIleInput accept=".jpg,.jpeg,.png,.gif" @change="change" :key="fileInputKey">
        <template>
          <v-btn dark width="161px" height="40px" rounded class="bannerBtn">{{ $t("myInfoEditProfileBanner") }}
          </v-btn>
        </template>
      </FIleInput>
      <v-img class="bannerurl" v-if="uploadFileUrl" :src="localFileUrl || uploadFileUrl" width="100%" height="100%" cover></v-img>
    </div>

    <div class="image">
      <v-avatar class="ava" size="191">
        <v-img v-if="typeof uploadAvatar == 'undefined'" src="@/assets/default.png" alt="" width="100%" height="100%" />
        <v-img v-else :src="localAvatar || uploadAvatar" alt="" width="100%" height="100%" />
      </v-avatar>
      <div class="edit_img mt-lg-4 mt-8 mb-lg-8 mb-6">
        <FIleInput accept=".jpg,.jpeg,.png,.gif" @change="avaChange" :key="fileInputKey">
          <div class="edit_image">{{ $t("myInfoEditImage") }}</div>
        </FIleInput>
      </div>
    </div>
    <div class="form">
      <div class="copy_link ml-lg-12 mb-10 d-flex flex-row">
        <span v-if="!isMobile">{{ did }}</span>
        <span v-if="isMobile">{{ did | didfilter }}</span>
      </div>
      <div>
        <div class="title mb-2">
          {{ $t("myInfoName") }}<span class="star">*</span>
        </div>
        <div class="box">
          <v-text-field ref="nameValue" v-model="nameValue" :rules="[rule.nameRules, rule.name]" label="" outlined maxlength="25">
          </v-text-field>
        </div>
      </div>
      <div class="box">
        <div class="title mt-3 mb-2">
          {{ $t("popupAccountAppealDescription") }}<span class="star">*</span>
        </div>
        <div>
          <v-textarea outlined ref="descValue" name="input-7-4" label="" counter="800" maxlength="800"
            :placeholder="this.$t('myInfoFormDescriptionPlaceholder')" v-model="descValue"  :rules="[desRule.descRules, desRule.desc]">
          </v-textarea>
        </div>
      </div>
      <div class="footer box">
        <button class="sub" submit @click="subCommit">
          {{ $t("myInfoFormSave") }}
        </button>
      </div>
      <v-snackbar v-model="isShowSuccess" color="success" centered timeout="2000" style="opacity: 0.8"><span
          style="color: white; font-size: 20px">{{
              successTitle
          }}</span></v-snackbar>
      <v-snackbar v-model="isShowFalse" color="error" centered timeout="2000" style="opacity: 0.8"><span
          style="color: white; font-size: 20px">{{
              falseTitle
          }}</span></v-snackbar>
    </div>

    <PromptBox ref="promptBox"></PromptBox>

    <uComponents ref="ucom"></uComponents>
  </div>
</template>

<script>
import FIleInput from "@/components/FIleInput.vue";
import api from "@/api";
import { getFileSrc } from "@/utils/file";
import PromptBox from "@/components/PromptBox.vue";
import logoimg from "@/assets/default.png";

const UPLOAD_KEY = "UPLOAD";
const SYNOPSIS_KEY = "SYNOPSIS";

export default {
  components: { FIleInput, PromptBox },
  data: function () {
    return {
      isCopy: false,
      did: "",
      nameValue: "",
      descValue: "",
         rule: {
         nameRules: value => !!value || 'Username is required.',
         name: value => {
            const pattern = /^[^'"]*$/
            return pattern.test(value) || 'inhibiting input quotation mark'
         },
      },
       desRule: {
         descRules: value => !!value || 'Description is required.',
         desc: value => {
            const pattern = /^[^'"]*$/
            return pattern.test(value) || 'inhibiting input quotation mark'
         },
      },
      
      uploadFileUrl: undefined,
      uploadAvatar: undefined,
      localAvatar: "",
      localFileUrl: "",
      homeBackground: "",
      profilePhoto: "",
      isShowSuccess: false,
      isShowFalse: false,
      successTitle: "",
      falseTitle: "",
      uploadInfo: "",
      logoimg,

      uploadfile: [],
      imgUrl: [],
      imgInputKey: 0,

      fileInputKey: 0,
      sendcode_time: 0,
    };
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
    showAlert: function () {
      return !!this.uploadInfo;
    },
  },
  async mounted() {
    if (this.$store.state.did != null) {
      let param = {
        address: this.$store.state.did,
      };
      let res = await api.home.loopringUserInfo(param);
      if (res.data == null) {
        this.did = this.$store.state.did;
        this.nameValue = "";
        this.descValue = "";
        this.homeBackground = "";
        this.profilePhoto = "";

        this.uploadAvatar = logoimg;
      } else {
		  if(!res.data.accountStatus){
			  res.data.accountStatus=1
		  }
        this.did = res.data.address;
        this.accountStatus = res.data.accountStatus;
        this.nameValue = res.data.name == "null" ? "" : res.data.name;
        this.descValue = res.data.description == "null" ? "" : res.data.description;
        this.homeBackground =
          res.data.background == "null" ? "" : res.data.background;
        this.profilePhoto =
          res.data.profilePhoto == "null" ? "" : res.data.profilePhoto;

        if (this.homeBackground) {
          this.uploadFileUrl = await getFileSrc(
            UPLOAD_KEY,
            this.homeBackground
          );
        } else {
          this.uploadFileUrl = logoimg;
        }
        if (this.profilePhoto) {
          this.uploadAvatar = await getFileSrc(UPLOAD_KEY, this.profilePhoto);
        } else {
          this.uploadAvatar = logoimg;
        }
        if (res.data.synopsisImage) {
          let imgLists = (res.data.synopsisImage).split(",");
          imgLists.forEach(async (item) => {
            if (item != '') {
              let dessrc = await getFileSrc(SYNOPSIS_KEY, item)
              this.uploadfile.push(dessrc)
              this.imgUrl.push(item)
            }
          })

        }
      }
    }
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
  methods: {
    async subCommit() {
      if (!(await this.verify())) {
        return;
      }

      this.homeBackground =
        this.homeBackground == "null" ? "" : this.homeBackground;
      this.profilePhoto = this.profilePhoto == "null" ? "" : this.profilePhoto;
      this.imgUrl = this.imgUrl == "" ? "[do-null]" : this.imgUrl;
      let nameValue = '';
      let descValue = '';
      if (this.nameValue) {
        let reg=new RegExp('"','g')//g代表全部
				this.nameValue=this.nameValue.replace(reg,"'");
        nameValue = encodeURIComponent((this.nameValue).replace(/(^\s*)|(\s*$)/g, ""));
      }
      if (this.descValue) {
        descValue = encodeURIComponent((this.descValue).replace(/(^\s*)|(\s*$)/g, ""));
      }

      let params = {
        accountStatus: this.accountStatus,
        address: this.$store.state.did,
        name: nameValue,
        accountId: '',
        description: descValue,
        background: this.homeBackground,
        profilePhoto: this.profilePhoto,
      };
      
      let res = await api.home.loopringUserSave(params);
      window.eventBus.$emit("MidifyUser");
      this.$toast("success", res.msg).then(() => {
        this.$router.push({name:"PersonalPage"});
      });
    },
    async verify() {
      let nameVer = this.$refs.nameValue.validate(true);
      let descValue = this.$refs.descValue.validate(true);
      !nameVer ? this.$refs.nameValue.focus() : "";
      !descValue ? this.$refs.descValue.focus() : "";
      return nameVer && descValue;
    },
    async change(file) {
      // JPG,PNG,GIF
      let ft = file.name.substr(file.name.lastIndexOf(".") + 1);
      if ("JPG,JPEG,PNG,GIF,MP4".indexOf(ft.toUpperCase()) == -1) {
        this.$toast("error", this.$t("file_type_not_support"));
        this.fileInputKey++;
        return;
      }

      this.fileInputKey++;
      let { path } = await this.ipfsAddFile(file);
      if (path) {
        this.homeBackground = path;
        this.uploadFileUrl = this.getImageIpfs(path);
      }

      this.localFileUrl = window.URL.createObjectURL(file);
    },
    async avaChange(file) {
      // JPG,PNG,GIF
      let ft = file.name.substr(file.name.lastIndexOf(".") + 1);
      if ("JPG,JPEG,PNG,GIF,MP4".indexOf(ft.toUpperCase()) == -1) {
        this.$toast("error", this.$t("file_type_not_support"));
        this.fileInputKey++;
        return;
      }

      this.fileInputKey++;
      let { path } = await this.ipfsAddFile(file);
      if (path) {
        this.profilePhoto = path;
        this.uploadAvatar = this.getImageIpfs(path);
      }
      
      this.localAvatar = window.URL.createObjectURL(file);
    },
    closeDialog() {
      this.uploadFileUrl = "";
      this.homeBackground = "";
    },
    onCopyClick() {
      var input = document.createElement("input");
      input.value = this.did;
      document.body.appendChild(input);
      input.select();
      document.execCommand("copy");
      document.body.removeChild(input);
      this.isCopy = true;
      setTimeout(() => (this.isCopy = false), 100);
    },
  },
};
</script>

<style lang="scss" scoped>
.contain {
  margin: 0;
  padding: 0;

  .banner {
    height: 301px;
    background-color: #f8f6fd;
    position: relative;

    .clo {
      visibility: hidden;
      position: relative;
      left: 580px;
      top: 15px;
      background-color: #fff;
      border-radius: 50%;
      cursor: pointer;
      z-index: 9999;
    }

    &:hover .clo {
      visibility: visible;
    }

    .bannerBtn {
      z-index: 999;
      position: absolute;
      left: 43%;
      top: 40%;
    }

    .bannerurl {
      margin-top: -60px;
    }

    .edit_profile {
      margin: 0 auto;
      text-align: center;
      width: 186px;
      height: 40px;
      background-color: #270645;
      border-radius: 20px;
      font-weight: bold;
      ;
      font-size: 15px;
      font-weight: normal;
      font-stretch: normal;
      line-height: 40px;
      letter-spacing: 0px;
      color: #ffffff;
    }
  }

  .image {
    margin-top: -43px;

    .ava {
      margin-left: 42%;
      background-color: #ddd;
      border: solid 8px #ffffff;
    }

    .edit_img {
      .edit_image {
        cursor: pointer;
        text-align: center;
        margin: 0 auto;
        width: 126px;
        height: 40px;
        background-color: #270645;
        border-radius: 20px;
        font-weight: bold;
        ;
        font-size: 15px;
        font-weight: normal;
        font-stretch: normal;
        line-height: 40px;
        letter-spacing: 0px;
        color: #ffffff;
        -webkit-user-select: none;
      }
    }
  }

  .form {
    height: auto;
    margin: 0 404px;

    .copy_link {
      text-align: center;

      span {
        display: inline-block;
        font-family: Helvetica;
        font-size: 15px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }

      .copy {
        display: inline-block;
        width: 13px;
        height: 15px;
        margin-left: 8px;
        cursor: pointer;
      }
    }

    .title {
      font-weight: bold;
      ;
      font-size: 15px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }

    .box {
      width: 450px;
        &::v-deep .v-messages__message{
     color: red;
   }
    }

    .inputimg {
      width: 450px;

      .uploadfile {
        margin-bottom: 14px;
        width: 141px;
        height: 140px;
        background-image: linear-gradient(#ffffff, #ffffff),
          linear-gradient(#f8f6fd, #f8f6fd);
        background-blend-mode: normal, normal;
        border-radius: 1px;
        border: solid 2px #e3e3e3;

        display: flex;
        justify-content: center;
        align-items: center;
        position: relative;
        margin-right: 15px;
        object-fit: contain;
        cursor: pointer;

        .FIleInput {
          width: 100%;
          height: 100%;
          padding: 30px 23px;

          .v-icon {
            width: 100%;
            height: 100%;
            padding: 25px;
          }

          .imgbtn {
            margin: 5px auto;
            background-color: #270645;
            border-radius: 12px;
            font-weight: bold;
            ;
            font-size: 9px;
            font-weight: bold;
            font-stretch: normal;
            line-height: 24px;
            letter-spacing: 0px;
            color: #ffffff;
          }
        }

        .icon {
          width: 20px;
          height: 20px;
          border-radius: 50%;
          position: absolute;
          top: 0px;
          right: 0px;
          background-color: #fff;
          z-index: 99;
          cursor: pointer;
        }

        .img {
          width: 100%;
          height: 100%;
          margin: 0;
          object-fit: contain;
        }
      }

      .file-limit {
        // width: 137px;
        height: 13px;
        font-family: Helvetica;
        font-size: 10px;
        font-weight: normal;
        font-stretch: normal;
        line-height: 14px;
        letter-spacing: 0px;
        color: #766983;
        margin-top: 5px;
        text-align: center;
      }
    }
  }

  .footer {
    margin-bottom: 190px;

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

    // width:100%;
    .banner {
      width: 100%;
      height: 180px;
      position: relative;

      .clo {
        visibility: hidden;
        position: relative;
        left: 90px;
        top: 15px;
        background-color: #fff;
        border-radius: 50%;
        cursor: pointer;
        z-index: 9999;
      }

      &:hover .clo {
        visibility: visible;
      }

      // padding-top: 30px;
      .bannerBtn {
        position: absolute;
        left: 22%;
        top: 30%;
      }

      .bannerurl {
        margin-top: -60px;
      }

      .edit_profile {
        margin: 0 auto;
      }
    }

    .image {
      margin-top: -50px;
      margin-left: 45px;

      .ava {
        margin: 0;
      }

      .edit_img {

        // width: 100%;
        .edit_image {
          margin-left: 30px;
        }
      }
    }

    .form {
      // width: 100%;
      margin: 0;

      .copy_link {
        width: 100%;

        span {
          width: 100%;
          display: inline-block;
        }

        .copy {
          display: inline-block;
        }
      }

      .box {
        width: 100%;
      }

      .inputimg {
        width: 100%;

        .uploadfile {
          width: 280px;
          height: 280px;

          .FIleInput {
            text-align: center;
            margin-top: 55%;
          }
        }

      }
    }

    .footer {
      .sub {
        width: 100%;
      }
    }
  }
}

.InstagramValue ::v-deep .v-icon::before {
  content: "";
  background-image: url(../../assets/icons/icon_Instagram.png);
  background-size: 100%;
  width: 22px;
  height: 22px;
}

.TwitterValue ::v-deep .v-icon::before {
  content: "";
  background-image: url(../../assets/icons/icon_twitter.png);
  background-size: 100%;
  width: 22px;
  height: 22px;
}

.YoutubeValue ::v-deep .v-icon::before {
  content: "";
  background-image: url(../../assets/icons/icon_youtube.png);
  background-size: 100%;
  width: 22px;
  height: 22px;
}

.SiteValue ::v-deep .v-icon::before {
  content: "";
  background-image: url(../../assets/icons/icon_site.png);
  background-size: 100%;
  width: 22px;
  height: 22px;
}

.alert {
  position: fixed;
  top: 100px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 99;
}

.star {
  color: red;
  margin-left: 10px;
}

.contain ::v-deep.v-messages__message {
  color: red;
}

.verifycode {
  cursor: pointer;
  text-align: center;
  margin: 0 auto;
  width: 126px;
  height: 40px;
  background-color: #270645;
  border-radius: 20px;
  font-weight: bold;
  ;
  font-size: 15px;
  font-weight: normal;
  font-stretch: normal;
  line-height: 40px;
  letter-spacing: 0px;
  color: #ffffff;
  -webkit-user-select: none;
  position: absolute;
  right: 8px;
  top: 8px;
}

.verifycode_dis {
  background-color: #766983 !important;
}

.code ::v-deep .v-text-field__details {
  display: none;
}
</style>
