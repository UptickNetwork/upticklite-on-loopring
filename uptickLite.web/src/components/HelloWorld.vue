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
    HelloWorld
    <v-btn block> Block Button </v-btn>
    <v-btn v-if="$vuetify.breakpoint.mobile"> isMobile </v-btn>
    <br />
    <svg-icon iconClass="s" />
    s: <v-icon v-text="$vuetify.icons.s" />
    <br />
    i8n: {{ $t("key") }}
  </div>
</template>

<script>
export default {
  name: "HelloWorld",

  data: () => ({}),
};
</script>
