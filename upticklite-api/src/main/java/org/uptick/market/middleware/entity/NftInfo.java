package org.uptick.market.middleware.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Data
@Document(collection = "nft_info")
@ToString
public class NftInfo implements Serializable {

    private String nftData;
    private String minter;
    private String nftType;
    private String tokenAddress;
    private String nftId;
    private Integer creatorFeeBips;
    private Integer royaltyPercentage;
    private Integer originalRoyaltyPercentage;
    private boolean status;
    private String nftFactory;
    private String nftOwner;
    private String nftBaseUri;
    private String royaltyAddress;
    private String originalMinter;
    private Long createdAt;


}
