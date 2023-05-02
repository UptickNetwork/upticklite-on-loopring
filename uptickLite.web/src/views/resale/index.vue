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
  <div class="resale" :class="{mobile: isMobile}">
    <div class="top">
      <SearchInput class="search" style="margin-bottom:-20px" @SearchChange="SearchChange" />
      <div class="top-right">
        <SortSelect v-if="!isMobile && showModel == 'nft'" :list="selectList1" v-model="activeSort1" />
        <SortSelect v-if="!isMobile" :list="selectList2" v-model="activeSort2" />
         <img  :class="[rotate?'go':'']" class="refresh" src="@/assets/icons/icon_refresh.png"  @click="refresh" />
      </div>
    </div>

    <div class="main">
      <div class="collect">
        <div class="card" v-for="(item, index) in nfts" :key="index">
          <Card type="sell" :hash="item.hash" :value="item" owerfrom="resale" />
        </div>
      </div>
    </div>
    
  	<v-dialog v-model="opencard">
	  <carousel class="otherpage" :list="nfts" v-if="opencard" @getOpen="OpenCard"></carousel>
	</v-dialog>
  </div>
</template>

<script lang="js">
import CardList from "@/components/workCard/CardList.vue";
import SortSelect from '@/components/SortSelect.vue';
import api from "@/api"
import Carousel from "@/components/carousel";
import { mapState } from 'vuex'
import SearchInput from '@/components/SearchInput.vue'

import CollectCard from '@/components/workCard/CollectCard.vue';
import Card from "@/components/workCard/Card.vue";

let last_time = 0;

export default {
  name: 'Marketplace',
  components: { CardList, SortSelect, Carousel, SearchInput, CollectCard, Card },
  props: {},
  data: function () {
    return {
      selectList1: [
        { value: "", label: "All" },
      ],
      activeSort1: sessionStorage.getItem('activeSort1') ? sessionStorage.getItem('activeSort1') : "",

      selectList2: [
        { value: 1, label: "Latest" },
        { value: 2, label: "Random" },
      ],
      activeSort2: sessionStorage.getItem('activeSort2') ? Number(sessionStorage.getItem('activeSort2')) : 1,
      nfts: [],
      pageNumber: 1,
      pageSize: 20,
      totalPage: false,
      isShowLoading: false,
      showModel: "nft", // collection
      nftImage: require("@/assets/icons/icon_cj_nft.png"),
      collectionImage: require("@/assets/icons/icon_cj_collection_g.png"),
       opencard: false,
       rotate: false
    };
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
    handler: function () {
      return {
        activeSort1: this.activeSort1,
        activeSort2: this.activeSort2,
      }
    },

  },
  watch: {
    handler: {
      handler: function () {
        clearTimeout(last_time);
        last_time = setTimeout(() => {
          this.nfts = [];
          this.collects = []
          this.pageNumber = 1;
          this.getColleectList();
        }, 1000)
      },
      deep: true
    }
  },
  methods: {
    loadChain() {
      let chainToken = localStorage.getItem("ChainToken");
      if(chainToken) {
          let coinData = JSON.parse(chainToken);
          coinData.forEach(ele => {
            this.selectList1.push({ value: ele.name, label: ele.name });
          });
      }
    },
    SearchChange(val) {
      clearTimeout(last_time);
      last_time = setTimeout(() => {
        this.nfts = [];
        this.collects = []
        this.pageNumber = 1;
        this.getColleectList(val);
      }, 600)
    },
    mode_click(mode) {
      this.showModel = mode;
      mode == "nft" ? this.nftImage = require("@/assets/icons/icon_cj_nft.png") : this.nftImage = require("@/assets/icons/icon_cj_nft_g.png");
      mode == "collection" ? this.collectionImage = require("@/assets/icons/icon_cj_collection.png") : this.collectionImage = require("@/assets/icons/icon_cj_collection_g.png");
      this.collects = []
      this.nfts = []
      this.getColleectList();
    },
    imgClick(item) {
      this.$router.push({ name: "CollectionPage", query: { tokenAddress: item.address } });
    },
    refresh() {
      this.rotate = true;
      setTimeout(() => { this.rotate = false }, 2000);
      this.nfts = []
      this.pageNumber = 1;
      this.getColleectList();

    },
    async getColleectList(search) {
      // sessionStorage.setItem('activeTab',this.activeTab)
      sessionStorage.setItem('activeSort1', this.activeSort1)
      sessionStorage.setItem('activeSort2', this.activeSort2)

      let params = {
        pageNumber: this.pageNumber,
        pageSize: this.pageSize,
        tokenType: this.activeSort1,
        search: encodeURIComponent(search || ""),
        sort: this.activeSort2,
        nftId: '',
        tokenAddress: ''
      };
      this.isShowLoading = true;
      return api.home.getResaleList(params).then(res => {
        let { list, totalPage } = res.data;
        this.totalPage = totalPage;
        this.nfts = this.nfts.concat(list);
        this.isShowLoading = false;
        if (this.nfts.length > 0) {
          this.showRightBottom = true
        } else {
          this.showRightBottom = false
        }
      })
    },
    async onScroll(e) {
      const { scrollTop, clientHeight, scrollHeight } =
        e.target.scrollingElement;
      if (scrollTop + clientHeight === scrollHeight && (this.totalPage > this.pageNumber)) {
        this.pageNumber++;
        await this.getColleectList();
      }

    },
    openCarousel() {
      this.opencard = true;
    },
    OpenCard(e) {
      this.opencard = e;
    },
    onLangChange() {
      this.selectList = [
        { value: 1, label: this.$t('marketType1') },
        { value: 2, label: this.$t('marketType2') },
        { value: 3, label: this.$t('marketType3') },
        { value: 4, label: this.$t('marketType4') },
        { value: 8, label: this.$t('marketType8') },
        { value: 9, label: this.$t('marketType9') },
        { value: 5, label: this.$t('marketType5') },
        { value: 6, label: this.$t('marketType6') },
      ];
      this.isRefsh++;
    }
  },
  mounted() {
    window.eventBus.$on('LangChange', this.onLangChange);
    this.activeSort1 = ''
    this.activeSort2 = 1;
    this.getColleectList()
    this.loadChain();
  },
};
</script>

<style lang="scss" scoped>
.top {
    ::v-deep.sort-select .rd.v-chip {
    box-shadow: none;
    border: none;
    margin-bottom: 2px;
    font-weight: bold;
  }
  position: relative;

  .search {
    width: 400px;
  }
    .refresh{
  height: 18px;
  width: 18px;
  margin-left: 15px;
}

  .top-right {
    width: 220px;
    top: 0px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: absolute;
    right: 0;
    bottom: 0;

    img {
      cursor: pointer;
    }

    .nft {
      margin-left:20px;
      width: 22px;
      height: 28px;
    }

    .collection {
      width: 25px;
      height: 25px;
      margin-left: 30px;
    }
  }
}
.right_bottom {
  position: fixed;
  bottom: 120px;
  right: 50px;
  z-index: 200;

  img {
	display: block;
	width: 59px;
	height: 59px;
	margin-top: 35px;
  }
}
.main {
  margin: 20px 0;

  .collect {
    display: flex;
    flex-wrap: wrap;

    .card {
      margin-top: 16px;
      margin-bottom: 16px;
      margin-right: 30px;
      &:nth-child(5n) {
        margin-right: 0;
      }
    }
  }
}


.resale.mobile {
  .top {
    display: flex;
    align-items: center;
    height: 50px;

    .search {
      margin-bottom: unset !important;
    }

    .top-right {
      width: unset;
      position: unset;
      margin-top: 16px;
    }

    .collection {
      margin-left: 20px;
    }

  }

  .collect {
    .card {
      width: 100%;
      margin-right: unset;
    }
  }
}
</style>
