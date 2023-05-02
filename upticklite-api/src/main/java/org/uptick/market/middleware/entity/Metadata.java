package org.uptick.market.middleware.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Metadata implements Serializable {
    private String uri;
    private Integer status;
    private Integer nftType;
    private Integer network;
    private String tokenAddress;
    private String nftId;
    private NftMetadata base;
}
