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
  <div :class="{ contain: true, mobile: isMobile }">
    <input
      :placeholder="this.$t('creatorSearch')"
      class="search-inpt mb-11 pa-5"
      v-model="ipt"
    /> 
    <div class="createlist" >
      <avatarlist :follow-list="followList" v-scroll="onScroll" :isMsg="isMsg"  :isStatus="isStatus"></avatarlist>
    </div>
  </div>
</template>

<script>
import Avatarlist from "./components/avatarlist";
import api from "@/api"

export default {
  name: "fans",
  components: { Avatarlist },
  data: () => ({
    isMsg: false,
    isStatus:true,
    followList: [],
    pageNumber: 0,
    pageSize: 10,
    totalPage: false,
    ipt: "",
  }),
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
      handler: function () {
         return {
            ipt: this.ipt,
         }
      }
  },
   watch: {
      handler : {
         handler: function () {
         this.pageNumber=0;
         this.getCreatorList()
      },
      deep: true
      }
   },
  created(){
    this.getCreatorList();
  },
  methods: {
   async getCreatorList(){
     this.pageNumber++;
     let params = {
       pageSize: this.pageSize,
       pageNumber: this.pageNumber,
      did: this.$store.state.did,
      searchKey: this.ipt,
      
      };
      let res = await api.home.createList(params);
     let list = res.data.list;
     this.followList = this.followList.concat(list);
     this.totalPage = res.data.totalPage;
    },
    onScroll(e) {
      const {
        scrollTop,
        clientHeight,
        scrollHeight,
      } = e.target.scrollingElement;
	  
      if (
          scrollTop + clientHeight == scrollHeight &&
          this.totalPage > this.pageNumber
      ) {
        this.getCreatorList();
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.contain {
  .search-inpt {
    width: 600px;
    height: 41px;
    background-color: #f0f0f0;
    border-radius: 20px;
    outline-color: #f0f0f0;
  }
  .title {
    width: 129px;
    height: 21px;
    font-weight: bold;;
    font-size: 25px;
    font-weight: bold;
    font-stretch: normal;
    line-height: 50px;
    letter-spacing: 0px;
    color: #270645;
  }
  &.mobile {
    width: 100%;
    .search-inpt {
      width: 100%;
    }
    .createlist {
      width: 100%;
    }
  }
}
</style>
