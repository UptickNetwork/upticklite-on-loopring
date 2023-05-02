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
   <div :class="{mobile: isMobile}">
      <div class="card-List" :class="{clearDisplay:owerfrom==='personalPage'}">
         <div class="card-box" v-for="(item, i) in list" :key="i" @click="toPage(item)">
            <Card :isMinPrice="isMinPrice" :value="item" :type="type" :owerfrom="owerfrom"/>
         </div>
      </div>
      <loading :isShowLoading="isShowLoading" ></loading>
   </div>
</template>

<script lang="js">
  import Card from "@/components/workCard/Card.vue";
  import Loading from "@/components/loading.vue";

  export default {
    name: "CardList",
    components: { Loading, Card },
    props: {
      list: {
        type: Array,
        default: () => []
      },
      type: String,
      isShowLoading: {
        type: Boolean
      },
  
      owerfrom: String,
      isMinPrice:String
    },
    data: function() {
      return {
      };
    },
    computed: {
		isMobile: function () {
			return this.$vuetify.breakpoint.mobile;
		}
	},
    methods: {
      toPage(item) {
      }
    }
  };
</script>

<style lang="scss" scoped>
   .card-List {
      padding-bottom: 10px;
      display: grid;
      width: 100%;
      // grid-gap: 32px;
      .card-box {
         //  min-width: 0 to fix grid && white-space
         min-width: 0;
         display: inline-block;
         margin: 9px;

         &:hover {
            box-shadow: 0 0 2px #1d42ff;
            cursor: pointer;
         }
      }
      @media screen and (max-width: 500px ) {
         grid-template-columns: repeat(1, 1fr);
         .v-card {
            width: auto;
            width: unset;
         }
      }

      @media screen and (min-width: #{map-get($grid-breakpoints, 'md')}) {
         grid-template-columns: repeat(2, 1fr);
         .v-card {
            // width: unset;
         }
      }
      @media screen and (min-width: 900px) {
         grid-template-columns: repeat(3, 1fr);
      }
      @media screen and (min-width: #{map-get($grid-breakpoints, 'lg')}) {
         grid-template-columns: repeat(5, 1fr);
      }
   }

   .clearDisplay {
      display: block !important;
   }

   .mobile {
      .card-box {
         display: unset;
      }
   }
</style>
