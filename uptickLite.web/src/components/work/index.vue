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
  <div class="work">
    <v-img
      class="main-img"
      :src="src"
      :contain="contain"
      width="100%"
      max-height="100%"
      :aspect-ratio="aspectRatio === -1 ? undefined : aspectRatio"
      @click="imgClick"
      style="cursor: pointer"
    >
      <div class="content">
        <v-icon v-if="attribute === 7" class="icon" size="30" color="#fff"
          >mdi-video</v-icon
        >
      </div>
    </v-img>
  </div>
</template>

<script>
import { getFileSrc } from "@/utils/file";

const WORK_KEY = "WORK";

export default {
  name: "Work",
  inheritAttrs: false,
  props: {
    aspectRatio: {
      type: Number,
      default: 1,
    },
    hash: {
      type: String,
    },
    attribute: Number, // 7 video
    contain: {
      type: Boolean,
      default: false,
    },
  },
  data: () => ({
    src: undefined,
    videoUrl: undefined,
    videoOptions: {},
  }),
  watch: {
    hash: {
      handler: async function (hash) {
        if (hash) {
          this.src = await getFileSrc(WORK_KEY, hash);
        }
      },
      immediate: true,
    },
  },
  methods: {
    imgClick() {
      this.$emit("imgClick");
    },
  },
};
</script>

<style lang="scss" scoped>
.work {
  .content {
    width: 217px;
    height: 217px;
    position: relative;
    height: 100%;
    .icon {
      position: absolute;
      left: 15px;
      bottom: 15px;
    }
  }
}
</style>
