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
        <v-card :class="{ card: true, mobile: isMobile }">
            <img class="clo" width="31px" height="31px" @click="closeDialog" src="@/assets/btn_close.png" alt="" />
            <div class="top">
                <h3>{{$t('messageMessage')}}</h3>
                <div class="auth">
                    <span class="width-18">{{currfriend.fansName}}</span>
                    <span class="val">{{ tineValue }}</span>
                </div>
            </div>
            <div class="msgbox" ref="msgbox">
                <ul>
                    <li v-for="i in msgList" :key="i.id">
                        <img class="fansPhoto" :src="i.fansPhoto || img" alt />
                        <div class="cont">
                            <pre class="msg">{{ i.message }}</pre>
                            <span class="time">{{ i.date }}</span>
                        </div>
                    </li>
                </ul>
            </div>

            <div class="ti-tx">
            </div>
            <div class="footer" ref="footer">
                <v-textarea class="txt" label placeholder v-model="msgtxt" outlined auto-grow rows="1"></v-textarea>
                <img class="sub" src="../../assets/icons/btn_send.png" @click="subCommit" />
            </div>
        </v-card>

        <PromptBox ref="promptBox"></PromptBox>
    </div>
</template>

<script>
    import PromptBox from "@/components/PromptBox.vue"

    import api from "@/api";
    import {
        getFileSrc
    } from "@/utils/file";
    import img from '@/assets/default.png';
    const WORK_KEY = "WORK";

    export default {
        components: {
            PromptBox
        },
        data: () => ({
            open: true,
            tineValue: "",
            senderPhoto: "",
            msgtxt: "",
            img,
            msgList: [],
        }),
        props: {
            currfriend: {
                type: Object
            }
        },
        computed: {
            isMobile: function() {
                return this.$vuetify.breakpoint.mobile;
            },
        },
        watch: {
            msgtxt() {
                this.txtKeyup();
            }
        },
        mounted() {
            this.init();
        },
        methods: {
            async init(value) {
               if(value) {
                   this.currfriend = value;
               };
               if (this.currfriend.fansAddress && this.currfriend.fansAddress.length > 12) {
                   this.tineValue = this.currfriend.fansAddress.substr(0, 6) + "..." + this.currfriend.fansAddress
                       .substr(-6)
               }
               
               this.msgList = [];
               let params = {
                  
                   friendAddress: this.currfriend.fansAddress
               };
               let res = await api.home.messageRecord(params);
               res.data.list.forEach(async v => {
                   let fansPhoto = "";
                   if (v.senderAddress == this.$store.state.did) {
                       fansPhoto = await getFileSrc(WORK_KEY, v.senderPhoto);
                       this.senderPhoto = fansPhoto;
                   } else {
                       fansPhoto = await getFileSrc(WORK_KEY, v.receiverPhoto);
                   }
                   if(fansPhoto=="null" || !fansPhoto) fansPhoto = img;
               
                   this.msgList.push({
                       id: v.id,
                       message: v.content,
                       fansPhoto: fansPhoto,
                       date: this.timestampToTime(v.sendTime)
                   });
               })
               setTimeout(() => {
                   document.getElementsByClassName("msgbox")[0].scrollTop = document.getElementsByTagName("ul")[0].offsetHeight + 1000;
               }, 300);
               
               
               params = {
                   userAddress: this.$store.state.did,
                   chainType: this.$store.state.chainType
               };
               let info = await api.home.createInfo(params);
               let photo = await getFileSrc(WORK_KEY, info.data.profilePhoto);
               if(photo=="null" || !photo) photo = img;
               this.senderPhoto = photo; 
            },
            async subCommit() {
                if (!this.msgtxt) {
                    this.$refs.promptBox.show("Information cannot be empty");
                    return;
                }
                let params = {
                    chainType: "IRIS_IRISHUB",
                    sender: this.$store.state.did,
                    receiver: this.currfriend.fansAddress,
                    content: this.msgtxt
                };
                
                api.home.message(params);

                this.msgList.push({
                    // id: v.id,
                    message: this.msgtxt,
                    fansPhoto: this.senderPhoto,
                    date: this.timestampToTime(new Date().getTime())
                })
                
                setTimeout(() => {
                    document.getElementsByClassName("msgbox")[0].scrollTop = document.getElementsByTagName("ul")[0].offsetHeight + 1000;
                }, 200);
                
                this.msgtxt = "";
            },
            closeDialog() {
                this.open = false;
                this.$emit("getOpen", this.open);
            },
            foxBtn() {
                alert(111);
            },
            timestampToTime(time) {
                let date = new Date(time)
                let Y = date.getFullYear() + '-';
                let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                let D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
                let h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
                let m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
                let s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
                let strDate = Y + M + D + h + m + s;
                return strDate;
            },
            txtKeyup() {
                let h = this.$refs.footer.offsetTop - 75;
                this.$refs.msgbox.style.height = h + "px";
            }
        },
    };
</script>

<style lang="scss" scoped>
    .width-18 {
        display: inline-block;
        width: 100px !important;
        overflow: hidden !important;
        white-space: nowrap !important;
        text-overflow: ellipsis !important;
    }

    .card {
        width: 400px;
        height: 600px;

        .clo {
            width: 25px;
            height: 25px;
            display: block;
            position: absolute;
            right: 15px;
            top: 10px;
            background-color: #fff;
            border-radius: 50%;
            cursor: pointer;
        }

        .top {
            padding: 0 25px;
            display: flex;
            flex-direction: column;
            border-bottom: 1px solid #eee;

            h3 {
                line-height: 50px;
                font-size: 20px;
                font-weight: bold;;
                color: #270645;
            }

            .auth {
                position: absolute;
                // top: 34px;
                right: 40px;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: flex-end;
                height: 50px;

                span {
                    width: 54px;
                    height: 13px;
                    font-weight: bold;;
                    font-size: 13px;
                    font-weight: bold;
                    font-stretch: normal;
                    letter-spacing: 0px;
                    color: #270645;
                    margin-right: 15px;
                    text-align: right;
                }

                .val {
                    width: 100px;
                    height: 10px;
                    font-family: Helvetica;
                    font-size: 10px;
                    font-weight: normal;
                    font-stretch: normal;
                    letter-spacing: 0px;
                    line-height: 8px;
                    color: #270645;
                    margin-top: 7px;
                    text-align: right;
                }
            }
        }

        .msgbox {
            margin: 10px 25px;
            height: 470px;
            overflow: hidden;
            overflow-y: auto;

            ul {
                list-style: none;
                padding-left: 0;

                li {
                    position: relative;
                    top: 0;
                    left: 0;
                    margin-top: 12px;
                    // min-height: 60px;

                    img {
                        position: absolute;
                        top: 0;
                        left: 0;
                        display: inline-block;
                        width: 30px;
                        height: 30px;
                        margin-top: 5px;
                    }

                    .cont {
                        clear: both;
                        display: inline-block;
                        // width: 300px;
                        min-height: 45px;
                        // margin: 0 0 19px 66px;
                        margin-left: 40px;
                        display: flex;
                        flex-direction: column;

                        .msg {
                            width: 100%;
                            // min-height: 17px;
                            font-family: Helvetica;
                            font-size: 14px;
                            color: #270645;
                             white-space:pre-wrap;
                            white-space:-moz-pre-wrap;
                            white-space:-pre-wrap;
                            white-space:-o-pre-wrap;
                            word-wrap:break-word;
                        }

                        .time {
                            font-family: Helvetica;
                            font-size: 10px;
                            font-weight: normal;
                            font-stretch: normal;
                            letter-spacing: 0px;
                            color: #270645;
                            margin-top: 10px;
                        }
                    }
                }
            }
        }

        .ti-tx {
            margin: 25px 28px;

            .title {
                width: 85px;
                height: 17px;
                font-weight: bold;;
                font-size: 15px;
                font-weight: bold;
                font-stretch: normal;
                letter-spacing: 0px;
                color: #270645;
            }

            .txt {
                width: 350px;
                height: 40px;
                max-height: 100px;
                background-image: linear-gradient(#ffffff, #ffffff),
                    linear-gradient(#f8f6fd, #f8f6fd);
                background-blend-mode: normal, normal;
                
                &::v-deep textarea{
                    max-height: 100px;
                }
            }
        }

        .footer {
            display: flex;
            align-items: flex-end;
            position: absolute;
            left: 25px;
            right: 25px;
            bottom: -15px;
            
            .txt {
                margin-right: 10px;
                &::v-deep .v-input__slot {
                    min-height: 40px !important;
                    display: flex;
                    align-items: center;
                }
                &::v-deep textarea{
                    max-height: 100px;
                    color: #270645;
                    font-size: 14px;
                    line-height: 22px;
                    padding-top: 3px;
                }
            }
            
            .sub {
                width: 36px;
                height: 36px;
                margin-bottom: 33px;
                cursor: pointer;
            }
            
            &::v-deep textarea {
                margin-top: 5px !important;
            }
        }

        &.mobile {
            .footer {
                .sub {
                    width: 36px;
                    height: 36px;
                }
            }
        }
    }

    .fansPhoto {
        border-radius: 50%;
    }

    .v-card {
        box-shadow: 0px 0px 9px 0px rgb(0 0 0 / 9%) !important;
    }
    
    
    .mobile.card {
        width: 350px !important;
        height: 600px;
    }
</style>
