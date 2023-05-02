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
  <img
    alt="logo"
    src="@/assets/image/logo_network.svg"
    :height="height"
    :width="`${simple ? height : 'auto'}`"
    v-bind="$attrs"
    v-on="$listeners"
    @click="openHome"
    :class="{ logo: true }"
  />
</template>

<script lang="js">

export default {
  name: 'Logo',
  inheritAttrs: false,
  
  props: {
      simple: Boolean,
      height: [String, Number]
  },

  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    }
  },

  methods:{
    openHome(){
       this.$router.push({name:"Home"})
    }

  }
};
</script>

<style lang="scss" scoped>
.logo {
  object-fit: cover;
  object-position: left;
  cursor:pointer;
}

</style>
