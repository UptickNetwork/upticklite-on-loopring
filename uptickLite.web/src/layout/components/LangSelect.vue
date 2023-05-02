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
    <v-menu :attach="$el" right offset-y nudge-bottom="10px">
      <template v-slot:activator="{ on, attrs, value }">
        <Chip v-bind="attrs" v-on="on">
          <span>{{ name }}</span>
          <v-icon class="ml-2" color="primary" v-if="value">mdi-menu-up</v-icon>
          <v-icon class="ml-2" color="primary" v-else>mdi-menu-down</v-icon>
        </Chip>
      </template>
      <List :list="lang" v-model="currentLang">
        <template v-slot:default="{ label }">
          {{ label }}
        </template>
      </List>
    </v-menu>
  </div>
</template>

<script lang="js">
import Chip from '@/components/Chip.vue';
import List from '@/components/List.vue';

const lang = [
  { label: 'English', value: 'en' },
  { label: '中文', value: 'zh' },
  { label: '日本語', value: 'ja' },
  { label: '코리아', value: 'ko' },
  { label: 'Português', value: 'pt' },
];

export default {
  name: 'LangSelect',
  components: { Chip, List },
  props: {
      value: String
  },
  data: function() {
    return {
        lang,
        currentLang: localStorage.getItem('KEY_LANG') || 'en'
    };
  },
  computed: {
    name: function() {
      const current = this.lang.find(v => v.value === this.currentLang);
      return current ? current.label : '';
    }
  },
  watch: {
    currentLang: {
      handler: function(v) {
        this.setLanguage(v);
      },
      immediate: true,
    }
  },
  methods:{
      setLanguage(language) {
        document.documentElement.lang = language;
        localStorage.setItem("KEY_LANG", language);
        this.$i18n.locale = language;
        this.$vuetify.lang.current = language;
        window.eventBus.$emit("LangChange", language);
      },
  },
};
</script>

<style lang="scss" scoped></style>
