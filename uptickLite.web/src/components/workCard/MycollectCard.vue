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
  <div>
    <v-card-text class="d-flex flex-column px-4 pt-4 pb-4">
      <div class="text-number mb-3">{{value.name}}</div>
      <div class="d-flex flex-row">
          <div class="d-flex item-center">
          <v-img v-if="value.marketPrice" :src="getChainByName(value.marketTokenType).img" alt="logo" width="23px" height="23px"/>
        </div>
        <div class="ml-2 text--number text--primary-rd font-weight-medium" style="line-height:22px;">
          {{value.marketPrice}}
        </div>
      </div>
    </v-card-text>

    <div style="float:right;display:flex;z-index:100;margin-top:-56px;margin-right:10px" v-if="value.saleStatus == 1">
      <v-img src="@/assets/icons/icon_onsale.png" alt="logo" width="71" height="56px"/>
    </div>

  </div>
</template>

<script lang='js'>

export default {
  name: 'SellCardContent',
  props: {
    value: {
      type: Object,
      default: () => {}
    }
  },
  data: function() {
    return {
    };
  },

};

</script>

<style lang='scss' scoped>
.text--primary-rd {
  color: #270645;
}
.text--secondary-rd {
  color: #766983;
}
.text--title {
  font-size: 20px;
  line-height: 24px;
}
.text--sub-title {
  font-size: 12px;
  line-height: 1;
}
.text-number{
  width: 190px;
  font-family: Helvetica;
	font-size: 13px;
	font-weight: bold;
	font-stretch: normal;
	line-height: 13px;
	letter-spacing: 0px;
	color: #270645;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.text--number {
  font-size: 15px;
  line-height: 1;
  	font-weight: bold !important;
}
.v-divider {
  border-color: #e3e3e3;
}
</style>
