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
  <div :class="{ mobile: isMobile, rd: true }">
    <main
      @click="toCarddetail"
      v-for="(card, index) of cards"
      :key="index"
      :card="card"
    >
      <v-img
        :src="card.src"
        height="281px"
        width="281px"
        style="border-radius: 5px"
      />
      <article>
        <div class="title padding-20" v-text="card.title"></div>
        <div
          class="padding-20"
          v-if="card.isAvatar || card.collect "
        >
          <Avatar size="40">
            <v-img :src="card.avatarSrc" alt="avatar" />
          </Avatar>
          <span class="sub" v-text="card.name"></span>
        </div>
        <div class="count_box" v-if="!card.collect || ">
          <div v-if="card.hasLine" class="line"></div>
          <div class="flex-between-center padding-20">
            <div class="d-flex flex-column">
              <span
                v-text="card.bidName"
                class="name"
              ></span>
              <div class="flex-start-center">
                <div>
                  <v-img
                    :src="$walletIcon"
                    alt="logo"
                    width="22px"
                    height="22px"
                  />
                </div>
                <span class="count" style="line-height:22px;" v-text="card.bidCount"></span>
              </div>
            </div>
            <div class="d-flex flex-column">
              <span
                v-text="card.endingName"
                class="name"
              ></span>
              <span
                class="countdown"
                v-if="card.countdown != null "
                >06h 12m 08s</span
              >
            </div>
            <div v-if="card.isSoldout" class="sold_out">Sold out</div>
          </div>
        </div>
      </article>
    </main>
  </div>
</template>

<script>
// import countLogo from "@/assets/icons/chain_iris_w.png";

export default {
  props: {
    cards: {
      required: true,
      type: Array,
    },
  },
  data() {
    return {
      // countLogo,
    };
  },
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  methods: {
    toCarddetail() {
      this.cards.map((i) => {
        this.$router.push({ name: "Saledetail" });
      });
    },
  },
};
</script>
<style lang="scss" scoped>

.d-flex {
  display: flex;
}

.flex-between-center {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex-column {
  flex-direction: column;
}

.flex-start-center {
  display: flex;
  align-items: center;
}

.padding-20 {
  padding: 0 20px;
}

.rd {
  //   width: 281px;
  display: grid;
  grid-template-columns: repeat(auto-fill, 281px);
  grid-gap: 32px 32px;
  main {
    width: 281px;
    height: auto;
    overflow: hidden;
    padding-bottom: 18px;
    background-image: linear-gradient(#ffffff, #ffffff),
      linear-gradient(#ffffff, #ffffff);
    background-blend-mode: normal, normal;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.09);
    border-radius: 5px;
    cursor: pointer;

    .title {
      font-weight: bold;;
      font-size: 20px;
      padding-top: 15px;
      padding-bottom: 11px;
      letter-spacing: 0px;
      color: #270645;
	  white-space: nowrap;
	  overflow: hidden;
	  text-overflow: ellipsis;
    }

    .sub {
      font-family: Helvetica;
      font-size: 15px;
      padding-left: 15px;
      letter-spacing: 0px;
      color: #270645;
    }

    .line {
      height: 1px;
      border-bottom: solid 1px #e3e3e3;
      margin: 13px 0 23px 0;
    }

    .name {
      font-family: Helvetica;
      font-size: 12px;
      color: #270645;
      padding-bottom: 11px;
    }

    .count {
      padding-left: 8px;
    }

    .countdown,
    .count {
      font-weight: bold;;
      font-size: 15px;
      color: #270645;
      font-weight: bold;
    }

    .sold_out {
      width: 115px;
      height: 36px;
      background-image: linear-gradient(
          91deg,
          #1d42ff 58%,
          #9969fc 100%
        ),
        linear-gradient(#ffffff, #ffffff);
      background-blend-mode: normal, normal;
      border-radius: 18px;
      text-align: center;
      font-weight: bold;;
      font-size: 13px;
      font-weight: normal;
      font-stretch: normal;
      line-height: 36px;
      letter-spacing: 0px;
      color: #ffffff;
      position: relative;
      right: -35px;
    }
  }

  &.mobile {
    width: 100% !important;
    main {
      width: 100%;
    }
  }
}
</style>
