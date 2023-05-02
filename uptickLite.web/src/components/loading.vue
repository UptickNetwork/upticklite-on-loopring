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
      <div v-if="isShowLoading" class="load py-lg-16 d-flex flex-column">
         <img src="@/assets/loading.gif" alt=""/>
         <h1>{{ $t('loadingLoading') }}...</h1>
      </div>
   </div>
</template>

<script>
  export default {
    name: "loading.vue",
    props: {
      isShowLoading: {
        type: Boolean
      },
    
    },
    data: () => ({}),
    computed: {
      isMobile: function() {
        return this.$vuetify.breakpoint.mobile;
      }
    }
  };
</script>

<style lang="scss" scoped>
   .contain {
      
      .load {
         img {
            display: block;
            margin: 0 auto;
            width: 42px;
            height: 42px;
         }

         h1 {
            margin: 30px auto;
         }
      }
   }

</style>
