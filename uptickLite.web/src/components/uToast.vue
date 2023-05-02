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
    <v-snackbar v-model="snackbarObj.isShow" :color="snackbarObj.color" centered :timeout="snackbarObj.timer" style="opacity: 0.8">
        <span style="color: white; font-size: 20px">{{ snackbarObj.title }}</span>
    </v-snackbar>
</template>

<script>
    export default {
        data() {
            return {
                snackbarObj: { isShow:false, color:"success", timer:3000, title:"" }
            }
        },
        methods: {
            show(type, txt, timer=3000) {
            	this.snackbarObj = {
                    isShow: true,
                    color: type,
                    timer: timer,
                    title: txt
                };
                return new Promise(function(resolve) {
                    setTimeout(() => {
                        resolve(true);
                    }, timer+300);
                });
            }
        },
    }
</script>

<style lang="scss" scoped>
    
</style>
