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
  <v-snackbar
    :centered="centered"
    :vertical="vertical"
    :timeout="timeout"
    :color="color"
    :multi-line = "multiLine"
    v-model="active"
    class="v-application vts"
    :class="classes"
    @click="dismiss"
    role="alert"
  >
    <v-icon
      dark
      left
      v-if="!!icon"
      class="vts__icon"
      :color="iconColor"
    >
      {{ icon }}
    </v-icon>

    <div class="vts__message" :class="{ 'vts__message--padded': showClose && !closeText }">
      <span v-html="message"></span>
      <slot></slot>
    </div>

    <v-btn
      :icon="!closeText"
      :text="!!closeText"
      class="vts__close"
      :class="{ 'vts__close--icon': !closeText }"
      :color="closeColor"
      v-if="showClose"
      @click="close"
    >
      <v-icon v-if="!closeText">{{ closeIcon }}</v-icon>
      <span v-if="!!closeText">{{ closeText }}</span>
    </v-btn>
  </v-snackbar>
</template>

<script>
export default {
  props: {
    color: {
      type: String,
      default: 'info'
    },
    icon: {
      type: String,
      default: ''
    },
    iconColor: {
      type: String,
      default: ''
    },
    classes: {
      type: [ String, Object, Array ],
      default: ''
    },
    message: {
      type: String,
      default: ''
    },
    timeout: {
      type: Number,
      default: 1000
    },
    dismissable: {
      type: Boolean,
      default: true
    },
    multiLine: {
      type: Boolean,
      default: false
    },
    vertical: {
      type: Boolean,
      default: true
    },
    centered:  {
      type: Boolean,
      default: true
    },
    showClose: {
      type: Boolean,
      default: false
    },
    closeText: {
      type: String,
      default: ''
    },
    closeIcon: {
      type: String,
      default: 'close'
    },
    closeColor: {
      type: String,
      default: ''
    }
  },
  data: () => ({
    active: false
  }),
  mounted() {
    this.$nextTick(() => this.show())
  },
  watch: {
    active: function (isActive, wasActive) {
      this.$emit('statusChange', isActive, wasActive)
    }
  },
  methods: {
    show() {
      this.active = true
    },
    close() {
      this.active = false
    },
    dismiss() {
      if (this.dismissable) {
        this.close()
      }
    }
  }
}
</script>

<style>
  .vts {
    max-width: none !important;
    width: auto !important;
  }
  .vts .v-snack__content {
    justify-content: flex-start;
  }
  .vts__icon {
    margin-right: 12px;
  }
  .vts__message {
    margin-right: auto;
  }
  .vts__close {
    margin: 0 -8px 0 24px !important;
    justify-self: flex-end;
  }
  .vts.v-snack--vertical .vts__icon {
    margin: 0 0 12px !important;
  }
  .vts.v-snack--vertical .v-snack__content {
    padding-bottom: 16px !important;
  }
  .vts.v-snack--vertical .vts__message--padded {
    padding: 12px 0 0;
  }
  .vts.v-snack--vertical .vts__icon + .vts__message {
    padding-top: 0;
  }
  .vts.v-snack--vertical .vts__close {
    margin: 12px 0 -8px !important;
  }
  .vts.v-snack--vertical .vts__close--icon {
    margin: 0 !important;
    position: absolute;
    right: 4px;
    top: 4px;
    transform: scale(0.75);
    padding: 4px !important;
  }
  .v-snack__action  {
    display: none;
  }
  .v-snack .v-snack__wrapper {
    opacity: 0.8;
  }
  .vts__message {
    color: white;
    font-size: 20px
  }
</style>