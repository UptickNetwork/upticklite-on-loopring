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
  <div :class="{ contain: true, mobile: isMobile }" style="overflow-x: hidden">
    <div class="top">
      <div class="ban">
        <img v-if="src" :src="src" alt="" />
      </div>
      <div class="topbox d-flex flex-lg-row flex-row" style="background-color: rgb(39, 6, 69,0.7);">
        <div class="img ml-7">
          <img v-if="src" :src="src" alt="" />
        </div>
        <div class="right d-flex flex-column">
          <div class="box" v-if="src">
            <div class="ava d-flex flex-row">
              <Avatar
                :did="collectItem.owner"
                :size="51"
                :hash="collectItem.creatorPhoto"
                imgType="middlev"
              />
              <div class="ml-7 ml-lg-4 fs-15 name">{{ collectItem.creatorName ? collectItem.creatorName: collectItem.owner.substr(0, 12) + "..." +
                        collectItem.owner.substr(-12) }}</div>
            </div>
            <div class="desc mt-2">
              {{ collectItem.name }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="tit" >
      <p v-html="collectItem.description"></p>
    </div>
    <div class="cards-container">
      <div class="search_create"> 
        <CollectionInput style="width: 400px" v-if="!isMobile" @change="searchChange" />
        <v-btn v-if="showCreate==true" class="uc createnft" width="150px" height="41px" rounded @click="createNft">+ Create NFT</v-btn>
      </div>

      <div class="cardlist ">
        <div v-if="empty" class="empty">{{$t('EmptyHere')}}</div>
        <div v-if="typeStatus == 'createorPage'" class="d-flex flex-wrap card">
          <Card  v-for="(item, i) in list" :key="i" v-scroll="onScroll" type="my" :value="item" owerfrom="my" />
        </div>
        <div v-else class="d-flex flex-wrap card">
           <Card  v-for="(item, i) in list" :key="i" v-scroll="onScroll" type="page" :value="item" owerfrom="market" />
        </div>
       
      </div>
    </div>
    
  </div>
</template>

<script>
import Avatar from "@/components/avatar/index.vue";
import Carousel from "../../components/carousel";
import message from "@/components/popup/message.vue";
import CollectionInput from "@/components/CollectionInput.vue";
import Card from "@/components/workCard/Card.vue";
import PutOnSale from "@/components/popup/putOnSale.vue";
import { mapState } from "vuex";
import api from "@/api";
import { getFileSrc } from "@/utils/file";
const COLLECTION_KEY = "COLLECTION";
let last_time = 0;

export default {
  name: "CollectionPage",
  components: {
    Carousel,
    message,
    Avatar,
    CollectionInput,
    Card,
    PutOnSale,
  },
  data: () => ({
    opencard: false,
    open: false,
    openmsg: false,
    offset: true,
    pageInfo: {},
    did: "",
    currfriend: {},
    list: [],
    sku: {},
    typeStatus: "",
	showCreate: false,
    showRightBottom: false,
    isShowLoading: false,
    imgUrl: "",
    src: "",
    saleStatus: "",
    pageNumber: 1,
    pageSize: 20,
    selectList: [
      { value: 1, label: "全部 NFT   " },
      { value: 2, label: "未上架销售" },
      { value: 3, label: "已上架销售" },
      { value: 4, label: "已结束的" },
    ],
    activeSort: 1,
    checkAllCard: false,
    isClick: true,
    isChange: false,
    isShowCheckbox: "",
    showAllCheck: false,
    totalNumber: 0,
    openputonsale: false,
    address:'',
    constructID:'',
    name:'',
    onSaleTokenIds: [],
    offSaleTokenIds:[],
    NFTlist:{},
    supplyLimit:2,
    collectItem: {},
    userData:{},
    url:'',
    empty:false
  }),
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
    handler: function () {
      return {
        ipt: this.searchInput,
      };
    },
    ...mapState({
      searchInput: (state) => state.searchInput,
    }),
  },
  async mounted() {
       window.eventBus.$on("burnCollect",this.burnCollect);
     let collection = this.$route.path
    this.url = this.$route.params.id
    let linkUrl = this.$route.params.id;
	
       let res_info = await api.home.collectionInfo({ linkUrl });
      this.collectItem = res_info.data;
    if(collection.indexOf("collection_card") != -1)
    {
      this.typeStatus = "cardPage"
    }else{
		
      if(collection.indexOf("collection_create") != -1)
      { 
        this.typeStatus = "createorPage"
		    if(this.collectItem.owner==this.$store.state.did){
		      	this.showCreate=true;
	    	}
         
      }else{ 
          this.typeStatus = "marketPage"
      }

    }
	let source=this.$route.params.source;
	if(source && source == 'launchpad'){
		 this.typeStatus = "launchpad"
	}else if(source && source == 'otherPage'){
	 this.typeStatus = "otherPage"
  }
    
    if (this.collectItem.description)
      this.collectItem.description = this.collectItem.description.replace(/\n/g, "<br/>");

    this.src = await getFileSrc(COLLECTION_KEY, this.collectItem.imgUrl);
    let val_user = window.sessionStorage.getItem("UserData");
    this.userData = JSON.parse(val_user);
    if( this.typeStatus == "createorPage" && !this.$store.state.did){
      this.empty = true;
    }else{
       await this.getColleectList();
    }
   
  },
  methods: {
     burnCollect(){
      console.log("wxl --- burnCollect")
      setTimeout(() => {
       this.pageNumber = 1;
      this.list = [];
        this.getColleectList();
      }, 5000);
      
    },
    searchChange(val) {
      clearTimeout(last_time);
      last_time = setTimeout(() => {
        this.list = [];
        this.pageNumber = 1;
        this.getColleectList(val);
      }, 600)
    },
    createNft() {
      this.$router.push({name: "CreateNft", query: { tokenAddress: this.collectItem.address }});
    },
    async getColleectList(search) {
      let sourceType
      if(this.typeStatus == "createorPage") {
       sourceType =5
      } else if(this.typeStatus == "launchpad" || this.typeStatus == "otherPage") {
       sourceType =1
      }
      else {
         sourceType =2
      }
      let params = {
          pageNumber: this.pageNumber,
          pageSize: this.pageSize,
          // tokenType: 
          search: encodeURIComponent(search || ""),
          // sort
          tokenAddress: this.collectItem.address,
          baseUri:this.url,
          sourceType:sourceType,
          owner:this.$store.state.did
          
        };

        let res = await api.home.getCollectnftList(params);
        let assetList = res.data.list;
		if(assetList.length>0){
			assetList.forEach(async (v) => {
          if (v != null &&v.listAmount==null) {
           v.listAmount=0
          }
        });
		}
        this.list = this.list.concat(assetList);
      
      if (
        (this.pageNumber == 1 || this.pageNumber == 0) &&
        this.list.length == 0
      ) {
        this.empty = true;
      } else {
        this.empty = false;
      }
	  
    },
    onScroll(e) {
        const {scrollTop, clientHeight, scrollHeight} =
          e.target.scrollingElement;
          
        if (scrollTop + clientHeight >= scrollHeight && (this.totalPage > this.pageNumber)) {
          // this.pageNumber++;
          this.getMyList();
        }
    },
    
  },
};
</script>

<style lang="scss" scoped>

.contain {
  padding: 0;
  margin: 0;
  .top {
    max-width: 1220px;
    height: 401px;
    margin: 0 auto;
    position: relative;
    .ban {
      max-width: 1220px;
      height: 401px;
      margin: 0 auto;
      opacity: 0.8;
      img {
       width: 100%;
        height: 100%;
        display: block;
        object-fit: cover;
      }
    }
    .topbox {
     position: absolute;
      left: 0;
      top: 0;
        width: 100%;
    height: 100%;
      margin: 0 auto;

      .img {
        width: 535px;
        height: 355px;
        margin-right: 50px;
        img {
          object-fit: contain;
          display: block;
          margin-top: 20px;
          width: 100%;
          height: 100%;
        }
      }
      .right {
        padding: 35px 0 25px;
        .rt_icon {
          .oneicon {
            width: 20px;
            height: 20px;
          }
        }
        .rb_icon {
          .twoicon {
            width: 22px;
            height: 26px;
          }
        }
        .box {
          min-width: 543px;
          margin:auto;
          .ava {
            .name {
              min-height: 17px;
              font-family: Helvetica;
              font-size: 15px;
              font-weight: bold;
              font-stretch: normal;
              letter-spacing: 0px;
              line-height: 50px;
              color: #ffffff;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
            }
          }
          .desc {
            margin-bottom: 50px;
            width: 543px;
            height: 133px;
            font-family: Helvetica;
            font-size: 40px;
            font-weight: bold;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #ffffff;
            word-break:break-all
          }
        }
      }
    }
  }
  .tit {
    text-align: center;
    margin-bottom: 29px;
    p {
      margin: 29px auto 0;
      max-width: 1018px;
      min-height: 0px;
      font-family: Helvetica;
      font-size: 14px;
      font-weight: normal;
      font-stretch: normal;
      line-height: 40px;
      letter-spacing: 0px;
      color: #270645;
    }
  }
  
  
  .cards-container {
    .search_create {
      display: flex;
      justify-content: space-between;
    }
    .cardlist {
      display: flex;
      flex-wrap: wrap;
      .v-card {
        margin-right: 30px;
        margin-bottom: 30px;
        &:nth-child(5n) {
          margin-right: 0;
        }
      }
      .v-card.mobile {
        width: 100%;
        margin-right: 0;
      }

      .empty {
        width: 100%;
        height: 500px;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
    
  }

  &.mobile {
    width: 100%;
    .card {
      width: 100%;
      margin-right: unset;
    }
    
    .cards-container {
      margin-right: unset;
    }
    .v-card {
      margin-right: unset !important;
    }
  }
}
</style>
