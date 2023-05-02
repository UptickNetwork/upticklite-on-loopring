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
  <v-text-field
    class="search-inpt1"
    :value="searchInput"
    @input="change"
    hide-details
    hide-spin-buttons
  >
    <template v-slot:prepend-inner
      ><img class="prepend-inner" src="@/assets/icons/search.png" alt
    /></template>
  </v-text-field>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "searchInput",
  computed: {
    ...mapState({
      searchInput: (state) => state.searchInput,
    }),
  },
  watch: {
  },
  methods: {
    change: function (v) {
      this.$emit("change", v);
      this.$store.dispatch("setSearchInput", v);
    },
  },
};
</script>

<style lang="scss">
.search-inpt1 {
     align-self: center;
     margin-bottom: 30px;
  .prepend-inner {
    width: 17px;
    height: auto;
  }
  &.v-text-field.v-input--is-focused > .v-input__control > .v-input__slot:after {
       content: none!important;
  }
  .v-input__control {
    .v-input__slot::before {
      content: none;
    }
  }
  .v-input__slot {
    width: 400px;
    height: 40px;
    align-items: center;
    background-color: #f0f0f0;
    border-radius: 20px;
    outline-color: #f0f0f0;
    .v-input__prepend-inner {
      align-self: center;
      margin-top: 0;
      margin-left: 14px;
      margin-right: 13px;
      padding-right: 0;
    }
    .v-text-field__slot {
      flex: 1;
      padding-right: 20px;
      width: unset;
      input {
        width: auto;
      }
    }
  }
}
</style>
