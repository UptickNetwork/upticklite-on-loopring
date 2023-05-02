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
  <v-card class="rd elevation-0" >
    <v-img class="img" :src="src"  width="100%" max-height="100%" @click="imgClick" style="cursor: pointer" />

    <span class="name">{{ pageValue.name }}</span>
    <span class="num">{{ pageValue.nftCount || 0 }}</span>

    <v-img v-if="showAvatar" class="avatar"  style="border: solid 2px #ffffff; cursor: pointer;" :src="usersrc" @click="tocollectpage" />
    <v-img v-if="otherpage" class="avatar" style="border: solid 2px #ffffff; cursor: pointer;" :src="usersrc" @click="tocollectpage" />
    <slot></slot>
  </v-card>
</template>

<script>
import { getFileSrc } from "@/utils/file";
const WORK_KEY = "WORK";

const AVATAR_KEY = "AVATAR";
import defaultImg from "@/assets/default.png";

export default {
  props: {
    hash: {
      type: String,
    },
    pageValue: {
      type: Object,
    },
    showAvatar: {
      type: Boolean,
      default: false
    },
    otherpage: {
      type: Boolean,
      default: false
    },
    type:{
       type: String,
    }
  },
  data: () => ({
    src: undefined,
    usersrc: "",
    defaultImg
  }),
  watch: {
    hash: {
      handler: async function (hash) {
        if (hash) {
          this.src = await getFileSrc(WORK_KEY, hash);
        }
      },
      immediate: true,
    },
  },
  async mounted() {
    if(this.pageValue.creatorPhoto != "null")
    {
      this.usersrc = await getFileSrc(AVATAR_KEY, this.pageValue.creatorPhoto);
    }else{
       this.usersrc = defaultImg;
    }
    	if(this.pageValue.listAmount){
    		this.pageValue.amount=this.pageValue.listAmount
    	}
  },
  methods: {
    tocollectpage(){
this.$router.push({
          name: "Otherpage",
          query: { did: this.pageValue.owner },
        });
    },
    imgClick() {
      let url
      if(this.pageValue.linkUrl)
      { 
       url = this.pageValue.linkUrl
      }
      if (this.type == "createorPage" ) {
           this.$router.push({
             path: `/collection_create/${url}`,   
        });
        
       
      }else if (this.type == "indexpage" ) {
           this.$router.push({
             path: `/collection/${url}/launchpad`,   
        });
        
       
      }else if(this.type == "otherPage"){
             this.$router.push({
             path: `/collection/${url}/otherPage`,   
        });
      }
      else{
        this.$router.push({
             path: `/collection/${url}`,   
        });
      }
    },
  },
};
</script>


<style lang="scss" scoped>
.v-card {
  width: 100%;
  height: 220px;
  background-color: #270645;
  border-radius: 5px;
  display: flex;
  justify-content: center;
  background-color: #270645;

  @media screen and (min-width: #{map-get($grid-breakpoints, 'lg')}) {
    width: 220px;
  }

  &:hover {
    border: solid 2px #1d42ff;
  }

  .img {
    object-fit: cover;
    opacity: 0.7;
  }

  .name {
    position: absolute;
    font-family: Helvetica;font-weight: bold;
    font-size: 15px;
    line-height: 24px;
    width: 150px;
    color: #ffffff;
    top: 18px;
    left: 18px;
    font-weight: bold !important;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .num {
    position: absolute;
    font-family: Helvetica;font-weight: bold;
    font-size: 15px;
    line-height: 24px;
    color: #ffffff;
    top: 42px;
    left: 18px;
  }

  .avatar {
    position: absolute;
    width: 41px;
    height: 41px;
    border-radius: 50% !important;
    bottom: 20px;
  }
}
</style>