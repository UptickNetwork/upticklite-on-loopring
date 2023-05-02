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
   <div>
      <v-menu v-model="show" :attach="$el" right offset-y nudge-bottom="10px">
         <template v-slot:activator="{ on, attrs, value }">
            <Chip v-bind="attrs" v-on="on" pill class="pl-0">
               <v-avatar :size="40">
                  <v-img :src="usersrc" />
               </v-avatar>
               <span class="ml-4 user" :class="{ star: newMsgCount > 0 }">{{ userName }}</span>
               <v-icon class="ml-3" color="primary" v-if="value">mdi-menu-up
               </v-icon>
               <v-icon class="ml-3" color="primary" v-else>mdi-menu-down</v-icon>
            </Chip>
         </template>
         <UserCenterNav @change="change" />
      </v-menu>
      <v-dialog v-model="openAppeal">
         <accountAppeal @getOpen="Open"></accountAppeal>
      </v-dialog>

      <v-dialog v-model="disAppeal">
         <v-card>
            <img class="card-close" width="31px" height="31px" @click="disAppeal = false" src="@/assets/btn_close.png"
               alt="" />
            <div style="width: 500px; height: 300px;">
               <div class="text">
                  <h1>{{ $t("UsersCannotAppeal") }}</h1>
               </div>
            </div>
         </v-card>
      </v-dialog>
   </div>
</template>

<script lang="js">
import Chip from "@/components/Chip.vue";
import UserCenterNav from "./UserCenterNav.vue";
import api from "@/api";
import { getFileSrc } from "@/utils/file";

const AVATAR_KEY = "AVATAR";
import defaultImg from "@/assets/default.png";


export default {
   name: "UserCenterSelect",
   components: {
      Chip,
      UserCenterNav,
   },
   props: {},
   data: function () {
      return {
         openAppeal: false,
         disAppeal: false,
         show: false,
         userName: "",
         usersrc: "",
         defaultImg,
         newMsgCount: 0
      };
   },
   methods: {
      async init() {
         if (this.$store.state.did != null) {
            let param = { address: this.$store.state.did };
            let res = await api.home.loopringUserInfo(param);
            if (res.data != null) {
               window.sessionStorage.setItem("UserData", JSON.stringify(res.data));

               if (res.data && res.data.profilePhoto) {
                  this.userName = res.data.name;
                  this.imgUrl = res.data.profilePhoto;
                  this.usersrc = await getFileSrc(AVATAR_KEY, this.imgUrl);
               } else {
                  this.usersrc = defaultImg;
                  this.userName = `${this.$store.state.did.substr(0, 3)}...${this.$store.state.did.substr(-3, 3)}`;
               }

            } else {
               this.usersrc = defaultImg;
               this.userName = `${this.$store.state.did.substr(0, 3)}...${this.$store.state.did.substr(-3, 3)}`;
               this.$router.push({
                  name: "MyInfo"
               });
            }
         }
      },
      async change(v) {
         if (v.toLowerCase() == "appeal") {
            this.openAppeal = true;
         } else if (v.toLowerCase() == "appealed") {
            this.disAppeal = true;
         }

         this.show = false;
      },
      Open(e) {
         this.openAppeal = e;
      }
   },
   async mounted() {
      window.eventBus.$on("MidifyUser", this.init);
      this.init();
   },
};
</script>

<style lang="scss" scoped>
.user {
   text-overflow: ellipsis;
   white-space: nowrap;
   overflow: hidden;
   width: 55px;
}

.card-close {
   display: block;
   position: absolute;
   right: 10px;
   top: 10px;
   background-color: #fff;
   border-radius: 50%;
   cursor: pointer;
}

.logo {
   margin: 20px;
}

.text {
   display: flex;
   height: 300px;
   justify-content: center;
   align-items: center;
   letter-spacing: 0.4rem;
}

.star::before {
   content: ".";
   font-size: 60px;
   color: red;
   position: absolute;
   left: 30px;
   top: -15px;
}
</style>
