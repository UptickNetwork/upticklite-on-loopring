
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
import Popup from './MessageTip.vue'

function init(Vue) {
    const PopupBox = Vue.extend(Popup);
    let instance = new PopupBox().$mount();
    document.body.appendChild(instance.$el);
    
    function show(opt) {
        Vue.nextTick(() => {
            instance.show(opt);
            // show 和弹窗组件里的show对应，用于控制显隐
        })
    }
    Vue.prototype.$mtip = show;
}

export default init;
