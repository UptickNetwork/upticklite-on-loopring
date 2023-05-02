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
  <div id="container" style="position: absolute;z-index: 999999;">
    <v-card :class="{ card: true, mobile: isMobile }">
      <div class="top">
        <h3>{{$t('messageMessage')}}</h3>
        <div class="auth">
          <span class="width-18">{{currfriend.fansName}}</span>
          <br />
          <span class="val">{{ tineValue }}</span>
        </div>
      </div>
      <div class="msgbox">
        <ul>
          <li v-for="i in msgList" :key="i.id">
            <img class="fansPhoto" :src="i.fansPhoto || img" alt />
            <div class="cont">
              <span class="msg">{{ i.message }}</span>
              <br />
              <span class="time">{{ i.date }}</span>
            </div>
          </li>
        </ul>
      </div>

      <div class="ti-tx">
        <v-textarea
          class="txt"
          label
          placeholder
          v-model="msgtxt"
          outlined
          rows="4"
          row-height="18"
        ></v-textarea>
      </div>
      <div class="footer">
        <v-btn class="sub" submit @click="subCommit">{{$t('popupTransferCommit')}}</v-btn>
      </div>
    </v-card>

	<PromptBox ref="promptBox"></PromptBox>
  </div>
</template>

<script>
	import PromptBox from "@/components/PromptBox.vue"

	import api from "@/api";
	import { getFileSrc } from "@/utils/file";
  import img from '@/assets/default.png';

	const WORK_KEY = "WORK";

//留言弹层
export default {
	components: { PromptBox },
  data: () => ({
    open: true,
    tineValue: "",
	senderPhoto: "",
    msgtxt: "",
    img,
    msgList: [],
  }),
  props: {
	currfriend: {
		type: Object
	}
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  async mounted() {
	  if(this.currfriend.fansAddress && this.currfriend.fansAddress.length > 12) {
		  this.tineValue = this.currfriend.fansAddress.substr(0,6)+"..."+this.currfriend.fansAddress.substr(-6)
	  }

  	this.msgList = [ ];
	let params = {
		
		friendAddress: this.currfriend.fansAddress
	};
	let res = await api.home.messageRecord(params);
	res.data.list.forEach(async v => {
		let fansPhoto = "";
		if(v.senderAddress==this.$store.state.did) {
			fansPhoto = await getFileSrc(WORK_KEY, v.senderPhoto);
			this.senderPhoto = fansPhoto;
		} else {
			fansPhoto = await getFileSrc(WORK_KEY, v.receiverPhoto);
		}

		this.msgList.push({
		    id: v.id,
		    message: v.content,
			fansPhoto: fansPhoto,
		    date: this.timestampToTime(v.sendTime)
		  });
	})


	params = {
	  userAddress: this.$store.state.did,
	  chainType:this.$store.state.chainType
	};
	let info = await api.home.createInfo(params);
	let photo = await getFileSrc(WORK_KEY, info.data.profilePhoto);
	this.senderPhoto = photo;
  },
  methods: {
    subCommit() {
		if(!this.msgtxt) {
			this.$refs.promptBox.show("Information cannot be empty");
			return;
		}
		let params = {
			chainType: "IRIS_IRISHUB",
			sender: this.$store.state.did,
			receiver: this.currfriend.fansAddress,
			content: this.msgtxt
		};
		api.home.message(params)
		this.msgList.push({
			// id: v.id,
			message: this.msgtxt,
			fansPhoto: this.senderPhoto,
			date: this.timestampToTime(new Date().getTime())
		})
		this.msgtxt = "";
    },
    closeDialog() {
      this.open = false;
      this.$emit("getOpen", this.open);
    },
	timestampToTime(time) {
	  let date = new Date(time)
	  let Y = date.getFullYear() + '-';
	  let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	  let D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate()) + ' ';
	  let h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) + ':';
	  let m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) + ':';
	  let s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds());
	  let strDate = Y+M+D+h+m+s;
	  return strDate;
	}
  },
};
</script>

<style lang="scss" scoped>
  .width-18{
    display:inline-block;
    width: 100px!important;
    overflow: hidden!important;
    white-space: nowrap!important;
    text-overflow: ellipsis!important;
  }
.card {
  width: 300px;
  min-height: 411px;
  .top {
    margin: 0 28px;
    display: flex;
    flex-direction: column;
    h3 {
      margin: 35px 0 22px;
      font-weight: bold;;
      font-size: 25px;
      color: #270645;
    }
    .auth {
      position: absolute;
      top: 34px;
      right: 27px;
      text-align: end;
      span {
        width: 54px;
        height: 13px;
        font-weight: bold;;
        font-size: 15px;
        font-weight: bold;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
        margin-right: 15px;
      }
      .val {
        width: 85px;
        height: 10px;
        font-family: Helvetica;
        font-size: 11px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        line-height: 8px;
        color: #270645;
      }
    }
  }
  .msgbox {
    margin: 0 28px;
    height: 150px;
    overflow: hidden;
    overflow-y: auto;
    ul {
      list-style: none;
      padding-left: 0;
      li {
        position: relative;
        top: 0;
        left: 0;
        min-height: 60px;
        img {
          position: absolute;
          top: 0;
          left: 0;
          display: inline-block;
          width: 46px;
          height: 46px;
          background-color: #6f58d9;
        }
        .cont {
          clear: both;
          display: inline-block;
          width: 155px;
          min-height: 45px;
          margin: 0 0 19px 66px;
          .msg {
            width: 100%;
            min-height: 17px;
            font-family: Helvetica;
            font-size: 13px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
          }
          .time {
            // margin-left: 11px;
            width: 84px;
            height: 9px;
            font-family: Helvetica;
            font-size: 10px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
          }
        }
      }
    }
  }

  .ti-tx {
    margin: 25px 28px;
    .title {
      width: 85px;
      height: 17px;
      font-weight: bold;;
      font-size: 15px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }
    .txt {
      width: 350px;
      height: 126px;
      background-image: linear-gradient(#ffffff, #ffffff),
        linear-gradient(#f8f6fd, #f8f6fd);
      background-blend-mode: normal, normal;
    }
  }
  .footer {
    display: flex;
    flex-direction: column;
    text-align: center;
    margin-bottom: 50px;
    .sub {
      margin: 0 auto 30px;
      min-width: 80%;
      height: 51px;
      background-image: linear-gradient(
          90deg,
          #d300fd 0%,
          #a439fe 26%,
          #7471ff 52%,
          #00fdcf 100%
        ),
        linear-gradient(#270645, #270645);
      background-blend-mode: normal, normal;
      border-radius: 25px;
      opacity: 0.9;
      color: #ffffff;
      font-size: 20px;
    }
  }
  &.mobile {
    .footer {
      .sub {
        width: 90%;
      }
    }
  }
}

.fansPhoto {
	border-radius: 50%;
}

.v-card {
    box-shadow: 0px 0px 9px 0px rgb(0 0 0 / 9%) !important;
}
</style>
