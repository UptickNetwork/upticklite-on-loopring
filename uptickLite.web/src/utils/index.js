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
export function share(type,imageUrl,contractId) {

  let url = window.document.URL;
  if(contractId!=undefined&&contractId!=''){
	  url=url.replace("carddetail","saledetail")
  }
  let enCodeUrl = encodeURIComponent(url);

  switch (type) {
    case "copyLink":
      if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(url);
      } else {
        let textArea = document.createElement("textarea");
        textArea.value = url;
        textArea.style.position = "fixed";
        textArea.style.left = "-999999px";
        textArea.style.top = "-999999px";
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        new Promise((res, rej) => {
          document.execCommand("copy") ? res() : rej();
          textArea.remove();
        });
      }
      this.$toastFn(this.$t("CopySuccess"), {
        color: 'success'
      });

      break;
    case "twitter":
      {

        createTwitterMeta(imageUrl);
        const a = document.createElement("a");
        a.style.display = "none";
        a.target = "_blank";
        a.href = `https://twitter.com/intent/tweet?url=${enCodeUrl}`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);

      }
      break;
    case "facebook":
      {
        createFacebookMeta(imageUrl);
        const a = document.createElement("a");
        a.style.display = "none";
        a.target = "_blank";
        a.href = `https://www.facebook.com/sharer/sharer.php?u=${enCodeUrl}`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
      }
      break;
    default:
      break;
  }
}


function createTwitterMeta(imgUrl){

    let meta0 = document.createElement('meta');
     meta0.setAttribute('name','twitter:card');
     meta0.setAttribute('content', 'summary_large_image');
     document.getElementsByTagName('head')[0].appendChild(meta0);

     let meta1 = document.createElement('meta');
     meta1.setAttribute('name','twitter:image');
     meta1.setAttribute('content', imgUrl);
     document.getElementsByTagName('head')[0].appendChild(meta1);



}

function createFacebookMeta(imgUrl){

  let meta0 = document.createElement('meta');
  meta0.setAttribute('name','og:image');
  meta0.setAttribute('content', imgUrl);
  document.getElementsByTagName('head')[0].appendChild(meta0);


}