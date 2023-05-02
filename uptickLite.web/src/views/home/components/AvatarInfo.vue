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
            <v-avatar size="70">
               <v-img
                   src="https://cdn.vuetifyjs.com/images/john.jpg"
                   alt="Midoriya"
               />
            </v-avatar>
            <div class="flex-center-start flex-column" style="margin-left: 20px">
               <span class="name">Midoriya</span>
               <span class="No">iaaue78***q23e</span>
            </div>
         </div>
         <v-btn class="follow-btn">Follow</v-btn>
      </header>
      <article>
         <p class="desc pading-gap">
            Artist living near water. Slow contemplations of space, form, and time
            manifest as digital visions.time manifest as digital visions
         </p>
         <div class="flex-start-center pading-gap">
            <div class="flex-center-start flex-column">
               <div class="num">213</div>
               <div class="sub">Fans</div>
            </div>
            <div class="flex-center-start flex-column followers">
               <div class="num">2123</div>
               <div class="sub">Followers</div>
            </div>
            <div class="flex-center-start flex-column">
               <div class="num">23</div>
               <div class="sub">Works</div>
            </div>
         </div>
         <div class="line"></div>
      </article>
      <footer class="pading-gap flex-start-center">
         <span class="avatar-name">Maple</span>
         <div style="position: relative; height: 30px">
            <div
                class="avatar"
                :style="{ left: `${index * 18}px` }"
                v-for="(info, index) of avatars"
                :key="index"
                @click="toPage"
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
   export default {
      data() {
         return {
            avatars: [
               {
                  src: "https://cdn.vuetifyjs.com/images/john.jpg",
               },
               {
                  src: "https://cdn.vuetifyjs.com/images/lists/2.jpg",
               },
               {
                  src: "https://cdn.vuetifyjs.com/images/logos/v.png",
               },
               {
                  src: "https://cdn.vuetifyjs.com/images/john.png",
               },
            ],
         };
      },
      methods: {
         toPage() {
            this.$router.push({name: "Otherpage"});
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
      }

      .No {
         font-family: Helvetica;
         font-size: 11px;
         color: #270645;
         padding-top: 4px;
      }

      .desc {
         text-overflow: -o-ellipsis-lastline;
         overflow: hidden;
         text-overflow: ellipsis;
         display: -webkit-box;
         -webkit-line-clamp: 2;
         line-clamp: 2;
         -webkit-box-orient: vertical;
         font-family: Helvetica;
         font-size: 14px;
         color: #270645;
         margin-top: 14px;
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

   .follow-btn {
      width: 85px;
      height: 41px;
      color: #270645;
      font-size: 15px;
      border-radius: 20px;
   }

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
      }
   }
</style>
