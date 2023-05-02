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
const webpack = require("webpack");
const CleanWebpackPlugin = require("clean-webpack-plugin");

const dllPath = "public/vendor";

module.exports = {
  entry: {
    vendor: ["vue", "vue-router", "vuex"],
    web3: ["web3"],
    mathjs: ["mathjs"],
    irisnet: ["@irisnet/irishub-sdk"],
  },
  output: {
    path: path.join(__dirname, dllPath),
    filename: "[name].dll.js",
    library: "[name]_[hash]",
  },
  plugins: [
    new CleanWebpackPlugin(["*.*"], {
      root: path.join(__dirname, dllPath),
    }),
    new webpack.DefinePlugin({
      "process.env": {
        NODE_ENV:  "'production'",
      },
    }),
    new webpack.DllPlugin({
      path: path.join(__dirname, dllPath, "[name]-manifest.json"),
      name: "[name]_[hash]",
      context: process.cwd(),
    }),
  ],
};
