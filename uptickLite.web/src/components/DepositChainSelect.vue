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
    <div class="content" :class="{ showInput: input }">
        <div class="select" @click="coinSelect" @mouseover="isMouseOver=true" @mouseout="isMouseOver=false">
            <img :src="showIcon" />
            <span class="txt">{{ coinData[coinIndex].name }}</span>
            <span class="icon">▼</span>
        </div>
        <div class="list" :class="{ reposition: Reposition }" :style="{ display: coinOpen }">
            <input ref="listBlur" type="text" style="width:0px;position:absolute;" @blur="coinListBlur" />
            <div class="item" v-for="(item, index) in coinData" :key="index" @mousedown="coinItemClick(item, index )">
                <img :src="item.img" />
                <span class="txt">{{ item.name }}</span>
            </div>
        </div>
        <input v-if="input" v-model.number="inputValue" type="text" placeholder="0.0000" @input="updateVal">
    </div>
</template>

<script lang="js">
import api from "@/api";
import { json } from "body-parser";

export default {
    name: 'ChainSelectInput',
    props: {
        input: {
            type: Boolean,
            default: false
        },
        ChainValue: Object,
        Reposition: Boolean
    },
    data: function () {
        return {
            coinData: [
                { name: "ETH", img: "https://loopring.mypinata.cloud/ipfs/QmaphjgDNJCqau5opCuhjeafVGiRnzQTUpSHTL3LJueGLW" },
                { name: "LRC", img: "https://loopring.mypinata.cloud/ipfs/QmZbAKL9SVf1Kze3XNTNFnFCpiBpTCiZHWyLU3Kn4vd9Uy" },
                { name: "USDT", img: "https://loopring.mypinata.cloud/ipfs/QmbvRtVfpiF7GVZW6H33yqxUzUGD5depFh475KQE3Qs9Yk" },
                { name: "DAI", img: "https://loopring.mypinata.cloud/ipfs/QmNzEHgp41ku6ZtgKPti4JerKWkWWLgvn5nDmy2CQX8e6L" },
                { name: "USDC", img: "https://loopring.mypinata.cloud/ipfs/QmYmmDe1oaVJRWW9Ms7Bzs5XwAxT1Javt3BJVxJxXzYRcA" },
            ],
            coinOpen: "none",
            coinIndex: 0,
            inputValue: "",

            isMouseOver: false
        }
    },
    computed: {
        showIcon() {
            if(this.coinData.length > 1) {
                return this.coinData[this.coinIndex].img;
            }
            return "";
        }
    },
    async mounted() {
        this.coinIndex = 1;
    },
    methods: {
        coinSelect(e) {
            if (this.coinOpen == "none"){
                this.coinOpen = "unset";
                this.$nextTick(() => {
                    this.$refs.listBlur.focus();
                });
            }
            else{
                this.coinOpen = "none";
            }
        },
        coinListBlur() {
            if(!this.isMouseOver) {
                this.coinOpen = "none";
            }
        },
        coinItemClick(item, index) {
            this.coinIndex = index;
            this.$emit("change", item, item.name);

            if(this.ChainValue) {
                this.setChainValue(item.name);
                const val = this.ChainValue[item.name];
                item.fee = val.format;
            }
        },
        updateVal() {
            this.$emit("input", this.inputValue, this.coinData[this.coinIndex].name);
        },
        setChainToken(index) {
            let item = this.coinData[index];
            this.coinItemClick(item, index);
        },
        setChainValue(name) {
            if(this.ChainValue) {
                const val = this.ChainValue[name];
                if(val) {
                    this.inputValue = val.format;
                } else {
                    this.inputValue = 0;
                }
            }
            this.updateVal();
        },
        setDepositValue(value) {
            this.inputValue = value;
        }
    }
};
</script>

<style lang="scss" scoped>
.content {
    border: solid 2px #e3e3e3;
    border-radius: 5px;
    display: flex;
    align-items: center;
    padding: 10px;
    position: relative;

    .select {
        display: flex;
        align-items: center;
        min-width: 140px;
        width: 100%;
        cursor: pointer;
        user-select: none;

        img {
            width: 40px;
            height: 40px;
        }

        .txt {
            font-family: Helvetica;
            font-size: 15px;
            color: #270645;
            margin-left: 10px;
        }

        .icon {
            margin-left: auto;
        }
    }

    .list {
        position: absolute;
        left: 14px;
        top: 50px;
        // margin-top: 270px;
        // margin-left: 2px;
        width: 112px;
        background-color: #ffffff;
        box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
        border-radius: 5px;
        z-index: 999;
        
        .item {
            display: flex;
            align-items: center;
            margin: 5px;
            padding: 5px 10px;
            cursor: pointer;

            img {
                width: 27px;
                height: 27px;
            }

            .txt {
                font-size: 13px;
                color: #270645;
                margin-left: 10px;
                width: 360px;
            }

            &:hover {
                background-color: #1d42ff;
                border-radius: 5px;
                .txt {
                    color: #ffffff;
                }
            }
        }
    }

    .reposition {
        position: fixed;
        left: unset;
        top: unset;
        margin-top: 270px;
        margin-left: 2px;
    }

    input {
        font-family: Helvetica;font-weight: bold;
        font-size: 24px;
        color: #766983;
        text-align: right;
        width: 350px;
        margin-left: auto;
        margin-right: 10px;

        &:focus {
            outline: none;
        }
    }
}

.showInput {
    .select {
        width: unset;
    }
}
</style>
