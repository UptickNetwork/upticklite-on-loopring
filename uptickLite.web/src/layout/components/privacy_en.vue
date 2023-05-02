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
    <div class="title">Privacy Policy</div>
    <div class="prag">
      <div class="mb-12">
        Uptick on Loopring	 (hereinafter referred to as "us") focuses on protecting user
        personal information and personal privacy. This privacy policy contains
        the relevant terms of our collection, processing, storage, use and
        protection of your personal information. We want to give you a clear
        picture of how we treat your personal information through this privacy
        policy, so we recommend that you read this privacy policy in its
        entirety to help you understand how to maintain your privacy
        information. If you have any questions about this privacy policy, you
        can contact us through our published contact information.
        <strong class="str">
          If you do not agree with any of this privacy policy, you should
          immediately stop using our services. When you use any of the services
          we provide, you agree to our collection, processing, storage, use and
          protection of your personal information in accordance with this
          privacy policy.
        </strong>
      </div>
      <div class="mb-12">
        <div class="mb-6">1. Scope of application</div>
        <div class="mb-8">
          This privacy policy applies to all services provided by the Uptick on Loopring
          platform. This privacy policy applies to your access to the Uptick on Loopring
          platform and / or log-in clients using the services provided by Uptick.
        </div>
        <div class="mb-8">
          The "Uptick on Loopring platform" in this policy includes, referred to but not
          limited to, the Uptick Loopring client.
        </div>
        <div class="mb-8">
          All our services are subject to this privacy policy. In addition, we
          will develop specific privacy policies for certain specific services
          to more specifically explain how we collect, process, store, use and
          protect your information in this specific service. The privacy policy
          for this particular service forms part of this privacy policy.<strong
            class="str"
            >If the privacy policy of a particular service is inconsistent with
            this privacy policy, the privacy policy of that particular service
            applies.</strong
          >
        </div>
        <div class="mb-8">
          <strong class="str"
            >This policy does not apply to information collected by other
            third-party services ("other third parties", including, but not
            limited to, your eounterparties to the transaction, any third-party
            websites, and third-party service providers) accessed through our
            services. We are not responsible for the actions and consequences of
            the use of your personal information by the aforementioned third
            parties.</strong
          >
          Please note that other third parties may have their own privacy
          protection policies; when you view pages created by other third
          parties or use applications developed by other third parties, you may
          find that the pages or Cookies or pixel tags. These cookies or tags
          are not under our control and their use is not governed by this
          policy.
        </div>
      </div>
      <div class="mb-12">
        <div class="mb-6">2. How we collect your personal information</div>
        <div class="mb-8">
          We collect information to comply with laws and regulations, provide
          you with better and more personalized services, and strive to improve
          your user experience. By providing us with the following information
          or using our services, you agree to our collection of information in
          accordance with this policy.
        </div>
        <div class="mb-8">
          In order to comply with the requirements of laws and regulations, we
          need you to authorize some information in order to provide you with
          basic functions related to the wallet and provide more personalized
          and convenient services. We only collect and use the necessary
          information.
        </div>
        <div class="mb-8">
          Based on your authorization, we may obtain you the following
          information: Device ID information (get the device ID and push you
          asset change notifications) , Albums (add logo images when you create
          your own token), storage (cache text, images, etc. to view the page
          more fluidly),Location information (recommended nearby activities),
          etc., you have the right to refuse or cancel authorization;
        </div>
        <div class="mb-8">
          Please note that any text, photo, video and other forms of information
          you submit during the content publishing process on the Uptick Loopring
          platform may also contain or be associated with your personal
          information. We will keep your information secure and we will not
          obtain, share or provide your information to third parties without
          your consent.
        </div>
        <div class="mb-16">
          You can choose not to provide certain information, but this may
          prevent you from using some of our featured services.
        </div>
        <div>
          Please understand that we use the information you provide to respond
          to your requests, to facilitate your enjoyment of the services we
          provide, to improve our website and to communicate with you.
        </div>
      </div>
      <div class="mb-12">
        <div class="mb-6">3. How we protect your personal information</div>
        <div class="mb-8">
          We will use a variety of reasonable physical, electronic, and
          administrative security measures to protect your personal information
          so that your personal information is not leaked, damaged, or lost. We
          will provide corresponding security measures to protect your
          information and provide reasonable security guarantees according to
          the existing technology. We will try our best to ensure that your
          information is not leaked, damaged or lost.
        </div>
      </div>
      <div class="mb-12">
        <div class="mb-6">4. Notifications and amendments</div>
        <div class="mb-8">
          To provide you with better services, our business will change from
          time to time, and this privacy policy will be adjusted accordingly. We
          will remind you of related content updates by issuing updated versions
          on our website, mobile terminal, or other means, and please visit us
          to stay up-to-date with the latest privacy policy. In the foregoing
          case, if you continue to use our services, you agree to accept and be
          bound by this revised policy.
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  mounted() {
      window.eventBus.$on('LangChange', this.onLangChange);
  },
  beforeDestroy() {
    window.eventBus.$off("LangChange", this.onLangChange);
  },
  methods: {
      onLangChange() {
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
      }
  }
}
</script>

<style lang="scss" scoped>
.contain {
  max-width: 806px;
  margin: 0 auto;
  .title {
    margin-top: 80px;
    margin-bottom: 50px;
    font-weight: bold;;
    font-size: 55px !important;
    font-weight: bold;
    line-height: 60px;
    font-stretch: normal;
    letter-spacing: 0px;
    color: #270645;
    text-align: center;
  }
  .prag {
    .mb-12 {
      font-family: Helvetica;
      font-size: 16px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
      .str {
        font-weight: bold;;
        font-style: italic;
        font-size: 16px;
        letter-spacing: 0px;
        color: #270645;
      }
      .mb-6 {
        font-weight: bold;;
        font-size: 30px;
        font-weight: bold !important;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }
    }
  }
}
</style>
