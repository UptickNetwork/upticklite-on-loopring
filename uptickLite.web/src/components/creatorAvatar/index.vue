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
   <div class="card">
      <header class="flex-between-center pading-gap">
         <div class="flex-center">
            <v-avatar size="70" @click="avatarClick" style="cursor: pointer;">
               <v-img :src="src"/>
            </v-avatar>
            <div class="flex-center-start flex-column" style="margin-left: 20px">
               <span class="name">{{ avaInfo.name  }}</span>
              <div class="didnum"  d-flex flex-row>
                  <span class="No">{{ avaInfo.address | didfilter }}</span>
                  <v-img  class="copy" :class="{click:isCopy}" src="@/assets/icons/icon_copy.png" @click.stop="onCopyClick" />
               </div>
            </div>
         </div>
         <div class="unflowbox" v-if="!avaInfo.followed && !isSelf">
            <v-btn @click.stop="unflowBtn" type="button" class="follow-btn unflow"
            >{{$t("follow")}}
            </v-btn
            >
         </div>
         <v-btn
             v-if="avaInfo.followed && !isSelf"
             @click.stop="flowedBtn"
             type="button"
             class="follow-btn flowed"
         >{{$t("avatarAvatarInfoSubscribed")}}
         </v-btn
         >
      </header>
      <article>
         <p class="desc pading-gap">{{ avaInfo.description }}</p>
         <div class="flex-start-center pading-gap mt-n2">
            <div class="flex-center-start flex-column">
               <div class="num">{{ avaInfo.fansNumber }}</div>
               <div class="sub">{{$t("avatarAvatarInfoFans")}}</div>
            </div>
            <div class="flex-center-start flex-column followers">
               <div class="num">{{ avaInfo.followCount }}</div>
               <div class="sub">{{$t("fansSubscrible")}}</div>
            </div>
         </div>
         <div class="line" v-if="avatars.length>0"></div>
      </article>
      <footer class="pading-gap flex-start-center" v-if="avatars.length>0">
         <span class="avatar-name">{{$t("avatarAvatarInfoFans")}}</span>
         <div style="position: relative; height: 30px">
            <div
                class="avatar"
                :style="{ left: `${index * 18}px` }"
                v-for="(info, index) of avatars"
                :key="index"
                @click="fansClick"
            >
               <v-avatar size="28">
                  <v-img :src="info.src" alt="Midoriya"/>
               </v-avatar>
            </div>
         </div>
      </footer>
   </div>
</template>

<script>
  import api from "@/api";
  import { getFileSrc } from "@/utils/file";
  import logoimg from "@/assets/default.png";

  const AVATAR_KEY = "AVATAR";
  export default {
    props: {
      did: {
        type: String
      }
    },
    data() {
      return {
        avaInfo: {},
        src: "",
        avatars: [],
        logoimg,
        isSelf: false,
        isCopy: false,
      };
    },
    created() {
      this.getHoverInfo();
      this.isSelf = this.did == this.$store.state.did;
    },
    filters: {
      didfilter: function(value) {
        if (value && value.length > 12) {
          return value.substr(0, 5) + "..." + value.substr(-5);
        } else {
          return "";
        }
      }
    },
    methods: {
        onCopyClick() {
		var input = document.createElement('input')
		input.value = this.avaInfo.address;
		document.body.appendChild(input);
		input.select();
		document.execCommand('copy');
		document.body.removeChild(input);
		this.isCopy = true;
		setTimeout(() => this.isCopy = false, 100);
	},
      async getHoverInfo() {
         console.log("wxl --- 333333333333")
        let params = {
          address: this.did,
           friendAddress: this.$store.state.did
        };
        let res = await api.home.loopringUserInfo(params);
        this.avaInfo = res.data;
        if (this.avaInfo.profilePhoto != null && this.avaInfo.profilePhoto != '') {
          this.src = await getFileSrc(AVATAR_KEY, this.avaInfo.profilePhoto);
        } else {
          this.src = logoimg;
        }
        if(res.data.fansInfoDtoList){
           let fanSrc = res.data.fansInfoDtoList;
        fanSrc.forEach(async (v) => {
          if (v.fansPhoto != null && v.fansPhoto !="") {
            let fansrc = await getFileSrc(AVATAR_KEY, v.fansPhoto);
            this.avatars.push({ src: fansrc });
          }
        });
        }
      },
      async unflowBtn() {
        let params = {
          userAddress: this.did,
          fansAddress: this.$store.state.did
        };
        let res = await api.home.addFollow(params);
        if (res.success === true) this.avaInfo.followed = true;
      },
      async flowedBtn() {
        let params = {
          userAddress: this.did,
          fansAddress: this.$store.state.did
        };
        let res = await api.home.removeFollow(params);
        if (res.success === true) this.avaInfo.followed = false;
      },
      avatarClick() {
        if(this.did == this.$store.state.did){
          this.$router.push({
            name: "PersonalPage",
            query: { did: this.avaInfo.address },
          });
        }else{
          this.$router.push({
            name: "Otherpage",
            query: { did: this.avaInfo.address },
          });
        }
      },
      fansClick() {
        if (this.avaInfo.address == this.$store.state.did) {
          this.$router.push({
            name: "Fans"
          });
        }
      }
    }
  };
</script>

<style lang="scss" scoped>
   .flex-between-center {
      display: flex;
      justify-content: space-between;
      align-items: center;
   }

   .flex-center {
      display: flex;
      justify-content: center;
      align-items: center;
   }

   .flex-center-start {
      display: flex;
      justify-content: center;
      align-items: flex-start;
   }

   .flex-start-center {
      display: flex;
      align-items: center;
   }

   .flex-column {
      flex-direction: column;
   }

   .pading-gap {
      padding: 0 24px;
   }

   .card {
      width: 356px;
      height: auto;
      padding: 14px 0;
      background-image: linear-gradient(#ffffff, #ffffff),
      linear-gradient(#6f58d9, #6f58d9);
      background-blend-mode: normal, normal;
      box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
      border-radius: 5px;

      .name {
         font-weight: bold;;
         font-size: 20px;
         color: #270645;
         width: 100px;
         overflow: hidden;
         white-space: nowrap;
         text-overflow: ellipsis;
      }
       .didnum{
         .No {
         font-family:Helvetica;
         font-size: 11px;
         color: #270645;
         padding-top: 4px;
         word-wrap: break-word;
         width: 100px;
         // display: inline-block;
         margin-right:3px;
      }
      .copy{
         // display:inline-block;
         // width:13px;
         // height:15px;
         	width: 13px;
				height: 15px;
				display: inline-block;
				vertical-align: middle;
				margin-left: 5px;
				cursor: pointer;
      }
      .click {
				margin-top: 5px;
			}
      }

      .No {
         font-family: Helvetica;
         font-size: 11px;
         color: #270645;
         padding-top: 4px;
         word-wrap: break-word;
         width: 100px;
      }

      .desc {
         text-overflow: -o-ellipsis-lastline;
         overflow: hidden;
         text-overflow: ellipsis;
         display: -webkit-box;
         -webkit-line-clamp: 2;
         line-clamp: 2;
         -webkit-box-orient: vertical;
         font-family:"\5FAE\8F6F\96C5\9ED1";
         font-size: 12px;
         color: #766983;
         margin-top: 5px;
         word-wrap:break-word;
      }

      .followers {
         padding: 0 46px;
      }

      .num {
         font-weight: bold;;
         font-size: 15px;
         color: #270645;
         font-weight: bold;
      }

      .sub {
         font-family: Helvetica;
         font-size: 14px;
         color: #766983;
      }

      .line {
         border-bottom: solid 1px #e3e3e3;
         margin: 18px 0;
      }
   }

   .unflowbox {
      width: 86px;
      height: 41px;
      box-sizing: border-box;
      
    border: solid 2px #1d42ff;
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
      background-color: #1d42ff !important;
      border-radius: 20px;
      font-weight: bold;;
      font-size: 15px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #ffffff !important;
   }

   // .follow-btn {
   //   width: 85px;
   //   height: 41px;
   //   color: #270645;
   //   font-size: 15px;
   //   border-radius: 20px;
   // }

   footer {
      .avatar-name {
         font-weight: bold;;
         font-size: 14px;
         color: #270645;
         font-weight: bold;
         padding-right: 14px;
      }

      .avatar {
         position: absolute;
         left: 0;
         top: 0;
         background: white;
         padding: 2px;
         border-radius: 50%;
         cursor: pointer;
      }
   }
</style>
