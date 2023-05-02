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
    <v-menu v-model="show" :attach="$el" right offset-y nudge-bottom="10px">
      <template v-slot:activator="{ on, attrs, value }">
        <Chip v-bind="attrs" v-on="on">
          <span>{{ valueToLabel(name) }}</span>
          <v-icon class="ml-2" color="primary" v-if="value">mdi-menu-up</v-icon>
          <v-icon class="ml-2" color="primary" v-else>mdi-menu-down</v-icon>
        </Chip>
      </template>
      <List :list="chain" @change="change" v-model="currentChaina">
      </List>
    </v-menu>
  </div>
</template>

<script lang="js">
import Chip from '@/components/Chip.vue';
import List from '@/components/List.vue';

export default {
  name: 'ChainSelect',
  components: { Chip, List },
  props: {
      value: String
  },
  data: function() {
    return {
        show: false,
         chain: [ 'Loopring'].map((v) => ({
            value: v,
            label: v,
        })),
        name:'',
        currentChaina: this.valueToLabel(localStorage.getItem('KEY_CHAIN') || 'Loopring')
    };
  },
  methods:{
      change: function (lbl){
        this.labelToValue(lbl);
        localStorage.setItem("KEY_CHAIN", this.name);
        this.setWallet(this.name);
        location.reload();
      },
      setWallet(name) {
      },
      valueToLabel(val) {
          if(val == "UPTICK") {
              return "UPTICK(COSMOS)";
          } else if (val == "UPTICK EVM") {
              return "UPTICK(EVM)";
          } 
          else {
              return "Loopring";
          }
      },
      labelToValue(lbl) {
          if (lbl == "UPTICK(COSMOS)") {
              this.name = "UPTICK";
          } else if(lbl == "UPTICK(EVM)") {
              this.name = "UPTICK EVM";
          } 
          else {
              this.name = "Loopring";
          }
      }
  },
  mounted(){
    this.name="UPTICK EVM"
    let chain = localStorage.getItem("KEY_CHAIN");
    if(!chain) {
        localStorage.setItem("KEY_CHAIN", this.name);
    } else {
        this.name = chain;
    }
    this.setWallet(this.name);
  }
};
</script>

<style lang="scss" scoped></style>
