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
    <v-card-title class="d-block text--title pt-4 px-5 pb-2 text-truncate"
      >{{value.name}}</v-card-title
    >
    <v-card-text class="d-flex flex-row align-center pb-0 px-5" style="margin-bottom: 18px;">
      <Avatar  size="40" :did="value.owner" :hash="profilePhoto" showAvatarInfo/>
      <div class="ml-4 text-truncate" style="max-width: 100%;">{{value.issuerName}}</div>
    </v-card-text>
  </div>
</template>

<script lang='js'>
import Avatar from '@/components/avatar/index.vue';
import api from "@/api";

export default {
  name: 'SellCardContent',
  components: { Avatar },
  props: {
    value: {
      type: Object,
      default: () => {}
    }
  },
    async mounted() {
		let params = {
		  userAddress: this.value.owner,
		  chainType:this.$store.state.chainType
		};
		let res = await api.home.createInfo(params);
		this.profilePhoto = res.data.profilePhoto;
    },
  data: function() {
    return {
		profilePhoto: ""
	};
  }
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
.text--number {
  font-size: 15px;
  line-height: 1;
}
.v-divider {
  border-color: #e3e3e3;
}
</style>
