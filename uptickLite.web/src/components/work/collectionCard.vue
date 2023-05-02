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
  <div class="work" style="display: flex; align-items: center；">
    <v-img
      v-if="!videoUrl && !isCanplay"
      :src="src"
      :contain="contain"
      width="100%"
      max-height="100%"
      :aspect-ratio="aspectRatio === -1 ? undefined : aspectRatio"
      @click="imgClick"
      style="cursor: pointer; display: flex; align-items: center;"
    >
      <div style="background-color: rgb(39, 6, 69,0.2); height: 281px; padding-top: 15px;">
           <div class="infos d-flex flex-row">
        <div class="d-flex flex-column contant"  style=" position: relative;">
          <div class="name">{{ value.name }}</div>
          <span v-if="value.nftCount != null"> {{value.nftCount}}{{ $t("item") }}</span>
        </div>
        <div v-if="mycreate != 'mycreate'">
            <div v-if="value.collectionType == 1">
           <img
          class="ml-11 mt-1"
          src="@/assets/icons/icon_collection.png"
          width="20px"
          height="20px"
          alt=""
          @mouseenter="moused()"
          @mouseleave ="leave()"
        />
        <div class="explain d-flex flex-row align-center" v-if="isshow">
          <span>{{ $t("nft_explain") }}</span>
        </div>
        </div>
        <div v-else-if="value.collectionType == 2">
           <img
          class="ml-11 mt-1"
          src="@/assets/icons/icon_folder.png"
          width="25px"
          height="20px"
          alt=""
          @mouseenter="moused()"
          @mouseleave ="leave()"
        />
        <div class="explain d-flex flex-row align-center" v-if="isshow">
          <span>{{ $t("collection_explain") }}</span>
        </div>

        </div>
        </div>
        <div v-else>
          <img
          v-if="value.isBurn"
          class="ml-11 mt-1"
          src="@/assets/icons/icon_delete.png"
          width="20px"
          height="20px"
          alt=""
          @click.stop="burnBtn"
        />
        </div> 
      </div>

      <div
        class="pb-0 px-5 text-avatar"
        style="margin-top: 150px; margin-left: 35%"
        v-if="mycreate != 'mycreate' "
      >
        <Avatar
          size="50"
          :did="value.owner"
          :hash="value.profilePhoto || value.issuerImg"
          imgType="middlev"
          showAvatarInfo
          :showCardList = "showCardList"
        />
      </div>
      <div
        class="pb-0 px-5 text-avatar"
        style="margin-top: 200px; margin-left: 35%"
        v-else
      > 
      </div>
   </div>
   
    </v-img>
    <video
      v-else
      :src="videoUrl"
      :contain="contain"
      :aspect-ratio="aspectRatio === -1 ? undefined : aspectRatio"
      @click="imgClick"
      style="cursor: pointer; width: 100%; max-height: 100%"
      controls
      :poster="src"
      @canplaythrough="canplaythrough"
      autoplay
      loop
      muted
    ></video>
		<v-dialog v-model="Burning">
			<burn :address="this.value.address" :type="type" @getOpen="openBurn" :pageValue="value"></burn>
		</v-dialog>
  </div>
  
</template>

<script>
import Burn from "@/components/popup/burn.vue";
import { getFileSrc } from "@/utils/file";
import Avatar from "@/components/avatar/index.vue";

const COLLECTION_KEY = "COLLECTION";
export default {
  name: "Work",
  inheritAttrs: false,
  components: { Avatar,Burn },
  props: {
    aspectRatio: {
      type: Number,
      default: 1,
    },
    hash: {
      type: String,
    },
    hashVideo: {
      type: String,
    },
    attribute: Number, // 7 video
    contain: {
      type: Boolean,
      default: false,
    },
    value: {
      type: Object,
    },
    mycreate:{
      type: String,
    }
  },
  data: () => ({
    type:"collectionNFT",
    src: undefined,
    videoUrl: undefined,
    videoOptions: {},
    isCanplay: false,
    isshow:false,
    showCardList:true,
    Burning: false,
  }),
  watch: {
    hash: {
      handler: async function (hash) {
        if (hash) {
          this.src = await getFileSrc(COLLECTION_KEY, hash);
        }
      },
      immediate: true,
    },
    hashVideo: {
      handler: async function (hash) {
        if (hash) {
          this.videoUrl = await getFileSrc(COLLECTION_KEY, hash, true);
        }
      },
      immediate: true,
    },
  },
  methods: {
    imgClick() {
      this.$emit("imgClick");
    },
    canplaythrough() {
      this.isCanplay = true;
    },
    moused() {
      this.isshow = true
    },
    leave(){
       this.isshow = false
    },
			burnBtn() {
				this.Burning = true;
			},
			openBurn(e) {
				this.Burning = e;
			},
  },
};
</script>

<style lang="scss" scoped>
.work {
  .content {
    width: 287px;
    height: 287px;
    position: relative;
    height: 100%;
    .icon {
      position: absolute;
      left: 15px;
      bottom: -130px;
    }
  }
  .infos {
    //  margin-top: -115px;
    .contant {
      margin-left: 26px;
      .name {
        width: 175px;
        height: 22px;
        font-family: Helvetica;
        font-size: 20px;
        font-weight: bold;
        font-stretch: normal;
        line-height: 24px;
        letter-spacing: 0px;
        color: #ffffff;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
      span {
        min-width: 29px;
        height: 16px;
        font-family: Helvetica;
        font-size: 16px;
        font-weight: normal;
        font-stretch: normal;
        line-height: 24px;
        letter-spacing: 0px;
        color: #ffffff;
      }
    }
    .explain {
      width: 137px;
      height: 49px;
      background-color: #270645;
      border-radius: 3px;
       position: absolute;
      top:55px;
      right: 15px;
      span {
        width: 111px;
        height: 30px;
        font-family: Helvetica;
        font-size: 12px;
        font-weight: normal;
        font-stretch: normal;
        line-height: 15px;
        letter-spacing: 0px;
        color: #ffffff;
        margin-left: 13px
      }
    }
  }
}
</style>
