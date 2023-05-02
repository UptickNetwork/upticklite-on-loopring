package org.uptick.market.middleware.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Data
@Document(collection = "nft_metadata")
@ToString
public class NftMetadata implements Serializable {

    private String name;
    private String description;
    private String image;

    private String localization;
    private String minter;
    private String collection_metadata;
    private String banner_uri;
    private String animation_url;
    private String mint_channel;
    private String properties;
    private String attributes;
    private String lincense_code ;
    private String license_code ;
    private String metadata_uri;
}
