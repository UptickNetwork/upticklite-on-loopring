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
        <v-card class="card d-flex flex-column">
            <img class="clo mb-6" @click="closeDialog" src="@/assets/icons/btn_close.png" alt="" />
            <div class="box py-10 px-8">
                <v-carousel v-if="items.length>0" hide-delimiters cycle interval="3000" show-arrows-on-hover>
                    <v-carousel-item v-for="(item, i) in items" :key="i" :src="item.src" class="fill-height"
                        style="height: 100%;"></v-carousel-item>
                </v-carousel>
            </div>
        </v-card>
    </div>
</template>
<script>
    import {
        getFileSrc
    } from "@/utils/file";
    const WORK_KEY = "WORK";

    export default {
        props: {
            list: {
                type: Array
            }
        },
        data: () => ({
            open: true,
            items: [{
                    src: "https://cdn.vuetifyjs.com/images/carousel/squirrel.jpg",
                },
                {
                    src: "https://cdn.vuetifyjs.com/images/carousel/sky.jpg",
                },
                {
                    src: "https://cdn.vuetifyjs.com/images/carousel/bird.jpg",
                },
                {
                    src: "https://cdn.vuetifyjs.com/images/carousel/planet.jpg",
                },
            ],
        }),
        mounted() {
            this.items = [];
            this.list.forEach(async v => {
                let img = await getFileSrc(WORK_KEY, v.imgUrl);
                this.items.push({
                    src: img
                })
            })
        },
        methods: {
            subCommit() {},
            closeDialog() {
                this.open = false;
                this.$emit("getOpen", this.open);
            },
        },
    };
</script>
<style lang="scss" scoped>
    .card {
        position: fixed;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(0, 0, 0, 0.7);
        z-index: 999;
        overflow: hidden;

        .clo {
            position: absolute;
            right: 60px;
            top: 60px;
            width: 30px;
            height: 31px;
            background-color: #fff;
            border-radius: 50%;
            margin-right: 0;
            cursor: pointer;
            border-radius: 50% !important;
            z-index: 99;
        }

        .box {
            width: 100%;
            height: 100%;
        }
    }
</style>


<style>
    .otherpage .v-carousel {
        height: 100% !important;
        /* border-radius: 40px; */
    }

    .otherpage .v-image {
        height: 100% !important;
    }

    .otherpage .v-image__image--cover {
        background-size: contain !important;
    }
</style>
