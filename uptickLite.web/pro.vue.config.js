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
const path = require("path");
const resolve = (dir) => path.join(__dirname, dir);

module.exports = {
  transpileDependencies: ["vuetify"],
  configureWebpack: {
    resolve: {
      alias: {
        "@": resolve("src"),
        "@components": resolve("src/components"),
      },
    },
  },
  css: {
    loaderOptions: {
      scss: {
        additionalData: `
          @import "~@/styles/variables.scss";
        `,
      },
    },
  },
  devServer: {
    public: '0.0.0.0:80',
    port: 80,
    open:true,
    https: false,
    proxy: {
			'/url': {
        target: 'http://file.upticknft.com/v2/image/url/',
				changeOrigin: true,
        secure:false,
				pathRewrite: {
					'^url': '/',
				},
      },

      '/video': {
         target: 'http://file.upticknft.com/file/url/video/',
         changeOrigin: true,
         secure:false,
         pathRewrite: {
           '^/video': '/',
         },
       },

       '/upImage': {
          // target: 'http://file.uptick.world/v2/image/upload/',
          target: 'http://file.upticknft.com/v2/image/upload/',
          changeOrigin: true,
          secure:false,
          pathRewrite: {
            '^/upImage': '/',
          },
        },
      
      '/upVideo': {
         // target: 'http://file.uptick.world/v2/image/upload/',
         target: 'http://file.upticknft.com/file/upload/video/',
         changeOrigin: true,
         secure:false,
         pathRewrite: {
           '^/upVideo': '/',
         },
       },

      '/merchant':{
        // target: 'http://file.uptick.world/v2/image/upload/',
        target: 'http://13.213.149.227:9993/api/1/merchant/',
        changeOrigin: true,
        secure:false,
        pathRewrite: {
          '^/merchant': '/',
        }
      },

      '/fee': {
        target: 'http://market.upticknft.com/api/1.0/market',
        changeOrigin: true,
        secure:false,
        pathRewrite: {
          '^/fee': '/',
        }
      }


		},
	
	},
};
