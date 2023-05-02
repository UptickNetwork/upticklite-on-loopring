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


public class StringUtils {

    public static String sort(String str) {

        char[] s1 = str.toCharArray();
        for (int i = 0; i < s1.length; i++) {
            for (int j = 0; j < i; j++) {
                if (s1[i] < s1[j]) {
                    char temp = s1[i];
                    s1[i] = s1[j];
                    s1[j] = temp;
                }
            }
        }

        String st = new String(s1);
        return st;
    }

    public static boolean isEmpty(String str) {
        return hasBlank(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isAnyBlank(String... args) {
        return hasBlank(args);
    }


    public static boolean hasBlank(Object... strings) {
        for (Object string : strings) {
            if (isBlank(string)) {
                return true;
            }
        }
        return false;
    }


    private static boolean isBlank(Object string) {
        return string == null || string.toString().trim().equals("");
    }

}
