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
    <v-app :class="{ 'drawer-opened': drawerOpened }" style="overflow: hidden;">
        <Header v-model="drawerOpened" :containerId="containerId" />
        <v-main v-show="!drawerOpened">
            <v-container :id="containerId" class="content">
                <router-view :key="key"></router-view>
            </v-container>
        </v-main>
        <Footer v-if="!drawerOpened" />
    </v-app>
</template>

<script>
    import Header from './Header.vue';
    import Footer from './Footer.vue';

    export default {
        name: 'Layout',
        components: {
            Header,
            Footer
        },
        data: function() {
            return {
                drawerOpened: false,
                containerId: 'container'
            };
        },
        computed: {
            key() {
                return this.$route.name !== undefined ?
                    this.$route.name + new Date() :
                    this.$route + new Date();
            }
        },
        beforeMount() {
            this.$store.dispatch('setInfo');
        }
    };
</script>

<style lang="scss" scoped>
    .drawer-opened {
        height: 100vh;
    }

    .content {
        margin: 0 auto;
        padding: 0;
        width: calc(100% - 40px);
        min-height: 550px;

        @media screen and (min-width: #{map-get($grid-breakpoints, 'lg')}) {
            width: 1220px;
            min-height: 550px;
        }
    }
</style>

<style>
    .width-100 {
        display: inline-block !important;
        width: 100% !important;
        height: auto !important;
        overflow: hidden !important;
        white-space: nowrap !important;
        text-overflow: ellipsis !important;
        -o-text-overflow: ellipsis !important;
    }

    .width-18 {
        display: inline-block !important;
        width: 180px !important;
        height: auto !important;
        overflow: hidden !important;
        white-space: nowrap !important;
        text-overflow: ellipsis !important;
        -o-text-overflow: ellipsis !important;
    }

    .width-8 {
        display: inline-block !important;
        width: 80px !important;
        height: auto !important;
        overflow: hidden !important;
        white-space: nowrap !important;
        text-overflow: ellipsis !important;
        -o-text-overflow: ellipsis !important;
    }

    .theme--light.v-tabs>.v-tabs-bar .v-tab:not(.v-tab--active),
    .theme--light.v-tabs>.v-tabs-bar .v-tab:not(.v-tab--active)>.v-icon,
    .theme--light.v-tabs>.v-tabs-bar .v-tab:not(.v-tab--active)>.v-btn,
    .theme--light.v-tabs>.v-tabs-bar .v-tab--disabled {
        color: #766983 !important;
    }
</style>
