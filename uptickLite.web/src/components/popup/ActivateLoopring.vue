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
    <div>
        <!-- Connected with -->
        <v-dialog v-model="dialog.L1">
            <v-card v-if="dialog.L1" class="connected-with" style="padding:30px;display:flex;flex-direction:column">
                <img class="close" src="@/assets/btn_close.png" @click="dialog.L1 = false" />
                <span class="tit">Connected with</span>
                <span class="addr">{{ $store.state.did | addressEllipsis }}</span>
                <div class="mid" id="copyid">
                    <div class="left" @click="copyClick($store.state.did)">
                        <img class="img" src="@/assets/icons/icon_copy.png" />
                        <span class="txt">Copy</span>
                    </div>
                    <div class="right" @click="disconnectClick">
                        <img class="img" src="@/assets/icons/icon_disconncet.png" />
                        <span class="txt">Disconnect</span>
                    </div>
                </div>
                <span class="remark">Please deposit to activate your Layer 2 account</span>
                <v-btn class="uc" @click="dialogL1Click">Activate Loopring L2
                </v-btn>
                <uComponents v-if="dialog.L1" ref="ucom"></uComponents>
            </v-card>
        </v-dialog>

        <!-- Activate Loopring L2 Account -->
        <v-dialog v-model="dialog.L2">
            <v-card v-if="dialog.L2" class="Activate-Loopring-L2-Account"
                style="padding:30px;display:flex;flex-direction:column">
                <img class="close" src="@/assets/btn_close.png" @click="dialog.L2 = false" />
                <span class="title">Activate Loopring L2 Account</span>
                <span class="remark remark1">As Ethereum's first ever zkRollup, Loopring L2 allows you to avoid costly
                    gas fees
                    and network congestion with the same security as mainnet - 100x cheaper and faster.</span>
                <span class="remark remark2">Activating your Loopring L2 account requires a small payment fee.</span>
                <v-btn class="uc" :class="{ disclick: !hasFees }" @click="dialogL2Click">OK</v-btn>
                <uComponents v-if="dialog.L2" ref="ucom"></uComponents>
            </v-card>
        </v-dialog>

        <!-- Activate Loopring L2 Account 2 -->
        <v-dialog v-model="dialog.L3">
            <v-card v-if="dialog.L3" class="Activate-Loopring-L2-Account-2"
                style="padding:30px;display:flex;flex-direction:column">
                <img class="close" src="@/assets/btn_close.png" @click="dialog.L3 = false" />
                <span class="title">Activate Loopring L2 Account</span>
                <span class="balance-red">Insufficient balance in your Loopring L2 account</span>
                <span class="remark1">Please make sure one of the below tokens with the minimum quantity is in your
                    Loopring L2 account to proceed:</span>
                <div class="balance">
                    <div class="head row">
                        <span>Token</span>
                        <span>Min Requirement</span>
                        <span>Availability</span>
                    </div>
                    <div class="row" v-for="(item, index) in fees.group" :key="index">
                        <span>{{ item.token }}</span>
                        <span>{{ item.format }}</span>
                        <span>{{ item.userBalance || '-' }}</span>
                    </div>
                </div>
                <span class="remark2">If you have already started the deposit, please be patient and recheck as
                    transactions on Ethereum can take up to 30 minutes.</span>
                <v-btn class="uc" @click="dialogL3Click">Add Assets</v-btn>
            </v-card>
        </v-dialog>

        <!-- Add assets & Activate -->
        <v-dialog v-model="dialog.L4">
            <v-card v-if="dialog.L4" class="Add-assets-Activate"
                style="padding:30px;display:flex;flex-direction:column">
                <img class="close" src="@/assets/btn_close.png" @click="dialog.L4 = false" />
                <span class="title">Add assets & Activate</span>
                <span class="remark">How would you like to add Loopring L2 assets?</span>
                <div class="target">
                    <div class="row" @click="activateFromMyCLick">
                        <img src="@/assets/icons/icon_01.png" />
                        <span>From my L1 account</span>
                        <img src="@/assets/icons/icon_arrow_r.png" />
                    </div>
                    <div class="row" @click="activeFromAnotherClick">
                        <img src="@/assets/icons/icon_02.png" />
                        <span>From another Loopring L2 account</span>
                        <img src="@/assets/icons/icon_arrow_r.png" />
                    </div>
                </div>
            </v-card>
        </v-dialog>

        <!-- Add Asset from My L1 & Activate -->
        <v-dialog v-model="dialog.L5">
            <v-card v-if="dialog.L5" class="Add-Asset-from-My-L1-Activate"
                style="padding:30px;display:flex;flex-direction:column">
                <img class="back" src="@/assets/icons/icon_arrow_l.png" @click="backL5Click" />
                <img class="close" src="@/assets/btn_close.png" @click="dialog.L5 = false" />
                <div>
                    <span class="title">Add Asset from My L1 & Activate</span>
                    <img class="iocn_wen" src="@/assets/icons/icon_wen.png" @mouseenter="isTip = true"
                        @mouseleave="isTip = false" />
                </div>
                <span class="tip" v-if="isTip">Once your deposit is confirmed on Ethereum, it will be added to your
                    balance within
                    about 2 minutes.</span>
                <div class="select-text">
                    <span>Select Layer 1 Token</span>
                    <span class="available" @click="availableClick">Available:{{L1Balance}}</span>
                </div>
                <DepositChainSelect ref="chainSelect" :input="true" :ChainValue="fees" :Reposition="true" @input="chainInput"
                    @change="chainChange"></DepositChainSelect>
                <span class="ib">{{ isInsufficient ? "Insufficient balance" : "" }}</span>
                <div class="remark">
                    <span>Please reserve enough ETH for gas!</span><br/>
                    <span>Please deposit {{ currentChain.fee }} {{ currentChain.name }} to activate Loopring L2.</span>
                </div>
                <v-btn class="uc" :class="{ disclick: !depositValue || isInsufficient, 'deposit-wait': depositLoading }"
                    @click="dialogL5Click">Need ETH For Gas</v-btn>
                <uComponents v-if="dialog.L5" ref="ucom"></uComponents>
            </v-card>
        </v-dialog>

        <!-- Receive Address -->
        <v-dialog v-model="dialog.L6">
            <v-card v-if="dialog.L6" class="Receive-Address" style="padding:30px;display:flex;flex-direction:column">
                <img class="back" src="@/assets/icons/icon_arrow_l.png" @click="backL6Click" />
                <img class="close" src="@/assets/btn_close.png" @click="dialog.L6 = false" />
                <span class="title">Receive Address</span>
                <span class="remark">Please send ETH, USDT, LRC, DAI, USDC funds from a Loopring L2 address</span>
                <div class="qtcode" ref="qrCodeAddress"></div>
                <div class="address" id="copyid">
                    <span class="addrtext">{{ address }}</span>
                    <img class="imgcopy" src="@/assets/icons/icon_copy.png" @click="copyClick(address)" />
                </div>
                <uComponents v-if="dialog.L6" ref="ucom"></uComponents>
            </v-card>
        </v-dialog>

        <!-- Activate Loopring L2 Account 3 -->
        <v-dialog v-model="dialog.L7">
            <v-card v-if="dialog.L7" class="Activate-Loopring-L2-Account-3"
                style="padding:30px;display:flex;flex-direction:column" @mousedown.stop="showFeesList = false">
                <img class="close" src="@/assets/btn_close.png" @click="dialog.L7 = false" />
                <span class="title">Activate Loopring L2 Account</span>
                <span class="remark">You have enough balance to pay for Layer 2 creation as below.</span>
                <div class="amount">
                    <div class="fee">
                        <span>Fee: </span><span class="chain" @mousedown.stop="chainClick">{{ currentFee.format }}
                            {{ currentFee.token }}</span>
                        <img @mousedown.stop="chainClick" src="@/assets/icons/icon_arrow_r.png" />
                    </div>
                    <div class="fees" v-if="showFeesList">
                        <div class="row" v-for="(item, index) in fees.group" :key="index"
                            @mousedown.stop="feeClick(index)">
                            <span class="am">{{ item.format }}</span>
                            <span class="to">{{ item.token }}</span>
                        </div>
                    </div>
                    <span>Your Layer 2 have: {{ currentFee.userBalance }} {{ currentFee.token }}</span>
                </div>
                <v-btn class="uc" :class="{ disclick: !isActivate }" @click="dialogL7Click">Activate Account</v-btn>
                <uComponents v-if="dialog.L7" ref="ucom"></uComponents>
            </v-card>
        </v-dialog>
    </div>
</template>

<script lang="js">
import DepositChainSelect from "@/components/DepositChainSelect.vue"
import QRCode from 'qrcodejs2'

import Web3 from 'web3';
import abi from '@/loopring/abi.json';

const tokenAddress = {
    "LRC": "0xfc28028d9b1f6966fe74710653232972f50673be",
    "USDT": "0xd4e71c4bb48850f5971ce40aa428b09f242d3e8a",
    "DAI": "0xcd2c81b322a5b530b5fa3432e57da6803b0317f7",
    "USDC": "0x47525e6a5def04c9a56706e93f54cc70c2e8f165",
}

export default {
    name: 'ActivateLoopring',
    components: { DepositChainSelect },
    data: function () {
        return {
            depositLoading: false,
            // page dialog visible
            dialog: {
                L1: false,  // Wallet login
                L2: false,  // Activation account description
                L3: false,  // Activate account currency
                L4: false,  // Activate account select
                L5: false,  // Wallet recharge
                L6: false,  // Account QR code
                L7: false,  // Expense confirmation
                L8: false,  // Expense confirmation wait
                L9: false,  // Activation failed
            },
            fees: {},
            depositValue: "",
            isTip: false,
            address: "",
            showFeesList: false,
            currentFee: {},
            isInsufficient: true,
            isActivate: false,
            currentChain: {},
            action: "deposit",
            hasFees: false,
            L1Balance: "0.0000000"
        }
    },
    filters: {
        addressEllipsis(val) {
            if (val && val.length > 10) {
                return val.substring(0, 7) + "..." + val.substring(val.length - 6);
            }
        }
    },
    mounted() {

    },
    methods: {
        async show() {
            this.dialog.L1 = true;
            try {
                const fee_res = await this.LRCHandler.getActiveAccountFee();
                this.fees = fee_res;
                this.currentFee = this.fees.group[0];
                this.hasFees = true;
            } catch (error) {
                console.log(error);
            }

            try {
                const userBalances = await this.LRCHandler.getAllUserBalances();
                this.fees.group.forEach(ele => {
                    const bal = userBalances[ele.tokenId];
                    if (bal) {
                        ele.userBalance = bal.format ;
                        this.action = "activate";
                    }
                });
                for (let i = 0; i < this.fees.group.length; i++) {
                    const ele = this.fees.group[i];
                    if(ele.userBalance) {
                        this.currentFee = ele;
                        break;
                    }
                }
            } catch (error) {
                console.log(error);
            }

            const addr_res = await this.LRCHandler.getAddresss();
            this.address = addr_res;

            this.isActivate = true;
        },
        dialogL1Click() {
            this.dialog.L1 = false;
            this.dialog.L2 = true;
        },
        dialogL2Click() {
            if(this.action == "deposit") {
                this.dialog.L2 = false;
                this.dialog.L3 = true;
            }
            else if(this.action == "activate") {
                this.dialog.L2 = false;
                this.dialog.L7 = true;
            }
            this.$forceUpdate();
        },
        dialogL3Click() {
            let has = false;
            for (let index = 0; index < this.fees.group.length; index++) {
                const ele = this.fees.group[index];
                if (ele.userBalance) {
                    has = true;
                }
            }
            if (!has) {
                this.dialog.L3 = false;
                this.dialog.L4 = true;
            } else {
                this.dialog.L3 = false;
                this.dialog.L7 = true;
            }
            this.$forceUpdate();
        },
        activateFromMyCLick() {
            this.dialog.L4 = false;
            this.dialog.L5 = true;
            
            this.$nextTick(async () => {
                let chainIndex = 0;
                for (let i = 0; i < this.fees.group.length; i++) {
                    const ele = this.fees.group[i];
                    const amt = await this.getMetaMaskBalance(ele.token, ele.tokenId);
                    if(amt > 0) {
                        chainIndex = ele.tokenId;
                        break;
                    }
                }
                this.$refs.chainSelect.setChainToken(chainIndex);
            });
        },
        backL5Click() {
            this.dialog.L4 = true;
            this.dialog.L5 = false;
        },
        async dialogL5Click() {
            if (this.depositValue <= 0 || this.depositLoading) return;

            this.depositLoading = true;
            let value = this.depositValue;
            let fee = 0;
            let gasPrice = 20;
            let gasLimit = 200000;
            try {
                let res = await this.LRCHandler.deposit(this.currentChain.name, value, fee, gasPrice, gasLimit);
                this.depositLoading = false;
                if (res) {
                    this.$emit("deposit", res);
                    this.$toast("success", "Deposit Success").then(() => {
                        this.dialog.L5 = false;
                        // this.dialog.L7 = true;
                    });
                }
            } catch (error) {
                this.depositLoading = false;
            }
        },
        backL6Click() {
            this.dialog.L4 = true;
            this.dialog.L6 = false;
        },
        activeFromAnotherClick() {
            this.dialog.L4 = false;
            this.dialog.L6 = true;

            this.$nextTick(() => {
                new QRCode(this.$refs.qrCodeAddress, {
                    text: this.address, // 需要转换为二维码的内容
                    width: 218,
                    height: 218,
                    colorDark: '#000000',
                    colorLight: '#ffffff',
                    correctLevel: QRCode.CorrectLevel.H
                });
            });
        },
        async dialogL7Click() {
            if (!this.currentFee.userBalance) return;

            let res = await this.LRCHandler.updateAccount(this.currentFee.token);
            if (res.hash || res.activate) {
                this.$emit("activate", res);
                this.$toast("success", "Activate Success").then(() => {
                    this.dialog.L7 = false;
                    location.reload();
                });
            }
			sessionStorage.removeItem("KEY_exchangeInfo");
			sessionStorage.removeItem("KEY_accInfo");
			sessionStorage.removeItem("KEY_keySeed");
			sessionStorage.removeItem("KEY_eddsaKey");
			sessionStorage.removeItem("KEY_apiKey");
			sessionStorage.removeItem("KEY_activeFee");
			sessionStorage.removeItem("KEY_LRCAccount");
        },
        chainInput(val, name) {
            this.depositValue = val;
            if(this.L1Balance != "0.0000000") {
                this.isInsufficient = this.L1Balance < this.depositValue;
            }
        },
        async chainChange(value) {
            this.currentChain = value;
            this.L1Balance = "0.0000000";

            let amt = await this.getMetaMaskBalance(this.currentChain.name, this.currentChain.tokenId);
            amt = Number(amt).toFixed(7);
            this.L1Balance = amt;
            this.isInsufficient = this.L1Balance < this.depositValue;
        },
        copyClick(val) {
            var input = document.createElement('input');
            input.value = val;
            // document.body.appendChild(input);
            document.getElementById("copyid").appendChild(input);
            input.select();
            document.execCommand('copy');
            document.getElementById("copyid").removeChild(input);

            this.$toast("success", "Copy Success", 1000);
        },
        disconnectClick() {
            this.dialog = {
                L1: false,
                L2: false,
                L3: false,
                L4: false,
                L5: false,
                L6: false,
                L7: false,
                L8: false,
                L9: false,
            };

            localStorage.clear();
            sessionStorage.clear();

            this.$store.commit('SET_DID', "");
            window.bscAddress = "";
            this.$router.push({ name: 'Home' })

            setTimeout(() => {
                window.location.reload();
            }, 1000);
        },
        chainClick() {
            this.showFeesList = !this.showFeesList;
        },
        async feeClick(idx) {
            this.isActivate = false;
            this.currentFee = this.fees.group[idx];
            this.showFeesList = false;
            const bal = await this.LRCHandler.getUserBalances(this.currentFee.tokenId);
            if (bal && bal.amountValue) {
                this.currentFee.userBalance = bal.amountValue;
                this.isActivate = true;
            } else {
                this.currentFee.userBalance = 0;
                this.isActivate = false;
            }
            this.$forceUpdate();
        },
        async getMetaMaskBalance(name, tokenId) {
            const acc = await window.ethereum.request({ method: 'eth_requestAccounts' });
            const fromAddress = acc[0];
            
            const web3 = await new Web3(window.ethereum);
            
            let amt = 0;
            if(name == "ETH") {
                amt = await web3.eth.getBalance(fromAddress);
            }
            else {
                let contract = new web3.eth.Contract(abi, tokenAddress[name]);
                amt = await contract.methods.balanceOf(fromAddress).call();
            }

            if(tokenId == 2 || tokenId == 8){       //USDT and USDC
                amt = amt / 1000000;
            }else{
                amt = web3.utils.fromWei(amt ,"ether");
            } 

            return amt;
        },
        availableClick() {
            this.depositValue = this.L1Balance;
            this.$refs.chainSelect.setDepositValue(this.depositValue);
        }
    }
}
</script>

<style scoped lang="scss">
.close {
    width: 30px;
    height: 31px;
    position: absolute;
    right: 10px;
    top: 11px;
    cursor: pointer;
}

.back {
    width: 20px;
    height: 20px;
    position: absolute;
    left: 12px;
    top: 15px;
    cursor: pointer;
    object-fit: contain;
}

.title {
    font-family: Helvetica-Bold !important;
    font-size: 25px !important;
    color: #270645 !important;
    width: 100% !important;
    text-align: center !important;
}

.uc {
    width: 100%;
    height: 50px !important;
    font-size: 20px !important;
    line-height: 100px;
    border-radius: 25px !important;
}

.v-btn.disclick {
    background-color: #766983 !important;
    pointer-events: none;
}

.deposit-wait::after {
    content: "";
    background: url(../../assets/loading.gif) no-repeat;
    background-size: 30px 30px;
    width: 30px;
    height: 30px;
    position: absolute;
    right: 130px;
    top: 11px;
    border-radius: 50%;
}

.connected-with {
    display: flex;
    width: 450px;
    align-items: center;
    // user-select: none;

    .tit {
        font-family: Helvetica;
        font-size: 15px;
        line-height: 48px;
        color: #766983;
    }

    .addr {
        font-weight: bold;
        ;
        font-size: 25px;
        line-height: 50px;
        color: #270645;
    }

    .mid {
        display: flex;
        justify-content: space-between;
        width: 60%;

        .left,
        .right {
            display: flex;
            align-items: center;
            cursor: pointer;

            .img {
                width: 13px;
                height: 15px;
            }

            .txt {
                font-family: Helvetica;
                font-size: 15px;
                line-height: 48px;
                color: #270645;
                margin-left: 10px;
            }
        }
    }

    .remark {
        font-family: Helvetica;
        font-size: 15px;
        line-height: 48px;
        color: #766983;
    }

}


.Activate-Loopring-L2-Account {
    width: 600px;
    padding: 30px 40px !important;

    .title {
        line-height: 70px;
    }

    .remark {
        font-family: Helvetica;
        font-size: 15px;
        color: #270645;
    }

    .remark2 {
        margin-top: 40px;
    }

    .uc {
        margin-top: 22px;
    }
}

.Activate-Loopring-L2-Account-2 {
    width: 600px;
    height: 513px;
    padding: 30px 40px !important;
    font-family: Helvetica;
    color: #270645;

    .balance-red {
        font-family: Helvetica;
        font-size: 15px;
        color: #ff6600;
        margin: 18px 0;
    }

    .balance {
        display: flex;
        flex-direction: column;
        margin: 15px 0px;

        .head {
            font-family: Helvetica;
            font-size: 15px;
            color: #766983;
        }

        .row {
            display: flex;
            justify-content: space-between;
            margin: 5px 0;
            font-size: 15px;

            span {
                width: 33%;
                text-align: center;

                &:first-child {
                    text-align: left;
                }

                &:last-child {
                    padding-left: 13%;
                    // width: 20%;
                }
            }
        }
    }

    .uc {
        margin-top: 15px;
    }
}

.Add-assets-Activate {
    width: 600px;
    height: 310px;
    font-family: Helvetica;
    font-size: 15px;
    color: #270645;
    padding: 30px 40px !important;

    .remark {
        font-family: Helvetica;
        font-size: 15px;
        color: #766983;
        margin-top: 20px;
        margin-left: 20px;
    }

    .target {
        margin-top: 10px;

        .row {
            width: 486px;
            height: 50px;
            border-radius: 5px;
            border: solid 2px #e3e3e3;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 20px auto;
            cursor: pointer;

            img {
                width: 20px;
                height: 18px;
                object-fit: contain;
                margin: 0 10px;
            }
        }
    }
}

.Add-Asset-from-My-L1-Activate {
    width: 600px;
    height: 330px;
    padding: 30px 40px !important;

    .iocn_wen {
        width: 19px;
        height: 19px;
        margin-left: 20px;
        cursor: pointer;
    }

    .select-text {
        font-family: Helvetica-Bold;
        font-size: 15px;
        color: #270645;
        margin-top: 20px;
        margin-bottom: 10px;
        display: flex;
        justify-content: space-between;
        padding: 0 5px;
        .available {
            cursor: pointer;
        }
    }

    .ib {
        font-family: Helvetica;
        font-size: 13px;
        color: #ff0000;
        margin-top: 5px;
        margin-left: auto;
        line-height: 18px;
        height: 18px;
    }

    .remark {
        font-size: 15px;
        color: #766983;
        margin-top: 15px;
        margin-bottom: 10px;
    }

    .tip {
        width: 221px;
        height: 70px;
        background-color: #270645;
        border-radius: 3px;
        font-family: MicrosoftYaHei;
        font-size: 12px;
        color: #ffffff;
        position: absolute;
        top: 60px;
        left: 320px;
        z-index: 99;
        padding: 7px 10px;
    }
}

.Receive-Address {
    width: 500px;
    height: 407px;
    font-family: MicrosoftYaHei;
    font-size: 13px;
    color: #766983;
    display: flex;
    align-items: center;

    .remark {
        width: 329px;
        margin: 15px 0;
        text-align: center;
    }

    .address {
        margin-top: 10px;
        display: flex;
        align-items: center;

        .imgcopy {
            width: 33px;
            height: 35px;
            padding: 10px;
            cursor: pointer;
        }
    }
}


.Activate-Loopring-L2-Account-3 {
    width: 500px;
    height: 302px;

    .remark {
        font-size: 13px;
        color: #766983;
        margin: 20px 0;
        width: 100%;
        text-align: center;
    }

    .amount {
        display: flex;
        flex-direction: column;
        font-family: Helvetica;
        font-size: 15px;
        color: #766983;
        margin-left: 20px;
        margin-top: 10px;
        margin-bottom: 30px;

        .fee {
            font-family: Helvetica;
            font-size: 15px;
            color: #766983;
            margin-bottom: 5px;

            .chain {
                cursor: pointer;
            }

            img {
                width: 12px;
                height: 12px;
                object-fit: contain;
                margin: 0 8px;
                cursor: pointer;
            }
        }

        .fees {
            display: flex;
            flex-direction: column;
            position: absolute;
            background-color: #ffffff;
            border-radius: 3px;
            box-shadow: 0px 0px 5px #333333;
            z-index: 9;
            top: 155px;
            left: 76px;

            .row {
                margin: 0px;
                font-family: Helvetica;
                font-size: 13px;
                color: #766983;
                padding: 5px 10px;
                cursor: pointer;

                .am {
                    width: 80px;
                }
            }
        }
    }
}
</style>