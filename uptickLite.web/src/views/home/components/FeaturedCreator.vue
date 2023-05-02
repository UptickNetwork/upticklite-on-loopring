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
  <div v-if="creator" class="featured-creator d-flex flex-column justify-center align-center">
    <Avatar :size="70" :did="creator.did" :hash="creator.profilePhoto" showAvatarInfo/>
    <div class="name mt-lg-2">{{username}}</div>
  </div>
</template>

<script>
import Avatar from "@/components/avatar/index.vue";

export default {
  name: "FeaturedCreator",
  components: { Avatar },
  props: {
    creator: {
      type: Object,
      require: true,
    },
  },
  data() {
  	return {
  		username: ""
  	}
  },
  mounted() {
	  this.username = this.creator.userName;
	  if(this.creator.userName.length > 12) {
		  this.username = this.creator.userName.substr(0,12);
	  }
  }
};
</script>

<style lang='scss' scoped>
.featured-creator {
  width: 131px;
  height: 131px;
  line-height: initial;
  background-image: linear-gradient(#ffffff, #ffffff),
    linear-gradient(#6f58d9, #6f58d9);
  background-blend-mode: normal, normal;
  box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
  border-radius: 5px;
}
.name {
  font-size: 15px;
	letter-spacing: 0px;
	color: #270645;
  width:90px;
  height: 20px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow:ellipsis;
  text-align:center;
}
</style>
