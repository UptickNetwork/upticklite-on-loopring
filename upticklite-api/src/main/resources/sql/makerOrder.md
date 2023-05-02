
all
===
    select maker_order.#use("cols")# from maker_order where maker_order.deleted = 0

sample
===
	select #use("cols")# from maker_order maker_order where  #use("condition")# #use("orderBy")#

cols
===
	maker_order.id,maker_order.valid_until,maker_order.maker_json,maker_order.signature,maker_order.contract_address,maker_order.nft_id,maker_order.signer,maker_order.sale_limit,maker_order.create_time,maker_order.update_time,maker_order.deleted,maker_order.json,maker_order.weight
updateSample
===
	maker_order.id=#id#,maker_order.valid_until=#validUntil#,maker_order.maker_json=#makerJson#,maker_order.signature=#signature#,maker_order.contract_address=#contractAddress#,maker_order.nft_id=#nftId#,maker_order.signer=#signer#,maker_order.sale_limit=#saleLimit#,maker_order.create_time=#createTime#,maker_order.update_time=#updateTime#,maker_order.deleted=#deleted#,maker_order.json=#json#,maker_order.weight=#weight#
forceDeleteByIds
===
    delete from maker_order where maker_order.id in (#join(ids)#)

deleteByIds
===
    update maker_order set maker_order.deleted = 1 where maker_order.id in (#join(ids)#)

condition
===
    maker_order.deleted = 0
	@if(!isEmpty(id)){
	 and maker_order.id=#id#
	@}
	@if(!isEmpty(validUntil)){
	 and maker_order.valid_until=#validUntil#
	@}
	@if(!isEmpty(makerJson)){
	 and maker_order.maker_json=#makerJson#
	@}
	@if(!isEmpty(signature)){
	 and maker_order.signature=#signature#
	@}
	@if(!isEmpty(contractAddress)){
	 and maker_order.contract_address=#contractAddress#
	@}
	@if(!isEmpty(nftId)){
	 and maker_order.nft_id=#nftId#
	@}
	@if(!isEmpty(signer)){
	 and maker_order.signer=#signer#
	@}
	@if(!isEmpty(saleLimit)){
	 and maker_order.sale_limit=#saleLimit#
	@}
	@if(!isEmpty(createTime)){
	 and maker_order.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and maker_order.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and maker_order.deleted=#deleted#
	@}
	@if(!isEmpty(json)){
	 and maker_order.json=#json#
	@}
	@if(!isEmpty(weight)){
	 and maker_order.weight=#weight#
	@}

orderBy
===
	order by maker_order.create_time desc

groupById
===
    group by maker_order.id

getDealMarkerOrderList
===
    select #use("cols")# from maker_order where  #use("condition")# #use("orderBy")#