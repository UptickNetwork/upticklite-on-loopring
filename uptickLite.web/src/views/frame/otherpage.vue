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
  <div :class="{ contain: true, mobile: isMobile }" style="overflow-x: hidden;">
    <header-page @getName="getName"></header-page>
    <div class="card">
      <tabs-page @list="getlist"></tabs-page>
    </div>
    <v-dialog v-model="opencard">
      <carousel class="otherpage" :list="list" v-if="opencard" @getOpen="OpenCard"></carousel>
    </v-dialog>
  </div>
</template>

<script>
import HeaderPage from "./components/headerPage";
import TabsPage from "./components/tabsPage";
import Carousel from "../../components/carousel";
import message from "@/components/popup/message.vue";

export default {
  name: "otherpage",
  components: {Carousel, TabsPage, HeaderPage, message},
  data: () => ({
    opencard: false,
    open: false,
    openmsg: false,
    offset: true,
    pageInfo: {},
    did: "",
    currfriend: {},
    list: []
  }),
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  mounted() {
    this.did = this.$route.query.did;
  },
  methods: {
    getName(e) {
      this.currfriend.fansAddress = this.did;
      this.currfriend.fansName = e
    },
    openCarousel() {
      this.opencard = true;
    },
    OpenCard(e) {
      this.opencard = e;
    },
    msgBtn() {
      this.openmsg = !this.openmsg;
    },
    OpenMsg(e) {
      this.openmsg = e;
    },
    getlist(dt) {
      this.list = dt;
    }
  },
};
</script>

<style lang="scss" scoped>
.contain {
  padding: 0;
  margin: 0;

  .card {
    /*max-height: 804px;*/
    height: auto;

    .right_bottom {
      position: fixed;
      bottom: 50px;
      right: 50px;
      z-index: 200;

      .img {
        display: block;
        width: 59px;
        height: 59px;
        margin-top: 35px;
      }
    }
  }

  &.mobile {
    width: 100%;
  }
}
</style>
