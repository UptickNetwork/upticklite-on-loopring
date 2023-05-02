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
    <CardList :list="list" v-scroll="onScroll" type="sell" :isShowLoading="isShowLoading" />
  </div>
</template>

<script>
import CardList from "@/components/workCard/CardList.vue";

import api from "@/api";

export default {
  name: "Gallery",
  components: { CardList },
  data: function () {
    return {
      list: [],
      pageNumber: 0,
      pageSize: 8,
      totalPage: false,
      isShowLoading: false,
     
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      let params = {
        chainType: "",
      };
      this.pageNumber++;
      this.isShowLoading = true;

      return api.home
        .getGalleryList({
          ...params,
          pageNumber: this.pageNumber,
          pageSize: this.pageSize,
        })
        .then((response) => {
          const { list, totalPage } = response.data;
          this.pageNumber;
          this.totalPage = totalPage;
          if (this.pageNumber == 1) {
            this.list = list;
          } else {
            this.list = this.list.concat(list);
          }
          this.isShowLoading = false;
       
        });
    },
    async onScroll(e) {
      const {
        scrollTop,
        clientHeight,
        scrollHeight,
      } = e.target.scrollingElement;
      if (
        scrollTop + clientHeight === scrollHeight &&
        this.totalPage > this.pageNumber
      ) {
        await this.getList();
      } 
   
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
