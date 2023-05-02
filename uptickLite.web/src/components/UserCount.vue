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
  <div class="d-flex justify-space-between align-center" :style="$vuetify.lang.current=='pt'?'width:300px':''">
    <div class="d-flex flex-column">
      <span class="text-subtitle-1 font-weight-medium line-height-1">{{myInfo.fansNumber}}</span>
      <span class="text-body-2 mt-2">{{$t('avatarAvatarInfoFans')}}</span>
    </div>
    <div class="d-flex flex-column">
      <span class="text-subtitle-1 font-weight-medium">{{myInfo.followCount}}</span>
      <span class="text-body-2 mt-2">{{$t('fansSubscrible')}}</span>
    </div>
    <div class="d-flex flex-column">
      <span class="text-subtitle-1 font-weight-medium">{{myInfo.listedCount}}</span>
      <span class="text-body-2 mt-2">{{$t('avatarlistWork')}}</span>
    </div>
	<div class="d-flex flex-column">
	  <span class="text-subtitle-1 font-weight-medium">{{myInfo.workCount}}</span>
	  <span class="text-body-2 mt-2">{{$t('createWorks')}}</span>
	</div>
  </div>
</template>

<script lang='js'>

export default {
  name: 'UserCount',
  props: {
    myInfo:{
      type:Object
    }
  },
  data: function() {
    return {};
  }
};

</script>

<style lang='scss' scoped>
.d-flex {
  line-height: 1 !important;
}
.text-subtitle-1 {
    font-weight: bold !important;
    font-size: 15px !important;;
    color: #270645 !important;;
    font-weight: bold !important;;
}
.text-body-2 {
    font-family: Helvetica !important;
    font-size: 14px !important;;
    color: #766983 !important;;
    margin-top: 0 !important;
}
</style>
