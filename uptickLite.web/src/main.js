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
import App from "./App.vue";
 //import "./registerServiceWorker";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import i18n from "@/plugins/i18n";

import * as filters from "./filters";

import "@/styles/index.scss";
import VueVideoPlayer from 'vue-video-player'
import 'video.js/dist/video-js.css'

import DatetimePicker from "vuetify-datetime-picker";
import toastInit from '@/components/toast';

import { init as extendInit} from "@/utils/extend";
import { init as loopringInit} from "@/loopring";

window.eventBus = new Vue();

extendInit();
loopringInit();

import {timestampToDate,timestampToDateTime,timestampToTimeHour,timestampToTimeMinute,timestampToTimeSecond } from "@/utils/helper.js"
Vue.prototype.timestampToDate = timestampToDate;
Vue.prototype.timestampToDateTime = timestampToDateTime;
Vue.prototype.timestampToTimeHour = timestampToTimeHour;
Vue.prototype.timestampToTimeMinute = timestampToTimeMinute;
Vue.prototype.timestampToTimeSecond = timestampToTimeSecond;


import uToast from "@/components/uToast.vue";
Vue.component("uToast", uToast);

// import basePage from "@/layout/BasePage.vue";
import uComponents from "@/components/uComponents.vue"
Vue.component("uComponents", uComponents);

import promptBox from "@/components/PromptBox.vue"
Vue.component("promptBox", promptBox);

//     type:success,error
Vue.prototype.$toast = function(type, txt, timer) {
    return this.$refs.ucom.toast(type, txt, timer);
}

Vue.prototype.$confirm = function(txt, ps) {
    this.$refs.ucom.confirm(txt, ps);
}

import messageTip from "./components/MessageTip"
messageTip(Vue);

import unlockDlg from "./components/UnlockDlg"
unlockDlg();

toastInit(Vue, {
  property: '$toastFn'
})

import ipfsServer from "@/api/ipfsServer";
ipfsServer();

Vue.prototype.getSessionStr = function(key) {
  let val = sessionStorage.getItem(key);
  return val;
}

Vue.prototype.getSessionObj = function(key) {
  let val = sessionStorage.getItem(key);
  if(val) {
    let obj = JSON.parse(val);
    return obj;
  }
  return null;
}

Vue.use(DatetimePicker);


Vue.use(VueVideoPlayer)

Vue.config.productionTip = false;

Object.keys(filters).forEach((key) => {
  Vue.filter(key, filters[key]);
});

new Vue({
  router,
  store,
  i18n,
  vuetify,
  render: (h) => h(App),
  async mounted() {

  },
}).$mount("#app");
