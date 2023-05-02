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
package org.uptick.market.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
public class DateUtils {
    private DateUtils() {

    }

    protected static ThreadLocal<SimpleDateFormat> defaultDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    protected static ThreadLocal<SimpleDateFormat> yyyyMMddHHmmssSSS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }
    };

    public static String formatDate() {
        return defaultDateFormat.get().format(now());
    }

    public static String formatYYYYMMddHHmmssSSS() {
        return yyyyMMddHHmmssSSS.get().format(now());
    }

    public static String formatDate(Date date) {
        return defaultDateFormat.get().format(date);
    }

    public static String formatDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date now() {
        return new Date();
    }

    public static Long unixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static Long unixTime(Date date) {
        return date.getTime() / 1000L;
    }

    public static Date parseDateString(String time) {

        try {
            return defaultDateFormat.get().parse(time);
        } catch (ParseException e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return null;
    }

    public static Date parseDateFromUnixTime(Long time) {
        return time == null ? null : new Date(time * 1000L);
    }
}
