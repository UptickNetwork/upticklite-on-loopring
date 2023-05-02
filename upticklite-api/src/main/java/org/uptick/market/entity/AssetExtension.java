
package org.uptick.market.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beetl.sql.core.annotatoin.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 2022-06-24
 */

@Table(name = "asset_extension")
@Data
@EqualsAndHashCode(callSuper = true)
public class AssetExtension extends CrudEntity {

    private static final long serialVersionUID = 123456789L;

    private BigDecimal issuerPrice;

    private Long likeCount;

    private Long viewCount;

    private String createOwner;

    private String nftId;

    private Long totalInventory;

    private String issuerTokenType;

    private String issuerCoinDenom;

    private BigDecimal daiTotalPrice;

    private BigDecimal ethTotalPrice;

    private BigDecimal lrcTotalPrice;

    private BigDecimal usdcTotalPrice;

    private BigDecimal usdtTotalPrice;


}
