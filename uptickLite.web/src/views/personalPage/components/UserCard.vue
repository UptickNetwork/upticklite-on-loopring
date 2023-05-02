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
  <div :class="{ contain: true, mobile: isMobile }">
    <div
      class="user-card d-flex flex-column align-center justify-center"
      style="cursor: pointer"
      @click="click"
    >

      <Avatar size="70" :hash="value.creatorPhoto" @click="click" />
      <div class="d-flex flex-column align-center">
        <div class="font-weight-medium">{{ value.creatorName ? value.creatorName  :value.creator.substr(0, 6) + "..." + value.creator.substr(-6) }}</div>
      </div>
      <div class="font-weight-medium">Listed {{ value.amount }}</div>
    </div>

    <v-dialog class="px-50" v-model="dialog" width="1040">
      <v-card
       id="box"
        class="px-10 overflow-auto"
        height="770"
        style="padding-top: 40px; position: relative"
      >
        <v-icon
          color="primary"
          style="top: 16px; right: 19px; position: absolute"
          size="30"
          @click="dialog = false"
        >
          mdi-close-circle</v-icon
        >
        <!-- <Cards :cards="cards" /> -->
        <CardList
          :list="list"
           v-scroll:#box="onScroll"
          type="page"
          :owerfrom="owerfrom"
          :isMinPrice="isMinPrice"
        />
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang='js'>

import CardList from "@/components/workCard/CardList.vue";
import Avatar from '@/components/avatar/index.vue';
import api from "@/api"


export default {
  name: 'UserCard',
  components: { CardList, Avatar },
  props: {
    value: Object,
    owerfrom: String,
    isMinPrice: String
  },
  data: function () {
    return {
      dialog: false,
      list: [],
      pageNumber: 0,
      pageSize: 20,
      totalPage: false,
	    sort:1
    };
  },
  filters: {
    didfilter: function (value) {
      if (value && value.length > 12) {
        return value.substr(0, 6) + "..." + value.substr(-6);
      } else {
        return "";
      }
    }
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  methods: {
    click: function () {
      this.dialog = true;
      this.getList()
    },
    async getList() {
		if(!this.$store.state.did){
			return
		}
      this.pageNumber++;
      let params = {
        pageSize: this.pageSize,
        pageNumber: this.pageNumber,
        owner: this.$store.state.did,
        creator: this.value.creator,
		    sort:1
      };
      let res = await api.home.getNftOwn(params);
      let { list, totalPage } = res.data;
      this.totalPage = totalPage;
      this.list = this.list.concat(list);
    },
    onScroll(e) {
         const {scrollTop, clientHeight, scrollHeight} =
           e.target;
         if (scrollTop + clientHeight + 10 >= scrollHeight && (this.totalPage > this.pageNumber)) {
            this.getList();
         }
      },
  }
};

</script>

<style lang='scss' scoped>
.contain {
  height: auto;

  .user-card {
    width: 180px;
    height: 206px;
    background-color: #ffffff;
    border-radius: 5px;
    border: solid 1px #cccccc;

    &:hover {
      border: solid 2px #1d42ff;
    }

    .font-weight-medium {
      margin: 5px;
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
    }
  }

  &.mobile {
    width: 100%;
  }
}
</style>
