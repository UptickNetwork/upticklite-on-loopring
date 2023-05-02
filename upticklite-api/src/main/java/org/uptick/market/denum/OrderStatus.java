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
package org.uptick.market.denum;


public enum OrderStatus {

    CLOSED(0),
    IN_PROGRESS(1),
    SUCCESS(2),
    FAILED(3),
    ;

    private final int status;


    OrderStatus(int status) {
        this.status = status;
    }

    public static OrderStatus parseValue(Integer value) {
        OrderStatus[] values = OrderStatus.values();
        for (OrderStatus t : values) {
            if (t.intValue().equals(value)) {
                return t;
            }
        }
        return null;
    }


    public Integer intValue() {
        return status;
    }
}
