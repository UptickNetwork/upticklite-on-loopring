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
		<v-card class="card d-flex flex-column py-10 px-8">
			<img class="clo" @click="closeDialog" src="@/assets/icons/btn_close.png" alt="" />
			<div class="box">
				<img class="large-img"  :src="productPic" alt="" v-if="!videoPic || videoPic=='null'" />
				<video autoplay loop 
                controls
                controlslist="nodownload" :src="videoPic" v-if="videoPic"></video>
			</div>
		</v-card>
	</div>
</template>
<script>
	import largeImg from "@/assets/test.png";
	export default {
		data: () => ({
			largeImg,
			newOpen: true,
		}),
		props: {
			productPic: {
				type: String
			},
			videoPic: {
				type: String
			}
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
	#container {
		height: 100%;
	}

	.card {
		height: 100%;
		width: 100%;
		background-color: #fff;

		.clo {
			position: absolute;
			right: 40px;
			top: 40px;
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
			text-align: center;

			img {
				width: auto;
				height: 100%;
			}

			video{
				width: auto;
				height: 100%;
			}
		}
	}
</style>


<style>
	.v-dialog--fullscreen {
		width: 100% !important;
		height: 100% !important;
	}
</style>
