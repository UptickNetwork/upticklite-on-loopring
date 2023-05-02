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
  <div class="marketplace" :class="{mobile: isMobile}">
    <div class="top mt-n4">
      <SearchInput class="search" style="margin-bottom:-20px" placeholder="Search by collections" @SearchChange="SearchChange" />
      <div class="top-right">
        <SortSelect v-if="!isMobile" :list="selectList2" v-model="activeSort2" />
         <img  :class="[rotate?'go':'']" class="refresh" src="@/assets/icons/icon_refresh.png"  @click="refresh" />
      </div>
    </div>

    <div class="main">
      <div class="collect">
        <div v-if="collects.length == 0 && !isShowLoading && showNotFond" class="empty d-flex flex-column">
          <div class="notFound">No collections found</div>
          <div class="tryAnother">We couldn't find any collections matching your query. Try another query</div>
        </div>
        <div class="card" v-for="(item, index) in collects" :key="index">
          <collect-card :pageValue="item" :hash="item.imgUrl" type="indexpage" collectsource="firspublish" showAvatar  v-scroll="onScroll">
          </collect-card>
        </div>
          <loading  :isShowLoading="isShowLoading" style="padding-left:500px;"></loading>
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
import Loading from "@/components/loading.vue";
let last_time = 0;
export default {
  name: 'Marketplace',
  components: { CardList, SortSelect, Carousel, SearchInput, CollectCard,Loading},
  props: {},
  data: function () {
    return {
      colors: [
        'primary',
        'secondary',
        'yellow darken-2',
        'red',
        'orange',
      ],
      selectList1: [
        { value: "", label: "All" },
      ],
      selectList2: [
        { value: 2, label: "Latest" },
        { value: 9, label: "Random" },
      ],
      activeSort2: sessionStorage.getItem('launch_activeSort2') ? Number(sessionStorage.getItem('launch_activeSort2')) : 2,
      collects: [],
      nfts: [],
      recommendList:[],
      activeSlide: 0,
      carouselPage: [],
      pageNumber: 1,
      pageSize: 20,
      totalPage: false,
      isShowLoading: false,
      rotate:false,
      opencard: false,
      searchInput:'',
      isBtnEabled: true,
      currfriend_group:{},
      showNotFond:false
    };
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
    handler: function () {
      return {
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
        }, 600)
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
      this.searchInput = val
      clearTimeout(last_time);
      last_time = setTimeout(() => {
        this.nfts = [];
        this.collects = []
        this.pageNumber = 1;
        this.getColleectList();
      }, 600)
    },
    refresh() {
      this.rotate = true;
      setTimeout(() => { this.rotate = false }, 2000);
      this.nfts = []
      this.collects = []
      this.pageNumber = 1;
      this.getColleectList();
    },
    async getColleectList() {
      sessionStorage.setItem('launch_activeSort2', this.activeSort2)

      let params = {
        pageSize: this.pageSize,
        pageNumber: this.pageNumber,
        search: encodeURIComponent(this.searchInput || ""),
        sort: this.activeSort2,
        nftShowType:3,
        // attribute: index,
      };
      this.isShowLoading = true;

        return api.home.getMarketnftList(params).then(res => {
          let { list, totalPage } = res.data;
          this.totalPage = totalPage;
              
          if(this.pageNumber == 1) {
            this.nfts = []
            this.collects = []
          }
          this.collects = this.collects.concat(list);
          this.isShowLoading = false;
          if (this.collects.length > 0) {
            this.showRightBottom = true
          } else {
            this.showRightBottom = false
          }
        })
      
    },
    async onScroll(e) {
      const { scrollTop, clientHeight, scrollHeight } =
        e.target.scrollingElement;
      if (scrollTop + clientHeight >= scrollHeight && (this.totalPage > this.pageNumber)) {
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
  },
  async mounted() {
    this.showNotFond = true
    this.getColleectList();
    this.loadChain();
  },
};
</script>

<style lang="scss" scoped>
.marketplace{
   ::v-deep.sort-select .rd.v-chip {
    box-shadow: none;
    border: none;
    margin-bottom: 2px;
    font-weight: bold;
  }
  .recommendTitle{
    width: 100%;
    text-align: center;
    margin: auto;
    font-family: Helvetica;
	font-size: 30px;
	font-weight: bold;
	font-stretch: normal;
	line-height: 70px;
	letter-spacing: 0px;
	color: #270645;
  }
  .recommendCard {
    display: flex;
    user-select: none;
    &::v-deep .v-carousel__controls{
      background:white;
      top:420px;
      .v-btn {
    color:   #766983!important;
}
  .v-btn--active {
    color:#1d42ff!important

  }
   .v-btn--active::before {
    background:white;
    
  }
  .v-icon{
    font-size:10px!important;
  }
    }
    .card {
      margin-top: 16px;
      margin-bottom: 16px;
      margin-right: 22px;

      &:nth-child(5n) {
        margin-right: 0;
      }
    }
  }
   .refresh{
  height: 18px;
  width: 18px;
  margin-left: 15px;
  margin-top: -4px;
}
    
    .go{
        transform:rotate(360deg);
        transition: all 2s;
        pointer-events: none;
        user-select: none;
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
.right_bottom1{
   position: fixed;
  bottom: 100px;
  right: 50px;
  z-index: 200;

  img {
	display: block;
	width: 59px;
	height: 59px;
	margin-top: 35px;
  }
}
.top {
  position: relative;
  .search {
    width: 400px;
  }

  .top-right {
     ::v-deep .sort-select .rd.v-chip {
    box-shadow: none;
    border: none;
    margin-bottom: 2px;
    font-weight: bold;
  }
  > .v-tabs {
    border-bottom: solid 1px #e3e3e3;

    .v-tab {
      padding: 0;
      margin-right: 50px;
     font-family:Helvetica;
      font-size: 25px;
      font-weight: bold;
      font-stretch: normal;
      line-height: 120px;
      letter-spacing: 0px;
      color: #270645;
    }
  }

  .search-inpt {
    width: 600px;
    height: 41px;
    background-color: #f0f0f0;
    border-radius: 20px;
    outline-color: #f0f0f0;
  }
    width: 320px;
    top: 0px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: absolute;
    right: 0;
    bottom: -15px;

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

.main {
  margin: 20px 0;

  .collect {
    display: flex;
    flex-wrap: wrap;
     .empty {
      width: 100%;
      height: 500px;
      display: flex;
      justify-content: center;
      align-items: center;
      .notFound{
        font-family: Helvetica;
        font-size: 24px;
        font-weight: bold;

      }
      .tryAnother{
          font-family: Helvetica;
        font-size: 18px;
        width: 320px;
        text-align: center;
      }
    }

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

.marketplace.mobile {
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

    .refresh{
      height: 25px;
      width: 25px;
      margin-left: 15px;
      margin-right: 15px;
    }
  }

  .collect {
    .card {
      width: 100%;
      margin-right: unset;
    }
  }

  .recommendCard {
    .card {
      width: 100%;
      margin-right: 10px;
    }
    .v-carousel {
      height: 420px !important;
    }
  }
}
</style>
