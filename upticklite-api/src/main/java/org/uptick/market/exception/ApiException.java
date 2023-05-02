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
package org.uptick.market.exception;


import org.uptick.market.denum.Action;
import lombok.Getter;

@Getter
public class ApiException extends Exception {
    private final String msg;
    private final Integer code;

    public ApiException(Action action, String lang) {
        this.msg = action.getMsg(lang);
        this.code = action.getCode();
    }

    public ApiException(Integer code, String message) {
        this.msg = message;
        this.code = code;
    }
}
