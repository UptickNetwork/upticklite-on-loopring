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
  <div class="root-home" v-if="!isMobile">
    <!-- <img class="image" src="@/assets/image/image1.jpg" /> -->

    <div class="content">
      <div class="left">
        <div class="tit">Build your NFT world on Loopring</div>
        <div class="desc">Uptick Lite is the community version of Uptick NFT marketplace, demonstrating the essential NFT operations on Loopring, the Ethereum zkRollup layer2. The community version is available to the public through the GPL open source license, contributed by Uptick Network, based on the strategic partnership between Uptick Network and Loopring. The support can be reached at <span @click="todiscord">Discord channel  <img  class="mb-n1" src="@/assets/icons/icon_discord.png" alt="" /></span> . Explore more commercial functions, visit <span @click="toLoopring"> Uptick on Loopring</span></div>
        <div class="btn">
          <button class="explore" @click="exploreClick">Explore</button>
          <button class="create" v-if="isDeposit == 3" @click="createClick">Create</button>
            <button class="create" v-else >Create</button>
        </div>
      </div>
      <div class="right">
        <img class="img" src="@/assets/image/home_lite_new1.jpg" />
      </div>
    </div>
    <uComponents ref="ucom"></uComponents>
  </div>
  <div class="mobile-home" v-else>
    <img class="img" src="@/assets/image/mhome_lite_new1.jpg" />
    <div class="content">
      <div class="tit">Build your NFT world on Loopring</div>
      <div class="desc">Uptick Lite is the community version of Uptick NFT marketplace, demonstrating the essential NFT operations on Loopring, the Ethereum zkRollup layer2. The community version is available to the public through the GPL open source license, contributed by Uptick Network, based on the strategic partnership between Uptick Network and Loopring. The support can be reached at <span @click="todiscord">Discord channel  <img  class="mb-n1" src="@/assets/icons/icon_discord.png" alt="" /></span> . Explore more commercial functions, visit <span @click="toLoopring"> Uptick on Loopring</span></div>
      <div class="btn">
        <button class="explore" @click="exploreClick">Explore</button>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "App",
  components: {

  },
  data: function () {
    return {
      isDeposit:''
    };
  },
  async mounted(){
	 this.isShowCreate = sessionStorage.getItem("showCreate");
	 this.isDeposit = sessionStorage.getItem("isDeposit");
  },
  methods: {
    exploreClick() {
      this.$router.push({ name: "Marketplace" });
    },
    toLoopring(){
      window.open("https://loopring.upticknft.com/index")
    },
     todiscord(){
      window.open("https://discord.gg/ZaPGHpsq")
    },
    async createClick() {

      this.$router.push({ name: "Create" });
    }
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
};
</script>

<style lang="scss" scoped>
.root-home {
  position: absolute;
  left: 0;
  right: 0;

  .image {
    width: 100%;
    height: auto;
    opacity: 0.1;
  }

  .bg_jb_w {
    position: fixed;
    top: 390px;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url(../../assets/image/bg_jb_w.png);
    background-size: 100% 100%;
  }

  .content {
    position: absolute;
    left: 0;
    right: 0;
    top: 50px;
    display: flex;
    justify-content: center;

    .left {
       position: relative;
          left: 110px;
      .tit {
        width: 550px;
        font-family: Helvetica;
        font-weight: bold;
        font-size: 53px;
        color: #270645;
        line-height: 60px;
         
  
      }

      .desc {
        width: 550px;
        font-family: Helvetica;
        font-size: 19px;
        letter-spacing: 1px;
        color: #270645;
        margin-top: 10px;
        span{
          color: #1d42ff;
          cursor: pointer;
        }
        img{
          width: 22px;
          height: 22px;
        }
      }
      .uptick{
         .text-title{
      font-size: 18px;
      color: #270645;
      a{
        color: #1d42ff;
        text-decoration: none;
      }
    }
      }

      .btn {
        width: 400px;
        display: flex;
        justify-content: space-between;
        margin-top: 24px;

        .explore {
          width: 181px;
          height: 51px;
          background-color: #270645;
          border-radius: 25px;
          font-family: Helvetica;
          font-weight: bold;
          font-size: 16px;
          color: #ffffff;
        }

        .create {
          width: 181px;
          height: 51px;
          background-color: #1d42ff;
          border-radius: 25px;
          font-family: Helvetica;
          font-weight: bold;
          font-size: 16px;
          color: #ffffff;
        }
      }
    }

    .right {
          margin-top: -60px;
      .img {
         width: 886px;
        height: 560px;
        background-color: #270645;
        border-radius: 5px;

      }
    }
  }
}

.mobile-home {
  width: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;



  .content {
    
    .tit {
      width: 100%;
      font-family: Helvetica;
      font-weight: bold;
      font-size: 32px;
      letter-spacing: 1px;
      color: #270645;
      margin-top: 30px;
      line-height: 35px;
    }

    .desc {
      width: 100%;
      font-family: Helvetica;
      font-size: 19px;

      color: #270645;
      margin-top: 10px;
      span{
          color: #1d42ff;
          cursor: pointer;
        }
        img{
          width: 22px;
          height: 22px;
        }
    }

    .btn {
      width: 100%;
      display: flex;
      justify-content: space-between;
      margin: 30px 0 40px 0;

      .explore {
        width: 100%;
        height: 51px;
        background-color: #270645;
        border-radius: 25px;
        font-family: Helvetica;
        font-weight: bold;
        font-size: 16px;
        color: #ffffff;
      }

    }
  }
}

.clearpos {
  position: static;
}
</style>
