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
  <div class="sort-select">
    <v-menu v-model="show" right offset-y nudge-bottom="10px">
      <template v-slot:activator="{ on, attrs, value: v }">
        <Chip v-bind="attrs" v-on="on">
          <span>{{ name }}</span>
          <v-icon class="ml-2" color="primary" v-if="v">mdi-menu-up</v-icon>
          <v-icon class="ml-2" color="primary" v-else>mdi-menu-down</v-icon>
        </Chip>
      </template>
      <List :list="list" :value="value" @input="input"> </List>
    </v-menu>
  </div>
</template>

<script lang="js">
import Chip from '@/components/Chip.vue';
import List from '@/components/List.vue';

export default {
  name: 'SortSelect',
  components: { Chip, List },
  props: {
      value: [String, Number],
      list: {
        type: Array,
        default: () => []
      }
  },
  data: function() {
    return {
        show: false
    };
  },
  computed: {
    selected: function() {
      return this.list.find(v =>
	   v.value === this.value);
    },
    name: function() {
      return this.selected ? this.selected.label :  '';
    }
  },
  methods: {
    input:function (e) {
      this.$emit('input',  e)
    }
  }
};
</script>

<style lang="scss" scoped>
.sort-select {
    margin-left: auto;
}
</style>
