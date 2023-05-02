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
  <div class="bookmarks-tabs">
    <v-item-group v-model="active" class="d-flex" mandatory>
      <v-item v-for="(v, i) in list" :key="i" v-slot="{ active, toggle }">
        <v-btn class="tab-action mr-4 mr-lg-6 flex-grow-lg-1" :class="{'item-active':tabIdx==i}" :input-value="active" @click="toggle();btnclick(i);">
          {{ v }}
        </v-btn>
      </v-item>
    </v-item-group>
    <v-window v-model="active">
      <v-window-item v-for="(v, i) in list" :key="i">
        <slot :index="i" />
      </v-window-item>
    </v-window>
  </div>
</template>

<script lang='js'>

export default {
  name: 'BookmarksTabs',
  props: {},
  data: function() {
    return {
        active: "",
        list: [this.$t("Collectibles"), this.$t("MyCreations")],
        tabIdx: 0,
	
    }
  },
  mounted() {
      window.eventBus.$on('LangChange', this.onLangChange);

      let i = sessionStorage.getItem("PersonalPage_Tabs");
      if(i == 1) {
          this.active = parseInt(i);
          this.btnclick(this.active);
          sessionStorage.removeItem("PersonalPage_Tabs");
      }
	 
  },
  methods: {
      btnclick(i) {
          this.$emit("onTabsClick",i);
          this.tabIdx = i;
      },
      onLangChange() {
        this.list = [this.$t("Collectibles"), this.$t("MyCreations")];
      }
  },
};

</script>

<style lang='scss' scoped>
.bookmarks-tabs {
  .tab-action {
    height: unset;
    font-size: 23px;
    letter-spacing: 0px;
    background-image: linear-gradient(#ffffff, #ffffff),
      linear-gradient(#270645, #270645);
    background-blend-mode: normal, normal;
    box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.17);
    border-bottom: none;
    border-radius: 20px;
    border-bottom-left-radius: unset;
    border-bottom-right-radius: unset;
	color: #766983;
    padding: 10px 18px;
     font-weight: bold;
      font-family: Helvetica;font-weight: bold;
  }
  
  .icon {
	  width: 30px;
	  height: 30px;
	  background-size: 100%;
  }
    
  .item-active {
      color: #1d42ff !important;
      font-weight: bold;
      font-family: Helvetica;font-weight: bold;
  }
  .v-btn:before {
      background-color: #fff;
  }
  
}
</style>
