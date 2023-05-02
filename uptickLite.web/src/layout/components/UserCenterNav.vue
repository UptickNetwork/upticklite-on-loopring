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
  <v-list nav dense color="#fff">
    <v-list-item exact v-for="item in userRuter" :key="item.value" color="#fff"
      @click="change(item.value)">
      <v-list-item-content>
        <v-list-item-title>
          {{ item.label }}
          <template v-if="item.value === 'Message' && newMsgCount > 0">
            <span class="star"></span>
          </template>
        </v-list-item-title>
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>

<script lang="js">
import api from "@/api"
import { killSession  } from "@/walletConnect";
export default {
  name: 'UserCenterNav',
  props: {},
  data: function () {
    return {
      newMsgCount: 0
    };
  },
  computed: {
    userRuter: function () {
      return [
        { label: this.$t('headerUserCenterNavMyHomePage'), value: 'PersonalPage' },
        { label: this.$t('headerUserCenterNavEditProfile'), value: 'MyInfo' },
        { label: this.$t('headerUserCenterNavMyOrders'), value: 'MyOrders' },
        { label: "My Loopring", value: 'MyLoopring' },
        { label: this.$t('headerUserCenterNavdisConnect'), value: 'DisConnect' },
      ]

    }

  },
  async mounted() {
    window.eventBus.$on('LangChange', this.onLangChange);

    if (this.$store.state.did != null) {
      let param = { address: this.$store.state.did };
      let res = await api.home.loopringUserInfo(param);
  

    }
  },
  methods: {
    change: function (v) {
      if (v == "DisConnect") {
     localStorage.clear();
		sessionStorage.clear();
        this.$store.commit('SET_DID', "");
        window.bscAddress = "";
		 killSession();
        this.$router.push({ name: 'Home' })
        window.location.reload();
		
		
      } else if (v == "MyLoopring") {
        window.open("https://loopring.io/#/layer2/");
      } else {
        this.$router.push({ name: v });
      }
    },
    onLangChange() {
      this.userRuter = [
        { label: this.$t('headerUserCenterNavMyHomePage'), value: 'PersonalPage' },
        { label: this.$t('headerUserCenterNavEditProfile'), value: 'MyInfo' },
        { label: this.$t('headerUserCenterNavMyOrders'), value: 'MyOrders' },
        { label: "My Loopring", value: 'MyLoopring' },
        { label: this.$t('headerUserCenterNavdisConnect'), value: 'DisConnect' },

      ]
    }
  }
};
</script>

<style lang="scss" scoped>
.v-list-item--active {
  background: #270645;

  .v-list-item__title {
    color: #fff;

  }
}

.star {
  position: relative;

  &::after {
    content: ".";
    font-size: 60px;
    color: red;
    position: absolute;
    right: -20px;
    top: -15px;
  }
}
</style>
