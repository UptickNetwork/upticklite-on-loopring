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
import Vue from "vue";
import VueRouter from "vue-router";
import Home from "@/views/home/index.vue";

Vue.use(VueRouter);
const originalPush = VueRouter.prototype.push;
const originalReplace = VueRouter.prototype.replace;
//push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
   if (onResolve || onReject)
      return originalPush.call(this, location, onResolve, onReject);
   return originalPush.call(this, location).catch(err => err);
};
//replace
VueRouter.prototype.replace = function push(location, onResolve, onReject) {
   if (onResolve || onReject)
      return originalReplace.call(this, location, onResolve, onReject);
   return originalReplace.call(this, location).catch(err => err);
};

const routes = [
   {
      path: "/",
      redirect: '/index',
   },
   {
      path: "/index",
      name: "Home",
      component: Home,
   },
   {
      path: "/marketplace",
      name: "Marketplace",
      component: () => import("@/views/marketplace/index.vue"),
   },
   {
      path: "/launchpad",
      name: "LaunchPad",
      component: () => import("@/views/launchPad/index.vue"),
   },
   {
      path: "/secondary",
      name: "Secondary",
      component: () => import("@/views/resale/index.vue"),
   },
   
   {
      path: '/creatorlist',
      name: 'Creatorlist',
      component: () => import('../views/frame/creatorNew.vue')
   },
   {
      path: '/saledetail',
      name: 'Saledetail',
      component: () => import('../views/frame/saledetails.vue')
   },
   {
      path: '/salecopy',
      name: 'Salecopy',
      component: () => import('../views/frame/salecopy.vue')
   },
   {
      path: '/otherpage',
      name: 'Otherpage',
      component: () => import('../views/frame/otherpage.vue')
   },
   {
      path: '/carddetail',
      name: 'Carddetail',
      component: () => import('../views/frame/carddetail.vue')
   },
   {
      path: '/create',
      name: 'Create',
      component: () => import('../views/frame/create.vue')
   },
   {
      path: '/createnft',
      name: 'CreateNft',
      component: () => import('../views/frame/createNft.vue')
   },
   {
      path: '/collect',
      name: 'Collect',
      component: () => import('../views/frame/collect.vue')
   },
   {
      path: '/myInfo',
      name: 'MyInfo',
      component: () => import('../views/frame/myInfo.vue')
   },
   {
      path:'/collection/:id',
      name:'CollectionPage',
      component: () => import("@/views/frame/collectionPage.vue")
   },
   {
      path:'/collection/:id/:source',
      name:'CollectionPage',
      component: () => import("@/views/frame/collectionPage.vue")
   },
   {
      path:'/collection_create/:id',
      name:'CollectionPage_create',
      component: () => import("@/views/frame/collectionPage.vue")
   },
   {
      path: '/myOrders',
      name: 'MyOrders',
      component: () => import('../views/frame/myOrders.vue')
   },
   {
      path: '/personal-page',
      name: 'PersonalPage',
      component: () => import('../views/personalPage/index.vue'),
   },
   {
      path: '/personal-pagecopy',
      name: 'PersonalPageCopy',
      component: () => import('../views/personalPage/indexcopy.vue'),
   },
   {
      path: '/helpcenter',
      name: 'HelpCenter',
      component: () => import('../layout/components/HelpPage.vue'),
   },
   {
      path: '/privacy',
      name: 'Privacy',
      component: () => import('../layout/components/privacy_en.vue'),
   },
   {
      path: '/term',
      name: 'Term',
      component: () => import('../layout/components/term_en.vue'),
   }
];



const router = new VueRouter({
   mode: "hash",
   base: process.env.BASE_URL,
   routes,
   scrollBehavior(to, from, savedPosition) {
      if (savedPosition) {
         return savedPosition
      } else {
         return { x: 0, y: 0 }
      }
   }
});

export default router;
