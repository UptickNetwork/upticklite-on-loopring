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
  <div class="horizontal-container" v-if="!isMobile">
    <div class="scroll-wrapper" ref="scroll">
      <div class="scroll-content">
        <slot />
      </div>
    </div>
  </div>
  <div v-else class="horizontal-container-mobile">
    <div class="scroll-content">
      <slot />
    </div>
  </div>
</template>

<script>
import BScroll from "@better-scroll/core";

export default {
  name: "ScrollX",
  props: {
    config: Object,
  },
  data() {
    return { bs: undefined };
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  updated() {
    this.bs.refresh();
  },
  mounted() {
    this.init();
  },
  beforeDestroy() {
    this.bs.destroy();
  },
  methods: {
    init() {
      this.bs = new BScroll(this.$refs.scroll, {
        scrollX: true,
        ...this.config,
        preventDefault: false,
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.horizontal-container {
  .scroll-wrapper {
    position: relative;
    white-space: nowrap;
    overflow: hidden;
    .scroll-content {
      display: inline-block;
    }
  }
}
.horizontal-container-mobile {
  width: 100%;
  overflow-x: auto;
  white-space: nowrap;
  .scroll-content {
    display: inline-block;
  }
}
::-webkit-scrollbar {
  width: 10px; 
  height: 1px;
}
::-webkit-scrollbar-thumb {
  background-color: #333;
}
::-webkit-scrollbar-track {
  background: transparent;
}
</style>
