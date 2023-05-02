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
  <div class="video">
    <video-player
      class="video-player-box"
      ref="videoPlayer"
      :options="playerOptions"
    >
    </video-player>
    <v-icon class="icon" size="30" color="#fff" @click="click"
      >mdi-video</v-icon
    >
  </div>
</template>

<script>
import { getFileSrc } from "@/utils/file";

const VIDEO_KEY = "VIDEO";

export default {
  name: "Video",
  props: {
    hash: String,
  },
  data() {
    return {
      playerOptions: {
        controls: false,
        fill: true,
      },
    };
  },
  computed: {
    player() {
      return this.$refs.videoPlayer.player;
    },
  },
  watch: {
    hash: {
      handler: async function (hash) {
        if (hash) {
          const src = await getFileSrc(VIDEO_KEY, hash, true);
          if (this.player && src) {
            this.player.src({
              type: "video/mp4",
              src,
            });
          }
        }
      },
      immediate: true,
    },
  },
  methods: {
    click: function () {
      this.player.play();
    },
  },
};
</script>

<style lang="scss" scoped>
.video {
  position: relative;
  .video-player-box {
    width: 100%;
    height: 100%;
  }
  .icon {
    position: absolute;
    left: 15px;
    bottom: 15px;
  }
}
</style>
