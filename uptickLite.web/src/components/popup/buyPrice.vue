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
  <div id="container">
    <v-card id="card" :class="{ card: true, mobile: isMobile }">
      <img class="clo" @click="closeDialog" src="@/assets/btn_close.png" alt="" />
      <div class="top">
        <span class="title"> {{ $t("popupBuyPriceBuyNow") }}</span>
        <h3 class="content">
          {{ $t("popupBuyPriceAfterSuccessful") }}
        </h3>
        <div class="form">
          <span class="titles">{{ $t("popupBuyPricePrice") }}</span>
          <v-text-field v-model="fixPrice" outlined :suffix="marketNftInfo.marketTokenType" type="input" class="textinput"
		     readonly
            style="margin-top: 8px"></v-text-field>
        </div>
        <div class="realPrice">
          <span class="feePrice">Loopring fee <span class="priceContent">{{feePrice}} {{ marketNftInfo.marketTokenType }}</span></span> 
          <span class="totalPrice">Total price <span class="priceContent">{{totalPrice}} {{ marketNftInfo.marketTokenType }}</span></span> 
      </div>
      </div>


      <div class="footer">
        <button class="sub v-btn uc" :class="{ 'sub-dis': isPay }" v-if="isLoad">
          {{ $t("loadBalance") }}
        </button>

        <button class="sub v-btn uc" :class="{ 'sub-dis': isPay }" submit @click="subCommitMarket" v-else-if="!needToShowLink">
          {{ $t("popupTransferCommit") }}
        </button>
        <button class="subdefect" v-else-if="needToShowLink" disabled="disabled">
          {{ $t("popupAuctionDefectPriceInsufficientFunds") }}
        </button>

        <span class="price">{{ $t("popupNewTransferBalance") }}：{{ amountFormat }} {{ marketNftInfo.marketTokenType }} </span>
         <span class="price" v-if="lockValue >0"> L2 Wallet Frozen amount：{{ lockValue }} {{ marketNftInfo.marketTokenType }} </span>

        <span v-if="needToShowLink" class="lrcLink"> {{ $t("insufficientBalance")}}
          <a target="_blank" :href="loopringLink" >{{ $t("exchangeNow")}}</a>
        </span>
        <span v-else class="lrcLink">
        </span> 

      </div>
    </v-card>
    <uComponents ref="ucom"></uComponents>
  </div>
</template>
<script>
import api from "@/api";
import {getFeePrice} from "../../utils/helper";
export default {
  data: () => ({
    newOpen: true,
    fixPrice: "",
    amountFormat: "",
    isPay: false,
    isLoad: false,
    chainToken: "",
    needToShowLink:false,
    loopringLink:"",
    feePrice:"",
    totalPrice:"",
    StorageId:'',
    lockValue:'',
  }),
  computed: {
    isMobile: function () {
      return this.$vuetify.breakpoint.mobile;
    },
  },
  props: {
    marketNftInfo: {
      type: Object,
    },
    userInfo: {
      type: Object,
    },
    Nft_sku: {
      type: Object,
    },
    assetLevel: {
      type: Number,
    },
    tokenId: {
      type: String,
    },

    assetId: {
      type: Number,
    },
    onsaleOwner: {
      type: String,
    },
    priceSecond: {
      type: String,
    },
    minprice: {
      type: String,
    },
    buytype: {
      type: Number,
    },
    owerfrom: {
      type: String,
    },
  },
  filters: {
    undef: function (value) {
      if (value != null && value != "null" && value != "underfined") {
        return "";
      }
      return value;
    }
  },
  async mounted() {
    this.isLoad = true;
    this.isPay = true
    this.fixPrice = this.marketNftInfo.marketPrice;
    let res = await this.LRCHandler.getOrderFee(
          this.marketNftInfo.contractAddress,
          this.marketNftInfo.marketTokenType,
         
        );  
    let tradeCost = res.data.amounts[0].tradeCost; 
	 this.chainToken = this.getChainTokenId(this.marketNftInfo.marketTokenType);
    this.feePrice = this.maxDecimal(getFeePrice(this.fixPrice,this.chainToken,tradeCost).feePrice)
    this.totalPrice = this.maxDecimal(parseFloat(this.feePrice) + parseFloat(this.fixPrice))
    let amount = await this.LRCHandler.getUserBalances(this.chainToken);
   this.amountFormat = Number(this.maxDecimal(Number(amount.amountValue) - Number(amount.lockedValue))  )
    this.lockValue = Number( this.maxDecimal(amount.lockedValue))
    this.isBalance = true;

   let StorageIdObj= await this.LRCHandler.getStorageId(this.chainToken);
   this.StorageId = StorageIdObj.orderId
    if(parseFloat(this.fixPrice) > parseFloat(this.amountFormat) || parseFloat(this.totalPrice) > parseFloat(this.amountFormat)){
      this.needToShowLink = true;
      this.loopringLink = "https://loopring.io/#/trade/lite/"+ this.marketNftInfo.marketTokenType + "-ETH";
    }

    this.isLoad = false;
    this.isPay = false	
  },
  methods: {
    async subCommitMarket() {
        try {
          this.isPay = true
          let resTokenId=await api.home.getNftTokenId({"owner":this.marketNftInfo.marketOwner,"tokenAddress": this.marketNftInfo.contractAddress,"nftId": this.marketNftInfo.nftId});
          if(resTokenId==null||resTokenId.data==null){
          this.$toast("error", "invalid NFT");
          return ;   
        }
      let res = await this.LRCHandler.getOrderFee(
          this.marketNftInfo.contractAddress,
          this.marketNftInfo.marketTokenType,
         
        );  
      let tradeCost = res.data.amounts[0].tradeCost;    
        let signature = await this.LRCHandler.takerOrder(
          resTokenId.data,
          this.marketNftInfo.nftData,
          "1",
          this.chainToken,
          this.fixPrice.toString(),
          getFeePrice(this.fixPrice,this.chainToken,tradeCost).tradeCostPrice,
          this.StorageId
        );
        console.log("wxl --- sewew",signature)
        if(!signature){
          this.$toast("error", "Signature request was rejected");
               this.isPay = false;
         return
      }
       
        let makerOrderJson = {
          "exchange": signature.exchange,
          "accountId": signature.accountId,
          "storageId": signature.storageId,
          "sellToken": signature.sellToken,
          "buyToken": signature.buyToken,
          "allOrNone": signature.allOrNone,
          "fillAmountBOrS": signature.fillAmountBOrS,
          "validUntil": signature.validUntil,
          "maxFeeBips": signature.maxFeeBips
        };
        let parms = {
          makerOwner:this.marketNftInfo.owner,
          owner: this.$store.state.did,
          accountId: this.userInfo.accountId,
          nftAddress: this.marketNftInfo.contractAddress,
          nftId: this.marketNftInfo.nftId,
          takerOrderJson: JSON.stringify(makerOrderJson),
          signature: signature.eddsaSignature,
          amount: 1
        }
        let trade = await api.home.nftTrade(parms);
    
        if (trade.success) {
         this.isPay = false
          this.closeDialog();
            this.$router.push({ name: 'Marketplace' });
          let t = this.$t("buySuccess")
					let reg=new RegExp('【N】','g')//g代表全部
					let regN=new RegExp('<br/>','g')//g代表全部
					let name = "【" + this.marketNftInfo.name + "】"
					let title=t.replace(reg,name).replace(regN," ");
  
		  //  });
        
            setTimeout(()=>{
                this.$mtip({
                title:title,
              });
             },2000)
        
        
        }else{
        this.$toast("error",trade.msg ).then(()=>{
        this.isPay = false
        this.closeDialog();
        });
        }
      } catch (error) {
        console.log(error)
        this.$toast("error", this.$t("buyFailed"));
        this.isPay = false
      }
    },
    

maxDecimal(number){
  return String(number).replace(/^(.*\..{4}).*$/, "$1")
},
    closeDialog() {
      this.newOpen = false;
      this.$emit("getOpen", this.newOpen);
    },
  },
};
</script>
<style lang="scss" scoped>
.card {
  display: flex;
  flex-direction: column;
  min-height: 400px;
  width: 600px;

  .top {
    display: flex;
    flex-direction: column;
    margin-left: 52px;

    .title {
      margin: 42px 0 0 0;
      width: 300px;
      height: 26px;
      font-weight: bold;;
      font-size: 25px;
      font-weight: bold;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #270645;
    }

    .content {
      margin: 10px 0 20px 0;
      width: 467px;
      height: 41px;
      font-family: Helvetica;
      font-size: 15px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #766983;
    }

    .form {
      .titles {
        margin-top: 24px;
        width: 37px;
        height: 13px;
        font-weight: bold;;
        font-size: 15px;
        font-weight: bold;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #270645;
      }

      .textinput {
        width: 500px;
        height: 75px;
        background-image: linear-gradient(#ffffff, #ffffff),
          linear-gradient(#f8f6fd, #f8f6fd);
        background-blend-mode: normal, normal;
        border-radius: 5px;
        
      }
    }
  }

  .clo {
    position: absolute;
    right: 10px;
    top: 10px;
    width: 30px;
    height: 31px;
    background-color: #fff;
    border-radius: 50%;
    margin-right: 0;
    cursor: pointer;
  }

  .footer {
    display: flex;
    flex-direction: column;
    text-align: center;
  

    .price {
      display: block;
      font-family: Helvetica;
      font-size: 13px;
      font-weight: normal;
      font-stretch: normal;
      color: #766983;
    }

    .sub {
      margin: 10px auto;
      width: 450px;
      height: 51px;
      background-blend-mode: normal, normal;
      border-radius: 25px;
      opacity: 0.9;
      font-size: 20px;
      color: #ffffff;
    }

    .subdefect {
      margin: 10px auto;
      width: 450px;
      height: 51px;
      background-image: linear-gradient(#766983, #766983),
        linear-gradient(#270645, #270645);
      background-blend-mode: normal, normal;
      border-radius: 25px;
      opacity: 0.9;
      font-size: 20px;
      color: #ffffff;
    }
  }

  &.mobile {
    width: 100%;
    .realPrice {
      display: flex;
      flex-direction: column;
      .totalPrice{
        margin-left:unset;
      }
    }
    .top {
      margin: 0 40px;

      .content {
        width: 100%;
        height: auto;
        word-wrap: break-word;
        margin-bottom: 20px;
      }
    }

    .footer {
      .sub {
        width: 90%;
      }
    }
  }
}

img {
  display: inline-block;
  width: 126px;
  height: 126px;
  background-color: #8a73ff;
  border-radius: 5px;
  margin: 1px 22px 10px 52px;
}

.sub-dis {
  position: relative;
  pointer-events: none;
  background-image: linear-gradient(#766983,
      #766983),
    linear-gradient(#270645,
      #270645) !important;
  background-blend-mode: normal,
    normal;
  border-radius: 25px;
  opacity: 0.9;
}

.sub-dis::after {
  content: "";
  background-image: url(../../assets/loading.gif);
  background-size: 100%;
  display: inline-block;
  position: absolute;
  width: 20px;
  height: 20px;
  margin-left: 190px;
}

.lrcLink{
  margin-top: 10px;
}

.totalPrice{
  margin-left:20px;
}

.priceContent{
  font-weight:bold;

}
</style>




