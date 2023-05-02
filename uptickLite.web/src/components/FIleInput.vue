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
<script lang='js'>
import { VFileInput } from 'vuetify/lib'

export default {
  extends: VFileInput,
  name: 'FIleInput',
  props: {
    hideInput: {
        type: Boolean,
        default: true
    },
    prependIcon: {
      type: String,
      default: null
    },
  },
  
  methods: {
    genSelfDefault() {
        const data = {
          style: {
            width: '100%'
          },
        attrs: {
          'aria-label': 'file-input',
          color: this.validationState,
          dark: this.dark,
          disabled: this.isDisabled,
          light: this.light
        },
        on: {
          click: e => {
            e.preventDefault();
            e.stopPropagation();
            this.$refs.input.click();
          },
          mouseup: e => {
            e.preventDefault();
            e.stopPropagation();
          }
        }
      };
      return this.$createElement('div',data, [ this.$slots.default]);
    },
    genContent() {
      return [this.genPrependSlot(), this.genSelfDefault(), this.genControl(), this.genAppendSlot()];
    },

  }
};

</script>

<style lang='less' scoped></style>
