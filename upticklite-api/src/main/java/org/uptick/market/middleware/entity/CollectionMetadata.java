package org.uptick.market.middleware.entity;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "collection_metadata")
@ToString
public class CollectionMetadata implements Serializable {

    private String contract;
    private String name;
    private String description;
    private String banner_uri;
    private String thumbnail_uri;
    private String avatar_uri;
    private String baseUrl;
    private String mint_channel;
    private String collection_metadata_uri;
    private String contract_status;

}
