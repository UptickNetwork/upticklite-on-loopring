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
	<div :class="{ contain: true, mobile: isMobile }">
		<div class="card_img mb-5">
			<img class="sizeimg mb-n5" :src="pageData.imgUrl" alt="" @click="openLargeImg" />

		</div>
		<div class="card_info d-flex flex-column mt-lg-15" style="width: 100%">
			<div class="cirtile">
				<div class="d-flex flex-row justify-space-between sharestyle">
					<div class="tit">{{ pageData.name }}</div>
				</div>
				<div class="cont d-flex flex-wrap" v-html="pageData.description"></div>
				<div class="title mt-7" v-if="PropertyList&&PropertyList.length>0">Properties</div>
				<div v-for="(item,index) in PropertyList" :key="index" class="d-flex flex-column">
					<div class="d-flex flex-row">
						<div class="Properties">{{item.trait_type}}</div>
						<div class="PropertiesValue "> {{item.value}}</div>
					</div>
				</div>
		

				<div class="mt-8 mb-2 mr-0 px-0 link d-flex flex-wrap NftID">
					NFT ID: {{ $route.query.nftId }}
				</div>
				<div class="d-flex flex-row contract" style="position: relative;">
					<div class="  d-flex flex-center createName" style="margin-top:-4px;">
						Contract <a :href="this.addressExploer" target="_blank" class="fontblue ml-2"
							style="text-decoration: none;">{{ ($route.query.tokenAddress ).substr(0,10)+"..."+($route.query.tokenAddress ).substr(-10)}}</a>						
					</div>
				</div>
				<div class="createName">
					Created by <span>{{ pageData.creatorName }}</span>
				</div>
			
				<div class="d-flex flex-row views mt-3 mb-8">
					<img src="@/assets/icons/icon_IPFS.png" alt="IPFS" />
					<a class="ml-4 mt-1" :href="IpfsHub" target="_blank">{{
            $t("saledetailViewOnIpfs")
          }}</a>
					<img src="@/assets/icons/icon_metadata.png" class="ml-10" alt="IPFS" />
					<a class="ml-4 mt-1" :href="IpfsMetadata" target="_blank">View Metadata</a>
				</div>

				<div class="d-flex flex-row"
					v-if="pageData.listAmount != 0 && pageData.amount > 0 && pageData.makerType != '2'">
					<div class="avaiable">Price {{ pageData.marketPrice }} {{ pageData.marketTokenType }}</div>
					<span class=" avaiable ml-6">Available {{ pageData.amount }}</span>
				</div>
				<div class="d-flex flex-row"
					v-if="pageData.listAmount == 0 && pageData.amount > 0 && pageData.makerType != '2'">

					<span class="avaiable">Available {{ pageData.amount }}</span>
				</div>

				<div class="footor d-flex flex-row justify-space-between mt-5">

					<div class="sub">
			
						<template >
					
					<div v-if="pageData.makerType == '1' || pageData.makerType == null">
						<!-- 已上架，显示下架按钮 -->
						<div v-if="pageData.listAmount > 0">
							<v-btn class="sale" dark rounded depressed width="105" height="40" @click="EndSaleBtn">
								{{ $t("popupSoldoutEndsale") }}
							</v-btn>
						</div>
					
						<!-- 已下架，显示上架按钮 -->
						<div v-else-if="pageData.listAmount == 0 && pageData.amount > 0">
							<div class="d-flex justify-space-between" style="margin-top: -8px">
								<div class="btnbox">
									<v-btn class="rd" rounded @click="TransferBtn">
										{{ $t("popupNewTransferTransfer") }}
									</v-btn>
								</div>
								<!-- 收藏部分的上架按钮  替换为 上架按钮 -->
								<div>
									<v-btn class="satrtsale uc" dark rounded depressed width="106" height="41"
										@click="putOnSaleBtn">{{ $t("popupMarketStartSale") }}
									</v-btn>
								</div>
							</div>
						</div>
						<div v-else-if="pageData.soldOut" class="auction_out" style="font-size:15px;font-weight:bold;">
							{{ $t("workCardSoldOut") }}
						</div>

					</div>							
					</template>

					</div>
					<div class="burn">
						<div class="d-flex flex-row align-center">
							<!-- 销毁弹层按钮 -->
							<img class="ml-5" src="@/assets/icons/icon_burn.png" width="20px" height="20px"
								v-if="pageData.isBurn == true" alt="" @click.stop="burnBtn" />
							<!-- 销售统计弹层按钮 -->
							<img class="ml-5" src="@/assets/icons/icon_data.png" width="20px" height="20px" alt=""
								v-if="pageData.creator == this.$store.state.did" @click="saleTotalBtn" />
						</div>
					</div>
				</div>
			</div>

		</div>
		<div class="table">
			<div class="mb-7 mt-7 tab_tit">{{ $t("saledetailHistory") }}</div>
			<v-data-iterator v-if="desserts.length > 0" class="table_cont" :items="desserts">
				<template v-slot:header>
					<div class="table_font d-flex justify-space-around mb-1 mt-4 py-3 px-lg-10 elevation-0"
						style="border: 1px solid #cccccc">
						<div style="width: 21%; font-weight: bold">
							{{ $t("saledetailHistoryEvent") }}
						</div>
						<div style="width: 21%; font-weight: bold">
							{{ $t("saledetailHistoryForm") }}
						</div>
						<div style="width: 21%; font-weight: bold">
							{{ $t("saledetailHistoryTo") }}
						</div>
						<div style="width: 21%; font-weight: bold">
							{{ $t("detailIndexTime") }}
						</div>
					</div>
				</template>
				<template v-slot:default="{ items }">
					<div class="table_item_font d-flex justify-space-around mb-1 py-2 elevation-0"
						style="border: 1px solid #cccccc" v-for="item in items" :key="item.time">
						<div style="width: 20%;font-size:14px;" class="item_font ml-lg-12 mr-1 pt-lg-1">
							{{ item.event }}
						</div>
						<div style="width: 20%;;font-size:14px;" class="d-flex flex-row mr-1 fom">
							<v-avatar size="30">
								<v-img :src="item.formavatarSrc" :alt="item.form" />
							</v-avatar>
							<span class="ml-3 item_font item_fonts" style="margin-top:5px;">{{ item.form }}</span>
						</div>
						<div style="width: 22%" class="d-flex flex-row mr-1 toto">
							<v-avatar size="30">
								<v-img :src="item.toavatarSrc" :alt="item.to" />
							</v-avatar>
							<span class="ml-3 item_font item_fonts"
								style="font-size: 14px;margin-top:5px;">{{ item.to }}</span>
						</div>

						<div style="width: 20%;font-size: 14px;margin-top:5px;" class="item_font mr-10">
							{{ item.time }}
						</div>
					</div>
				</template>
			</v-data-iterator>
			<v-data-iterator v-else class="table_cont" :items="desserts" hide-default-footer>
				<template v-slot:header>
					<div class="table_font d-flex justify-space-around mb-1 mt-8 py-3 px-lg-10 elevation-1">
						<div style="width: 21%; font-weight: bold">
							{{ $t("saledetailHistoryEvent") }}
						</div>
						<div style="width: 21%; font-weight: bold">
							{{ $t("saledetailHistoryForm") }}
						</div>
						<div style="width: 21%; font-weight: bold">
							{{ $t("saledetailHistoryTo") }}
						</div>
						<div style="width: 21%; font-weight: bold">
							{{ $t("detailIndexTime") }}
						</div>
					</div>
				</template>
				<template v-slot:default="{ items }">
					<div class="table_item_font d-flex justify-space-around mb-1 py-2 elevation-1" v-for="item in items"
						:key="item.time">
						<div style="width: 20%" class="item_font ml-lg-7 mr-1">
							{{ item.event }}
						</div>
						<div style="width: 20%" class="d-flex flex-row mr-1 fom">
							<v-avatar size="30">
								<v-img :src="item.formavatarSrc" :alt="item.form" />
							</v-avatar>
							<span class="ml-3 item_font item_fonts">{{ item.form }}</span>
						</div>
						<div style="width: 22%" class="d-flex flex-row mr-1 toto">
							<v-avatar size="30">
								<v-img :src="item.toavatarSrc" :alt="item.to" />
							</v-avatar>
							<span class="ml-3 item_font item_fonts">{{ item.to }}</span>
						</div>
			
						<div style="width: 20%" class="item_font mr-10">
							{{ item.time }}
						</div>
					</div>
				</template>
			</v-data-iterator>
		</div>
		<v-dialog v-model="opensale">
			<resale v-if="opensale" @getOpen="Open" :pageData="pageData"></resale>
		</v-dialog>
		<div v-if="openmsg" style="position: fixed; right: 20px; bottom: 20px; z-index: 99">
			<message @getOpen="OpenMsg" :currfriend="currfriend"></message>
		</div>
		<v-dialog v-model="Transfer">
			<new-transfer :imgUrl="sharePath" :address="address" :level="1" :name="pageData.name"
				:contracId="pageData.id" :offSaleTokenIds="offSaleTokenIds" :parentValue="pageData"
				source="carddetailTransfer" @getOpen="opentransfer"></new-transfer>
		</v-dialog>
		<v-snackbar v-model="isShowFalse" color="error" centered timeout="2000" style="opacity: 0.8"><span
				style="color: white; font-size: 20px">{{
        falseTitle
      }}</span></v-snackbar>
		<v-snackbar v-model="isShowSuccess" color="success" centered timeout="2000" style="opacity: 0.8"><span
				style="color: white; font-size: 20px">{{
        successTitle
      }}</span></v-snackbar>
		<v-snackbar v-model="isShowDelistFalse" color="error" centered timeout="2000" style="opacity: 0.8"><span
				style="color: white; font-size: 20px">{{
        delistFalseTitle
      }}</span></v-snackbar>
		<v-snackbar v-model="isShowDelistSuccess" color="success" centered timeout="2000" style="opacity: 0.8"><span
				style="color: white; font-size: 20px">{{
        delistSuccessTitle
      }}</span></v-snackbar>

		<v-dialog fullscreen v-model="openLargemg">
			<large-img ref="largeDialog" v-if="openLargemg" :productPic="pageData.imgUrl" @getOpen="closeLargeImg">
			</large-img>
		</v-dialog>
		<!-- 结束售卖 -->
		<v-dialog v-model="Sold">
			<soldout :address="address" :onSaleTokenIds="onSaleTokenIds" :offSaleTokenIds="offSaleTokenIds"
				:name="pageData.name" :parentValue="pageData" @getOpen="openSold"></soldout>
		</v-dialog>
		<v-dialog v-model="openputonsale">
         <market v-if="openputonsale" :address="address" :onSaleTokenIds="onSaleTokenIds"
            :offSaleTokenIds="offSaleTokenIds" :name="name"  :parentValue="pageData" v-bind="$attrs" v-on="$listeners"
            @getOpen="openPutOnSale"></market>
      </v-dialog>

		<v-dialog v-model="Total">
			<saletotal :pageData="pageData" :address="address" :id="pageData.id" :name="pageData.name"
				:minPrice="pageData.minPrice" @getOpen="openTotal"></saletotal>
		</v-dialog>
		<!-- 销毁 -->
		<v-dialog v-model="Burning">
			<burn :address="address" :name="pageData.name" @getOpen="openBurn" :pageValue="pageData"></burn>
		</v-dialog>
	</div>
</template>

<script>
	import resale from "@/components/popup/resale.vue";
	import message from "@/components/popup/message.vue";
	import Transfer from "@/components/popup/transfer.vue";
	import LargeImg from "../../components/largeImg";
	import api from "@/api";
	import {
		getFileSrc
	} from "@/utils/file";
	import {
		share
	} from "@/utils";
	import logoimg from "@/assets/default.png";
	import Soldout from "@/components/popup/soldout.vue";
	import PutOnSale from "@/components/popup/putOnSale.vue";
	import Saletotal from "@/components/popup/saletotal.vue";
	import Burn from "@/components/popup/burn.vue";
	import newTransfer from "@/components/popup/newTransfer.vue";
	import Market from "@/components/popup/market.vue";

	const BANNER_KEY = "BANNER";
	const AVATAR_KEY = "AVATAR";

	export default {

		components: {
			resale,
			message,
			Transfer,
			LargeImg,
			Soldout,
			PutOnSale,
			Saletotal,
			Burn,
			newTransfer,
			Market
		},
		name: "carddetail",
		data: () => ({
			Appeal: false,
			Banprompt:false,
			currfriend: {},
			pageData: {},
			opensale: false,
			headers: [{
					text: "Event",
					value: "event",
					sortable: false
				},
				{
					text: "Form",
					value: "form",
					sortable: false
				},
				{
					text: "To",
					value: "to",
					sortable: false
				},
				{
					text: "Transcation",
					value: "transcation",
					sortable: false
				},
				{
					text: "Time",
					value: "time",
					sortable: false
				},
			],
			PropertyList: [


			],
			desserts: [],
			openShare: false,
			openmsg: false,
			opentrans: false,
			isOnsale: true,
			addressExploer: "",
			IpfsHub: "",
			IpfsMetadata: "",
			sharePath: "",
			nftAddress: "",
			tokenid: "",
			isShowFalse: false,
			isShowSuccess: false,
			isShowDelistSuccess: false,
			isShowDelistFalse: false,
			falseTitle: "reslse filed",
			successTitle: "resale success",
			delistSuccessTitle: "delist success",
			delistFalseTitle: "delist filed",
			openLargemg: false,
			contractId: "",
			logoimg,
			assetId: "",
			Sold: false,
			openputonsale: false,
			now: "",
			start: "",
			end: "",
			insure: "",
			day: 0,
			hour: 0,
			minutes: 0,
			second: 0,
			seconds: "",
			hh: 0,
			mm: 0,
			ss: 0,
			moreseconds: "",
			openaban: false,
			auctionNo: '',
			auctionStatus: '',
			address: '',
			imgUrl: '',
			name: '',
			constructID: '',
			onSaleTokenIds: '',
			offSaleTokenIds: '',
			supplyLimit: '',
			nftType: '',
			avliableLenth: 0,
			Total: false,
			isShowAva: false,
			Burning: false,
			Transfer: false,
			isShowHover: true,
			isshowLincense: false,
			show: false,
		}),

		computed: {
			isMobile: function() {
				return this.$vuetify.breakpoint.mobile;
			},
		},
		filters: {
			emptystr: function(value) {
				if (!value || value.indexOf("NaN") >= 0) return " ";
				return value;
			},
			didfilter: function(value) {
				if (value && value.length > 24) {
					return value.substr(0, 12) + "..." + value.substr(-12);
				} else {
					return "";
				}
			},
		},
		async mounted() {
			//  this.Time(); //调用定时器
			window.eventBus.$on('nftAppealed', this.nftAppealed);
			await this.initData();
			window.eventBus.$on('burnCollect', this.collection);
			window.addEventListener("click", this.clickOther);
		},
		methods: {
			async nftAppealed(){
				console.log("wxl -- 3333333");
				await this.initData();

			},
			getTime() {
				this.now = Date.parse(new Date());
				this.start = this.pageData.auctionStartTime;
				this.end = this.pageData.auctionEndTime;
				// this.start = Date.parse(new Date("2022-01-10 14:18:00"));
				// this.end = Date.parse(new Date("2022-01-10 14:19:00"));
				this.insure = 1000 * 60 * 60 * 48 + this.end; //48小时 暂时1小时

				if (this.pageData.auctionStatus == 4 || this.pageData.auctionStatus == 0 || this.pageData.auctionStatus ==
					1) {
					if (this.pageData.auctionNoBid && this.pageData.auctionNoBid == true) {
						this.insure = 1000 * 60 * 60 * 1 + this.end; //1小时 流拍1小时
					}


					if (this.now < this.start) {
						this.seconds = (this.start - this.now) / 1000;
					} else if (this.now < this.end) {
						this.seconds = (this.end - this.now) / 1000;
					} else if (this.now < this.insure) {
						this.moreseconds = (this.insure - this.now) / 1000;
					}
				}
				if (this.pageData.auctionStatus == 5) {
					if (this.insure > this.now) {
						this.moreseconds = (this.insure - this.now) / 1000;
					} else {
						this.moreseconds = 0;
					}

				}
			},
			// 天 时 分 秒 格式化函数
			countDown() {
				if (this.seconds > 0) {
					let d = parseInt(this.seconds / (24 * 60 * 60));
					let h = parseInt(((this.seconds / (60 * 60)) % 24) + d * 24);
					this.hour = h < 10 ? "0" + h : h;
					let m = parseInt((this.seconds / 60) % 60);
					this.minutes = m < 10 ? "0" + m : m;
					let s = parseInt(this.seconds % 60);
					this.second = s < 10 ? "0" + s : s;
				}
			},
			moreCountDown() {
				if (this.moreseconds > 0) {
					let d = parseInt(this.moreseconds / (24 * 60 * 60));
					let h = parseInt(((this.moreseconds / (60 * 60)) % 24) + d * 24);
					this.hh = h < 10 ? "0" + h : h;
					let m = parseInt((this.moreseconds / 60) % 60);
					this.mm = m < 10 ? "0" + m : m;
					let s = parseInt(this.moreseconds % 60);
					this.ss = s < 10 ? "0" + s : s;
				}
			},
			//定时器没过1秒参数减1
			Time() {
				setInterval(() => {
					if (this.seconds > 0) {
						this.seconds -= 1;
						this.countDown();
					} else if (this.moreseconds > 0) {
						this.moreseconds -= 1;
						this.moreCountDown();
					} else {
						this.getTime();
					}

				}, 1000);
			},
			collection() {
				this.initData()
			},
			moused() {
				console.log("wxl --- 222222")
				this.show = true
			},
			leave() {
				this.show = false
			},
			async initData() {
				let params = {
					tokenAddress: this.$route.query.tokenAddress,
					nftId: this.$route.query.nftId,
					owner: this.$store.state.did,
				};
				if (!params.owner) {
					// saledetail
					let targetUrl = window.location.href.replace("carddetail", "saledetail");
					location.href = targetUrl;
					return
				}
				let res = await api.home.nftTokenInfo(params);
				if (res.code == 10002) {
					let targetUrl = window.location.href.replace("carddetail", "saledetail").replace(
						"from=personalPage", "");
					location.href = targetUrl;
					return
				}
				let ipfsImg = "";
				let ipfsVideo = "";


				if (!res.data.imgUrl) {
					res.data.imgUrl = "QmPcXBirGiUSyjb8i2FQUpNDTtPHBsgo7tviwwm4YQeJ6p"
				}
				ipfsImg = res.data.imgUrl;
				this.sharePath = ipfsImg;
				res.data.imgUrl = await getFileSrc(BANNER_KEY, res.data.imgUrl);
				res.data.issuerImg = await getFileSrc(BANNER_KEY, res.data.creatorPhoto);
				res.data.issuesTime = this.timestampToDate(res.data.issuesTime);
				this.addressExploer = "https://explorer.loopring.io/collections/" + res.data.contractAddress
					.toLowerCase()

				this.IpfsHub = res.data.imgUrl;
				this.IpfsMetadata = this.getImageIpfs(res.data.metadataUrl);
				this.pageData = res.data;
				this.PropertyList = JSON.parse(res.data.properties)
				if (this.PropertyList)
					this.PropertyList = this.PropertyList.filter((item) => item.trait_type != '' && item.value != '')
				this.pageData.description = this.pageData.description.replace(
					/\n/g,
					"<br/>"
				);
				if (!this.pageData.creatorName) {
					this.pageData.creatorName = this.pageData.creator.substr(0, 6) + "..." + this.pageData.creator
						.substr(-6)
				}
				this.nftAddress = this.pageData.nftAddress;
				this.tokenid = this.pageData.tokenId;
				this.auctionNo = this.pageData.auctionNo;
				this.auctionStatus = this.pageData.auctionStatus;
				this.address = this.pageData.contractAddress;

				this.imgUrl = this.pageData.imgUrl;
				this.name = this.pageData.name;
				this.constructID = this.pageData.id;
				this.onSaleTokenIds = this.pageData.onSaleTokenIds;
				this.offSaleTokenIds = this.pageData.offSaleTokenIds;
				this.supplyLimit = this.pageData.supplyLimit;

				this.nftType = this.pageData.nftType;
				if (this.pageData.offSaleTokenIds != null) {
					this.avliableLenth = this.pageData.offSaleTokenIds.length;
				}
				if (this.pageData.onSaleTokenIds != null) {
					this.avliableLenth += this.pageData.onSaleTokenIds.length;
				}

				let res2 = await api.home.assetHistory({
					nftId: this.$route.query.nftId,
					tokenAddress: this.$route.query.tokenAddress,
					aboutMe: 1,
					owner: this.$store.state.did
				});
				this.desserts = [];
				res2.data.forEach((v) => {
					let from = logoimg;
					let to = logoimg;
					if (v.senderPhoto) {
						from = this.getImageIpfs(v.senderPhoto);
					}
					if (v.recipientPhoto) {
						to = this.getImageIpfs(v.recipientPhoto);
					}
					let fromName = v.from.substr(0, 5) + "..." + v.from.substr(-5);
					let toName = v.to.substr(0, 5) + "..." + v.to.substr(-5);
					if (v.senderName) {
						fromName = v.senderName;
					}
					if (v.recipientName) {
						toName = v.recipientName;
					}
					let price = v.amount + " " + v.marketTokenType
					if (v.amount == null) {
						price = "";
					}

					let event = "";
					switch (v.txType) {
						case 1:
							event = "MINT"
							break;
						case 2:
							event = "LIST"
							break;
						case 3:
							event = "DELIST"
							break;
						case 4:
							event = "TRADE"
							break;
						case 5:
							event = "TRANSFER"
							break;
					}
					this.desserts.push({
						event: event,
						price: price,
						form: fromName,
						to: toName,
						time: this.timestampToDateTime(v.createTime),
						formavatarSrc: from,
						toavatarSrc: to
					});
				});
				this.Time(); //调用定时器

				// 判断用户是否存在
				let param = {
					address: this.pageData.creator,
				};
				let ress = await api.home.loopringUserInfo(param);
				if (ress.data.createTime == null) {
					this.isShowHover = false
				} else {
					this.isShowHover = true
				}
			},
			share,
			shareBtn() {
				this.openShare = true;
			},
			saleBtn() {
				this.opensale = true;
			},
			//销售统计弹层
			saleTotalBtn() {
				this.Total = true;
			},
			openTotal(e) {
				this.Total = e;
			},
				 openBanprompt(e){
         this.Banprompt = e
      },
			Open(e) {
				if (e == 1) {
					this.initData();
					this.opensale = false;
				} else {
					this.opensale = e;
				}
			},
			// 结束售卖弹层
			EndSaleBtn() {
				this.Sold = true;
			},
			openSold(e) {
				this.Sold = e;

			},
			TransferBtn() {
				this.Transfer = true;
			},
			opentransfer(e) {
				this.Transfer = e;
			},
			// 销毁
			burnBtn() {
				this.Burning = true;
			},
			openBurn(e) {
				this.Burning = e;
			},
			//开始售卖/拍卖弹层
			putOnSaleBtn() {
		

               this.openputonsale = true;
            
				
			},
			openPutOnSale(e) {
				console.log("wxl ---- openPutOnSale",e)
				this.openputonsale = e;

			},
			AbanBtn() {
				this.openaban = true;
			},
			openAban(e) {
				this.openaban = e;
			},
		

			clickOther() {
				this.isShowAva = false
				this.openShare = false
			},
			async offSaleBtn() {
				let param = {
					assetId: this.pageData.id,
				};

				let res = await api.home.nftSingleDeList(param);
				if (res.success == true) {
					this.pageData.saleStatus = "0";
					this.isShowDelistSuccess = true;
					this.delistSuccessTitle = this.$t(res.msg);
				} else {
					this.isShowDelistFalse = true;
					this.falseTitle = this.$t(res.msg);
				}
			},
			async offSaleBtnOld() {
				let denomId = this.nftAddress;
				let sender = this.$store.state.did;
				let nftIds = [];
				nftIds.push(this.tokenid);

				if (this.$chainName == "UPTICK EVM") {
					this.$wallet.setContractAddress(this.pageData.nftType, {
						address: this.pageData.nftAddress,
						platformAddress: this.pageData.platformAddress,
					});
					let result = await this.$wallet.revokeApprovesWithArray(
						this.pageData.nftType,
						nftIds,
						this.pageData.price,
						[this.assetId]
					);
					if (result.hash) {
						this.isShowSuccess = true;
						this.falseTitle = this.$t(res.data.msg);
					} else {
						this.isShowFalse = true;
						this.falseTitle = this.$t(res.data.msg);
					}
					return;
				}

				let param = {
					callBack: `${this.$env.VUE_APP_API_URL}` +
						"/api/1.0/callback/offsale?&contractAddress=" +
						denomId +
						"&owner=" +
						sender,
					denom: denomId,
					tokenIds: nftIds,
					owner: sender,
				};

				let res = await api.home.Saleout(param);
				if (res.data.success == true) {
					this.isShowSuccess = true;
					this.falseTitle = this.$t(res.data.msg);
				} else {
					this.isShowFalse = true;
					this.falseTitle = this.$t(res.data.msg);
				}
			},
			msgBtn() {
				this.currfriend = {
					fansName: this.pageData.creatorName,
					fansAddress: this.pageData.creator,
				};
				this.openmsg = true;
			},
			//弹框关闭后获取open值
			OpenMsg(e) {
				this.openmsg = e;
			},
			toPage() {
				if (this.pageData.creator == this.$store.state.did) {
					this.$router.push({
						name: "PersonalPage",
						query: {
							did: this.pageData.creator
						},
					});
				} else {
					this.$router.push({
						name: "Otherpage",
						query: {
							did: this.pageData.creator
						},
					});
				}
			},
			timestamptitleTime(time) {
				let date = new Date(time);
				let Y = date.getFullYear() + ".";
				let M =
					(date.getMonth() + 1 < 10 ?
						"0" + (date.getMonth() + 1) :
						date.getMonth() + 1) + ".";
				let D =
					(date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + " ";
				let strDate = Y + M + D;
				return strDate;
			},
			//查看大图
			openLargeImg() {
				this.openLargemg = true;
			},
			//弹框关闭后获取open值
			closeLargeImg(e) {
				this.openLargemg = e;
			},
			mousedlincense() {
				this.isshowLincense = true
			},
			leavelincense() {
				this.isshowLincense = false
			},
	
			//申诉弹层
			AppealBtn() {
				this.Appeal = true;
			},
			openAppeal(e) {
				this.Appeal = e;
			},
		},
	};
</script>

<style lang="scss" scoped>

	.elevation-1 {
		border-bottom: 1px solid #eee;
	}

	.table ::v-deep .elevation-1 {
		height: 51px;
		box-shadow: 2px 2px 5px #ccc !important;
		background-blend-mode: normal, normal;
		border-radius: 5px;
	}

	.pointer {
		cursor: pointer;

		img {
			width: 42px;
			height: 42px;
			border-radius: 50%;
		}
	}

	.explaincontract {
		width: 130px;
		height: 40px;
		background-color: #270645;
		border-radius: 3px;
		position: absolute;
		top: 25px;
		left: 220px;
		z-index: 1;

		span {
			margin: auto;
			font-family: Helvetica;
			font-size: 12px;
			font-weight: normal;
			font-stretch: normal;
			line-height: 15px;
			letter-spacing: 0px;
			color: #ffffff;
			margin-left: 13px;
			margin-right: 13px;
		}

	}

	.contain {
		margin: 0;
		padding: 0;

		.card_img {
			/*width: 1221px;*/
			/*height: 611px;*/
			background-color: #f8f6fd;

			img {
				display: block;
				width: 100%;
				height: 610px;
				margin: 0 auto;
				object-fit: contain;
			}

			.status {
				float: right;
				position: relative;
				right: 0;
				bottom: 56px;

				img {
					width: 85px;
					height: 85px;
				}

				.ban {
					width: 25px;
					height: 25px;
					display: block;
					position: absolute;
					top: 50px;
					left: -34px;
				}
				
			}
		}

		.cirtile {

			/*width: 748px;*/
			.sharestyle {
				position: relative;

				.tit {
					font-family: Helvetica;
					font-weight: bold !important;
					font-size: 40px;
					font-weight: bold;
					font-stretch: normal;
					letter-spacing: 0px;
					color: #270645;
				}

				.share {
					width: 166px;
					height: 161px;
					background-image: linear-gradient(#ffffff, #ffffff),
						linear-gradient(#6f58d9, #6f58d9);
					background-blend-mode: normal, normal;
					box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
					border-radius: 5px;
					position: absolute;
					right: 25px;
					top: 0px;
					z-index: 20000;

					img,
					.icon {
						display: inline-block;
						width: 25px;
						height: 25px;
					}

					span {
						font-weight: bold;
						font-size: 13px;
						font-weight: normal;
						font-stretch: normal;
						letter-spacing: 0px;
						color: #270645;
					}
				}
			}

			.cont,
			.link {
				font-family: Helvetica;
				font-size: 16px;
				font-weight: normal;
				font-stretch: normal;
				letter-spacing: 0px;
				color: #270645;
				width: 722px;
			}

			.title {
				font-family: Helvetica;
				font-size: 15px;
				font-weight: bold;
				font-stretch: normal;
				letter-spacing: 0px;
				color: #270645;
			}

			.lincenseimg {
				position: relative;

				img {

					height: 30px;
				}

				.explainlincense {
					width: 330px;
					height: 100px;
					background-color: #270645;
					border-radius: 3px;
					position: absolute;
					top: 35px;
					// left: 25px;
					z-index: 1;

					span {
						margin: auto;
						font-family: Helvetica;
						font-size: 12px;
						font-weight: normal;
						font-stretch: normal;
						line-height: 15px;
						letter-spacing: 0px;
						color: #ffffff;
						margin-left: 13px;
						margin-right: 13px;
					}

				}
			}

			.Properties {
				width: 185px;
				font-family: Helvetica;
				font-size: 15px;
				font-weight: normal;
				font-stretch: normal;
				letter-spacing: 0px;
				color: #270645;
			}

			.PropertiesValue {
				font-family: Helvetica;
				font-size: 15px;
				font-weight: normal;
				font-stretch: normal;
				letter-spacing: 0px;
				color: #270645;
			}

			.fontblue {
				color: #1d42ff;
			}

			.NftID {
				font-family: Helvetica;
				font-size: 14px;
			}

			.createName {
				font-family: Helvetica;
				font-size: 14px;
				font-weight: normal;
				font-stretch: normal;
				letter-spacing: 0px;
				color: #270645;

				span {
					
					color: #1d42ff;
				}
			}

			.views {
				a {
					text-decoration: none;
				}

				img {
					width: 30px;
					height: 30px;
				}

				.mt-1 {
					font-weight: bold;
				}
			}

			.avaiable {
				font-family: Helvetica;
				font-size: 15px;
				font-weight: bold;
				font-stretch: normal;
				color: #270645;
			}

			.footor {
				.sub {
					position: relative;
					.text--sub-title {
						font-size: 15px;
						font-weight: bold;
					}

					.text--number {
						font-size: 15px;
						font-weight: bold !important;
					}

					.sale {
						background-color: #1d42ff !important;
						width: 100%;
						height: 100%;
						border: none;
						border-radius: 20px;
						background: #fff;
						font-family: Helvetica;
						font-weight: bold;
						font-size: 15px;
						font-weight: bold;
						font-stretch: normal;
						letter-spacing: 0;
						margin-top: -10px;
					}

					.btnbox {
						width: 106px;
						height: 41px;
						box-sizing: border-box;
						padding: 2px;
						// background-image: linear-gradient(
						//         0deg,
						//         #d202fd 0%,
						//         #a538fe 26%,
						//         #776eff 51%,
						//         #01facd 100%
						// );
						background-color: #1d42ff;
						border-radius: 20px;
						margin-left: -5px;
						margin-right: 10px;

						.rd {
							width: 100%;
							height: 100%;
							border: none;
							border-radius: 17px;
							background: #fff;
							font-family: Helvetica;
							font-weight: bold;
							font-size: 15px;
							font-weight: bold;
							font-stretch: normal;
							letter-spacing: 0;
							color: #270645;
						}
					}

					.satrtsale {
						width: 106px;
						height: 41px;
						background-color: #1d42ff !important;
						border-radius: 20px;
						font-family: Helvetica;
						font-weight: bold;
						font-size: 15px !important;
						font-weight: bold;
						font-stretch: normal;
						line-height: 100px;
						letter-spacing: 0px;
						color: #ffffff;
						text-align: center;
					}

					.withdraw {
						width: 91px;
						height: 28px;
						background-color: #1d42ff !important;
						border-radius: 20px !important;
						font-family: Helvetica;
						font-weight: bold;
						font-size: 15px !important;
						font-weight: bold;
						font-stretch: normal;
						line-height: 100px;
						letter-spacing: 0px;
						color: #ffffff;
						text-align: center;
						border-radius: 17px;
						margin-top: 10px;
					}
					.explain {
					width: 185px;
					height: 56px;
					background-color: #270645;
					border-radius: 3px;
					position: absolute;
					top: 15px;
					left:0px;
					z-index: 2;

					span {
						font-family: Helvetica;
						font-size: 12px;
						font-weight: normal;
						font-stretch: normal;
						line-height: 15px;
						letter-spacing: 0px;
						color: #ffffff;
						margin-left: 13px;
						margin-right: 13px;
					}
	}
				}
			}
		}

		.btn_box {
			margin-left: 126px;
			width: 850px;

			.ava {
				max-height: 42px;

				.name {
					display: block;
					font-family: Helvetica;
					font-weight: bold;
					font-size: 15px;
					font-weight: normal;
					font-stretch: normal;
					letter-spacing: 0px;
					color: #270645;
				}

				.time {
					font-family: Helvetica;
					font-size: 11px;
					font-weight: normal;
					font-stretch: normal;
					letter-spacing: 0px;
					color: #766983;
				}

				.msg {
					width: 25px;
					height: 24px;
				}

				.share {
					width: 166px;
					height: 161px;
					background-image: linear-gradient(#ffffff, #ffffff),
						linear-gradient(#6f58d9, #6f58d9);
					background-blend-mode: normal, normal;
					box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
					border-radius: 5px;
					position: absolute;
					right: 180px;
					top: 730px;

					img,
					.icon {
						display: inline-block;
						width: 25px;
						height: 25px;
					}

					span {
						font-family: Helvetica;
						font-weight: bold;
						font-size: 13px;
						font-weight: normal;
						font-stretch: normal;
						letter-spacing: 0px;
						color: #270645;
					}
				}
			}

			.views {
				a {
					text-decoration: none;
				}

				img {
					width: 30px;
					height: 30px;
				}

				.mt-1 {
					font-weight: bold;
				}
			}

			.btn {
				text-align: left;

				button {
					width: 106px;
					height: 41px;
					background-color: #1d42ff;
					border-radius: 20px;
					font-family: Helvetica;
					font-weight: bold;
					font-size: 15px;
					font-weight: normal;
					font-stretch: normal;
					line-height: 41px;
					letter-spacing: 0px;
					color: #ffffff;
				}
			}
		}

		.table {
			margin-bottom: 84px;

			.tab_tit {
				font-family: Helvetica;
				font-weight: bold !important;
				font-size: 25px;
				font-weight: normal;
				font-stretch: normal;
				letter-spacing: 0px;
				color: #270645;

				.table_item_font {
					.item_fonts {
						text-overflow: ellipsis;
						white-space: nowrap;
						overflow: hidden;
						width: 150px;
					}
				}
			}
		}

		&.mobile {
			.auction-status {
				// display: flex;
				flex-direction: column !important;

				.ml-6 {
					margin-left: 0px !important;
				}
			}

			.NftID {
				word-break: break-all;
			}

			.card_img {
				width: 100%;

				.sizeimg {
					width: 100%;
					height: auto;
				}

				.status {
					float: right;
					position: relative;
					right: 0;
					bottom: 65px;

					img {
						width: 85px;
						height: 85px;
					}

					.ban {
						width: 25px;
						height: 25px;
						display: block;
						position: absolute;
						top: 50px;
						left: -34px;
					}
					
				}
			}

			.cirtile {

				.cont,
				.link {
					max-width: 100%;
				}
			}

			.card_info {
				.sharestyle {
					margin-top: 20px;
				}

				.btn_box {
					max-width: 100%;

					.ava {
						.share {
							width: 166px !important;
							height: 161px;
							background-image: linear-gradient(#ffffff, #ffffff),
								linear-gradient(#6f58d9, #6f58d9);
							background-blend-mode: normal, normal;
							box-shadow: 0px 0px 9px 0px rgba(0, 0, 0, 0.09);
							border-radius: 5px;
							position: relative;
							right: 150px;
							top: 50px;

							img,
							.icon {
								display: inline-block;
								width: 25px;
								height: 25px;
							}

							span {
								font-family: Helvetica;
								font-weight: bold;
								font-size: 13px;
								font-weight: normal;
								font-stretch: normal;
								letter-spacing: 0px;
								color: #270645;
							}
						}
					}
				}
			}

			.table {
				margin-bottom: 100px;

				.table_font {
					font-weight: bold;
					font-family: Helvetica;
					font-size: 14px;
					font-stretch: normal;
					letter-spacing: 0px;
					color: #270645;
				}

				.table_item_font {
					.item_font {
						font-weight: normal;
						font-family: Helvetica;
						font-size: 14px;
						font-stretch: normal;
						letter-spacing: 0px;
						color: #270645;
						display: flex;
						align-items: center;
					}

					.item_fonts {
						text-overflow: ellipsis;
						white-space: nowrap;
						overflow: hidden;
						width: 10px;
					}

					.v-avatar {
						margin-top: 20px;
					}
				}
			}
		}
	}

	.sizeimg {
		object-fit: contain;
	}

	.video {
		max-height: 610px;
		max-width: 100%;
		width: 100%;
		height: 100%;
		margin: auto;
		object-fit: contain;
		align-items: center;
		/*垂直居中*/
		justify-content: center;
		/*水平居中*/
	}
</style>
