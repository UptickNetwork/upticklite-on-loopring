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
import Vue from "vue";
import Vuetify from "vuetify/lib/framework";
import IconFont from "@/components/iconfont";
import i18n from "@/plugins/i18n";

Vue.use(Vuetify);

Vuetify.config.silent = false;

export default new Vuetify({
  icons: {
    values: IconFont,
  },
  lang: {
    t: (key, ...params) => {
      if (key === '$vuetify.noDataText') {
        return ''
      }
      if (key === '$vuetify.timePicker.prevMonthAriaLabel') {
        return 'prev'
      }
      if (key === '$vuetify.timePicker.nextMonthAriaLabel') {
        return 'next'
      }
      if (key === '$vuetify.timePicker.am') {
        return 'am'
      }
      if (key === '$vuetify.timePicker.pm') {
        return 'pm'
      }
      if (key === '$vuetify.dataFooter.itemsPerPageText') {
        return ''
      }
      if (key === '$vuetify.dataFooter.pageText') {
        return ''
      }
      if (key === '$vuetify.dataFooter.itemsPerPageAll') {
        return 'All'
      }
      return i18n.t(key, params);
    },
  },
  breakpoint: {
    thresholds: {
      sm: 640,
      lg:900,
    },
    mobileBreakpoint: 900,
    scrollBarWidth: 24,
  },
  theme: {
    themes: {
      light: {
        primary: "#270645",
        secondary: '#766983',
      },
      dark: {
        primary: "#fff",
      },
    },
  },
});
