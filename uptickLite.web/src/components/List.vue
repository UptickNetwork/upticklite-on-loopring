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
  <v-list nav dense :light="false">
    <v-list-item-group mandatory :value="value" @change="change" color="#fff">
      <v-list-item v-for="item in list" :value="item.value" :key="item.value" color="#fff" @click="itemclick(item.value)">
        <v-list-item-content>
          <v-list-item-title v-if="$scopedSlots.default">
            <slot :value="item.value" :label="item.label"> </slot>
          </v-list-item-title>
          <v-list-item-title v-else v-text="item.label"></v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list-item-group>
  </v-list>
</template>

<script>
export default {
  name: "List",
  inheritAttrs: false,
  props: {
    list: {
      type: Array,
      defualt: () => [],
    },
    value: [String, Number],
  },
  methods: {
      itemclick(v) {
        this.$emit("input", v);
        this.$emit("change", v);
      },
  },
};
</script>

<style lang="scss" scoped>
.v-list-item.v-item--active {
  background: $primary;
  &::v-deep .v-list-item__title {
		color: #fff;
    
	}
}
.theme--light.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled) {
  color: $primary;
 
}



</style>
