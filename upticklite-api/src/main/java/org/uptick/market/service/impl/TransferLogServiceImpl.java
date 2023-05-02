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
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.uptick.market.dao.TransferLogDao;
import org.uptick.market.denum.TxType;
import org.uptick.market.dto.PageFrame;
import org.uptick.market.dto.TransferLogDto;
import org.uptick.market.entity.Asset;
import org.uptick.market.entity.OrderDetails;
import org.uptick.market.entity.TransferLog;
import org.uptick.market.entity.User;
import org.uptick.market.service.OrderDetailsService;
import org.uptick.market.service.TransferLogService;
import org.uptick.market.service.UserService;
import org.uptick.market.util.IdUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@SuppressWarnings("unchecked")
@Slf4j
@Service
public class TransferLogServiceImpl extends BaseServiceImpl<TransferLog, TransferLogDao> implements TransferLogService {


    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private UserService userService;

    @Override
    public void addLog(List<Asset> assets, String from, String to, String txid, TxType txType) {

        String serialNo = IdUtils.getUUID();

        List<TransferLog> transferLogList = new ArrayList<>();
        for (Asset var : assets) {

            TransferLog transferLog = new TransferLog();
            transferLog.setAssetId(var.getId());
            transferLog.setFrom(from);
            transferLog.setContractAddress(var.getContractAddress());
            transferLog.setSerialNo(serialNo);
            transferLog.setNftId(var.getNftId());
            transferLog.setTo(to);
            transferLog.setTxId(txid);
            transferLog.setTxType(txType.getType());
            transferLogList.add(transferLog);

        }
        saveBatch(transferLogList);
    }

    @Override
    public List<TransferLogDto> nftHistoryPage(TransferLog param, PageFrame pageFrame) {
        PageQuery<TransferLog> pageQuery = new PageQuery<>();
        pageQuery.setPageSize(pageFrame.getPageSize());
        pageQuery.setPageNumber(pageFrame.getPageNumber());
        pageQuery.setParas(param);
//        PageQuery<TransferLogDto> transferLogDtoPageQuery = this.getEntityDao().nftHistoryPage(pageQuery);
        List<TransferLogDto> transferLogDtos = this.getEntityDao().nftHistory(param);
        if (!CollectionUtils.isEmpty(transferLogDtos)) {

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setNftId(transferLogDtos.get(0).getNftId());
            List<OrderDetails> template = orderDetailsService.template(orderDetails);

            for (OrderDetails details : template) {
                TransferLogDto transferLog = new TransferLogDto();
                transferLog.setFrom(details.getSender());
                User userByAddress = userService.getUserByAddress(details.getSender());
                if (userByAddress != null) {
                    transferLog.setSenderName(userByAddress.getName());
                    transferLog.setSenderPhoto(userByAddress.getProfilePhoto());
                }
                transferLog.setTo(details.getRecipient());
                User userByAddress1 = userService.getUserByAddress(details.getRecipient());
                if (userByAddress1 != null) {
                    transferLog.setRecipientName(userByAddress1.getName());
                    transferLog.setRecipientPhoto(userByAddress1.getProfilePhoto());
                }
                transferLog.setTxType(4);
                transferLog.setNftId(details.getNftId());
                transferLog.setCreateTime(details.getCreateTime());
                transferLog.setAmount(details.getPayPrice());
                transferLog.setMarketTokenType(details.getTokenType());
                transferLogDtos.add(transferLog);
            }

        }
        Collections.sort(transferLogDtos, new Comparator<TransferLogDto>() {
            public int compare(TransferLogDto a, TransferLogDto b) {
                return b.getCreateTime().compareTo(a.getCreateTime());
            }
        });

        return transferLogDtos;
    }

}
