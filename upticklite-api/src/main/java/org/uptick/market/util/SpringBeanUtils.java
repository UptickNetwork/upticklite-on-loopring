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

import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class SpringBeanUtils {
    private static ConfigurableApplicationContext applicationContext;

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    private static Map<Class, Object> map = new HashMap<Class, Object>();

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clz) {
        if (applicationContext == null) {
            return null;
        }
        if (map.containsKey(clz)) {
            return (T) map.get(clz);
        } else {
            T bean = applicationContext.getBean(clz);
            map.put(clz, bean);
            return bean;
        }
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
