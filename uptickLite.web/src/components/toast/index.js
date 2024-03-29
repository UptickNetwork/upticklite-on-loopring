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
/**
 * fork  https://github.com/eolant/vuetify-toast-snackbar
 */

import Toast from './Toast.vue'

function init(Vue, globalOptions = {}) {
  let cmp = null
  const queue = []
  const property = globalOptions.property || '$toast'

  function createCmp(options) {
    const component = new Vue(Toast)
    const componentOptions = { ...Vue.prototype[property].globalOptions, ...options }

    if (componentOptions.slot) {
      component.$slots.default = componentOptions.slot
      delete componentOptions.slot
    }

    Object.assign(component, componentOptions)
    document.body.appendChild(component.$mount().$el)

    return component
  }

  function show(message, options = {}) {
    if (cmp) {
      const isQueueable = options.queueable !== undefined ? options.queueable : globalOptions.queueable

      if (isQueueable) {
        queue.push({ message, options })
      }
      else {
        cmp.close()
        queue.unshift({ message, options })
      }

      return
    }

    options.message = message
    cmp = createCmp(options)
    cmp.$on('statusChange', (isActive, wasActive) => {
      if (wasActive && !isActive) {
        cmp.$nextTick(() => {
          cmp.$destroy()
          cmp = null

          if (queue.length) {
            const toast = queue.shift()
            show(toast.message, toast.options)
          }
        })
      }
    })
  }

  function shorts(options) {
    const colors = ['success', 'info', 'error', 'warning']
    const methods = {}

    colors.forEach(color => {
      methods[color] = (message, options) => show(message, { color, ...options })
    })

    if (options.shorts) {
      for (let key in options.shorts) {
        const localOptions = options.shorts[key]
        methods[key] = (message, options) => show(message, { ...localOptions, ...options })
      }
    }

    return methods
  }

  function getCmp() {
    return cmp
  }

  function clearQueue() {
    return queue.splice(0, queue.length)
  }

  Vue.prototype[property] = Object.assign(show, {
    globalOptions,
    getCmp,
    clearQueue,
    ...shorts(globalOptions)
  })
}

if (typeof window !== 'undefined' && window.Vue) {
  window.Vue.use(init)
}

export default init