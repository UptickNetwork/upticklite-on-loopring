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
    <div :class="{ box: true, mobile: isMobile }">
      <div class="title">{{$t('FooterHelpCenter')}}</div>
      <div class="content d-flex flex-lg-row flex-column">
        <img class="mr-lg-8" src="@/assets/image_faq.png" alt="" />
        <div class="con pt-lg-12">
          <div class="tit mb-lg-7">
            {{$t('HelpCenterContent')}}
          </div>
          <div class="link d-flex flex-column flex-lg-row">
            <a href="https://www.uptick.network/#/" target="_blank"
              >Uptickproject {{$t('HelpCenterWebsite')}},</a
            >
            <a href="https://blog.uptickproject.com/" target="_blank"
              >Uptickproject {{$t('HelpCenterblog')}},</a
            >
            <a :href="`mailto:${mailurl}`" target="_blank"
              >{{$t('HelpCenterMail')}}.</a
            >
          </div>
        </div>
      </div>
    </div>
</template>

<script>
export default {
  name: "HelpPage",
  data() {
    return {
      mailurl: "hello@uptickproject.com",
    };
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
};
</script>

<style lang="scss" scoped>
  .box {
    min-height: 741px;
    margin-left: 267px;
    .title {
      margin-top: 80px;
      margin-bottom: 107px;
      margin-left: 167px;
      font-weight: bold;;
      font-size: 60px !important;
      font-weight: bold;
      font-stretch: normal;
      line-height: 64px;
      letter-spacing: 0px;
      color: #270645;
    }

    .content {
      img {
        display: block;
        width: 167px;
        height: 162px;
      }
      .con {
        .tit {
          max-width: 578px;
          font-family: Helvetica;
          font-size: 16px;
          font-weight: normal;
          font-stretch: normal;
         //  line-height: 30px;
          letter-spacing: 0px;
          color: #270645;
        }
        .link {
          font-weight: bold;;
          font-style: italic;
          font-size: 16px;
          font-weight: bold;
          letter-spacing: 0px;
          color: #270645;
        }
      }
      a {
        text-decoration: none;
      }
    }
  &.mobile {
     margin:0;
    .title{
       width:100%;
       margin:0;
    }
    .content{
       width:100%;
       .con{
          .tit{
             width:100%;
          }
       }
    }
  }
}
</style>
