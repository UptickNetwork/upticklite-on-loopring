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
  <div :class="{ content: true, mobile: isMobile }">
    <div class="top-right">
      <img class="nft" :src="nftImage" @click="mode_click('nft')" />
      <img
        class="collection"
        :src="collectionImage"
        @click="mode_click('collection')"
      />
    </div>
    <div class="main">
      <div class="collect" v-if="showModel == 'collection'">
         <div v-if="empty" class="empty">{{$t('EmptyHere')}}</div>
        <div class="card" v-for="(item, index) in collects" :key="index">
          <collect-card
            :pageValue="item"
            :hash="item.imgUrl"
            :showAvatar="showAvatar"
             type="otherPage"
            @imgClick="imgClick(item)"
          ></collect-card>
        </div>
      </div>

      <div class="collect" v-if="showModel == 'nft'">
         <div v-if="empty" class="empty">{{$t('EmptyHere')}}</div>
        <div class="card" v-for="(item, index) in nfts" :key="index">
          <Card type="page" :hash="item.hash" :value="item" owerfrom="market" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SortSelect from "@/components/SortSelect.vue";
import CardList from "@/components/workCard/CardList.vue";
import CollectCard from '@/components/workCard/CollectCard.vue';
import Card from "@/components/workCard/Card.vue";
import api from "@/api";

export default {
  name: "tabsPage",
  components: { CardList,CollectCard, SortSelect,Card },
  data: function () {
    return {
      isShowLoading: false,
      pageNumber: 1,
      pageSize: 20,
      totalPage: false,
      activeTab: undefined,
      showModel: "nft", // collection
      nftImage: require("@/assets/icons/icon_cj_nft.png"),
      collectionImage: require("@/assets/icons/icon_cj_collection_g.png"),
      tablist: [
        { value: "0", label: this.$t("otherpageTabsPageAll") },
        { value: "3", label: this.$t("otherpageTabsPageArtwork") },
        { value: "4", label: this.$t("amCreateCollectibles") },
        { value: "5", label: this.$t("createCategoriesPhotography") },
        { value: "7", label: this.$t("amCreateVideo") },
        { value: "9", label: this.$t("otherpageTabsPageHotResale") },
      ],
      activeSort: "",
      selectlist: [
        { value: "", label: this.$t("otherpageTabsPageAll") },
        { value: "1", label: this.$t("otherpageTabsPageOnsale") },
        { value: "0", label: this.$t("workCardSoldOut") },
      ],
      list: [],
      collects: [],
      nfts: [],
      showAvatar:false,
      pageInfo: {},
       empty:false,
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
      };
    },
  },
  watch: {
    handler: {
      handler: function () {
        this.pageNumber = 0;
        this.list = [];
        this.users = [];
        this.getOtherpageList();
      },
      deep: true,
    },
  },
  mounted() {
    this.getOtherpageList();
  },
  methods: {
    async getOtherpageList() {
      this.isShowLoading = true;
      if (this.showModel == "nft") {
        let params = {
          pageSize: this.pageSize,
          pageNumber: this.pageNumber,
          creator: this.$route.query.did,
        };
        return api.home.getPersonnftList(params).then((res) => {
          let { list, totalPage } = res.data;
          this.totalPage = totalPage;
          if(this.pageNumber == 1) {
            this.nfts = [];
          }
          this.nfts = this.nfts.concat(list);
           if ((this.pageNumber== 1|| this.pageNumber== 0)&& list.length == 0) {
              this.empty = true;
            } else {
              this.empty = false;
            }
          this.isShowLoading = false;
        });
      } else if (this.showModel == "collection") {
        let params = {
          pageSize: this.pageSize,
          pageNumber: this.pageNumber,
          creator: this.$route.query.did,
          filter:1
        };
        return api.home.getPersoncollectionList(params).then((res) => {
          let { list, totalPage } = res.data;
          this.totalPage = totalPage;
          if(this.pageNumber == 1) {
            this.collects = [];
          }
          this.collects = this.collects.concat(list);
           if ((this.pageNumber== 1|| this.pageNumber== 0)&& list.length == 0) {
              this.empty = true;
            } else {
              this.empty = false;
            }
          this.isShowLoading = false;
        });
      }
    },
    mode_click(mode) {
      this.showModel = mode;
      mode == "nft"
        ? (this.nftImage = require("@/assets/icons/icon_cj_nft.png"))
        : (this.nftImage = require("@/assets/icons/icon_cj_nft_g.png"));
      mode == "collection"
        ? (this.collectionImage = require("@/assets/icons/icon_cj_collection.png"))
        : (this.collectionImage = require("@/assets/icons/icon_cj_collection_g.png"));
      this.nfts = [];
      this.collects = [];
      this.getOtherpageList();
    },
    onScroll(e) {
      const { scrollTop, clientHeight, scrollHeight } =
        e.target.scrollingElement;
      if (
        Math.ceil(scrollTop + clientHeight) === scrollHeight &&
        this.totalPage > this.pageNumber
      ) {
        this.isShowLoading = true;
        this.getOtherpageList();
      } else if (this.totalPage == this.pageNumber) {
        this.isShowLoading = false;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.flex-start-center {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}
.empty {
	width: 100%;
	height: 500px;
	display: flex;
	justify-content: center;
	align-items: center;
}

.content {
  width: 100%;
  height: auto;
  margin-bottom: 51px;
  position:relative;

  .top-right {
    width: 80px;
    top: -30px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: absolute;
    right: 0;

    img {
      cursor: pointer;
    }
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

}
</style>
