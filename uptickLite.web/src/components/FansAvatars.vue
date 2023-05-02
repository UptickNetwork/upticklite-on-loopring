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
  <div class="pl-3">
    <v-avatar class="ml-n3" size="30" v-for="(info, index) of avaList" :key="index">
      <v-img :src="info.src" alt="Midoriya" />
    </v-avatar>
  </div>
</template>

<script lang='js'>
import { getFileSrc } from "@/utils/file";

const AVATAR_KEY = "AVATAR";

export default {
  name: 'FansAvatars',
  props: {
    avatars: {
      type: Array,
    }
  },
  data() {
    return {
      avaList: []
    }
  },
  mounted() {
    this.getFans()
  },
  methods: {
    getFans() {
      this.avatars.forEach(async v => {
        if (v) {
          let fansrc = await getFileSrc(AVATAR_KEY, v);
          this.avaList.push({ src: fansrc });
        }
      })
    }
  },
};

</script>

<style lang='scss' scoped>
.v-avatar {
  box-sizing: content-box;
  border: 3px solid white;
}
</style>
