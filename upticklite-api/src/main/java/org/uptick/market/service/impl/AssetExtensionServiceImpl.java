package org.uptick.market.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.uptick.market.dao.AssetExtensionDao;
import org.uptick.market.entity.AssetExtension;
import org.uptick.market.service.AssetExtensionService;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unchecked")
@Slf4j
@Service
public class AssetExtensionServiceImpl extends BaseServiceImpl<AssetExtension, AssetExtensionDao> implements AssetExtensionService {

    @Override
    public int updateByNftId(AssetExtension assetExtension) {
        if (assetExtension == null || assetExtension.getNftId() == null) {
            return 0;
        }
        AssetExtension assetExtensionReq = new AssetExtension();
        assetExtensionReq.setNftId(assetExtension.getNftId());
        assetExtensionReq = this.getEntityDao().templateOne(assetExtensionReq);
        if (assetExtensionReq == null) {
            return 0;
        }
        assetExtension.setId(assetExtensionReq.getId());
        return this.getEntityDao().updateTemplateById(assetExtension);

    }
}
