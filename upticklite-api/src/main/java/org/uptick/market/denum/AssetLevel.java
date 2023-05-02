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


public enum AssetLevel {
    MARKET(1), FLEA_MARKET(2);

    private final Integer value;

    AssetLevel(Integer value) {
        this.value = value;

    }


    public static AssetLevel parseValue(Integer value) {
        AssetLevel[] values = AssetLevel.values();
        for (AssetLevel t : values) {
            if (t.intValue() == value) {
                return t;
            }
        }
        return null;
    }

    public Integer intValue() {
        return value;
    }
}
