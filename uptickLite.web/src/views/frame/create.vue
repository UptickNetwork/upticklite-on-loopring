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
    <div class="main d-flex flex-lg-row flex-column">


      <div class="card_img d-flex flex-column mr-lg-16">
        <div class="title mt-3 mb-2" style="font-size:23px !important">
          Create Collection
        </div>
        <div class="title1 mb-2">
          Here you can create a collection. To add an NFT into an existing collection, please navigate to [My Profile]
        </div>
        <img v-if="uploadFileUrl" class="clo" width="31px" height="31px" @click.stop="closeDialog"
          src="@/assets/btn_close.png" alt="" />

        <div class="d-flex flex-column justify-center align-center creatimg" style="
            width: 451px;
            height: 450px;
            border: dashed 2px #e3e3e3;
            position: relative;
            overflow: hidden;
          ">
          <FIleInput accept=".jpg,.jpeg,.png,.gif" @change="change" v-if="!uploadFileUrl" :key="fileInputKey">
            <div class="uploadbox drop_area" :class="{ dropwait: isDrop }" @drop="enentDrop" @dragleave="dragleave"
              @dragenter="dragleave" @dragover="dragleave">
              <div class="drop_txt"></div>
              <template>
                <v-btn class="uc" width="161px" height="40px" rounded>{{ $t("createChooseFile") }}
                </v-btn>
                <div class="mt-5 createLimit" style="font-size: 15px;color: #766983;margin-bottom:20px">This file will be used as the cover image for your collection, please choose accordingly.</div>
                <div class="mt-2 createLimit">{{ $t("createLimit") }}</div>
              </template>
              <div class="progress_area" v-if="isDrop">
                <span :style="{ width: progressValue + '%' }"></span>
              </div>
            </div>
          </FIleInput>
          <video v-else-if="fileType === 'video/mp4'" :src="localFileUrl || uploadFileUrl" width="100%" height="100%" contain loop
            controls controlslist="nodownload"></video>
          <v-img v-else :src="localFileUrl || uploadFileUrl" width="100%" height="100%" contain></v-img>
        </div>


        <div>
          <div class="title mt-3 mb-2">Collection Name<span class="star">*</span></div>
          <div class="box">
            <v-text-field ref="nameValue" v-model="nameValue" :rules="nameRules" label="" outlined :key="refresh_key">
            </v-text-field>
          </div>
        </div>

        <div>
          <div class="title mt-3 mb-2">
            {{ $t("popupAccountAppealDescription") }}<span class="star">*</span>
          </div>
          <div class="box">
            <v-textarea ref="descriptionValue" :rules="descRules" outlined name="input-7-4" label="" value=""
              v-model="descriptionValue" :key="refresh_key"></v-textarea>
          </div>
        </div>

        <div>
          <div class="title mt-2 mb-2">{{ $t("shortUrl") }}<span class="star">*</span></div>
          <div class="desc">{{ $t("url_describe") }}</div>
          <div class="url mb-2">{{prePrefixBaseUrl}}</div>
          <div class="box">
            <v-text-field ref="shortUrlValue" v-model="shortUrlValue" :rules="urlRules" label="" outlined
              :key="refresh_key" maxlength="50" :disabled="isEdit"></v-text-field>
          </div>
        </div>




        <div class="copyright box d-flex flex-row mb-16">
          <div class="checkbox mt-n5">
            <v-checkbox v-model="checkbox" label=""></v-checkbox>
          </div>
          <div class="content">
            {{ $t("createPleaseEnsure") }}
            <p>
              {{ $t("createAgreed")
              }}<span style="cursor: pointer" @click="toTerms">《{{ $t("createCopyright") }}》</span>
            </p>
          </div>
        </div>

      </div>

      <div class="look cover">
        <img v-if="uploadFileHash.imgUrl && uploadFileHash.imgUrl != '00'" :src="uploadFileHash.imgUrl" class="bg" />
        <span class="avatarName">{{ uploadFileHash.name }}</span>
        <img v-if="uploadImageUrl" class="clo" width="31px" height="31px" @click.stop="closeImgDialog"
          src="@/assets/btn_close.png" alt="" />
        <FIleInput accept=".jpg,.jpeg,.png,.gif" class="imgfile" @change="imgChange" v-if="showUploadImage"
          :key="fileInputKey">
          <div class="uploadbox drop_area" style="
              width: 200px;
              height: 140px;
              margin-left: -44px;
              margin-top: -90px;
            " :class="{ dropwait: isDropImg }" @drop="enentDropImg" @dragleave="dragleave" @dragenter="dragleave"
            @dragover="dragleave">
            <div class="drop_txt" style="margin-top: 70px"></div>
            <template>
              <v-btn class="uc" width="161px" height="40px" rounded>{{ $t("createChooseFile") }}
              </v-btn>
              <div class="mt-5">{{ $t("popupNftAppealLimit") }}</div>
            </template>
          </div>
        </FIleInput>
        <Avatar class="avatar2" size="40" :hash="uploadFileHash.profilePhoto" />
        <div class="title mt-3 mb-2 preview">
          Preview in the market
        </div>
      </div>
    </div>

    <div class="footer box">
      <!-- <span class="price">{{ $t("popupAuctionMintFee") }}: {{ feeValue }} {{ $walletUnit }}</span> -->
      <!--      <span class="price"> Gas: {{ value2 }} IRIS</span>-->
      
      <v-btn class="uc" height="51px" disabled v-if="subDisabled">
        {{ $t("popupTransferCommit") }}
      </v-btn>
      <v-btn class="uc" height="51px" @click="subCommit" v-else>
        {{ $t("popupTransferCommit") }}
      </v-btn>

    </div>

    <div class="mask" v-if="showMask">
      <img class="img" src="@/assets/loading.gif" alt="" />
    </div>

    <v-snackbar v-model="isShowFalse" color="error" centered timeout="2000" style="opacity: 0.8"><span
        style="color: white; font-size: 20px">{{
            falseTitle
        }}</span></v-snackbar>
    <v-snackbar v-model="isShowSuccess" color="success" centered timeout="2000" style="opacity: 0.8"><span
        style="color: white; font-size: 20px">{{
            successTitle
        }}</span></v-snackbar>
    <PromptBox ref="promptBox" @commit="promptBoxCommit"></PromptBox>
    <uComponents ref="ucom"></uComponents>
  </div>
</template>
<script>
import FIleInput from "@/components/FIleInput.vue";
import Avatar from '@/components/avatar/index.vue';
import api from "@/api";
import { getFileSrc } from "@/utils/file";
import PromptBox from "@/components/PromptBox.vue";
import { toIpfsLink, toBaseUri,toS3Link } from "../../utils/helper"

const UPLOAD_KEY = "UPLOAD";
let fileUrl = "";
let imageUrl = "";

export default {
  components: { FIleInput, Avatar, PromptBox },
  data: () => ({
    refresh_key: 0,
    fileInputKey: 0,
    feeValue: 0,
  isEdit:false,
    subDisabled: true,
    checkbox: false,
    switch1: false,
    isShowFalse: false,
    isShowSuccess: false,
    falseTitle: "this is a error",
    successTitle: "create success",
    nameValue: "",
    shortUrlValue: "",
	baseUrl:"",
	prePrefixBaseUrl:"",
    tagValue: "",
    amountValue: "1",
    amountListValue: "",
    categoryValue: "",
    descriptionValue: "",
    attribute: "",
    denomType: "",
    isShowLoading: false,
    adult: "",
    issuerName: "",
    CategoryList: [
      {
        texts: "Artwork",
        value: 1,
      },
      {
        texts: "Collectibles",
        value: 2,
      },
      {
        texts: "Photography",
        value: 3,
      },
      {
        texts: "Video",
        value: 4,
      },
    ],

    AmountList: [{
      text: "多个（ERC-1155）",
      value: 4,
    },
    {
      text: "单个（ERC-721）",
      value: 3,
    },

    ],
    nameRules: [
      (v) => !!v || "Please upload an name for your item",
      (v) => !(/[!$%^*(){}:"<>?！￥]/.test(v)) || "Cannot contain special symbols",
      (v) => (v && v.length <= 80) || "Name must be less than 80 characters",
    ],
    urlRules: [
      (v) => !!v || "URL format error",

      (v) => (/^[0-9a-zA-Z_]{1,}$/.test(v)) || "URL format error",

    ],
    tagList: [],
    descRules: [(v) => !!v || "Please upload an description for your item"],
    rules: {
      EditionValue: (value) => {
        // const pattern = /^([1-9][0-9]{0,2}|100)$/;
        const pattern = /^[0-9]$|^[0-4][0-9]$|^50$/
        return pattern.test(value) || "Invalid amount for sale";
      },
    },

    uploadFileHash: {
      imgUrl: "",
      name: "",
      did: "",
      owner: "",
      profilePhoto: "",
    },
    uploadFileUrl: "",
    uploadImageUrl: "",
    showUploadImage: false,
    localFileUrl: "",
    fileType: "",
    showMask: false,
    commit_msg: "",
    methodType: 11,
    isDrop: false,
    isDropImg: false,
    progressValue: 0,
    nftInfo:{}
  }),
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  async mounted() {
    window.eventBus.$on("LangChange", this.onLangChange);
    this.onLangChange();
this.prePrefixBaseUrl = process.env.VUE_APP_PREFIX_BASE_URL; 
    this.categoryValue = 1;
    this.amountListValue = 4;

    if(this.$route.params.NFTInfo)
       {
         this.isEdit = true
         this.nftInfo = this.$route.params.NFTInfo;
           this.uploadFileUrl = this.getImageIpfs(this.nftInfo.collectionImgUrl);
          this.nameValue = this.nftInfo.name;
          this.shortUrlValue = this.nftInfo.linkUrl;
          this.uploadFileHash.imgUrl = this.getImageIpfs(this.nftInfo.collectionImgUrl)
      //    this.folderId = nftInfo.id
          imageUrl = this.nftInfo.collectionImgUrl
         if(this.nftInfo.description){
                this.descriptionValue = this.nftInfo.description.replace(
        /<br\s*\/?>/g, "\n"
      );
         }
  
       }

    if (this.$store.state.did) {
      let param = { address: this.$store.state.did };

      let res = await api.home.loopringUserInfo(param);
      if (res.data != null) {
        this.uploadFileHash.did = res.data.did;
        this.uploadFileHash.profilePhoto = res.data.profilePhoto;
        this.uploadFileHash.issuerName = res.data.userName;
        this.uploadFileHash.userAddress = res.data.userAddress;
        this.issuerName = this.uploadFileHash.issuerName;
      } else {
        this.$router.push({
          name: "MyInfo",
        });
      }
    }


  },


  watch: {
    nameValue: {
      handler: function(newValue) {
        this.uploadFileHash.name = newValue;
        this.submitStatus();
      },
      immediate: true
    },
    uploadFileUrl: {
      handler: function() {
        this.submitStatus();
      },
      immediate: true
    },
    descriptionValue: {
      handler: function() {
        this.submitStatus();
      },
      immediate: true
    },
    shortUrlValue: {
      handler: function() {
        this.submitStatus();
      },
      immediate: true
    },
    checkbox: {
      handler() {
        this.submitStatus();
      },
      immediate: true
    },

    async amountListValue(newValue) {
      if (newValue == 2) {
        this.amountValue = 1;
      }
    },

    amountValue(newValue) {
      this.feeValue = (Number(newValue) * this.feeValue).toFixed(1);
    },
  },
  methods: {
    async updateUserBalances() {
      let amount = await this.LRCHandler.getUserBalances(1);
      this.amountFormat = amount;
    },
    submitStatus() {
      this.subDisabled = !(
        this.nameValue && this.uploadFileUrl && this.descriptionValue && this.shortUrlValue && this.checkbox
      );
    },
    verify() {
      // let mountVer = this.$refs.amountValue.validate(true);
      let nameVer = this.$refs.nameValue.validate(true);
      let emailVer = this.$refs.descriptionValue.validate(true);
      let shortUrlVer = this.$refs.shortUrlValue.validate(true);

      // !mountVer ? this.$refs.amountValue.focus() : "";
      !nameVer ? this.$refs.nameValue.focus() : "";
      !emailVer ? this.$refs.descriptionValue.focus() : "";
      !shortUrlVer ? this.$refs.shortUrlValue.focus() : "";

      return nameVer && emailVer && shortUrlVer;
    },
    async subCommit() {
      localStorage.setItem("collection_card", 1);

      if (!this.verify()) {
        return;
      }
      if (this.uploadFileUrl == "") {
        this.$toast("error", "Please upload File");
        return;
      }

      if (!imageUrl) {
        window.scrollTo(0, 0);
        this.$toast("error", this.$t("amCreateSelectCover"));
        return;
      }

      if (this.checkbox == false) {
        this.isShowFalse = true;
        this.falseTitle = this.$t("amCreateFalseTitle1");
        return;
      }

      // denomtype  
      if (this.amountListValue === 1) {
        this.denomType = 2;
      }
      // attribute  3-artwork 4-collectibles 5-photography 7-video
      if (this.categoryValue === 1) {
        this.attribute = 3;
      } else if (this.categoryValue === 2) {
        this.attribute = 4;
      } else if (this.categoryValue === 3) {
        this.attribute = 5;
      } else if (this.categoryValue === 4) {
        this.attribute = 7;
        this.methodType = 12;
      }
      this.showMask = true;
      try {
		  this.baseUrl=  toBaseUri(this.shortUrlValue);
        let tokenAddress = await this.LRCHandler.computeNFTAddress(
          this.$store.state.did,
        this.baseUrl
        );

        let ipfs = toIpfsLink(imageUrl);
      
        let ipfsParams = {
          contract:tokenAddress,
          thumbnail_uri:ipfs,
          banner_uri:ipfs,
          avatar_uri:ipfs,
          tile_uri:ipfs,
           name: this.uploadFileHash.name,
           description: this.descriptionValue,
           baseUrl:  this.baseUrl
        }
          if(this.$route.params.NFTInfo){
            console.log("wxl --- collectionMetadata",this.nftInfo.collectionMetadata)
             let collectionMetadata =  this.nftInfo.collectionMetadata
             collectionMetadata= collectionMetadata.substring(37);
            collectionMetadata= collectionMetadata.slice(0, collectionMetadata.length - 9)
            ipfsParams.key = collectionMetadata
        }
     
        let collectionMetadata = await api.home.updateCollection(ipfsParams)
        if(!collectionMetadata.data){
          return
        }
     
         let param = {
          imgUrl: imageUrl,
          name: encodeURIComponent(this.uploadFileHash.name),
          description: encodeURIComponent(this.descriptionValue),
          collectionMetadata: toS3Link(collectionMetadata.data),
          owner: this.$store.state.did,
          baseUrl:this.baseUrl,
		      linkUrl:this.shortUrlValue
        };
      if(this.$route.params.NFTInfo){
        param.id = this.nftInfo.id
      
      }else{
        param.address=tokenAddress
      }


        let  deploy_res = await api.home.nftDeploy(param);
        this.showMask = false;
        

         

        if (deploy_res.success == true) {
          this.$toast("success", this.$t("amCreateSuccessTitle")).then(() => {
            let url = this.shortUrlValue
            this.$router.push({
						path: `/collection_create/${url}`,
          });
                   
					});
        } else {
          this.$toast("error", this.$t(deploy_res.msg));
        }

      } catch (e) {
        //TODO handle the exception
        console.log(e);

        this.showMask = false;
        this.$toast("error", this.$t("amCreateFalseTitle2"));
      }
    },

    addBtn() {
      let isHas = this.tagList.indexOf(this.tagValue);
      if (this.tagList.length < 5 && this.tagValue && isHas <= -1) {
        this.tagList.push(this.tagValue);
      }
      this.tagValue = "";
    },

    clearTag(i) {
      this.tagList.splice(i, 1);
    },

    async change(file) {
      // JPG,PNG,GIF,MP4
      let ft = file.name.substr(file.name.lastIndexOf(".") + 1);
      if ("JPG,JPEG,PNG,GIF".indexOf(ft.toUpperCase()) == -1) {
        this.$toast("error", this.$t("file_type_not_support"));
        this.fileInputKey++;
        return;
      }

      this.isDrop = true;
      this.fileInputKey++;

      this.fileType = file.type;

      // if (file.type == "video/mp4") {
      //   if (file.size > 20971520) {
      //     this.isDrop = false;
      //     this.$toast("error", this.$t("video_not_support"));
      //     return;
      //   }
      //   let res = await api.video.uploadVideo(file, this.onUploadProgress);
      //   if (res.data.data == null) {
      //     this.isDrop = false;
      //     this.$toast("error", res.data.msg);
      //     return;
      //   }
      //   fileUrl = res.data.data;
      //   this.uploadFileUrl = await getFileSrc(UPLOAD_KEY, res.data.data, true);
      //   this.showUploadImage = true;
      //   this.categoryValue = 4;
      // } else 
      {
        if (file.size > 10242880) {
          this.isDrop = false;
          this.$toast("error", this.$t("image_not_support"));
          return;
        }
        // let res = await api.image.uploadImage(file, this.onUploadProgress);
        let {path} = await this.ipfsAddFile(file);
        if (!path) {
          this.isDrop = false;
          // this.$toast("error", res.data.msg);
          return;
        }
        imageUrl = path;
        this.uploadFileHash.imgUrl = this.getImageIpfs(imageUrl);
        this.uploadFileUrl = this.getImageIpfs(imageUrl);

        this.showUploadImage = false;
        if (this.categoryValue == 4) {
          this.categoryValue = 1;
        }
      }
      this.isDrop = false;
      this.progressValue = 0;

      
      this.localFileUrl = window.URL.createObjectURL(file);
      this.uploadFileHash.localImgUrl = this.uploadFileUrl;

    },
    async imgChange(file) {
      // JPG,PNG,GIF
      let ft = file.name.substr(file.name.lastIndexOf(".") + 1);
      if ("JPG,JPEG,PNG,GIF".indexOf(ft.toUpperCase()) == -1) {
        this.$toast("error", this.$t("file_type_not_support"));
        this.fileInputKey++;
        return;
      }

      this.isDropImg = true;
      let res = await api.image.uploadImage(file);
      this.fileInputKey++;
      if (!res.data.data) {
        this.isDropImg = false;
        this.isShowFalse = true;
        this.falseTitle = res.data.msg;
        return;
      }
      // imageUrl = await getFileSrc(UPLOAD_KEY, res.data.data);
      imageUrl = res.data.data;
      this.uploadFileHash.imgUrl = this.getImageIpfs(imageUrl);
      this.uploadImageUrl = await getFileSrc(UPLOAD_KEY, res.data.data);
      this.showUploadImage = false;
      this.isDropImg = false;
    },
    closeDialog() {
      this.uploadFileUrl = "";
      this.imgUrl = "";
      this.showUploadImage = false;
      this.uploadImageUrl = "";
      this.uploadFileHash.imgUrl = "00";
      if (this.categoryValue == 4) this.categoryValue = 1;
    },
    closeImgDialog() {
      this.uploadImageUrl = "";
      this.imgUrl = "";
      this.uploadFileHash.imgUrl = "00";
      if (this.fileType == "video/mp4" && this.uploadFileUrl) {
        this.showUploadImage = true;
        this.categoryValue = 4;
      } else {
        this.showUploadImage = false;
      }
    },
    promptBoxCommit(type) {
      if (type == 1) {
        this.$router.push({ name: "PersonalPage" });
        sessionStorage.setItem("PersonalPage_Tabs", 1);
      }
    },
    toTerms() {
      if (this.$vuetify.lang.current == "en") {
        let routeUrl = this.$router.resolve({
          path: "/term",
          query: { id: 96 },
        });
        window.open(routeUrl.href, "_blank");
      } else {
        let routeUrl = this.$router.resolve({
          path: "/term_zh",
          query: { id: 96 },
        });
        window.open(routeUrl.href, "_blank");
      }
    },

    enentDrop(e) {
      e.stopPropagation();
      e.preventDefault();
      let file = e.dataTransfer.files[0];
      this.change(file);
    },
    enentDropImg(e) {
      e.stopPropagation();
      e.preventDefault();
      let file = e.dataTransfer.files[0];
      this.imgChange(file);
    },
    dragleave(e) {
      e.stopPropagation();
      e.preventDefault();
    },
    onUploadProgress(e) {
      this.progressValue = (e.loaded / e.total) * 100;
    },
    onLangChange() {
      this.CategoryList[0].texts = this.$t("otherpageTabsPageArtwork");
      this.CategoryList[1].texts = this.$t("amCreateCollectibles");
      this.CategoryList[2].texts = this.$t("createCategoriesPhotography");
      this.CategoryList[3].texts = this.$t("amCreateVideo");
      this.AmountList[0].text = this.$t("create_1155_title");
      this.AmountList[1].text = this.$t("create_721_title");
      //this.AmountList[2].texts = this.$t("amCreateNotSale");
      this.nameRules = [
        (v) => !!v || this.$t("amCreateTipUpload"),
        (v) => !(/[!@#$%^&*(){}:"<>?！￥]/.test(v)) || "Cannot contain special symbols",
        (v) => (v && v.length <= 80) || this.$t("amCreateTipNameMust"),
      ];
      this.descRules = [(v) => !!v || this.$t("amCreateTipDescription")];
      this.rules = {
        EditionValue: (value) => {
          // const pattern=/^(?!0)(?:[0-9]{1,3}|1000)$/;
          let pattern = /^[0-9]$|^[0-4][0-9]$|^50$/

          return pattern.test(value) || this.$t("amCreateTipInvalid");
        },
      };
      this.refresh_key++;
    },
  },
};
</script>

<style lang="scss" scoped>
.title {
  font-family: Helvetica;
  font-size: 15px !important;
  font-weight: bold;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #270645;
}

.title1 {
  font-family: Helvetica;
  font-size: 12px !important;
  font-weight: bold;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #270645;
  width: 460px;
}

.box {
  width: 451px;
   &::v-deep .v-messages__message{
     color: red;
   }
}

.tag_item {
  background-color: #e3e3e3;
  border-radius: 5px;
  border: solid 2px #e3e3e3;
}

.s_witch {
  width: 61px;
  height: 36px;
  border-radius: 18px;
  margin-left: 112px;
}

.contain {
  width: 100%;
  height: auto;
  margin: 0 auto;
  padding: 0;
  .contain ::v-deep.v-messages__message {
  color: red;
}

  .main {
    margin-left: 205px;

    .card_img {
      position: relative;

      .creatimg {
        .uploadbox {
          text-align: center;
          margin: auto;
        }
      }

      .clo {
        visibility: hidden;
        position: absolute;
        left: 410px;
        top: 110px;
        background-color: #fff;
        border-radius: 50%;
        cursor: pointer;
        z-index: 9999;
      }

      &:hover .clo {
        visibility: visible;
      }

      .upload {
        text-align: center;
        vertical-align: middle;
        height: 450px;
        background-image: linear-gradient(#ffffff, #ffffff),
          linear-gradient(#f8f6fd, #f8f6fd);
        background-blend-mode: normal, normal;
        border-radius: 5px;
        border: solid 2px #e3e3e3;
      }

      .copyright {
        .content {
          p {
            margin-top: 20px;
            font-family: Helvetica;
            font-size: 15px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;

            span {
              color: #6f58d9;
              text-decoration: none;
            }
          }
        }
      }
    }

    .look {
      margin-top: 16px;

      &:hover .clo {
        visibility: visible;
      }

      .clo {
        visibility: hidden;
        position: relative;
        left: 245px;
        top: 40px;
        background-color: #fff;
        border-radius: 50%;
        cursor: pointer;
        z-index: 9999;
      }

      .card_info {
        width: 280px;
        height: 385px;
        background-image: linear-gradient(#ffffff, #ffffff),
          linear-gradient(#6f58d9, #6f58d9);
        background-blend-mode: normal, normal;
        box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
        border-radius: 5px;
      }

      .bom_tit {
        text-align: center;
        font-family: Helvetica;
        font-size: 13px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #766983;
      }
    }
  }

  .footer {
    display: flex;
    flex-direction: column;
    text-align: center;
    margin-bottom: 50px;
    margin-left: 205px;

    .price {
      display: block;
      font-family: Helvetica;
      font-size: 13px;
      font-weight: normal;
      font-stretch: normal;
      color: #766983;
    }

    .uc {
      border-radius: 25px;
      margin-top: -30px
    }

    .sub {
      margin: 14px auto;
      width: 450px;
      height: 50px;
      background-image: linear-gradient(90deg,
          #d300fd 0%,
          #a439fe 26%,
          #7471ff 52%,
          #00fdcf 100%),
        linear-gradient(#270645, #270645);
      background-blend-mode: normal, normal;
      border-radius: 25px;
      opacity: 0.9;
      font-family: Helvetica;
      font-weight: bold;
      font-size: 20px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #ffffff;
    }
  }

  .drop_area {
    border: 1px dashed #fff;
    height: 400px;
    width: 400px;
    position: relative;

    .drop_txt {
      display: flex;
      flex-direction: column;
      margin-top: 130px;
    }
  }

  .dropwait::after {
    content: "";
    position: absolute;
    // background-color: rgba(0,0,0,0.4);
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    // background-image: url(../../assets/loading.gif);
    background-size: 30%;
    background-repeat: no-repeat;
    background-position: 50%;
  }

  .progress_area {
    width: 400px;
    height: 10px;
    background-color: #bbb;
    border-radius: 30px;
    margin-top: 140px;
    overflow: hidden;

    span {
      display: block;
      background-color: #766983;
      height: 100%;
      width: 0%;
    }
  }

  .cover {
    width: 221px;
    height: 221px;
    background-color: #270645;
    border-radius: 5px;

    .avatarName {
      font-family: Helvetica;
      font-weight: bold;
      font-size: 15px;
      line-height: 24px;
      color: #ffffff;
      position: absolute;
      left: 16px;
      top: 20px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      width: 150px;
    }

    .bg {
      width: 100%;
      height: 100%;
      position: absolute;
      object-fit: cover
    }

    &::v-deep .v-avatar {
      position: absolute;
      bottom: 20px;
      margin-left: 93px;
    }

  }

  &.mobile {
    padding: 0 !important;

    .main {
      margin-left: 0;

      .card_img {
        width: 100%;

        .creatimg {
          width: 100% !important;
          height: 240px !important;

          .uploadbox {
            margin: 40px auto;
          }
        }

        .clo {
          visibility: hidden;
          position: relative;
          left: 200px;
          top: 40px;
          background-color: #fff;
          border-radius: 50%;
          cursor: pointer;
          z-index: 9999;
        }

        &:hover .clo {
          visibility: visible;
        }

        .upload {
          height: 20rem;
        }
      }

      .look {
        margin-bottom: 5rem;

        &:hover .clo {
          visibility: visible;
        }

        .clo {
          visibility: hidden;
          position: absolute;
          left: 200px;
          top: 40px;
          background-color: #fff;
          border-radius: 50%;
          cursor: pointer;
          z-index: 9999;
        }

        .card_info {
          width: 100%;
          height: 24rem;
        }

      }
    }

    .box {
      width: 100%;
    }

    .footer {
      margin-bottom: 100px;
      margin-left: 0;

      .sub {
        width: 100%;
      }
    }
    .title1 {
      width: 100%;
    }
    .createLimit {
      width: 88vw
    }
    .drop_area {
      width: 88vw;
    }
    .look {
      margin: -40px auto 110px auto !important;
    }
  }
}

.look {
  position: relative;
  margin-top: 100px !important;

  &::v-deep .text-number {
    display: none !important;
  }

  &::v-deep .text-avatar {
    padding-bottom: 20px !important;
  }

  .avatar2::after {
    content: "";
    width: 100%;
    height: 100px;
    left: 0;
    bottom: 0;
    // background-color: red;
    position: absolute;
  }

  .preview {
    position: absolute;
    top: 220px;
    left: 35px;
  }
}

.mask {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.commit-close {
  display: block;
  position: absolute;
  right: 10px;
  top: 10px;
  background-color: #fff;
  border-radius: 50%;
  cursor: pointer;
}

.commit-logo {
  margin: 20px;
}

.commit-text {
  display: flex;
  height: 150px;
  justify-content: center;
  align-items: center;
  letter-spacing: 0.4rem;
}

.imgfile {
  position: absolute;
  top: 80px;
  left: 55px;
  z-index: 999;
  text-align: center;
}



.v-select ::v-deep.v-text-field__details {
  display: none;
}

.desc {
  font-family: Helvetica;
  font-size: 13px;
  color: #766983;
  position: relative;
}

// common
.star {
  color: red;
  font-size: 18px;
  font-family: "宋体";
  margin-left: 10px;
}
</style>
