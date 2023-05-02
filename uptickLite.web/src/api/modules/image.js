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
import   {$postThird } from '@/utils/request';

export function uploadImage(file, onUploadProgress) {
    
    
    const formData = new FormData();
    formData.append('image', file);

    //post third
    return $postThird('/uptickapi/ipfs/upload/image', formData, {
        headers: {
            'Content-Type': 'application/octet-stream',
            accessKey: 'uptick',
            accessKeySecret: 'AF0F59F1EEC1728CCFCD5B7B6203D229'
        },
        onUploadProgress
    })
}

export function getNftImg({
    hash,
    size = 'small'
}) {
    
    return $postThird('/image', undefined, {
        params: { hash, size },
		headers: {
			accessKey: 'uptick',
			accessKeySecret: 'AF0F59F1EEC1728CCFCD5B7B6203D229',
		},
	});

};



