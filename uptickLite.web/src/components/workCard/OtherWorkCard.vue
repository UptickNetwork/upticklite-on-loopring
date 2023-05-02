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
    <v-card-title class="d-block text--title pt-4 px-5 pb-2 text-truncate"
      >{{value.name}}</v-card-title
    >
    <v-card-text v-if="value.useType == 1" class="d-flex flex-row justify-space-between py-4 px-5 " style="min-height: 70px">
    </v-card-text>
    <v-card-text  v-else   class="d-flex flex-row px-5 pt-7 pb-5 text-number">
        <div >
                  <v-img :src="$walletIcon" alt="logo" width="22px" height="22px"/>
          </div>
      <div class="ml-2 text--number text--primary-rd font-weight-medium" style="line-height:22px;">
        {{value.minPrice}}
      </div>
    </v-card-text>

  </div>
</template>

<script lang='js'>
export default {
  name: 'SellCardContent',
  props: {
    value: {
      type: Object,
      default: () => {}
    }
  },
  data: function() {
    return {
      isStart: false,
      now: "",
      start: "",
      end: "",
      day: 0,
      hour: 0,
      minutes: 0,
      second: 0,
      seconds: "",
    };
  },
  mounted() {
    this.Time(); 
  },
  methods: {
    getTime() {
      this.now = Date.parse(new Date());
    },

    countDown() {
      let d = parseInt(this.seconds / (24 * 60 * 60));
      this.day = d < 10 ? "0" + d : d;
      let h = parseInt(((this.seconds / (60 * 60)) % 24) + d * 24);
      this.hour = h < 10 ? "0" + h : h;
      let m = parseInt((this.seconds / 60) % 60);
      this.minutes = m < 10 ? "0" + m : m;
      let s = parseInt(this.seconds % 60);
      this.second = s < 10 ? "0" + s : s;
    },

    Time() {
      setInterval(() => {
        if (this.seconds > 0) {
          this.seconds -= 1;
          this.countDown();
        }else{
          this.getTime();
        }
      }, 1000);
    },
  },
};

</script>

<style lang='scss' scoped>
.text--primary-rd {
  color: #270645;
}
.text--secondary-rd {
  color: #766983;
}
.text--title {
  font-size: 20px;
  line-height: 24px;
}
.text--sub-title {
  font-size: 12px;
  line-height: 1;
}
.text--number {
  font-size: 15px;
  line-height: 1;
}
.v-divider {
  border-color: #e3e3e3;
}
</style>
