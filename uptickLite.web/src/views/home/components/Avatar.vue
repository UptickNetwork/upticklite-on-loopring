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
  <v-tooltip
    bottom
    v-model="showAvatar"
    color="white"
    content-class="tooltip-avatar"
    open-on-hover
    close-delay="1000"
    :open-on-click="false"
    offset-overflow
  >
    <template v-slot:activator="{ on, attrs }">
      <v-avatar
        :size="size"
        @click="click"
        v-bind="attrs"
        v-on="on"
        style="cursor: pointer"
      >
        <slot />
      </v-avatar>
    </template>
    <AvatarInfo />
  </v-tooltip>
</template>

<script>
import AvatarInfo from "./AvatarInfo.vue";

export default {
  components: {
    AvatarInfo,
  },
  props: {
    size: [String, Number],
  },
  data() {
    return {
      showAvatar: false,
    };
  },
  methods: {
    click: function() {
      this.$router.push({
        name: 'Otherpage'
      })
    }
  }
};
</script>

<style lang="scss" scoped>
.tooltip-avatar {
  z-index: 12081208 !important;
  opacity: 1 !important;
  padding: 0 !important;
  pointer-events: initial !important;
}
</style>
