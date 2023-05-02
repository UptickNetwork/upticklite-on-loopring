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
import org.uptick.market.dao.UserDao;
import org.uptick.market.entity.User;
import org.uptick.market.service.UserService;
import org.uptick.market.util.StringUtils;


@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserDao> implements UserService {


    @Override
    public User getUserByAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return null;
        }
        User param = new User();
        param.setAddress(address);
        param.setDeleted(false);
        return templateOne(param);
    }

    @Override
    public User getUserByAccountId(Long accountId) {
        if (accountId == null) {
            return null;
        }
        User param = new User();
        param.setAccountId(accountId);
        param.setDeleted(false);
        return templateOne(param);
    }
}
