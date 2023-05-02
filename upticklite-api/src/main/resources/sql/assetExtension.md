
all
===
    select asset_extension.#use("cols")# from asset_extension where asset_extension.deleted = 0

sample
===
	select #use("cols")# from asset_extension asset_extension where  #use("condition")# #use("orderBy")#

cols
===
	asset_extension.id,asset_extension.nft_id,asset_extension.create_owner,asset_extension.like_count,asset_extension.view_count,asset_extension.issuer_price,asset_extension.eth_total_price,asset_extension.lrc_total_price,asset_extension.usdt_total_price,asset_extension.dai_total_price,asset_extension.usdc_total_price,asset_extension.create_time,asset_extension.update_time,asset_extension.deleted,asset_extension.weight,asset_extension.json,asset_extension.total_inventory,asset_extension.issuer_token_type
updateSample
===
	asset_extension.id=#id#,asset_extension.nft_id=#nftId#,asset_extension.create_owner=#createOwner#,asset_extension.like_count=#likeCount#,asset_extension.view_count=#viewCount#,asset_extension.issuer_price=#issuerPrice#,asset_extension.eth_total_price=#ethTotalPrice#,asset_extension.lrc_total_price=#lrcTotalPrice#,asset_extension.usdt_total_price=#usdtTotalPrice#,asset_extension.dai_total_price=#daiTotalPrice#,asset_extension.usdc_total_price=#usdcTotalPrice#,asset_extension.create_time=#createTime#,asset_extension.update_time=#updateTime#,asset_extension.deleted=#deleted#,asset_extension.weight=#weight#,asset_extension.json=#json#,asset_extension.total_inventory=#totalInventory#,asset_extension.issuer_token_type=#issuerTokenType#
forceDeleteByIds
===
    delete from asset_extension where asset_extension.id in (#join(ids)#)

deleteByIds
===
    update asset_extension set asset_extension.deleted = 1 where asset_extension.id in (#join(ids)#)

condition
===
    asset_extension.deleted = 0
	@if(!isEmpty(id)){
	 and asset_extension.id=#id#
	@}
	@if(!isEmpty(nftId)){
	 and asset_extension.nft_id=#nftId#
	@}
	@if(!isEmpty(createOwner)){
	 and asset_extension.create_owner=#createOwner#
	@}
	@if(!isEmpty(likeCount)){
	 and asset_extension.like_count=#likeCount#
	@}
	@if(!isEmpty(viewCount)){
	 and asset_extension.view_count=#viewCount#
	@}
	@if(!isEmpty(issuerPrice)){
	 and asset_extension.issuer_price=#issuerPrice#
	@}
	@if(!isEmpty(ethTotalPrice)){
	 and asset_extension.eth_total_price=#ethTotalPrice#
	@}
	@if(!isEmpty(lrcTotalPrice)){
	 and asset_extension.lrc_total_price=#lrcTotalPrice#
	@}
	@if(!isEmpty(usdtTotalPrice)){
	 and asset_extension.usdt_total_price=#usdtTotalPrice#
	@}
	@if(!isEmpty(daiTotalPrice)){
	 and asset_extension.dai_total_price=#daiTotalPrice#
	@}
	@if(!isEmpty(usdcTotalPrice)){
	 and asset_extension.usdc_total_price=#usdcTotalPrice#
	@}
	@if(!isEmpty(createTime)){
	 and asset_extension.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and asset_extension.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and asset_extension.deleted=#deleted#
	@}
	@if(!isEmpty(weight)){
	 and asset_extension.weight=#weight#
	@}
	@if(!isEmpty(json)){
	 and asset_extension.json=#json#
	@}
	@if(!isEmpty(totalInventory)){
	 and asset_extension.total_inventory=#totalInventory#
	@}
	@if(!isEmpty(issuerTokenType)){
	 and asset_extension.issuer_token_type=#issuerTokenType#
	@}

orderBy
===
	order by asset_extension.create_time desc

groupById
===
    group by asset_extension.id



getAssetExtensionInfoByNftIds
===
    select 
        #use("cols")# 
    from
        asset_extension
    where 
        asset_extension.deleted = 0
        @if(!isEmpty(nftIds)){
            and asset_extension.nft_id in (#join(nftIds)#)
        @}
        #use("orderBy")#