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
  <v-menu open-on-hover bottom offset-y :close-on-content-click="false" :disabled="isShowAvatarInfo()">
    <template v-slot:activator="{ on, attrs }">
      <v-avatar
        @click.stop="click"
        v-bind="{ ...attrs, ...$attrs }"
        v-on="{ ...on, ...$listeners }"
        style="cursor: pointer"
      >
        <v-img class="img" style="border: solid 2px #ffffff;" :src="getImageIpfs(hash, 'avatar')" alt="avatar" />
      </v-avatar>
    </template>
    <AvatarInfo :did="did" />
  </v-menu>
</template>

<script>
import AvatarInfo from "./AvatarInfo.vue";
import logoimg from "@/assets/default.png"

export default {
  name: "Avatar",
  inheritAttrs: false,
  components: {
    AvatarInfo,
  },
  props: {
    hash: {
      type: String,
    },
    did: {
      type: String,
    },
    showAvatarInfo: {
      type: Boolean,
      default: false,
    },
  },
  data: () => ({
    logoimg,
    src: undefined,
    showAvatar: false,
    avaInfo: {},
	  isMenuBottom: false
  }),
  mounted() {
  },
  methods: {
    click() {
      if(!this.did) return;
      
      if(this.did == this.$store.state.did){
        this.$router.push({
          name: "PersonalPage",
          query: { did: this.did },
        });
      }else{
        this.$router.push({
          name: "Otherpage",
          query: { did: this.did },
        });
      }
    },
	isShowAvatarInfo() {
		if(this.showAvatarInfo && this.did) {
			 return false;
		} else {
			return true;
		}
	}
  },
};
</script>

<style lang="scss" scoped>
    .img ::v-deep.v-responsive__sizer {
        padding-bottom: 100% !important;
    }
</style>
