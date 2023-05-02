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
  <v-tabs class="rd" hide-slider exact>
    <v-tab :ripple="false" :to="{ name: 'Home'}">{{ $t('headerNavHome') }}</v-tab>
    <v-tab :ripple="false" :to="{ name: 'LaunchPad'}"> LaunchPad  </v-tab>
    <v-tab :ripple="false" :to="{ name: 'Marketplace'}">{{ $t('headerNavMarketplace') }}</v-tab>
  </v-tabs>
</template>

<script lang="js">

export default {
  name: 'Nav',
  inheritAttrs: false,
  data: function() {
    return {};
  }
};
</script>

<style lang="scss" scoped>
.v-tabs.rd {
  width: unset;
  flex: 0 1 auto;
  &::v-deep .v-item-group {
    flex: 0 1 auto;
    width: unset;
    .v-slide-group__content, .v-slide-group__wrapper {
      flex: 0 1 auto;
    }
  }
 
  .v-tab {
    padding-right: 4px;
    padding-left: 20px;
    color: #1d42ff;
  }
  .img{
    margin-left: -18px;
    margin-top:12px;
  }
  
}

  .icon:after {
    content: "";
    background-image: url(../../assets/icons/icon_promo.png);
    background-size: 100%;
    width: 20px; height: 25px;
    margin-left: 8px;
    
  }
</style>
