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
 import api from "@/api";
import Vue from "vue";
import Vuex from "vuex";


Vue.use(Vuex);

const chain = [{
    chainType: 'IRIS_IRISHUB',
    chainIdDesc: 'IRIS链',
    chainId: 'nyancat-9',
    simplify: 'IRIS'
  },
  {
    chainType: 'EVM_BSC',
    chainIdDesc: '币安智能链',
    chainId: '97',
    simplify: 'BNB'
  },
  {
    chainType: 'EVM_POLYGON',
    chainIdDesc: 'Polygon链',
    chainId: '80001',
    simplify: 'MATIC'
  },
  {
    chainType: 'EVM_ETH',
    chainIdDesc: '以太坊链',
    chainId: '42',
    simplify: 'ETH'
  },
]

export default new Vuex.Store({
  state: {
    userAddress: 'iaa1ax4p4vkzccmldun8kpvqn45xru3twd6e6fvgp0', //test
    //链的信息
    chainType: chain[0].chainType,
    //个人信息
    info: {
      nickName: '',
      did: 'iaa1ax4p4vkzccmldun8kpvqn45xru3twd6e6fvgp0', //test
      userAddress: 'iaa1ax4p4vkzccmldun8kpvqn45xru3twd6e6fvgp0' //test
    },
    did:'',
	homeScrollTop: 0,

    searchInput: undefined,
  },
  mutations: {
    SET_DID(state, did) {
      state.did = did
    },
    SET_CHAINTYPE(state, chainType) {
      state.chainType = chainType
    },
    SET_USER_INFO(state, info) {
      state.info = info
    },
    SET_HOME_SCROLLTOP(state, top) {
      state.homeScrollTop = top
    },
    SET_SEARCH(state, input) {
      state.searchInput = input
    },
  },
  actions: {
    setdid({
      commit
    }, did) {
      commit('SET_DID', did)

    },
    setChainType({
      commit
    }, chainType) {
      commit('SET_CHAINTYPE', chainType)
    },
    async setInfo({
      state,
      commit
    }) {
      const {
        chainType,
        address
      } = state;

      const response = await api.home.loopringUserInfo({
        chainType,
        address
      })
      commit('SET_USER_INFO', response)

    },
    setSearchInput({ commit }, input) {
      commit('SET_SEARCH', input)
    },
  },
  getters: {
    did: state => state.info && state.info.did
  },
  modules: {},

});