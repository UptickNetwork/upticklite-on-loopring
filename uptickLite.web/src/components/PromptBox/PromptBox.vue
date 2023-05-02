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
	<div class="content" v-if="isShow">
		<div class="box">
			<img class="close" @click="close" src="@/assets/btn_close.png" alt="" />
			<div class="msg">
				<span>{{ tipmsg }}</span>
			</div>
			<div class="commit v-btn uc" @click="commit">{{ $t("popupTransferCommit") }}</div>
		</div>
	</div>
</template>

<script>
	export default {
		props: {
			msg: String
		},
		data() {
			return {
				isShow: false,
				tipmsg: "",
                params: ""
			}
		},
		methods: {
			show(str, ps) {
				if(str) {
					this.tipmsg = str;
				} else {
					this.tipmsg = this.msg;
				}
				this.isShow = true;
                
                this.params = ps;
			},
			hide() {
				this.isShow = false;
			},
			close() {
				this.hide();
			},
			commit() {
				this.$emit("commit", this.params);
				this.close();
			}
		},
	}
</script>

<style lang="scss" scoped>
	.content {
		position: fixed;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.4);
		z-index: 99999999;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.box {
		position: relative;
		width: 600px;
		height: 259px;
		background-image: linear-gradient(#ffffff,
				#ffffff),
			linear-gradient(#6f58d9,
				#6f58d9);
		background-blend-mode: normal,
			normal;
		box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
		border-radius: 5px;

		.close {
			position: absolute;
			right: 10px;
			top: 10px;
			width: 30px;
			height: 31px;
			background-color: #fff;
			border-radius: 50%;
			margin-right: 0;
			cursor: pointer;
		}

		.msg {
			margin-top: 35px;
			height: 130px;
			display: flex;
			justify-content: center;
			align-items: center;

			span {
				width: 411px;
				max-height: 90px;
				font-weight: bold;;
				font-size: 20px;
				letter-spacing: 0px;
				color: #270645;
				text-align: center;

				display: -webkit-box;
				-webkit-box-orient: vertical;
				-webkit-line-clamp: 3;
				overflow: hidden;
			}
		}

		.commit {
			width: 450px;
			height: 50px;
			background-blend-mode: normal,
				normal;
			border-radius: 25px;
			opacity: 0.9;

			width: 450px;
			font-weight: bold;;
			font-size: 20px;
			line-height: 50px;
			letter-spacing: 0px;
			color: #ffffff;
			text-align: center;

			cursor: pointer;

			position: absolute;
			bottom: 40px;
			left: 50%;
			transform: translateX(-50%);
		}
	}
</style>
