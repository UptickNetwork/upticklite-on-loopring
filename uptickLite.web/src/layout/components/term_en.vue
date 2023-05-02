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
    <div class="title">Copyright Notice</div>
    <div class="prag">
      <div class="items">
        <div class="mb-7">1</div>
        <div class="mb-12">
          Any pictures, graphics, fonts, texts and other design materials
          displayed and disseminated in Uptick on Loopring are created and uploaded by users
          of the application. Users are requested to ensure that they abide by
          the copyright of the uploaded content, or have the relevant
          authorization. Without permission from the original content creator,
          users may not modify, adapt, or compile other people's work, and may
          not create or distribute secondary works based on the works displayed
          in Uptick on Loopring. All adverse consequences arising from the user's improper
          use, or beyond the corresponding authorized scope shall be the
          responsibility of the user, and Uptick on Loopring will not be liable for this.
        </div>
      </div>
      <div class="items">
        <div class="mb-7">2</div>
        <div class="mb-12">
          Uptick on Loopring does not control the copyright, patent rights, trademark
          rights, trade secrets and other intellectual property rights of
          related materials uploaded by users. And does not assume any economic
          losses and related responsibilities caused by the content uploaded by
          users' copyright disputes.
        </div>
      </div>
      <div class="items">
        <div class="mb-7">3</div>
        <div class="mb-12">
          Uptick on Loopring will regularly inspect the content created and uploaded by
          users, and has the right to deal with illegal content involving
          infringement, falsehood, pornography, violence, immorality, excessive
          advertising, etc. Uptick on Loopring reserves the rights to delete relevant
          content without notifying the corresponding publishing user in
          advance. After processing, the content cannot be viewed in the app,
          but will remain in the personal account.
        </div>
      </div>
      <div class="items">
        <div class="mb-7">4</div>
        <div class="mb-12">
          Uptick on Loopring encourages users to supervise the works in the app. Uptick on Loopring has
          clearly informed users not to infringe the legal rights of others, and
          has set up professionals to conduct regular/irregular reviews of the
          works displayed in Uptick on Loopring. However, due to the large amount of content
          uploaded in Uptick on Loopring, it is impossible to exclude all the works with
          defects of ownership. If you find that there are works infringing on
          your, or any third-party intellectual property rights, you can file a
          complaint in the app, or contact us at service@Uptick on Loopringproject.com with
          detailed information. We will verify and deal with it within a
          reasonable time after receiving your feedback.
        </div>
        <div class="mb-12">
          The feedback you submit should include the following materials:
        </div>
        <div class="mb-12">
          (1)Uptick on Loopring account information, including personal information and
          contact information.<br />
          (2)Links, names and related information of related works that you need
          to complain about.<br />
          (3)If you are the original creator, provide relevant ownership
          materials (including, but not limited to, first publication records,
          copyright registration certificates, work source documents, etc.) that
          can initially prove that you own the rights to the content in
          question. If you are a third party, please provide an explanation on
          the situation.
        </div>
      </div>
      <div class="items">
        <div class="mb-7">5</div>
        <div class="mb-12">
          Without the permission of Uptick on Loopring, no one is allowed to steal the works
          displayed and disseminated by Uptick on Loopring through crawler programs,
          scripts, robot programs, etc., and must not deliberately bypass the
          network barriers set by Uptick on Loopring in order to violate Uptick on Loopring's legitimate
          rights and interests; do not copy or imitate Uptick on Loopring's website
          implementation. Infringement, disassembly, decompilation, reverse
          engineering, etc., of Uptick on Loopring's source code and website technical
          measures, is not allowed.
        </div>
      </div>
      <div class="items">
        <div class="mb-7">6</div>
        <div class="mb-12">
          Please ensure that you have fully understood and agreed to the
          relevant statements in this document. If you do not accept some or all
          of the terms in this statement, please stop using the Uptick on Loopring service
          immediately. Your use of the Uptick on Loopring service is deemed to be known to
          you and agree to abide by the relevant terms of Uptick on Loopring.
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
    line-height:60px;
    font-stretch: normal;
    letter-spacing: 0px;
    color: #270645;
    text-align: center;
  }
  .prag {
    .items {
      .mb-7 {
        font-weight: bold;;
        font-size: 30px;
        font-weight: bold !important;
        font-style: italic;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }
      .mb-12 {
        font-family: Helvetica;
        font-size: 16px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }
    }
  }
}
</style>
