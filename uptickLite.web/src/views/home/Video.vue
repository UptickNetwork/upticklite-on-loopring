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
  <ScrollX>
    <div class="scroll-item" v-for="(item, i) in video" :key="i" @click="toPage(item.id)">
      <Card :value="item" type="sell"/>
    </div>
  </ScrollX>
</template>

<script>
import ScrollX from "@/components/scroll/ScrollX.vue";
import Card from '@/components/workCard/Card.vue';

export default {
  name: "Video",
  components: { ScrollX, Card },
  props: {
    video: {
      type: Array,
      default: () => [],
    },
  },
  mounted() {
  },
  methods: {
	  toPage(id) {
		this.$router.push({ name: "Saledetail", query: {id: id} });
	  }
  }
};
</script>

<style lang="scss" scoped>
.scroll-item {
  display: inline-block;
  margin: 9px;
  
  &:hover {
	  box-shadow: 0 0 2px #999;
	  cursor: pointer;
  }
}
</style>
