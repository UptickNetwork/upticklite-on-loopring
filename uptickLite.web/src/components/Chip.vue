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
  <v-chip
    class="rd"
    color="#fff"
    text-color="primary"
    :ripple="false"
    v-bind="$attrs"
    v-on="$listeners"
  >
    <slot />
  </v-chip>
</template>

<script lang="js">

export default {
  name: 'Chip',
  inheritAttrs: false
};
</script>

<style lang="scss" scoped>
.v-chip.rd {
  height: 40px;
  font-size: 13px;
  background-image: linear-gradient(#ffffff, #ffffff),
    linear-gradient(#270645, #270645);
  background-blend-mode: normal, normal;
  box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
  border-radius: 20px;
  

}
</style>
