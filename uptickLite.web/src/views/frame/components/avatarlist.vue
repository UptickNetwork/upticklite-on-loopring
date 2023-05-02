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
    <div class="cont flex-grid d-flex flex-wrap">
      <div
        @click="toMainPage(i)"
        class="follow px-2 d-flex flex-lg-row flex-row justify-space-between"
        v-for="i in followList"
        :key="i.id"
      >
        <div class="ml-0 align-center d-flex flex-lg-row">
          <Avatar
            :size="70"
            v-if="!isMobile"
            :hash=" i.profilePhoto"
            showAvatarInfo
			:did="i.did"
          />
          <Avatar
            :size="30"
            v-if="isMobile"
            :hash=" i.profilePhoto"
            showAvatarInfo
          />
          <div class="folinfo d-flex flex-lg-column flex-column ml-1 ml-lg-4">
            <span class="db auth">{{
               i.userName
            }}</span>
            <span v-if='!isMobile' class="db tine_id">{{
               i.did
            }}</span>
            <span v-if='isMobile' class="db tine_id">{{
               i.did  | didfilter
            }}</span>
            <span class="db tine_long">{{ $t('avatarlistWork') }} {{ i.workCount }}</span>
          </div>
        </div>
        <div class="mt-4 mb-4 d-flex flex-row">
          <div class="status mt-lg-4 mt-1 mr-1 mr-lg-4" v-if="isStatus">
            <div class="unflowbox" v-if="!i.followed">
              <v-btn rounded class="unflow" @click.stop="unflowBtn(i)"
                >{{$t('follow')}}</v-btn
              >
            </div>
            <v-btn v-if="i.followed" class="flowed" @click.stop="flowedBtn(i)"
              >{{$t('avatarAvatarInfoSubscribed')}}</v-btn
            >
          </div>
          <div
            class="msg mt-3 ml-0 mt-lg-6 mr-lg-6"
            v-if="isMsg"
            @click.stop="msgBtn(i)"
          >
            <!-- <img src="@/assets/icons/btn_message.png" alt="" /> -->
          </div>
        </div>
      </div>

    </div>

	<!-- <v-dialog v-model="open" style=""> -->
	<div class="mask" v-if="open">
	  <message class="message" @getOpen="Open" v-if="open" :currfriend="currfriend"></message>
	</div>
	<!-- </v-dialog> -->
  </div>
</template>

<script>
import message from "@/components/popup/message.vue";
import Avatar from "@/components/avatar/index.vue";
import api from "@/api";

export default {
  components: { message, Avatar },
  name: "avatarlist",
  data: () => ({
    open: false,
    fansAddress: "",
    ismsg: true,
    userAddress: "",
    fans:'',
    user:'',

    currfriend: {},
  }),
  props: {
    followList: {
      type: Array,
      required: true,
    },
    isMsg: {
      type: Boolean,
      required: true,
    },
    isStatus: {
      type: Boolean,
      required: true,
    },
    value: {
      type: Object,
      default: () => {},
    },
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  created() {
    this.ismsg = this.isMsg;
  },
  filters: {
  	didfilter: function(value) {
		if(value && value.length > 12) {
			return value.substr(0,6)+"..."+value.substr(-6);
		} else {
			return "";
		}
  	}
  },
  methods: {
    toMainPage(i) {
      if (this.ismsg) {
        this.$router.push({ name: "Otherpage", query: { did: i.did } });
      } else {
        this.$router.push({ name: "Otherpage", query: { did: i.did } });
      }
    },
    async unflowBtn(i) {
      if (this.ismsg) {
        this.userAddress = i.fansAddress;
      } else {
        this.userAddress = i.did;
      }
      if(this.ismsg && this.isStatus){
        // this.fans = this.userAddress
        // this.user = this.$store.state.userAddress
         this.user = this.userAddress
        this.fans = this.$store.state.did

      }else if(!this.ismsg && this.isStatus){
        this.fans = this.$store.state.did
        this.user = this.userAddress
      }
      let params = {
        fansAddress: this.fans,
        userAddress: this.user,
      };
      let res = await api.home.addDo(params);
      if (res.success === true) i.followed = true;
    },
    async flowedBtn(i) {
      if (this.ismsg) {
        this.userAddress = i.fansAddress;
      } else {
        this.userAddress = i.did;
      }
      if(this.ismsg && this.isStatus){
       this.user = this.userAddress
        this.fans = this.$store.state.did
      }else if(!this.ismsg && this.isStatus){
        this.fans = this.$store.state.did
        this.user = this.userAddress
      }
      let params = {
        fansAddress: this.fans,
        userAddress: this.user,
      };
      let res = await api.home.removeDo(params);
      if (res.success === true) i.followed = false;
    },
    msgBtn(msg) {
      this.currfriend = {
          fansAddress: msg.did,
          fansName: msg.userName
      };
      this.open = true;
    },
    Open(e) {
      this.open = e;
    },
  },
};
</script>

<style lang="scss" scoped>
.flex-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, 600px);
  grid-gap: 14px 18px;
}

.d-flex {
  display: flex;
}

.flex-wrap {
  flex-flow: row wrap;
}

.flex-between-center {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex-column {
  flex-direction: column;
}

.bd {
  display: block;
}

.contain {
  .cont {
    width: 100%;
    height: auto;
    margin-bottom: 50px;
    cursor: pointer;

    .follow {
      display: inline-block;
      width: 600px;
      height: 101px;
      padding: 0 27px;
      background-image: linear-gradient(#ffffff, #ffffff),
        linear-gradient(#6f58d9, #6f58d9);
      background-blend-mode: normal, normal;
      box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
      border-radius: 5px;

      .folinfo {
        max-width: 408px;
        margin-left: 18px;

        .auth {
          font-weight: bold;;
          font-size: 15px;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #270645;
        }

        .tine_id {
          font-family: Helvetica;
          font-size: 11px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #270645;
        }

        .time_long {
          font-family: Helvetica;
          font-size: 14px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #270645;
        }
      }

      .status {
        // margin: 0 0 0 175px;

        .unflowbox {
          width: 86px;
          height: 41px;
          box-sizing: border-box;
          padding: 2px;
          background-image: linear-gradient(
            0deg,
            #d202fd 0%,
            #a538fe 26%,
            #776eff 51%,
            #01facd 100%
          );
          border-radius: 20px;

          .unflow {
            width: 100%;
            height: 100%;
            border: none;
            border-radius: 20px;
            background: #fff;
            font-weight: bold;;
            font-size: 15px;
            font-weight: bold;
            font-stretch: normal;
            letter-spacing: 0;
            color: #270645;
          }
        }

        .flowed {
          width: 121px;
          height: 41px;
          background-color: #270645;
          border-radius: 20px;
          font-weight: bold;;
          font-size: 15px;
          font-weight: bold;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #ffffff;
        }
      }

      .msg {
        img {
          width: 25px;
          height: 24px;
        }
      }
    }
  }

  &.mobile {
    .cont {
      width: 100%;

      .follow {
        width: 100%;
        height: auto;

        .folinfo {
          width: auto;
          .tine_id {
            word-break: break-all;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
            min-width: 90px;
          }
        }

        .status {
          margin: 0;
          .unflowbox,
          .flowed {
            padding: 0 5px;
            width: auto;
            font-size: 12px !important;
          }
          .unflowbox {
            padding: 2px;
          }
        }

        .msg {
          img {
            width: 20px;
            height: 20px;
          }
        }
      }
    }
  }

}

.mask {
	background-color: rgba(0,0,0,0.4);
	position: fixed;
	left: 0;
	top: 0;
	right: 0;
	bottom: 0;
	z-index: 999;
	.message {
		  // background-color: red !important;
		  position: fixed !important;
		  right: 50px !important;
		  bottom: 0px !important;
	}
}

</style>
