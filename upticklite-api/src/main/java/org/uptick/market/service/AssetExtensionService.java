package org.uptick.market.service;


import org.uptick.market.entity.AssetExtension;

import java.util.List;

/**
 *
 */
public interface AssetExtensionService extends BaseService<AssetExtension> {

    /**
     * @param assetExtension
     * @return
     */
    int updateByNftId(AssetExtension assetExtension);

}
