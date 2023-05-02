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
   <div class="container">
      <div class="title mb-7">{{ $t('headerUserCenterNavMyFavorites') }}</div>
         <CardList :list="list"  v-scroll="onScroll"  type='collect' :isShowLoading="isShowLoading" />
   </div>
</template>

<script>
   import CardList from "@/components/workCard/CardList.vue";
   import api from  "@/api"

   export default {
      components: {CardList},
      data: () => ({
         list: [],
         pageNumber: 0,
         pageSize: 20,
         totalPage: false,
         isShowLoading: false,
      }),
      mounted() {
         this.getColleectList();
      },
      methods:{
         async getColleectList(){
            this.pageNumber++;
            let params={
               followType:'1',
               pageSize:this.pageSize,
               pageNumber:this.pageNumber,
               did:this.$store.state.did
            };
            this.isShowLoading = true;
            let res= await api.home.followList(params);
            let {list,totalPage} = res.data;
            this.totalPage = totalPage;
            this.list = this.list.concat(list);
            this.isShowLoading = false;
         },

         onScroll(e) {
            const { scrollTop, clientHeight, scrollHeight } =
              e.target.scrollingElement;
            if (scrollTop + clientHeight === scrollHeight && (this.totalPage > this.pageNumber)) {
               this.getColleectList();
            }
         },
      },
   };
</script>

<style lang="scss" scoped>
   .container {
      padding: 0;
      margin: 0;

      .title {
         font-weight: bold;;
         font-size: 40px;
         font-weight: bold;
         font-stretch: normal;
         letter-spacing: 0px;
         color: #270645;
      }
   }
</style>
