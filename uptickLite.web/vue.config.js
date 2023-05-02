const path = require("path");
// const webpack = require("webpack");
// const AddAssetHtmlPlugin = require("add-asset-html-webpack-plugin");
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
    public: "localhost:8087",
    port: 8087,
    open: true,
    https: false,
    proxy: {
      "uptickapi": {
       target: `${process.env.VUE_APP_API_URL}` + "/api/1.0/",
        changeOrigin: true,
        secure: false,
        pathRewrite: {
          "^/uptickapi": "/",
        },
      },
	  "uptickservice": {
      target: `${process.env.VUE_APP_SERVICE_URL}` + "/lrc/",
	    changeOrigin: true,
	    secure: false,
	    pathRewrite: {
	      "^/uptickservice": "/",
	    },
	  },
      "uptickS3": {
        target: "http://metadata.upticknft.com/",
         changeOrigin: true,
         secure: false,
         pathRewrite: {
           "^/uptickS3": "/",
         },
       },

    },
  },
};
