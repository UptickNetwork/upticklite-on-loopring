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
  <div  class="t" :class="{mobile: isMobile}">
       <div class="text-title" v-if="isMobile">View all NFTs of L2 on<a  href="https://loopring.io/#/nft/assetsNFT" target="_blank">「Loopring」</a></div>
        <div class="text-title " v-if="isMobile">Explore more commercial functions,visit<a href="https://loopring.upticknft.com/index" target="_blank">「Uptick on Loopring」</a></div>
      <div class="switch d-flex justify-space-between">
      <div class="image" v-if="activeSort != 4">
        <img class="nft" :src="nftImage" @click="mode_click('nft')" />
        <img
          class="collection"
          :src="collectionImage"
          @click="mode_click('collection')"
        />    
      </div>
      <div class="image" v-else></div>
      <div class="select d-flex flex-row" >
        <div class="text-title" v-if="!isMobile">View all NFTs of L2 on<a  href="https://loopring.io/#/nft/assetsNFT" target="_blank">「Loopring」</a></div>
        <div class="text-title " v-if="!isMobile">Explore more commercial functions,visit<a href="https://loopring.upticknft.com/index" target="_blank">「Uptick on Loopring」</a></div>
        <SortSelect
          class="mr-1 dot mt-lg-1 ml-8"
          :list="selectList"
          v-model="activeSort"
        />
        <img
          :class="[rotate ? 'go' : '']"
          class="refresh"
          src="@/assets/icons/icon_refresh.png"
          @click="refresh"
        />
      </div>
       </div>
    <v-window v-model="activeSort" v-if="showModel == 'nft'">
      <v-window-item value="1">
        <div v-if="empty" class="empty">{{ $t('EmptyHere') }}</div>
        <div class="cards-container mt-n4">
          <CardList :list="list" v-scroll="onScroll" owerfrom="personalPage" type="my" />
          <loading :isShowLoading="isShowLoading"></loading>
        </div>
      </v-window-item>
      <v-window-item value="2">
        <div v-if="empty" class="empty">{{ $t('EmptyHere') }}</div>
        <div class="cards-container mt-n4">
          <CardList :list="list" v-scroll="onScroll" type="my" owerfrom="personalPage" />
          <loading :isShowLoading="isShowLoading"></loading>
        </div>
      </v-window-item>
      <v-window-item value="3">
        <div v-if="empty" class="empty">{{ $t('EmptyHere') }}</div>
        <div class="d-flex flex-wrap user-list">
          <UserCard class="mb-3 user-card" v-for="(item, i) in users" :key="i" :value="item" owerfrom="personalPage"
            isMinPrice="lastprice" />
        </div>
        <loading :isShowLoading="isShowLoading"></loading>
      </v-window-item>
    </v-window>
    
    <div class="collect" v-if="showModel == 'collection'">
      <div class="card" v-for="(item, index) in list" :key="index">
        <collect-card :pageValue="item" :hash="item.imgUrl" type="createorPage" >
         <v-img class="imgdel" :class="{ 'showdel': item.amount == 0 }" src="@/assets/icons/icon_delete.png"
          @click.stop="delclick(item)" />
        <v-btn  v-if="item.owner == did" class="uc createnft" width="150px" height="41px" rounded @click="createNft(item)">+ Create NFT</v-btn></collect-card>
      </div>
    </div>
    <v-dialog v-model="Burning">
         <burn v-if="Burning" :pageValue="burnParams" @getOpen="openBurn" ownerFrom="burnCollect"></burn>
      </v-dialog>

  </div>
  
</template>

<script lang='js'>
import SortSelect from '@/components/SortSelect.vue';
import UserCard from "./UserCard.vue";
import CardList from "@/components/workCard/CardList.vue";
import api from "@/api"
import Loading from "@/components/loading.vue";
import CollectCard from '@/components/workCard/CollectCard.vue';
import Burn from "@/components/popup/burn.vue";

export default {
  name: 'Collections',
  components: { SortSelect, UserCard, CardList, Loading, CollectCard,Burn },
  props: {},
  data: function () {
    return {
      did:'',
      burnParams:{},
      Burning: false,
      activeTab: undefined,
      tabList: [
        { value: "undefined", label: this.$t('otherpageTabsPageAll') },
        { value: "3", label: this.$t('otherpageTabsPageArtwork') },
        { value: "4", label: this.$t('amCreateCollectibles') },
        { value: "5", label: this.$t('createCategoriesPhotography') },
        { value: "7", label: this.$t('amCreateVideo') }
      ],

      activeSort: '2',
      selectList: [
        { value: '2', label: this.$t('ByReceivingTime') },
        { value: '1', label: this.$t('ByCreationTime') },
        { value: '3', label: this.$t('ByCreator') },

      ],
      list: [],
      pageNumber: 1,
      pageSize: 20,
      totalPage: false,
      users: [],
      loading: false,
      isShowLoading: false,
      empty: false,
       rotate:false,
      showModel: "nft", // collection
      nftImage: require("@/assets/icons/icon_cj_nft.png"),
      collectionImage: require("@/assets/icons/icon_cj_collection_g.png"),
    };
  },
  computed: {
    isMobile: function () {
			return this.$vuetify.breakpoint.mobile;
		},
    handler: function () {
      return {
        activeTab: this.activeTab,
        activeSort: this.activeSort,
      }
    }
  },
  watch: {
    handler: {
      handler: function () {
        this.pageNumber = 1;
        this.list = [];
        this.users = [];
           if (this.showModel == "nft") {
        this.getColleectList();
      } else if (this.showModel == "collection") {
        this.getCollectionList();
      }
      },
      deep: true
    }
  },
  mounted() {
    this.did = this.$store.state.did
    window.eventBus.$on('LangChange', this.onLangChange);
     window.eventBus.$on("burnCollect",this.burnCollect);
    this.getColleectList()
  },
  methods: {
    burnCollect(){
      console.log("wxl --- burnCollect")
      setTimeout(() => {
       this.pageNumber = 1;
        this.list = [];
        this.users = [];
          if (this.showModel == "nft") {
        this.getColleectList();
      } else if (this.showModel == "collection") {
        this.getCollectionList();
      }
      }, 5000);
      
    },
    async getColleectList() {
		if(!this.$store.state.did){
			return;
		}
      if (this.loading) {
        return;
      }
      this.loading = true;
      let index = 0;
      if (this.activeTab == 0) {
        index = ''
      } else if (this.activeTab == 1) {
        index = 3
      } else if (this.activeTab == 2) {
        index = 4
      } else if (this.activeTab == 3) {
        index = 5
      } else if (this.activeTab == 4) {
        index = 7
      }
      
      this.isShowLoading = true;
      if (this.activeSort == 3) {
        let params = {
          pageSize: this.pageSize,
          pageNumber: this.pageNumber,
          owner: this.$store.state.did,
        };
        let response = await api.home.getOwnByCreator(params);
        let user = response.data.list;
  
		if(this.pageNumber == 1){
			this.users=user;
		}else{
			      this.users = this.users.concat(user);
		}
        if (this.pageNumber == 1 && user.length == 0) {
          this.empty = true;
        } else {
          this.empty = false;
        }

      } else {
        let params = {
          pageSize: this.pageSize,
          pageNumber: this.pageNumber,
          owner: this.$store.state.did,
          sort: this.activeSort,
        };
        let res = await api.home.getNftOwn(params);
        let { list, totalPage } = res.data;
        this.totalPage = totalPage;
        this.list = this.list.concat(list);
        if ((this.pageNumber == 1 || this.pageNumber == 0) && list.length == 0) {
          this.empty = true;
        } else {
          this.empty = false;
        }

      }
      this.loading = false;
      this.isShowLoading = false;
      this.$emit("list", this.list);
    },
    async getCollectionList() {
      this.loading = true;
      this.isShowLoading = true;

      let params = {
        pageSize: this.pageSize,
        pageNumber: this.pageNumber,
        owner: this.$store.state.did,
        sort: this.activeSort,
      };
      let res = await api.home.getOwnByCollection(params);
      let { list, totalPage } = res.data;
      this.totalPage = totalPage;
      this.list = this.list.concat(list);

      if ((this.pageNumber == 1 || this.pageNumber == 0) && list.length == 0) {
        this.empty = true;
      } else {
        this.empty = false;
      }
      this.loading = false;
      this.isShowLoading = false;
    },
    mode_click(mode) {
      this.showModel = mode;
      mode == "nft" ? this.nftImage = require("@/assets/icons/icon_cj_nft.png") : this.nftImage = require("@/assets/icons/icon_cj_nft_g.png");
      mode == "collection" ? this.collectionImage = require("@/assets/icons/icon_cj_collection.png") : this.collectionImage = require("@/assets/icons/icon_cj_collection_g.png");
      this.pageNumber = 1;
      this.list = [];
      if (mode == "nft") {
        this.getColleectList();
         if(this.selectList.length == 2)
        this.selectList.push(
          {  value: '3', label: this.$t('ByCreator') }
          )
      } else if (mode == "collection") {
        this.getCollectionList();
         if(this.selectList.length == 3){
          this.selectList.pop(this.selectList.length -1);
        }
      }
    },
    refresh(){
     this.rotate=true;
     setTimeout(() => { this.rotate=false }, 2000);
     this.list = []
     this.users = []
    this.pageNumber = 1	
    if (this.showModel == "nft") {
        this.getColleectList();
      } else if (this.showModel == "collection") {
        this.getCollectionList();
      }
    
     
    },
     createNft(item) {
      this.$router.push({ name: "CreateNft", query: { tokenAddress: item.address } });
    },
    delclick(item){
      console.log("wxl --- item",item)
       this.burnParams = item
       this.Burning = true;
      

    },
    
      openBurn(e) {
         if (e == 1) {
            this.Burning = false;
         }
         else {
            this.Burning = e;
         }
      },
    onScroll(e) {
      const { scrollTop, clientHeight, scrollHeight } =
        e.target.scrollingElement;
      if (Math.floor(scrollTop) + clientHeight === scrollHeight && (this.totalPage > this.pageNumber)) {
        this.pageNumber++;
        this.getColleectList();
      }
    },
    onLangChange() {
      this.tabList = [
        { value: "undefined", label: this.$t('otherpageTabsPageAll') },
        { value: "3", label: this.$t('otherpageTabsPageArtwork') },
        { value: "4", label: this.$t('amCreateCollectibles') },
        { value: "5", label: this.$t('createCategoriesPhotography') },
        { value: "7", label: this.$t('amCreateVideo') }
      ];
      this.selectList = [
        { value: '1', label: this.$t('ByReceivingTime') },
        { value: '2', label: this.$t('ByCreationTime') },
        { value: '3', label: this.$t('ByCreator') },

      ];
    }
  },

};

</script>

<style lang='scss' scoped>
.text-title{
      font-size: 12px;
      color: #270645;
          width: 100%;
    text-align: center;
      a{
        color: #1d42ff;
        text-decoration: none;
      }
    }
.t {
  position: relative;

  ::v-deep.sort-select .rd.v-chip {
    box-shadow: none;
    border: none;
    margin-bottom: 2px;
    font-weight: bold;
  }
}

.switch {
  margin-top: 10px;
  height: 65px;
  position: relative;
  .image {
    display: flex;
    align-items: center;
    cursor: pointer;
    .nft {
      width: 22px;
      height: 28px;
    }
  
    .collection {
      width: 25px;
      height: 25px;
      margin-left: 30px;
    }
 
  }
  .select {
      ::v-deep .sort-select .rd.v-chip {
    box-shadow: none;
    border: none;
    margin-bottom: 2px;
    font-weight: bold;
  }
    display: flex;
    align-items: center;
    .clearmode {
      height: 65px;
      .content {
        display: flex;
        align-items: center;
        	font-size: 14px;
          font-weight: bold;
      }
    }
    .text-title{
      width: 350px;
      font-size: 12px;
      color: #270645;
      a{
        color: #1d42ff;
        text-decoration: none;
      }
    }
    .refresh {
      height: 18px;
      width: 18px;
      margin-top: 5px;
      margin-left: 15px;
    }

    .go {
      transform: rotate(360deg);
      transition: all 2s;
      pointer-events: none;
      user-select: none;
    }
  }
}


.empty {
  width: 100%;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.v-tab {
  font-size: 18px;
  border-bottom: 1px solid #e3e3e3;
}

.user-card {
  margin-right: 23px;

  &:last-child {
    margin-right: 0;
  }
}


  .collect {
    display: flex;
    flex-wrap: wrap;
    position: relative;
   
    .card {
      margin-top: 16px;
      margin-bottom: 16px;
      margin-right: 30px;

      &:nth-child(5n) {
        margin-right: 0;
      }

      .imgdel {
    position: absolute;
    right: 18px;
    top: 18px;
    width: 25px;
    height: 25px;
    z-index: 9;
    display: none;
    cursor: pointer;
  }

  .createnft {
    position: absolute;
    bottom: 26px;
   display: none;
  }
  
  &:hover {
    .createnft {
      display: unset;
    }

    .showdel {
      display: unset;
    }
  }
    }
    
  }

  .mobile {
    .switch {
      position: unset;
    }

    .card {
      width: 100%;
    }
  }
</style>
