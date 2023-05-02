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
   <div class="contain">
      <v-card :class="{card:true,mobile:isMobile}">
         <img
             class="clo"
             @click="closeDialog"
             src="@/assets/icons/btn_close.png"
             alt=""
         />
         <div class="bid d-flex flex-column">
            <span v-text="card.bidName" class="name mb-3"></span>
            <div class="flex-start-center d-flex flex-row" style="align-items: center;">
               <div class="pt-1 mr-2">
                  <v-img :src="$walletIcon" alt="logo" width="22px" height="22px"/>
               </div>
               <span class="count" style="line-height:22px;" v-text="card.bidCount"></span>
            </div>
         </div>
         <div class="list ">
            <div class="title mb-6">{{$t('popupSaletotalSalesStatistics')}}</div>
            <div class="list_cont d-flex flex-row">
               <div class="col d-flex flex-column ml-3">
                  <div class="row">{{$t('popupSaletotalTotalRelease')}}: {{totalInfo.assetExtension.totalInventory}}</div>
                  <div class="row mt-n5">{{$t('popupSaletotalSold')}}: {{totalInfo.saleAmount}}</div>
                  <div class="row mt-n5">{{$t('popupSaletotalInStock')}}: {{totalInfo.amount}}</div>
               </div>
            </div>
         </div>
         <div class="table  mb-4">
            <div class="d-flex flex-row justify-space-between">
               <div class="title">{{$t('popupSaletotalOrders')}}</div>
            </div>
            
            <div class="orderlist">
               <div 
                    class="itm mb-1 d-flex flex-row justify-space-between px-2"
                    v-for="i in orderlist"
                    :key="i.id"
               >
                  <div class="d-flex flex-row justify-space-around mt-2">
                     <Avatar :size="30" :hash="i.recipientPhoto"/>
                     <span class="ml-2 mt-2 name item_font">{{ i.recipientName || i.recipient.substr(0, 5) + "..." + i.recipient.substr(-5) }}</span>
                  </div>
                  <div class="price">{{ i.orderPrice }} {{i.tokenType}}</div>
                  <div class="time ml-2" :title="i.createTime | didfilter">{{ i.createTime | didfilter }}</div>
               </div>
            </div>
         </div>
      </v-card>
      <div v-if="openmsg" style="position: fixed; right: 20px; bottom: 20px; z-index: 99;">
         <message @getOpen="OpenMsg" v-if="openmsg" :currfriend="currfriend"></message>
      </div>
      <div v-if="openmsg_group" style="position: fixed; right: 20px; bottom: 20px; z-index: 99;">
         <message-group @getOpen="OpenMsg_group" v-if="openmsg_group" :currfriend="currfriend_group"></message-group>
      </div>
      <uToast ref="toast"></uToast>
   </div>
</template>

<script>
  import message from "@/assets/icons/btn_message.png";
  import Message from "./message";
  import MessageGroup from "./messageGroup";
  import api from "@/api";
  import Avatar from "@/components/avatar/index.vue"

  export default {
    components: { Avatar, Message, MessageGroup },
    data: () => ({
      message,
      open: true,
      openmsg: false,
      openmsg_group: false,
      card: {
        bidName: "",
        bidCount: ""
      },
      orderlist: [],
      totalInfo: {},
      pageNumber: 1,
      pageSize: 400,
      totalPage: false,
      currfriend:{},
      currfriend_group:{}
    }),
    props: {
       pageData: {
          type: Object
       },
      address: {
        type: String
      },
      id: {
        type: Number
      },
	  name: {
	    type: String
	  },
	  minPrice: {
	    type: String
	  },
    },
    computed: {
      isMobile: function() {
        return this.$vuetify.breakpoint.mobile;
      }
    },
    filters: {
      didfilter: function(value) {
        let date = new Date(value);
        let Y = date.getFullYear() + " ";
        let M = (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1) + "/";
        let D = (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + "/";
        let h = (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
        let m = (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) + ":";
        let s = (date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds());
        let strDate = M + D + Y + h + m + s;
        return strDate;
      }
    },
    mounted() {
      this.getList();
	  this.card.bidName=this.name
	  if(this.minPrice!=undefined){
		  this.card.bidCount=this.minPrice
	  }

    },
    methods: {
      async getList() {
        let params = {
          pageNumber: this.pageNumber,
          pageSize: this.pageSize,
          nftId: this.pageData.nftId,
          tokenAddress: this.pageData.contractAddress
        };
        let res = await api.home.orderStatements(params);
        this.totalInfo = res.data;
        this.orderlist = res.data.orderList;
      },
      onScroll(e) {
        const { scrollTop, clientHeight, scrollHeight } =
          e.target.scrollingElement;
        if (
          scrollTop + clientHeight === scrollHeight &&
          this.totalPage > this.pageNumber
        ) {
          this.getList();
        }
      },
      closeDialog() {
        this.open = false;
        this.$emit("getOpen", this.open);
      },
      msgBtn(i) {
        if(i.assetOwner == this.$store.state.did) {
           this.$refs.toast.show("error", "不能给自己留言", 1000);
           return;
        }
        this.currfriend.fansAddress = i.assetOwner;
        this.currfriend.fansName = i.issuerName;
        this.openmsg_group = false;
        this.openmsg = true;
      },
      OpenMsg(e) {
        this.openmsg = e;
      },
      messageAllClick() {
        this.currfriend_group.group = 1;
        this.currfriend_group.fansAddress = this.address;
        this.currfriend_group.fansName = this.$t("notice_group_leavemsg");
        this.openmsg = false;
        this.openmsg_group = true;
      },
      OpenMsg_group(e) {
        this.openmsg_group = e;
      },
    }
  };
</script>

<style lang="scss" scoped>
   .card {
      display: flex;
      flex-direction: column;
      padding: 0 50px;
      min-height: 600px;
      width: 660px;

      .clo {
         position: absolute;
         right: 10px;
         top: 10px;
         width: 30px;
         height: 31px;
         background-color: #fff;
         border-radius: 50%;
         margin-right: 0;
         cursor: pointer;
      }

      .bid {
         margin: 16px 0 50px;

         .name {
            font-weight: bold;;
            font-size: 20px;
            font-weight: bold;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
         }

         .count {
            font-weight: bold;;
            font-size: 15px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
         }
      }

      .list {
         .title {
            font-weight: bold;;
            font-size: 20px;
            font-weight: bold;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
            margin-top:-76px;
         }

         .list_cont {
            height: 118px;

            .col {
               width: 50%;
               padding: 0;
               text-align: left;

               .row {
                  font-family: Helvetica;
                  font-size: 13px;
                  font-weight: normal;
                  font-stretch: normal;
                  letter-spacing: 0px;
                  color: #270645;
                  vertical-align: middle;
               }
            }
         }
      }

      .table {
         .title {
            font-weight: bold;;
            font-size: 20px;
            font-weight: bold;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
         }

         .orderlist {
            width: 100%;
            height: 263px;
            overflow: hidden;
            overflow-y: auto;

            .itm {
               height: 51px;
               background-image: linear-gradient(#ffffff, #ffffff),
               linear-gradient(#6f58d9, #6f58d9);
               background-blend-mode: normal, normal;
               box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
               border-radius: 5px;
               margin: 5px;

               .item_font {
                  text-overflow: ellipsis;
                  white-space: nowrap;
                  overflow: hidden;
                  width: 60px;
               }

               .name {
                  width: 80px;
                  font-family: Helvetica;
                  font-size: 13px;
                  font-weight: normal;
                  font-stretch: normal;
                  letter-spacing: 0px;
                  color: #270645;
               }

               .price {
                  font-weight: bold;;
                  font-size: 13px;
                  font-weight: bold;
                  font-stretch: normal;
                  letter-spacing: 0px;
                  line-height: 50px;
                  color: #270645;
               }

               .time {
                  font-family: Helvetica;
                  font-size: 13px;
                  font-weight: normal;
                  font-stretch: normal;
                  line-height: 50px;
                  letter-spacing: 0px;
                  color: #270645;
               }

               img {
                  display: inline-block;
                  vertical-align: middle;
               }
            }
         }
      }

      &.mobile {
         .table {
            .orderlist {
               width: 100%;

               .itm {
                  .price {
                     min-width: 50px;
                  }

                  .time {
                     text-overflow: ellipsis;
                     white-space: nowrap;
                     overflow: hidden;
                     width: 40px;
                  }

                  .item_font {
                     text-overflow: ellipsis;
                     white-space: nowrap;
                     overflow: hidden;
                     width: 30px;
                  }
               }

            }
         }
      }
   }
</style>
