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
	<div :class="{ 'personal-page': true, mobile: isMobile }">
		<div class="top">
			<v-img class="img" v-if="ban" :src="ban" cover alt="" />
		</div>
		<div class="d-flex flex-column flex-lg-row">
			<div class="d-flex mb-n10 flex-column align-center">
				<div class="avatar-box align-self-lg-start rounded-circle">
					<v-avatar :size="180">
						<v-img :src="src" alt="" />
					</v-avatar>
				</div>
			</div>
			<div class="ml-lg-8 mt-5 mt-lg-6 d-flex flex-column align-center align-lg-start">
				<div style="font-size: 40px; font-weight: bold;">{{ myInfo.name }}</div>
				<div class="no_copy">
					<span v-if="!isMobile" class="No">{{ myInfo.address }}</span>
					<span v-if="isMobile" class="No">{{ myInfo.address | didfilter }}</span>
					<img class="copy ml-2" :class="{ click: isCopy }" src="@/assets/icons/icon_copy.png"
						@click="onCopyClick" />
				</div>
				<div class="cont d-flex flex-wrap" v-html="myInfo.description"></div>
			</div>
		</div>

		 <div class="" style="min-height: 500px">
     		 <Collections @list="getlist" />
    	</div>

		<v-dialog v-model="opencard">
			<carousel class="otherpage" :list="list" v-if="opencard" @getOpen="OpenCard"></carousel>
		</v-dialog>
		<v-dialog fullscreen v-model="openLargemg">
			<large-img ref="largeDialog" v-if="openLargemg" :productPic="productPic" @getOpen="Open">
			</large-img>
		</v-dialog>
	</div>
</template>

<script lang='js'>
import UserCount from "@/components/UserCount.vue";
import BookmarksTabs from "@/components/BookmarksTabs.vue";

import Collections from "./components/Collections.vue";
import api from "@/api";
import {
	getFileSrc
} from "@/utils/file";
import defaultImg from "@/assets/default.png";
import Carousel from "@/components/carousel";
import LargeImg from "@/components/largeImg.vue";
import ScrollX from "@/components/scroll/ScrollX.vue";

const BANNER_KEY = "BANNER";
const AVATAR_KEY = "AVATAR";
const SYNOPSIS_KEY = "SYNOPSIS";

export default {
	name: "PersonalPage",
	components: {
		UserCount,
		BookmarksTabs,
		Collections,
		Carousel,
		LargeImg,
		ScrollX
	},
	props: {},
	data: function () {
		return {
			myInfo: {},
			avatars: [],
			src: "",
			ban: "",
			defaultImg,
			opencard: false,
			list: [],

			isCopy: false,
			showRightBottom: false,

			loading: false,
			selection: 1,
			Twitter: '',
			Youtube: '',
			Homepage: '',

			//展示图片
			imgList: [
				//接口获取的三张图
			],
			openLargemg: false,
			productPic: '',
			joinTime: '',
			bscAddress: window.bscAddress
		};
	},
	computed: {
		isMobile: function () {
			return this.$vuetify.breakpoint.mobile;
		}
	},
	filters: {
		didfilter: function (value) {
			if (value && value.length > 24) {
				return value.substr(0, 12) + "..." + value.substr(-12);
			} else {
				return "";
			}
		}
	},
	mounted() {
		this.getInfo();
		window.eventBus.$on('LangChange', this.onLangChange);
	},
	methods: {
		onLangChange() {
			this.avatars = [];
			this.imgList = []

			this.getInfo();
		},

		reserve() {
			this.loading = true;
			setTimeout(() => (this.loading = false), 2000);
		},
		forceUpdate() {
			this.$forceUpdate();
		},
		async getInfo() {
			// let params = { did: this.$store.state.did };
			// let res = await api.home.getMyInfo(params);

			let params = {
				address: this.$store.state.did,
			};

			let res = await api.home.loopringUserInfo(params);
			this.myInfo = res.data;


			if (this.myInfo.profilePhoto) {
				this.src = await getFileSrc(AVATAR_KEY, this.myInfo.profilePhoto);
			} else {
				this.src = defaultImg;
			}

			if (this.myInfo.background != null) {
				this.ban = await getFileSrc(BANNER_KEY, this.myInfo.background);
				if (this.ban == "null") this.ban = "";
			} else {
				this.ban = defaultImg
			}


			if (res.data.synopsisImage) {
				let imgLists = (res.data.synopsisImage).split(",");
				imgLists.forEach(async (v) => {
					if (v != '') {
						let imgsrc = await getFileSrc(SYNOPSIS_KEY, v);
						this.imgList.push({ src: imgsrc });
					}
				});
			}
			if (this.myInfo.joinTime != null) {
				let t = this.$t("otherpage_join")
				let reg = new RegExp('【N】', 'g')
				this.joinTime = t.replace(reg, this.myInfo.joinTime);

			}
			if (this.myInfo.twitter != null) {
				let newtwitter = this.myInfo.twitter.indexOf("http");
				if (newtwitter == 0) {
					this.Twitter = `${this.myInfo.twitter}`
				}
				if (newtwitter == -1) {
					this.Twitter = `https://${this.myInfo.twitter}`
				}
			}
			if (this.myInfo.youtube != null) {
				let newyoutube = this.myInfo.youtube.indexOf("http");
				if (newyoutube == 0) {
					this.Youtube = `${this.myInfo.youtube}`
				}
				if (newyoutube == -1) {
					this.Youtube = `https://${this.myInfo.youtube}`
				}
			}
			if (this.myInfo.homepage != null) {
				let newhomepage = this.myInfo.homepage.indexOf("http");
				if (newhomepage == 0) {
					this.Homepage = `${this.myInfo.homepage}`
				}
				if (newhomepage == -1) {
					this.Homepage = `https://${this.myInfo.homepage}`
				}
			}
			
			if (this.myInfo.description)
				this.myInfo.description = this.myInfo.description.replace(/\n/g, "<br/>")
			if (!this.myInfo) {
				this.myInfo = {
					did: this.$store.state.did,
					fansNumber: 0,
					followCount: 0,
					workCount: 0
				}
			}

			if (this.myInfo.profilePhoto) {
				this.src = await getFileSrc(AVATAR_KEY, this.myInfo.profilePhoto);
			} else {
				this.src = defaultImg;
			}

			if (this.myInfo.background != null) {
				this.ban = await getFileSrc(BANNER_KEY, this.myInfo.background);
				if (this.ban == "null") this.ban = defaultImg;
			} else {
				this.ban = defaultImg
			}
		
		},
		openCarousel() {
			this.opencard = true;
		},
		OpenCard(e) {
			this.opencard = e;
		},
		openLargeImg(item) {
			this.productPic = item.src
			this.openLargemg = true;
			// this.$refs.largeDialog.playVideo();
		},
		Open(e) {
			this.openLargemg = e;
		},
		getlist(dt) {
			this.list = dt;
			if (this.list.length > 0) {
				this.showRightBottom = true
			} else {
				this.showRightBottom = false
			}
		},
		onCopyClick() {
			var input = document.createElement('input')
			input.value = this.$chainName == "UPTICK EVM" ? this.bscAddress : this.myInfo.address;
			document.body.appendChild(input);
			input.select();
			document.execCommand('copy');
			document.body.removeChild(input);
			this.isCopy = true;
			setTimeout(() => this.isCopy = false, 100);
		},
		onMediaClick(val) {
			window.open(val, '_blank');
		},
		onMediaClick1() {
			window.open('www.baidu.com', '_blank');
		},
		avatarClick() {
			this.$router.push({ name: "Fans" })
		},
		onTabsClick(v) {
			v == "Collections" ? this.showRightBottom = true : this.showRightBottom = false;
		}
	}
};
</script>

<style lang='scss' scoped>
a {
	text-decoration: none;
	vertical-align: middle;

	img,
	span {
		display: inline-block;
	}
}

img {
	display: inline-block;
	width: 22px;
	height: 22px;
	margin: 0 7px 0 0;
}

.personal-page {
	.top {
		height: 301px;
		background-color: #f8f6fd;

		.img {
			width: 100%;
			height: 100%;
		}
	}

	.no_copy {
		.No {
			padding-top: 4px;
			font-family: Helvetica;
			font-size: 13px;
			font-weight: normal;
			font-stretch: normal;
			letter-spacing: 0px;
			color: #270645;
		}

		.copy {
			width: 13px;
			height: 15px;
			display: inline-block;
			vertical-align: middle;
			margin-left: 23px;
			cursor: pointer;
		}

		.click {
			margin-top: 5px;
		}
	}

	.cont {
		margin-top: 12px;
		max-width: 842px;
		font-family: Helvetica;
		font-size: 14px;
		font-weight: normal;
		font-stretch: normal;
		line-height: 16px;
		letter-spacing: 0px;
		color: #270645;
	}

	.linkurl {
		margin-bottom: 12px;

		.media {
			display: inline-block;
			word-wrap: break-word;
			word-break: break-all;

			a {

				img,
				span {
					display: inline;
				}
			}

			img,
			span {
				display: inline;
			}
		}
	}

	.avatar-box {
		position: relative;
		top: -40px;
		left: 0;
		border: 6px white solid;
	}

	.introduction {
		display: -webkit-box;
		overflow: hidden;
		font-size: 13px;
		white-space: normal !important;
		text-overflow: ellipsis;
		word-wrap: break-word;
		-webkit-line-clamp: 3;
		-webkit-box-orient: vertical;
	}

	.imgbox {
		max-width: 996px;
		overflow-x: auto;
		overflow-y: hidden;
		white-space: nowrap;

		.box {
			height: 300px;
			// width:auto;
			margin-right: 5px;

			img {
				width: auto;
				height: 300px;
				display: block // object-fit: contain;

			}
		}
	}

	.info-box>div:first-of-type {
		width: 260px;
	}

	&.mobile {
		.pt-11{
			padding-top: 20px !important;
		}

		.info-box {
			width: 100%;

			>div {
				width: 100%;
			}

			.ingbox {
				margin-top: 25px;
				margin-bottom: 30px;

				// width:100%;
				.box {
					height: 350px;

					// width:263px;
					img {
						width: auto;
						height: 100%;
						// object-fit: contain;
					}
				}
			}
		}

		.linkurl {
			margin-bottom: 12px;

			.media {
				display: inline-block;
				word-wrap: break-word;
				word-break: break-all;

				// width:100%;
				a {

					img,
					span {
						display: inline;
					}
				}

				img,
				span {
					display: inline;
				}
			}
		}
	}



}

.right_bottom {
	position: fixed;
	bottom: 100px;
	right: 50px;
	z-index: 200;

	img {
		display: block;
		width: 59px;
		height: 59px;
		margin-top: 35px;
	}
}


.avatar {
	position: absolute;
	left: 0;
	top: 0;
	background: white;
	padding: 2px;
	border-radius: 50%;
	cursor: pointer;
}
</style>
