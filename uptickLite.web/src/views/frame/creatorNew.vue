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
    <div class="content">
        <div class="item-row featured-creators" v-if="FeaturedCreators.length>0">
            <span class="title">{{$t("homeFeaturedCreatorsTitle")}}</span>
            <div class="group">
                <div class="item" v-for="(item,index) in FeaturedCreators" :key="index">
                    <Avatar :size="71" :did="item.did" :hash="item.profilePhoto" showAvatarInfo />
                    <span class="txt">{{item.userName|substr10}}</span>
                </div>
            </div>
        </div>
        <div class="item-row best-sellers" v-if="BestSellers.length>0">
            <span class="title">{{$t("BestSellers")}}</span>
            <span class="subtitle">{{$t("Last30Days")}}</span>
            <div class="group">
                <div class="item" v-for="(item,index) in BestSellers" :key="index">
                    <span class="idx">{{index+1}}</span>
                    <Avatar :size="71" :did="item.did" :hash="item.profilePhoto" showAvatarInfo />
                    <span class="txt">{{item.userName|substr10}}</span>
                </div>
            </div>
        </div>
        <div class="item-row productive-creators" v-if="ProductiveCreators.length>0">
            <span class="title">{{$t("MostCreators")}}</span>
            <span class="subtitle">{{$t("Last30Days")}}</span>
            <div class="group">
                <div class="item" v-for="(item,index) in ProductiveCreators" :key="index">
                    <span class="idx">{{index+1}}</span>
                    <Avatar class="avatar" :size="71" :did="item.did" :hash="item.profilePhoto" showAvatarInfo />
                    <span class="txt">{{item.userName|substr10}}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import Avatar from "@/components/avatar/index.vue";
    import api from "@/api";

    export default {
        components: {
            Avatar
        },
        data() {
            return {
                FeaturedCreators: [],
                BestSellers: [],
                ProductiveCreators: [],
                showBox: false
            }
        },
        filters: {
            substr10: function(value) {
                if (value && value.length > 10) {
                    return value.substr(0, 10);
                }
                return value;
            }
        },
        async mounted() {
            let res = await api.home.creatorList({
                chainType: this.$store.state.chainType,
            });
            this.FeaturedCreators = res.data.recommendList.splice(0, 10);
            this.BestSellers = res.data.hotList.splice(0, 10);
            this.ProductiveCreators = res.data.mostWorkList.splice(0, 10);
            if (this.FeaturedCreators.userNamelength > 12) {
                if (this.creator && this.creator.userName.length > 12) {
                    this.username = this.creator.userName.substr(0, 12);
                }
            }
        },
    }
</script>

<style lang="scss">
    .item-row {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 20px;

        .title {
            font-weight: bold;;
            font-size: 25px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #270645;
            margin-top: 30px;
        }

        .subtitle {
            font-size: 15px;
        }

        .group {
            display: flex;
            flex-wrap: wrap;
            width: 760px;
            margin-top: 20px;
            margin-left: 5px;

            .item {
                display: flex;
                flex-direction: column;
                align-items: center;
                margin: 20px 10px;
                width: 130px;
                height: 131px;
                background-image: linear-gradient(#ffffff,
                        #ffffff),
                    linear-gradient(#6f58d9,
                        #6f58d9);
                background-blend-mode: normal,
                    normal;
                box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
                border-radius: 5px;
                padding-top: 20px;

                .idx {
                    width: 10px;
                    height: 20px;
                    font-weight: bold;;
                    font-size: 25px;
                    font-style: italic;
                    font-stretch: normal;
                    // line-height: 100px;
                    letter-spacing: 0px;
                    margin-bottom: 30px;
                }

                .txt {
                    display: block;
                    line-height: 0;
                    font-size: 15px;
                    color: #270645;
                    margin-top: 20px;
                }
            }
        }
    }

    .best-sellers .item {
        height: 171px !important;
        padding-top: 10px !important;

        .idx {
            color: #9846ff;
        }
    }

    .productive-creators .item {
        height: 171px !important;
        padding-top: 10px !important;

        .idx {
            color: #00ecbd;
        }
    }
</style>
