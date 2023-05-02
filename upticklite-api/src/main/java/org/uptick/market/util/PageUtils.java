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
import org.beetl.sql.core.engine.PageQuery;
import org.uptick.market.dto.AssetDto;
import org.uptick.market.dto.PageFrame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
public class PageUtils {
    public static PageQuery<AssetDto> pageQuery(PageFrame pageFrame, List<AssetDto> assetDtoList) {
        int pageSize = pageFrame.getPageSize();
        int pageNumber = pageFrame.getPageNumber();
        Collections.sort(assetDtoList, new Comparator<AssetDto>() {
            public int compare(AssetDto a, AssetDto b) {
                return b.getCreateTime().compareTo(a.getCreateTime());
            }
        });
        PageQuery<AssetDto> pageQuery = new PageQuery<>();
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setPageSize(pageSize);
        List<AssetDto> assetDtoResult = new ArrayList<>();
        if (pageNumber == 1) {
            for (int i = 0; i < pageSize; i++) {
                int length = assetDtoList.size();
                if (length == i) {
                    break;
                }
                assetDtoResult.add(assetDtoList.get(i));
            }
            pageQuery.setList(assetDtoResult);
        } else {
            Integer s = (pageNumber - 1) * pageSize;
            Integer number = Integer.valueOf(s);
            Integer size = number + pageSize;
            for (int i = number; i < size; i++) {
                int length = assetDtoList.size();
                if (length <= i) {
                    break;
                }
                assetDtoResult.add(assetDtoList.get(i));
            }
        }
        pageQuery.setList(assetDtoResult);
        pageQuery.setTotalRow(assetDtoList.size());
        return pageQuery;

    }
}
