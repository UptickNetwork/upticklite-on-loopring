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
package org.uptick.market.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.uptick.market.dao.DataMapDao;
import org.uptick.market.entity.DataMap;
import org.uptick.market.service.DataMapService;

import java.util.List;


@SuppressWarnings("unchecked")
@Slf4j
@Service
public class DataMapServiceImpl extends BaseServiceImpl<DataMap, DataMapDao> implements DataMapService {


    @Override
    public DataMap findOneByKey(String key) {
        DataMap param = new DataMap();
        param.setDeleted(false);
        param.setName(key);
        return templateOne(param);
    }

    @Override
    public void updateValueByKey(String key, String value) {
        DataMap dataMap = findOneByKey(key);
        if (dataMap != null) {
            dataMap.setValue(value);
            updateTemplateById(dataMap);
        }
    }


    @Override
    public List<DataMap> findByKey(String key) {
        DataMap param = new DataMap();
        param.setDeleted(false);
        param.setName(key);
        return template(param);
    }
}
