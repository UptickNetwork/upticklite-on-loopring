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
        <slot></slot>
        
        <PromptBox ref="confirm"></PromptBox>
        <uToast ref="toast"></uToast>
    </div>
</template>

<script>
    export default {
        methods: {
            toast(type, txt, timer) {
                return this.$refs.toast.show(type, txt, timer);
            },
            confirm(txt, ps) {
                this.$refs.confirm.show(txt, ps);
            }
        },
    }
</script>

<style lang="scss">
</style>
