
package org.uptick.market.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 2022-04-12
 */

@Document(collection = "asset")
@ToString
@Data
public class AssetDB {

    private String contractAddress;

    private String description;

    private String name;

    private String imgUrl;

    private String nftData;

    private String nftId;

    private String creator;

    private String nftType;

    private String channel;

    private Integer originalRoyaltyPercentage;

    private Integer royaltyPercentage;

    private String metadataUrl ;

    private String properties ;

    private String lincenseCode ;

    private Date createTime;


}
