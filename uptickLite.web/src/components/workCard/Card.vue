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
  <v-card class="rd elevation-0">
    <Work :hash="hash" :attribute="attribute" :fileUrl="fileUrl" @imgClick="workClick" />
    <SellCardContent v-if="type === 'sell'" :value="value" :isMinPrice="isMinPrice" />
    <MyCardContent v-else-if="type === 'my'" :value="value" />
    <CollectCardContent v-else-if="type === 'collect'" :value="value" />
    <PageCardContent v-else-if="type === 'page'" :value="value" />
    <OtherWorkCard v-else-if="type === 'other'" :value="value" />
    <MycollectCard v-else-if="type === 'mycollect'" :value="value" />
    <slot></slot>
  </v-card>
</template>

<script>
import Work from "@/components/work/index.vue";
import SellCardContent from "./SellCardContent.vue";
import MyCardContent from "./MyCardContent.vue";
import CollectCardContent from "./CollectCardContent.vue";
import PageCardContent from "./PageCardContent.vue";
import OtherWorkCard from "./OtherWorkCard.vue"
import MycollectCard from "./MycollectCard.vue"

export default {
  name: "Card",
  components: { OtherWorkCard, Work,SellCardContent, MyCardContent, CollectCardContent, PageCardContent, MycollectCard },
  props: {
    value: {
      type: Object,
    },
    type: {
      type: String,
    },
    owerfrom: {
      type: String,
    },
    favorite: {
      type: String
    },
    isMinPrice: {
      type: String
    },
    pagetype: {
      type: String
    }
  },
  data: () => ({
    loading: false,
    selection: 1,
  }),
  computed: {
    hash: function () {
      return this.value && this.value.imgUrl;
    },
    attribute: function () {
      return this.value && this.value.attribute;
    },
    fileUrl: function () {
      return this.value && this.value.fileUrl;
    },
  },
  methods: {
    reserve() {
      this.loading = true;
      setTimeout(() => (this.loading = false), 2000);
    },
    workClick() {
      if (this.owerfrom == "personalPage" || this.owerfrom == "my") {
        this.$router.push({ name: "Carddetail", query: { from: this.owerfrom, tokenAddress: this.value.contractAddress, nftId: this.value.nftId, assetId: this.value.id } });
      } else if (this.owerfrom == "myCreations") {
          this.$router.push({ name: "Saledetail", query: { id: this.value.id } });
      }
      else {
        let query = { };
        // if (this.owerfrom == "resale") {
        //   query.tokenAddress = this.value.contractAddress;
        //   query.id = this.value.id;
        // } else {
          query.tokenAddress = this.value.contractAddress;
          query.nftId = this.value.nftId;
           query.owner = this.value.owner
        
        this.$router.push({ name: "Saledetail", query });
      }
    },
    forceUpdate() {
      this.$forceUpdate();
    }
  },
};
</script>


<style lang="scss" scoped>
.v-card.rd {
  overflow: hidden;
  width: 100%;
  min-height: 300px;
  background-image: linear-gradient(#ffffff, #ffffff),
    linear-gradient(#6f58d9, #6f58d9);
  background-blend-mode: normal, normal;
  border-radius: 5px;
  border: 1px solid #cccccc;

  &:hover {
    border-radius: 5px;
    border: solid 2px #1d42ff;
  }

  @media screen and (min-width: #{map-get($grid-breakpoints, 'lg')}) {
    width: 220px;
    background-image: linear-gradient(#ffffff, #ffffff),
      linear-gradient(#ffffff, #ffffff);
    background-blend-mode: normal, normal;
    border-radius: 5px;
  }
}
</style>
