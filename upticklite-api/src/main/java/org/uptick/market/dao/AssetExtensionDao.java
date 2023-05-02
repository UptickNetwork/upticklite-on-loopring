package org.uptick.market.dao;


import org.beetl.sql.core.annotatoin.Param;
import org.springframework.stereotype.Component;
import org.uptick.market.entity.AssetExtension;

import java.util.List;


/**
 * mapper 2022-04-12 14:28:17
 */
@Component
public interface AssetExtensionDao extends BaseDao<AssetExtension> {


    List<AssetExtension> getAssetExtensionInfoByNftIds(@Param("nftIds") List<String> nftIds);

    int updateByNftId(AssetExtension assetExtension);
}
