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
<div class="foot">
   <v-footer
    app
    absolute
    class="pa-0 pb-5 pb-lg-5 d-flex flex-column flex-lg-row"
    color="#1d42ff"
    :elevation="0"
  >
    <v-container
      class="
        d-flex
        flex-column flex-lg-row
        align-center
        py-0
        px-10 px-lg-0
        mt-3 mt-lg-5
      "
    >
     
      <template v-if="isMobile">
        <div >
          <Logo height="43px" />
        </div>
         
        <div class="d-flex justify-space-around " style="width: 100%">
          <a :href="`http://${mediumurl}`" target="_blank">
            <img
              src="@/assets/icons/icon_medium.png"
              style="width: 30px; height: 30px"
            />
          </a>
          <a :href="twitterurl" target="_blank">
            <img
              src="@/assets/icons/icon_twitter.png"
              class="ml-8"
              style="width: 30px; height: 30px"
            />
          </a>
          <a :href="teleurl" target="_blank">
            <img
              src="@/assets/icons/icon_telegram.png"
              class="ml-8"
              style="width: 30px; height: 30px"
            />
          </a>
          <a :href="`mailto:${mailurl}`" target="_blank">
            <img
              src="@/assets/icons/icon_mail.png"
              class="ml-8"
              style="width: 30px; height: 30px"
            />
          </a>
        </div>
      </template>
      <template v-else>
        <div class="d-flex justify-space-around" style="width:100%">
            <Logo height="43px" />
        <v-spacer></v-spacer>
        <div class="d-flex flex-row">
            <div class="about d-flex flex-row" style="font-weight:bold">
          <a href="https://www.uptick.network/#/" target="_blank" style="font-weight:bold">{{$t('FooterAbout')}}</a>
          <a class="ml-10" href="https://blog.uptickproject.com/" target="_blank" style="font-weight:bold">{{$t('FooterBlog')}}</a>
        </div>
        
             <a :href="`mailto:${mailurl}`" target="_blank">
            <img
              src="@/assets/icons/icon_mail.png"
              class="ml-8"
              style="width: 28px; height: 28px"
            />
          </a>
        <a :href="`https://${mediumurl}`" target="_blank">
          <img
            src="@/assets/icons/icon_medium.png"
            class="ml-8"
            style="width: 30px; height: 30px"
          />
        </a>
        <a :href="twitterurl" target="_blank">
          <img
            src="@/assets/icons/icon_twitter.png"
            class="ml-7"
            style="width: 30px; height: 30px"
          />
        </a>
        <a :href="teleurl" target="_blank">
          <img
            src="@/assets/icons/icon_telegram.png"
            class="ml-6"
            style="width: 30px; height: 30px"
          />
        </a>
        </div>
        </div>
       
      

         
      </template>
    </v-container>
  </v-footer>
</div>
 
</template>

<script lang="js">
  import Logo from "@/components/footLogo/index.vue";

  export default {
    name: "Footer",
    components: { Logo },
    props: {},
    data: function() {
      return {
        mediumurl: "uptickproject.medium.com",
        twitterurl:'https://twitter.com/Uptickproject',
        teleurl:'https://t.me/uptickproject',
        mailurl:'hello@uptickproject.com'
      };
    },
    computed: {
      isMobile: function() {
        return this.$vuetify.breakpoint.mobile;
      }
    },
    methods: {
      openmedium() {
        window.open().opener = null;
        window.location.href = this.url;
      },
      opentwitter() {
        window.open().opener = null;
        window.location.href = this.url;
      },
      opentelegram() {
        window.open().opener = null;
        window.location.href = this.url;
      },
      toHelp(){
        this.$router.push({name:'HelpCenter'})
      },
      toPrivacy(){
        if(this.$vuetify.lang.current == 'en'){
          this.$router.push({name:'Privacy'})
        }else if(this.$vuetify.lang.current == 'zh'){
          this.$router.push({name:'Privacyzh'})
        }else if(this.$vuetify.lang.current == 'ja'){
          this.$router.push({name:'Privacy'})
        }else if(this.$vuetify.lang.current == 'ko'){
          this.$router.push({name:'Privacy'})
        }else if(this.$vuetify.lang.current == 'pt'){
          this.$router.push({name:'Privacy'})
        }
      },
      toTerms(){

          if(this.$vuetify.lang.current == 'en'){
          this.$router.push({name:'Term'})
        }else if(this.$vuetify.lang.current == 'zh'){
          this.$router.push({name:'Termzh'})
        }else if(this.$vuetify.lang.current == 'ja'){
          this.$router.push({name:'Term'})
        }else if(this.$vuetify.lang.current == 'ko'){
          this.$router.push({name:'Term'})
        }else if(this.$vuetify.lang.current == 'pt'){
          this.$router.push({name:'Term'})
        }

      }
    }
  };
</script>

<style lang="scss" scoped>

a {
  text-decoration: none;
  font-weight: normal;
  color: #ffffff !important;
}
.pointer {
  color: #ffffff !important;
  cursor: pointer;
}
.fotnav {
  max-width: 1220px;
  margin: 25px auto 16px;

  .left {
    font-family: Helvetica;
    font-size: 15px;
    font-weight: normal;
    font-stretch: normal;
    line-height: 24px;
    letter-spacing: 0px;
    color: #270645;
    .pointer {
      cursor: pointer;
    }
  }
  .right {
    font-family: Helvetica;
    font-size: 15px;
    font-weight: normal;
    font-stretch: normal;
    line-height: 24px;
    letter-spacing: 0px;
    color: #270645;
    text-align: right;
    .pointer {
      cursor: pointer;
    }
  }
}
.fotmobile {
  width: 320px;
  margin: 25px auto 16px;

  .left {
    font-family: Helvetica;
    font-size: 15px;
    font-weight: normal;
    font-stretch: normal;
    line-height: 24px;
    letter-spacing: 0px;
    color: #270645;
    .pointer {
      cursor: pointer;
    }
  }
  .right {
    font-family: Helvetica;
    font-size: 15px;
    font-weight: normal;
    font-stretch: normal;
    line-height: 24px;
    letter-spacing: 0px;
    color: #270645;
    text-align: right;
    .pointer {
      cursor: pointer;
    }
  }

  .base-box{
    width: 100%;
    margin-bottom: -40px;
  }

  .top-item{
    font-weight: 600;
    margin-bottom: 20px;
    text-align: center;
  }

  .bottom-item{
    margin-bottom: 20px;
    text-align: center;
  }

  .last-item{
    text-align: center;
  }
}
</style>
