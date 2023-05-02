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


public enum Action {


    ERROR(-1, "Request error"),
    SUCCESS(0, "Success"),
    SYSTEM_ERROR(10001, "System exception"),
    PARAM_ERROR(10002, "Param error"),

    NFT_TOKEN_EXIST(20001, "NFT token already exists"),
    NFT_TOKEN_NOT_EXIST(20002, "NFT token not exists"),
    BURN_ERROR(20003, "Failure to meet destruction rules"),
    TRADE_ERROR(20004, "Trade error"),
    NFT_BASEURL_EXIST(20005, "Short URL already exists"),
    INSUFFICIENT_INVENTORY(20006, "Insufficient inventory"),
    NFT_ID_EXIST(20007, "NFT id already exists"),
    NFT_NOT_ENOUGH(20009, "NFT not enough"),
    NFT_BUG_LIMIT(20008, "NFT buy limit"),
    ;


    private int code;
    private String msg;


    Action(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg(String lang) {
        return this.msg;
    }


    @Override
    public String toString() {
        return "var Action {" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
