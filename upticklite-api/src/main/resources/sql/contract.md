all
===
    select contract.#use("cols")# from contract where contract.deleted = 0

sample
===
	select #use("cols")# from contract contract where  #use("condition")# #use("orderBy")#

cols
===
	contract.id,contract.name,contract.address,contract.description,contract.img_url,contract.owner,contract.collection_metadata,contract.base_url,contract.amount,contract.deleted,contract.weight,contract.json,contract.create_time,contract.update_time,contract.link_url,contract.recommend_start_time,contract.recommend_stop_time,contract.recommend_disabled,contract.contract_status
updateSample
===
	contract.id=#id#,contract.name=#name#,contract.address=#address#,contract.description=#description#,contract.img_url=#imgUrl#,contract.owner=#owner#,contract.collection_metadata=#collectionMetadata#,contract.base_url=#baseUrl#,contract.amount=#amount#,contract.deleted=#deleted#,contract.weight=#weight#,contract.json=#json#,contract.create_time=#createTime#,contract.update_time=#updateTime#,contract.link_url=#linkUrl#,contract.recommend_start_time=#recommendStartTime#,contract.recommend_stop_time=#recommendStopTime#,contract.recommend_disabled=#recommendDisabled#,contract.contract_status=#contractStatus#
forceDeleteByIds
===
    delete from contract where contract.id in (#join(ids)#)

deleteByIds
===
    update contract set contract.deleted = 1 where contract.id in (#join(ids)#)

condition
===
    contract.deleted = 0
	@if(!isEmpty(id)){
	 and contract.id=#id#
	@}
	@if(!isEmpty(name)){
	 and contract.name=#name#
	@}
	@if(!isEmpty(address)){
	 and contract.address=#address#
	@}
	@if(!isEmpty(description)){
	 and contract.description=#description#
	@}
	@if(!isEmpty(imgUrl)){
	 and contract.img_url=#imgUrl#
	@}
	@if(!isEmpty(owner)){
	 and contract.owner=#owner#
	@}
	@if(!isEmpty(collectionMetadata)){
	 and contract.collection_metadata=#collectionMetadata#
	@}
	@if(!isEmpty(baseUrl)){
	 and contract.base_url=#baseUrl#
	@}
	@if(!isEmpty(amount)){
	 and contract.amount=#amount#
	@}
	@if(!isEmpty(deleted)){
	 and contract.deleted=#deleted#
	@}
	@if(!isEmpty(weight)){
	 and contract.weight=#weight#
	@}
	@if(!isEmpty(json)){
	 and contract.json=#json#
	@}
	@if(!isEmpty(createTime)){
	 and contract.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and contract.update_time=#updateTime#
	@}
	@if(!isEmpty(linkUrl)){
	 and contract.link_url=#linkUrl#
	@}
	@if(!isEmpty(recommendStartTime)){
	 and contract.recommend_start_time=#recommendStartTime#
	@}
	@if(!isEmpty(recommendStopTime)){
	 and contract.recommend_stop_time=#recommendStopTime#
	@}
	@if(!isEmpty(recommendDisabled)){
	 and contract.recommend_disabled=#recommendDisabled#
	@}
	@if(!isEmpty(contractStatus)){
	 and contract.contract_status=#contractStatus#
	@}

orderBy
===
	order by contract.create_time desc

groupById
===
    group by contract.id


collection
===

    select @pageTag(){
       #use("cols")#, t.listAmount,creatorUser.name as creatorName,creatorUser.profile_photo as creatorPhoto,t.create_time as ctime
    @}
    from (
        select at.contract_address,count(at.nft_id) as listAmount,at.create_time
        from (
            select asset.contract_address,asset.nft_id,asset.create_time
            from asset
            where  asset.deleted = 0
            @if(!isEmpty(contractAddress)){
             and asset.contract_address=#contractAddress#
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
            @if(!isEmpty(nftData)){
             and asset.nft_data=#nftData#
            @}
            @if(!isEmpty(nftId)){
             and asset.nft_id=#nftId#
            @}

            @if(!isEmpty(saleStatus)){
             and asset.sale_status=#saleStatus#
            @}
            @if(!isEmpty(level)){
             and asset.level=#level#
            @}
            @if(!isEmpty(denomType)){
             and asset.denom_type=#denomType#
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

            group by asset.nft_id

            
        ) at
        group by at.contract_address

    ) t
    left join contract on contract.address=t.contract_address
    left join user creatorUser on creatorUser.address=contract.owner 
    where 1=1    
    @if(!isEmpty(search)){
        and contract.name like #'%'+search+'%'#
    @}
    @if(sort==1){
        order by t.create_time desc
    @}
    @if(sort==2){
        order by rand()
    @}

collectionInfo
===
    select 
    #use("cols")# ,creatorUser.name as creatorName,creatorUser.profile_photo as creatorPhoto,asset.listAmount
    from contract  
    left join user creatorUser on creatorUser.address=contract.owner 
    left join (
            select ast.contract_address,count(ast.nft_id) as listAmount
            FROM(
                select asset.contract_address ,asset.nft_id
                from asset
                where asset.contract_address=#address# and level=1 and sale_status=1
                group by asset.nft_id
            ) ast
    ) asset on asset.contract_address=contract.address
    where  #use("condition")# #use("orderBy")# 


contractPage
===

    select @pageTag(){
        t.*,creatorUser.name as creatorName,creatorUser.profile_photo as creatorPhoto
    @}
    from (
        select #use("cols")#
        from contract
        where  #use("condition")#
        #use("orderBy")#
    ) t
    left join user creatorUser on creatorUser.address=t.owner
    order by t.create_time desc


creators
===
    select count(t.owner)
    from (
    select owner
    from contract
    group by contract.owner
    ) t
    limit 0,1

getMyContractPage
===

    select @pageTag(){
        t.*
    @}
    from (
        select 
            ast.*,
            user.profile_photo as creatorPhoto,user.name as creatorName
        from(
            select
                #use("cols")#,asset.channel,asset.creator
            from 
                asset
                inner join contract on asset.contract_address = contract.address
            where  
                contract.deleted = 0
                and contract.name is not null
                @if(!isEmpty(owner)){
                    and asset.owner=#owner#
                @}
                @if(!isEmpty(search)){
                    and contract.name like #'%'+search+'%'#
                @}
            group by contract.address
            union
            select
                #use("cols")#,asset.channel,asset.creator
            from
                contract
            left join asset on asset.contract_address = contract.address
            where  
                contract.deleted = 0
                and contract.name is not null
                @if(!isEmpty(owner)){
                    and contract.owner=#owner#
                @}
                @if(!isEmpty(search)){
                    and contract.name like #'%'+search+'%'#
                @}
            group by contract.address
        ) ast
        LEFT JOIN `user` ON `user`.address = ast.owner
        where
            ast.deleted = 0
            @if(!isEmpty(filter) &&  filter == 1){
                and ast.owner = #owner#
            @}
            @if(!isEmpty(filter) &&  filter == 3){
                and ((ast.channel is null and ast.id is not null) or ast.channel != 'Uptick-Loopring')
            @}
        group by ast.address
        order by ast.create_time desc
    ) t


getOtherCollectionInfo
===
    select @pageTag(){
        t.*
    @}
    from (
        
        @if(isEmpty(filter) || filter != 1){
            SELECT
                #use("cols")#,asset.level,asset.sale_status
            FROM
                (SELECT * FROM asset WHERE asset.deleted = 0 and asset.owner = #owner#)asset
                
            LEFT JOIN
                contract on asset.contract_address = contract.address
            where
                contract.deleted =0
                and contract.name is not null
                @if(isEmpty(filter)){
                    and asset.sale_status = 1 and asset.owner = #owner#
                @}
                @if(!isEmpty(filter) &&  filter == 2){
                    and asset.sale_status = 0 and asset.owner = #owner#
                @}
                group by contract.address
                order by contract.create_time desc
        @}
        @if(!isEmpty(filter) && filter == 1){
            SELECT
                #use("cols")#,asset.level,asset.sale_status
            FROM
                (SELECT * FROM contract WHERE contract.deleted = 0 and contract.owner = #owner# )contract
            inner JOIN
                asset on asset.contract_address = contract.address
            where
                contract.deleted =0
                and contract.name is not null
                group by contract.address
                order by contract.create_time desc
        @}
    ) t

getCollectionInfo
===
    select @pageTag(){
        t.*
    @}
    from (
        
    
        SELECT
            #use("cols")#,asset.level,asset.sale_status
        FROM
            contract
        inner JOIN
            (SELECT * FROM asset WHERE asset.deleted = 0 and asset.sale_status = 1 )asset
            on asset.contract_address = contract.address
        where
            contract.deleted =0
            and contract.name is not null
            @if(!isEmpty(address)){
                and contract.address = #address#
            @}
            @if(!isEmpty(search)){
                and contract.name like #'%'+search+'%'#
            @}
            group by contract.address
            @if(!isEmpty(sortType) && sortType == 2){
            order by contract.create_time desc
            @}
            @if(!isEmpty(sortType) && sortType == 9){
            order by RAND()
            @}
        
    ) t