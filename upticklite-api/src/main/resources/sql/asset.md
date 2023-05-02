
all
===
    select asset.#use("cols")# from asset where asset.deleted = 0

sample
===
	select #use("cols")# from asset asset where  #use("condition")# #use("orderBy")#

cols
===
	asset.id,asset.contract_address,asset.name,asset.description,asset.creator,asset.owner,asset.account_id,asset.img_url,asset.nft_data,asset.nft_id,asset.channel,asset.royalty_percentage,asset.original_royalty_percentage,asset.amount,asset.sale_status,asset.list_amount,asset.level,asset.nft_type,asset.market_price,asset.market_time,asset.market_owner,asset.market_token_type,asset.maker_order_id,asset.maker_type,asset.create_time,asset.update_time,asset.deleted,asset.weight,asset.json,asset.metadata_url,asset.properties,asset.lincense_code,asset.asset_status
updateSample
===
	asset.id=#id#,asset.contract_address=#contractAddress#,asset.name=#name#,asset.description=#description#,asset.creator=#creator#,asset.owner=#owner#,asset.account_id=#accountId#,asset.img_url=#imgUrl#,asset.nft_data=#nftData#,asset.nft_id=#nftId#,asset.channel=#channel#,asset.royalty_percentage=#royaltyPercentage#,asset.original_royalty_percentage=#originalRoyaltyPercentage#,asset.amount=#amount#,asset.sale_status=#saleStatus#,asset.list_amount=#listAmount#,asset.level=#level#,asset.nft_type=#nftType#,asset.market_price=#marketPrice#,asset.market_time=#marketTime#,asset.market_owner=#marketOwner#,asset.market_token_type=#marketTokenType#,asset.maker_order_id=#makerOrderId#,asset.maker_type=#makerType#,asset.create_time=#createTime#,asset.update_time=#updateTime#,asset.deleted=#deleted#,asset.weight=#weight#,asset.json=#json#,asset.metadata_url=#metadataUrl#,asset.properties=#properties#,asset.lincense_code=#lincenseCode#,asset.asset_status=#assetStatus#
forceDeleteByIds
===
    delete from asset where asset.id in (#join(ids)#)

deleteByIds
===
    update asset set asset.deleted = 1 where asset.id in (#join(ids)#)

condition
===
    asset.deleted = 0
	@if(!isEmpty(id)){
	 and asset.id=#id#
	@}
	@if(!isEmpty(contractAddress)){
	 and asset.contract_address=#contractAddress#
	@}
	@if(!isEmpty(name)){
    and asset.name like #'%'+name+'%'#
	@}
	@if(!isEmpty(description)){
	 and asset.description=#description#
	@}
	@if(!isEmpty(creator)){
	 and asset.creator=#creator#
	@}
	@if(!isEmpty(owner)){
	 and asset.owner=#owner#
	@}
	@if(!isEmpty(accountId)){
	 and asset.account_id=#accountId#
	@}
	@if(!isEmpty(imgUrl)){
	 and asset.img_url=#imgUrl#
	@}
	@if(!isEmpty(nftData)){
	 and asset.nft_data=#nftData#
	@}
	@if(!isEmpty(nftId)){
	 and asset.nft_id=#nftId#
	@}
	@if(!isEmpty(channel)){
	 and asset.channel=#channel#
	@}
	@if(!isEmpty(royaltyPercentage)){
	 and asset.royalty_percentage=#royaltyPercentage#
	@}
	@if(!isEmpty(originalRoyaltyPercentage)){
	 and asset.original_royalty_percentage=#originalRoyaltyPercentage#
	@}
	@if(!isEmpty(amount)){
	 and asset.amount=#amount#
	@}
	@if(!isEmpty(saleStatus)){
	 and asset.sale_status=#saleStatus#
	@}
	@if(!isEmpty(listAmount)){
	 and asset.list_amount=#listAmount#
	@}
	@if(!isEmpty(level)){
	 and asset.level=#level#
	@}
	@if(!isEmpty(nftType)){
	 and asset.nft_type=#nftType#
	@}
	@if(!isEmpty(marketPrice)){
	 and asset.market_price=#marketPrice#
	@}
	@if(!isEmpty(marketTime)){
	 and asset.market_time=#marketTime#
	@}
	@if(!isEmpty(marketOwner)){
	 and asset.market_owner=#marketOwner#
	@}
	@if(!isEmpty(marketTokenType)){
	 and asset.market_token_type=#marketTokenType#
	@}
	@if(!isEmpty(makerOrderId)){
	 and asset.maker_order_id=#makerOrderId#
	@}
	@if(!isEmpty(makerType)){
	 and asset.maker_type=#makerType#
	@}
	@if(!isEmpty(createTime)){
	 and asset.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and asset.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and asset.deleted=#deleted#
	@}
	@if(!isEmpty(weight)){
	 and asset.weight=#weight#
	@}
	@if(!isEmpty(json)){
	 and asset.json=#json#
	@}
	@if(!isEmpty(metadataUrl)){
	 and asset.metadata_url=#metadataUrl#
	@}
	@if(!isEmpty(properties)){
	 and asset.properties=#properties#
	@}
	@if(!isEmpty(lincenseCode)){
	 and asset.lincense_code=#lincenseCode#
	@}
	@if(!isEmpty(assetStatus)){
	 and asset.asset_status=#assetStatus#
	@}

orderBy
===
	order by asset.create_time desc

groupById
===
    group by asset.id


conditionOwner
===
    asset.deleted = 0
	@if(!isEmpty(id)){
	 and asset.id=#id#
	@}
	@if(!isEmpty(contractAddress)){
	 and asset.contract_address=#contractAddress#
	@}
	@if(!isEmpty(name)){
    and asset.name like #'%'+name+'%'#
	@}
	@if(!isEmpty(description)){
	 and asset.description=#description#
	@}
	@if(!isEmpty(accountId)){
	 and asset.account_id=#accountId#
	@}
	@if(!isEmpty(imgUrl)){
	 and asset.img_url=#imgUrl#
	@}
	@if(!isEmpty(nftData)){
	 and asset.nft_data=#nftData#
	@}
	@if(!isEmpty(nftId)){
	 and asset.nft_id=#nftId#
	@}
	@if(!isEmpty(channel)){
	 and asset.channel=#channel#
	@}
	@if(!isEmpty(amount)){
	 and asset.amount=#amount#
	@}
	@if(!isEmpty(saleStatus)){
	 and asset.sale_status=#saleStatus#
	@}
	@if(!isEmpty(listAmount)){
	 and asset.list_amount=#listAmount#
	@}
	@if(!isEmpty(level)){
	 and asset.level=#level#
	@}
	@if(!isEmpty(nftType)){
	 and asset.nft_type=#nftType#
	@}
	@if(!isEmpty(marketPrice)){
	 and asset.market_price=#marketPrice#
	@}
	@if(!isEmpty(marketTime)){
	 and asset.market_time=#marketTime#
	@}
	@if(!isEmpty(marketOwner)){
	 and asset.market_owner=#marketOwner#
	@}
	@if(!isEmpty(marketTokenType)){
	 and asset.market_token_type=#marketTokenType#
	@}
	@if(!isEmpty(makerOrderId)){
	 and asset.maker_order_id=#makerOrderId#
	@}
	@if(!isEmpty(makerType)){
	 and asset.maker_type=#makerType#
	@}
	@if(!isEmpty(createTime)){
	 and asset.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and asset.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and asset.deleted=#deleted#
	@}
	@if(!isEmpty(weight)){
	 and asset.weight=#weight#
	@}
	@if(!isEmpty(json)){
	 and asset.json=#json#
	@}



getMarketInfo
===
    select t.* ,ownerUser.name as ownerName,ownerUser.profile_photo as ownerPhoto,contract.name as tokenName,contract.img_url as tokenImgUrl
    from(
        select #use("cols")#, count(id) as  listAmount 
        from asset 
        where  #use("condition")#
        group by asset.nft_id
    ) t
    left join user ownerUser on ownerUser.address=t.owner
    left join contract on contract.address=t.contract_address

getContractList
===
    select count(id) as  listAmount,min(market_price) as minPrice ,max(market_price) as maxPrice 
    from asset 
    where  #use("condition")# 


getMyCreatedListPage
===

    select @pageTag(){
        t.*
    @}
    from (
        select ast.*
        from(
            select distinct(asset.id) as tid, #use("cols")#
            from asset
            where  #use("condition")#
            @if(!isEmpty(search)){
                and asset.name like #'%'+search+'%'#
            @}
            order by asset.level asc
        ) ast
        group by ast.nft_id
        order by ast.create_time desc
    ) t



getMyOwnListPage
===

    select @pageTag(){
        t.*,ownerUser.name as ownerName,ownerUser.profile_photo as ownerPhoto,creatorUser.name as creatorName,creatorUser.profile_photo as creatorPhoto
    @}
    from (
        select #use("cols")#
        from asset
        where  #use("condition")#
        @if(sort==1){
	        order by asset.create_time desc
	    @}
        @if(sort==2){
	        order by asset.update_time desc
	    @}
        
    ) t
    left join user ownerUser on ownerUser.address=t.owner
    left join user creatorUser on creatorUser.address=t.creator
        @if(sort==1){
	        order by t.create_time desc
	    @}
        @if(sort==2){
	        order by t.update_time desc
	    @}

 getMyOwnByCreatorListPage
===

    select @pageTag(){
        t.*,creatorUser.name as creatorName,creatorUser.profile_photo as creatorPhoto
    @}
    from (
        select asset.creator,count(asset.id) as amount,asset.update_time
        from asset
        where  #use("condition")#
        group by asset.creator
        order by asset.update_time desc
    ) t
    left join user creatorUser on creatorUser.address=t.creator
    order by t.update_time desc


getTokenInfo
===
    select #use("cols")#,ownerUser.name as ownerName,ownerUser.profile_photo as ownerPhoto,creatorUser.name as creatorName,creatorUser.profile_photo as creatorPhoto
    from asset
    left join user ownerUser on ownerUser.address=asset.owner
    left join user creatorUser on creatorUser.address=asset.creator
    where  #use("condition")#
    order by asset.level asc,asset.sale_status desc
    limit 0,1



getMarketListPage
===

    select @pageTag(){
        t.*,ownerUser.name as ownerName,ownerUser.profile_photo as ownerPhoto
    @}
    from (
        select #use("cols")#,count(asset.id)
        from asset
        where  #use("condition")#
        @if(!isEmpty(search)){
            and asset.name like #'%'+search+'%'#
        @}
        group by asset.nft_id
        @if(sort==1){
            order by asset.market_time desc
        @}
        @if(sort==2){
            order by rand()
        @}
    ) t
    left join user ownerUser on ownerUser.address=t.owner
        @if(sort==1){
            order by t.market_time desc
        @}
        @if(sort==2){
            order by rand()
        @}


getFleaMarketListPage
===

    select @pageTag(){
        t.*,ownerUser.name as ownerName,ownerUser.profile_photo as ownerPhoto,creatorUser.name as creatorName,creatorUser.profile_photo as creatorPhoto
    @}
    from (
        select #use("cols")#
        from asset
        where  #use("condition")#
        @if(!isEmpty(search)){
            and asset.name like #'%'+search+'%'#
        @}
        @if(sort==1){
            order by asset.market_time desc
        @}
        @if(sort==2){
            order by rand()
        @}
    ) t
    left join user ownerUser on ownerUser.address=t.owner
    left join user creatorUser on creatorUser.address=t.creator
    @if(sort==1){
            order by t.market_time desc
    @}
    @if(sort==2){
            order by rand()
    @}


getHomepageListPage
===

    select @pageTag(){
        t.*,ownerUser.name as ownerName,ownerUser.profile_photo as ownerPhoto
    @}
    from (
        select #use("cols")#
        from asset
        where  #use("condition")#
        group by asset.nft_id
        order by asset.create_time desc
    ) t
    left join user ownerUser on ownerUser.address=t.owner
    order by t.create_time desc

delNftByNftId
===
    update asset set asset.deleted = 1 where  asset.contract_address=#contractAddress# and asset.nft_id=#nftId#


getNftNumber
===
    select count(t.nft_id)
    from(
        select nft_id
        from asset
        where asset.deleted = 0
        group by asset.nft_id
    ) t

getMyCreatedTokenPage
===

    select @pageTag(){
        t.*,contract.contract_status,contract.base_url
    @}
    from (
        select ast.*
        from(
            select 
                distinct(asset.id) as tid,#use("cols")#
            from 
                asset
            where
                #use("conditionOwner")#
                @if(!isEmpty(owner)){
                    and asset.creator =#owner#
                    and asset.`level` = 1
                @}
                @if(!isEmpty(search)){
                    and asset.name like #'%'+search+'%'#
                @}

            UNION

            select 
                distinct(asset.id) as tid, #use("cols")#
            from 
                asset
            where
                #use("conditionOwner")#
                @if(!isEmpty(owner)){
                    and asset.owner=#owner#
                    and asset.creator !=#owner#
                @}
                @if(!isEmpty(search)){
                    and asset.name like #'%'+search+'%'#
                @}

            UNION
            
                select 
                    distinct(asset.id) as tid, #use("cols")#
                from 
                    asset
                where
                    #use("conditionOwner")#
                    @if(!isEmpty(owner)){
                        and asset.owner!=#owner#
                        and asset.creator =#owner#
                    @}
                    @if(!isEmpty(search)){
                        and asset.name like #'%'+search+'%'#
                    @}
        ) ast
        where
            ast.deleted = 0
            @if(!isEmpty(filter) &&  filter == 1){
                and ast.creator = #owner#
            @}
            @if(!isEmpty(filter) &&  filter == 3){
                and (ast.channel is null or ast.channel != 'Uptick-Loopring')
            @}
        group by ast.nft_id
        order by ast.create_time desc
    ) t
    inner join contract on contract.address = t.contract_address


getMyTokenCreatPage
===

    select @pageTag(){
        t.*,contract.contract_status,contract.base_url
    @}
    from (
        select ast.*
        from(

            select 
                distinct(asset.id) as tid, #use("cols")#
            from 
                asset
            where
                #use("condition")#
        ) ast
        where
            ast.deleted = 0
        group by ast.nft_id
        order by ast.create_time desc
    ) t
    inner join contract on contract.address = t.contract_address


templateDto
===
    select #use("cols")# ,ownerUser.name as ownerName,ownerUser.profile_photo as ownerPhoto,creatorUser.name as creatorName,creatorUser.profile_photo as creatorPhoto
    from asset 
    left join user ownerUser on ownerUser.address=asset.owner
    left join user creatorUser on creatorUser.address=asset.creator
    where  
        #use("condition")# 
        @if(!isEmpty(search)){
            and asset.name like #'%'+search+'%'#
        @}
        #use("orderBy")#

getOtherTokenPage
===
    select @pageTag(){
        t.*
    @}
    from (
        select ast.*,ownerUser.name as ownerName,ownerUser.profile_photo as ownerPhoto
        from(
            select
                distinct(asset.id) as tid, #use("cols")#,extension.create_owner,extension.nft_id as exNftId
            from 
                (select * from
                    asset_extension
                    where
                    asset_extension.deleted = 0
                    @if(!isEmpty(owner)){
                    and asset_extension.create_owner = #owner#
                    @}
                ) as extension
            left JOIN 
                asset  
                on 
                    #use("conditionOwner")#
                    and extension.nft_id = asset.nft_id
                    and asset.level = 1
                    @if(!isEmpty(search)){
                        and asset.name like #'%'+search+'%'#
                    @}
            UNION
                SELECT 
                    distinct(asset.id) as tid, asset.*,asset_extension.create_owner,asset_extension.nft_id as exNftId
                from asset 
                LEFT JOIN asset_extension on asset.nft_id = asset_extension.nft_id
                WHERE asset.deleted = 0 and asset.`owner` = #owner# and LEVEL = 2
        ) ast
        left join user ownerUser on ownerUser.address=ast.create_owner
        where
            ast.sale_status = 1 or ast.id is NULL
        order by ast.create_time desc
    ) t


getmarketPlace
===
    select @pageTag(){
        t.*
        @if(!isEmpty(sortType) && sortType == 3){
            ,order_details.create_time as orderSoldTime,order_details.pay_price as order_price,order_details.token_type as orderTokenType
        @}
        @if(!isEmpty(sortType) && (sortType == 7 || sortType == 8)){
            ,asset_extension.view_count,asset_extension.like_count
        @}
        ,contract_status
    @}
    from (
        select 
            #use("cols")#,ownerUser.name as ownerName,ownerUser.profile_photo as ownerPhoto,contract.contract_status
        from (
            select 
                #use("cols")# 
            from
                asset
            where  
                #use("condition")#
                @if(isEmpty(sortType) || sortType != 3){
                    and asset.sale_status =1
                @}
                @if(!isEmpty(sortType) && sortType == 4){
                    and asset.maker_type = 2
                @}
                @if(!isEmpty(search)){
                    and asset.name like #'%'+search+'%'#
                @}
                @if(!isEmpty(minPrice) && !isEmpty(maxPrice)){
                    and asset.market_price >= #minPrice# and asset.market_price <= #maxPrice# 
                @}
                @if(!isEmpty(minPrice) && isEmpty(maxPrice)){
                    and asset.market_price >= #minPrice#
                @}
                @if(!isEmpty(maxPrice) && isEmpty(minPrice)){
                    and asset.market_price <= #maxPrice#
                @}
                @if(!isEmpty(saleModel)){
                    and asset.maker_type = #saleModel#
                @}
                @if(!isEmpty(copyright)){
                   and asset.lincense_code = 'CC0'
                @}
        ) asset
        left join user ownerUser on ownerUser.address=asset.owner and ownerUser.deleted = 0
        left join contract on contract.address = asset.contract_address and contract.deleted = 0 
    ) t 

    @if(!isEmpty(sortType) && sortType == 1){
        order by t.create_time desc
    @}
    @if(!isEmpty(sortType) && sortType == 2){
        order by t.market_time desc
    @}
    @if(!isEmpty(sortType) && sortType == 3){
        inner join order_details on order_details.nft_id = t.nft_id
        @if(!isEmpty(tokenType)){
            and order_details.token_type=#tokenType#
        @}
        @if(!isEmpty(orderMarketType)){
            and order_details.order_market_type=#orderMarketType#
        @}
        order by order_details.create_time desc
    @}
    @if(!isEmpty(sortType) && sortType == 5){
        order by t.market_price
    @}
    @if(!isEmpty(sortType) && sortType == 6){
        order by t.market_price desc
    @}
    @if(!isEmpty(sortType) && sortType == 7){
        inner join asset_extension on asset_extension.nft_id = t.nft_id
        order by asset_extension.view_count desc
    @}
    @if(!isEmpty(sortType) && sortType == 8){
        inner join asset_extension on asset_extension.nft_id = t.nft_id
        order by asset_extension.like_count desc
    @}
    @if(!isEmpty(sortType) && sortType == 9){
        order by RAND()
    @}
