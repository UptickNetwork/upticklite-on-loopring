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
          Create NFT
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
                <div class="mt-5">{{ $t("createLimit") }}</div>
              </template>
              <div class="progress_area" v-if="isDrop">
                <span :style="{ width: progressValue + '%' }"></span>
              </div>
            </div>
          </FIleInput>
          <video v-else-if="fileType == 'video/mp4'" :src="localFileUrl || uploadFileUrl" width="100%" height="100%" contain loop
            controls controlslist="nodownload"></video>
          <v-img v-else :src="localFileUrl || uploadFileUrl" width="100%" height="100%" contain></v-img>
        </div>
        <div>
          <div class="title mt-3 mb-2">NFT Name<span class="star">*</span></div>
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
          <div class="d-flex flex-row Properties" style="position: relative;">
              <div class="title  ">Properties (Limit 64) </div>
             <img class="icon" style="width:20px;height:20px;margin-top:7px;margin-left:10px;" @mouseenter="moused()"
                @mouseleave="leave()" src="@/assets/icons/icon_wen.png"  />
             <div class="explain d-flex flex-row align-center" v-if="isshow" >
                <span>Tags can be added to the NFT for easy searchability and distinction</span>
              </div>
          </div>
         
            <div class="list">
              <div class="item d-flex flex-row" v-for="(item,index) in prodList" :key="index">
                <div class="itemkey  d-flex flex-column">
                  <div>Key</div>
                 <!-- <v-text-field ref="nameValue" v-model="item.key"  label="" outlined :key="refresh_key"></v-text-field> -->
                  <input  v-model="item.trait_type" type="text" style="text-align:left" :maxlength="10">
                </div>
                 <div class="valuekey  d-flex flex-column ml-3">
                  <div>Value</div>
                 <!-- <v-text-field ref="nameValue" v-model="item.value"  label="" outlined :key="refresh_key"></v-text-field> -->
                 <input  v-model="item.value" type="text" style="text-align:left" :maxlength="20">
                </div>
                <div class="img">
                  <img class="icon" src="@/assets/icons/icon_d.png" @click="imgRemove(index)" />
                </div>

              </div>

            </div>
        </div>
        <div>
          <button  class="add_btn mt-5" @click="addpropertie" v-if="prodList.length< 64"> + Add property</button>
        </div>
  
        <div class="mb-4 box">
          <div class="title mt-3 mb-2">{{ $t("Royalty (%)") }}</div>
          <div class="percentage">Represents the percentage to be received from each subsequent resale (max 10%)</div>
          <div>
            <v-text-field ref="royaltyPercentage" :rules="[rules.RoyaltyPercentage]" v-model="royaltyPercentage"
              :suffix="'%'" label="" outlined solo></v-text-field>
          </div>
       
        </div>


        <div class="mb-4 box">
          <div class="title mt-3 mb-2">{{ $t("createAmount") }}</div>
          <div>
            <v-text-field ref="amountValue" :rules="[rules.EditionValue]" v-model="amountValue"
              :suffix="this.$t('createAmountEdition')" label="" outlined solo :disabled="this.amountListValue === 2">
            </v-text-field>
          </div>
        </div>

        <div class="title box mb-9">
          {{ $t("createLssuer") }}：{{ issuerName }}
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
      <div class="look ml-lg-3" style="position: relative">
        <img v-if="uploadImageUrl" class="clo" width="31px" height="31px" @click.stop="closeImgDialog"
          src="@/assets/btn_close.png" alt="" />
        <FIleInput accept=".jpg,.jpeg,.png,.gif" class="imgfile" @change="imgChange" v-if="showUploadImage"
          :key="fileInputKey">
          <div class="uploadbox drop_area" style="
              width: 210px;
              height: 210px;
              margin-left: -50px;
              margin-top: -80px;
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
        <Card class="avatar2" owerfrom="create" :value="uploadFileHash" type="sell" />
        <div class="title mt-3 mb-2 preview">
          Preview in the market
        </div>
      </div>
    </div>
    <div class="footer box">
      <span class="price mb-4">{{ $t("popupAuctionMintFee") }}: {{ feeValueFormat }} {{ $walletUnit }}</span>
      <v-btn class="subdefect" height="51px" disabled v-if="subDisabled && !needToShowLink">  
        {{ $t("popupTransferCommit") }}
      </v-btn>
      <v-btn class="subdefect" height="51px" disabled v-else-if="subDisabled && needToShowLink">  
        {{ $t("popupAuctionDefectPriceInsufficientFunds") }}
      </v-btn>
      <v-btn class="uc" height="51px" @click="subCommit" v-else>
        {{ $t("popupTransferCommit") }}
      </v-btn>
      <span class="price mt-4">{{ $t("popupNewTransferBalance") }}: {{ transferValue }}
        {{ $walletUnit }}</span>
        <span class="price" v-if="lockValue >0"> L2 Wallet Frozen amount：{{ lockValue }} {{ $walletUnit }} </span>

      <span v-if="needToShowLink" class="lrcLink"> {{ $t("insufficientBalance") }}
        <a target="_blank" href="https://loopring.io/#/trade/lite/LRC-ETH">{{ $t("exchangeNow") }}</a>
      </span>
      <span v-else class="lrcLink">
      </span>
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
import Card from "@/components/workCard/Card.vue";
import api from "@/api";
import { getFileSrc } from "@/utils/file";

import PromptBox from "@/components/PromptBox.vue";

import { toIpfsLink, toBaseUri } from "../../utils/helper"

const UPLOAD_KEY = "UPLOAD";
let fileUrl = "";
let imageUrl = "";

export default {
  components: { FIleInput, Card, PromptBox },
  data: () => ({
    royaltyPercentage: 5,
    refresh_key: 0,
    fileInputKey: 0,
    feeValue: 0,
    prodList:[{trait_type:'',value:''},
    ],
    subDisabled: true,
    feeValueFormat: 0.1,
    transferValue: "",
    checkbox: false,
    switch1: false,
    isShowFalse: false,
    isShowSuccess: false,
    falseTitle: "this is a error",
    successTitle: "create success",
    nameValue: "",
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
    needToShowLink: false,
    isShowUnlock: true,
    lockValue:'',
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
      text: "multiple（ERC-1155）",
      value: 4,
    },
    {
      text: "single（ERC-721）",
      value: 3,
    },

    ],
    nameRules: [
      (v) => !!v || "Please upload an name for your item",
      (v) => (v && v.length <= 80) || "Name must be less than 80 characters",
    ],
    descRules: [(v) => !!v || "Please upload an description for your item"],
    rules: {
      EditionValue: (value) => {
        if (value <= 0 || value > 100000) {
          return "Invalid amount for sale.";
        }
        let pattern = /^[1-9]\d{0,3}|100000$/
        return pattern.test(value) || "Invalid amount for sale.";
      },
      RoyaltyPercentage: (value) => {
        if (value < 0) {
          return "Invalid Loyalty Percentage";
        }
        if (value > 10) {
          return "Invalid Loyalty Percentage";
        }
         const pattern = /^[0-9]*[0-9][0-9]*$/;
        return pattern.test(value) || "Invalid Loyalty Percentage";
      }
    },

    uploadFileHash: {
      imgUrl: "",
      name: "　",
      did: "",
      ownerName: "",
      ownerPhoto: "",
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

    collectItem: {},
    url: '',
    isshow:false,
    isshowLincense:false
  }),
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  async mounted() {
    console.log("wxl --- isMobile",this.$vuetify.breakpoint.mobile)
    window.eventBus.$on("SignatureEvent", this.updateUserBalances);
    let tokenAddress = this.$route.query.tokenAddress;
    let res_info = await api.home.collectionInfo({ tokenAddress });
    if (res_info.data) {
      this.collectItem = res_info.data;
	  if(res_info.data.linkUrl){
		   this.url = res_info.data.linkUrl
	  }else{
		   this.url = res_info.data.address
	  }
     
    }

	api.home.getSystemParams({keys:"loyalty"}).then((systemParams) => {	
	 this.royaltyPercentage=systemParams.data[0].value
	});
		
    this.categoryValue = 1;
    this.amountListValue = 4;

    this.feeValueFormat = await this.LRCHandler.getFee(this.$store.state.did,this.collectItem.baseUrl);
    
    this.LRCHandler.getUserBalances(1).then((amount) => {
      this.transferValue = Number(this.maxDecimal(Number(amount.amountValue) - Number(amount.lockedValue))  )
      this.lockValue = this.maxDecimal(amount.lockedValue)
      console.log("wxl ---- updateUserBalances",amount,this.transferValue)
 
      if (Number(this.feeValueFormat) > Number(this.transferValue)) {
        this.needToShowLink = true;
      }
    });

    if (this.$store.state.did) {
      let param = { address: this.$store.state.did };

      let res = await api.home.loopringUserInfo(param);

      if (res.data != null) {
        this.uploadFileHash.ownerPhoto = res.data.profilePhoto;
        this.uploadFileHash.ownerName = res.data.name;
        if (!this.uploadFileHash.ownerName) {
          let did = this.$store.state.did;
          this.uploadFileHash.ownerName = did.substr(0, 5) + "..." + did.substr(-3);
        }
        this.uploadFileHash.userAddress = res.data.userAddress;
        this.issuerName = this.uploadFileHash.ownerName;
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
    royaltyPercentage: {
      handler: function() {
        this.submitStatus();
      },
      immediate: true
    },
    amountValue: {
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
    }
  },
  methods: {
    maxDecimal(number){
  return String(number).replace(/^(.*\..{4}).*$/, "$1")
},

    async updateUserBalances() {
      let amount = await this.LRCHandler.getUserBalances(1);
     
      this.transferValue = amount.amountValue;
    },
    submitStatus() {
      this.subDisabled = !(
        this.nameValue && this.uploadFileUrl && this.descriptionValue && this.royaltyPercentage && this.amountValue && this.checkbox && parseFloat(this.feeValueFormat) < parseFloat(this.transferValue)
      );
    },
    verify() {
      let mountVer = this.$refs.amountValue.validate(true);
      let nameVer = this.$refs.nameValue.validate(true);
      let emailVer = this.$refs.descriptionValue.validate(true);
      let royaltyPercentage = this.$refs.royaltyPercentage.validate(true);
      !mountVer ? this.$refs.amountValue.focus() : "";
      !nameVer ? this.$refs.nameValue.focus() : "";
      !emailVer ? this.$refs.descriptionValue.focus() : "";
      !royaltyPercentage ? this.$refs.royaltyPercentage.focus() : "";
      return nameVer && emailVer && mountVer && royaltyPercentage;
    },

    async subCommit() {
      this.feeValueFormat = await this.LRCHandler.getFee(this.$store.state.did, this.collectItem.baseUrl);
      
      if (!this.verify()) {
        return;
      }

      if (this.needToShowLink) {
        window.scrollTo(0, 0);
        this.$toast("error", this.$t("insufficientBalance"));
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
      if (this.amountListValue === 1) {
        this.denomType = 2;
      }
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
        let attributes = {}
        let properties={}
        this.prodList.forEach((v)=>{
          if(v.trait_type)
         properties[v.trait_type]=v.value
          
        }) 

         let ipfsParams = {
          mint_channel:"UptickLite",
          license_code:"",
          collection_metadata: this.collectItem.collectionMetadata,
          name: this.nameValue,
          description: this.descriptionValue,
          image: toIpfsLink(imageUrl),
          animation_url: toIpfsLink(imageUrl),
          royalty_percentage: this.royaltyPercentage,
          properties:properties,
          attributes: this.prodList,
          minter: this.$store.state.did
        };
         let collectionMetadata = await this.ipfsAddJSON(JSON.stringify(ipfsParams))
      
        let mintNft_res = await this.LRCHandler.mintNft(
          this.collectItem.baseUrl,
          this.collectItem.address,
          collectionMetadata.path,
          this.amountValue,
          this.royaltyPercentage);
        if(window.walletType == 2) {   
          if (!mintNft_res || !mintNft_res.response || !mintNft_res.response.accountId) {
            this.$toast("error", "Unlock your account to check your assets.")
            this.showMask = false;
            return;
          }
        } else if (window.walletType == 3) {
          if (!mintNft_res || !mintNft_res.response || !mintNft_res.nftId) {
            this.$toast("error", "Unlock your account to check your assets.")
            this.showMask = false;
            return;
          }
          if(mintNft_res.response.code == 500000){
             this.$toast("error", "Signature request was rejected");
              this.showMask = false;
             return;
          }
        }
		this.prodList.forEach((v)=>{
     properties[v.trait_type]=v.value
     v.trait_type = v.trait_type.replace( /^["|'](.*)["|']$/g,"$1")
    v.value = v.value.replace( /^["|'](.*)["|']$/g,"$1")
		 v.trait_type=encodeURIComponent((v.trait_type).replace(/(^\s*)|(\s*$)/g, ""));
		  v.value=encodeURIComponent((v.value).replace(/(^\s*)|(\s*$)/g, ""));
		})
		
	  
        let param = {
          contractAddress: this.collectItem.address,
          description: encodeURIComponent(this.descriptionValue),
          name: encodeURIComponent(this.uploadFileHash.name),
		      royaltyPercentage: this.royaltyPercentage,
          owner: this.$store.state.did,
          tokenId: mintNft_res.response.nftTokenId,
          imgUrl: this.uploadFileHash.imgUrl,
          nftData: mintNft_res.response.nftData,
          nftId: mintNft_res.nftId,
          accountId: mintNft_res.response.accountId,
          amount: this.amountValue,
		      properties:JSON.stringify(this.prodList),
          metadataUrl:collectionMetadata.path,
          lincenseCode:"",
		  
        };
		
        let deploy_res = await api.home.nftMint(param);
        this.showMask = false;

        if (deploy_res.success == true) {
          window.sessionStorage.setItem("TokenAddress", mintNft_res.nftTokenAddress);

            this.$toast("success", this.$t("amCreateSuccessTitle")).then(() => {
            let url = this.shortUrlValue
            this.$router.push({
						path: `/collection_create/${this.url}`,
          });
                   
					});
           
        } else {
          this.$toast("error", this.$t(deploy_res.msg));
        }

      } catch (e) {
        console.log(e);
        this.showMask = false;
        this.$toast("error", this.$t("amCreateFalseTitle2"));
      }
    },
    async change(file) {
      let ft = file.name.substr(file.name.lastIndexOf(".") + 1);
      if ("JPG,JPEG,PNG,GIF".indexOf(ft.toUpperCase()) == -1) { // JPG,JPEG,PNG,GIF,MP4
        this.$toast("error", this.$t("file_type_not_support"));
        this.fileInputKey++;
        return;
      }

      this.isDrop = true;
      this.fileInputKey++;

      this.fileType = file.type;

      if (file.type == "video/mp4") {
        if (file.size > 20971520) {
          this.isDrop = false;
          this.$toast("error", this.$t("video_not_support"));
          return;
        }
        this.showUploadImage = true;
        this.categoryValue = 4;
      } else {
        if (file.size > 10242880) {
          this.isDrop = false;
          this.$toast("error", this.$t("image_not_support"));
          return;
        }
      }
      let {path} = await this.ipfsAddFile(file);
      if (!path) {
        this.isDrop = false;
        this.showUploadImage = false;
        if (this.categoryValue == 4) {
          this.categoryValue = 1;
        }
        return;
      }
      imageUrl = path;
      this.uploadFileHash.imgUrl = path;
      this.uploadFileUrl = this.getImageIpfs(imageUrl);
      this.isDrop = false;
      this.progressValue = 0;

    
      this.localFileUrl = window.URL.createObjectURL(file);
      this.uploadFileHash.localImgUrl = this.uploadFileUrl;

    },
    async imgChange(file) {
      let ft = file.name.substr(file.name.lastIndexOf(".") + 1);
      if ("JPG,JPEG,PNG,GIF".indexOf(ft.toUpperCase()) == -1) {
        this.$toast("error", this.$t("file_type_not_support"));
        this.fileInputKey++;
        return;
      }

      this.isDropImg = true;
      let {path} = await this.ipfsAddFile(file);
      this.fileInputKey++;
      if (!path) {
        this.isDropImg = false;
        this.isShowFalse = true;
        // this.falseTitle = res.data.msg;
        return;
      }
      imageUrl = path;
      this.uploadFileHash.imgUrl = imageUrl;
      this.uploadImageUrl = await getFileSrc(UPLOAD_KEY, path);
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
      e.preventDefault(); //必填字段
      let file = e.dataTransfer.files[0];
      this.change(file);
    },
    enentDropImg(e) {
      e.stopPropagation();
      e.preventDefault(); //必填字段
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

    addpropertie(){
      if(this.prodList.length < 64)
      this.prodList.push({trait_type:'',value:''})
      console.log("22222",this.prodList)
    },
    imgRemove(i){
      console.log("wwwww",i)
      if(this.prodList.length >1)
       this.prodList.splice(i,1)
       console.log("33333",this.prodList)
    },
    moused() {
      console.log("33333333");
      this.isshow = true;
    },
    leave() {
      this.isshow = false;
    },
    mousedlincense(){
      this.isshowLincense = true

    },
    leavelincense(){
      this.isshowLincense = false
    }
    
  },
};
</script>

<style lang="scss" scoped>
.title {
  font-weight: bold;
  font-size: 15px !important;
  font-weight: bold;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #270645;
  display: flex;
  align-items: center;
}
 .explain {
      width: 260px;
      height: 45px;
      background-color: #270645;
      border-radius: 3px;
      position: absolute;
      top: 33px;
      right: 47px;
      span {
        font-family:Helvetica;
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
    .explainlincense{
      width: 330px;
      height: 170px;
      background-color: #270645;
      border-radius: 3px;
      position: absolute;
      top: 44px;
      right: -65px;
      z-index: 1;
      span {
        margin: auto;
        font-family:Helvetica;
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
.add_btn{
  	width: 136px;
	height: 40px;
	background-color: #ffffff;
	border-radius: 5px;
  border: solid 1px rgba(158, 158, 158);
}
.list{
  .item{
    .itemkey{
       input {
         border: solid 1px rgba(158, 158, 158);
         border-radius: 5px;
        font-family: Helvetica;
        font-size: 16px;
        height: 60px;
        color: rgba(0, 0, 0, 0.78);
        text-align: right;
        width: 170px;
        margin-left: auto;
        margin-right: 10px;
        padding-left: 10px;

        &:focus {
            outline: none;
        }
    }

    }
    .valuekey{
         input {
         border: solid 1px rgba(158, 158, 158);
         border-radius: 5px;
        font-family: Helvetica;
        font-size: 16px;
         color: rgba(0, 0, 0, 0.78);
        text-align: right;
        width: 220px;
        height: 60px;
        margin-left: auto;
        margin-right: 10px;
        padding-left: 10px;

        &:focus {
            outline: none;
        }
    }


    }
    .img{
      .icon{
        margin-top: 40px;
        margin-left: 10px;
        height: 20px;
        width: 20px;
      }
    }
  }

}
.percentage{
  font-size: 15px !important;
  color: #766983;
  margin-bottom: 10px;
}
.percentageNote{
   font-size: 12px !important;
  color:red;
  margin-top:-15px;
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

  .main {
    margin-left: 205px;

    .card_img {
      position: relative;

      .creatimg {
        .uploadbox {
          text-align: center;
          margin: 200px auto;
        }
      }

      .clo {
        visibility: hidden;
        position: absolute;
        left: 410px;
        top: 60px;
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

      &::v-deep .work {
        background-color: #e3e3e3;
      }

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

      .preview {
        position: absolute;
        top: 320px;
        left: 35px;
      }
    }
  }

  .footer {
    display: flex;
    flex-direction: column;
    text-align: center;
    margin-bottom: 50px;
    margin-left: 205px;

    .uc {
      border-radius: 25px;
    }
       .subdefect {
         font-family: Helvetica;
      margin: 10px auto;
      width: 450px;
      height: 51px;
      background-image: linear-gradient(#766983, #766983),
        linear-gradient(#270645, #270645);
      background-blend-mode: normal, normal;
      border-radius: 25px;
      opacity: 0.9;
      font-size: 20px;
      color: #ffffff !important;
    }

    .price {
      display: block;
      font-family: Helvetica;
      font-size: 13px;
      font-weight: normal;
      font-stretch: normal;
      color: #766983;
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
      font-weight: bold;
      ;
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
          position: relative;
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
  margin-top: 50px !important;

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
    position: absolute;
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

.contain ::v-deep.v-messages__message {
  color: red;
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

.lrcLink {
  margin-top: 10px;
}
.star {
  color: red;
  font-size: 18px;
  font-family: "宋体";
  margin-left: 10px;
}
.explain {
      width: 260px;
      height: 45px;
      background-color: #270645;
      border-radius: 3px;
      position: absolute;
      top: 33px;
      left: 155px;
      span {
        font-family:Helvetica;
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
    .explainlincense{
      width: 330px;
      height: 170px;
      background-color: #270645;
      border-radius: 3px;
      position: absolute;
      top: 44px;
      left: 200px;
      z-index: 1;
      span {
        margin: auto;
        font-family:Helvetica;
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
</style>
