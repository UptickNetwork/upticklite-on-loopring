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
        <div class="select">
            <img :src="coinData[coinIndex].img" />
            <span class="txt">{{ coinData[coinIndex].name }}</span>
        </div>
        <div class="list" :style="{ display: coinOpen }">
            <input ref="listBlur" type="text" style="width:0px;position:absolute;" @blur="coinListBlur" />
            <div class="item" v-for="(item, index) in coinData" :key="index" @mousedown="coinItemClick(index)">
                <img :src="item.img" />
                <span class="txt">{{ item.name }}</span>
            </div>
        </div>
        <input v-model.number="inputValue" type="text" placeholder="0.0000" @input="updateVal">
    </div>
</template>

<script lang="js">

export default {
    name: 'ChainInput',
    props: {
        coinData: Array
    },
    data: function () {
        return {
            coinOpen: "none",
            coinIndex: 1,
            inputValue: ""
        }
    },
    methods: {
        coinSelect() {
            if (this.coinOpen == "none")
                this.coinOpen = "unset";
            else
                this.coinOpen = "none";
            this.$nextTick(() => {
                this.$refs.listBlur.focus();
            });
        },
        coinListBlur() {
            this.coinOpen = "none";
        },
        coinItemClick(index) {
            this.coinIndex = index;
            this.$emit("change", this.coinIndex);
        },
        updateVal() {
            this.$emit("input", this.inputValue);
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
        width: 140px;

        img {
            width: 40px;
            height: 40px;
        }

        .txt {
            font-family: Helvetica;
            font-size: 20px;
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
        width: 112px;
        background-color: #ffffff;
        box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
        border-radius: 5px;
        z-index: 9;

        .item {
            display: flex;
            align-items: center;
            margin: 5px;
            padding: 5px 10px;

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
            }
        }
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
</style>
