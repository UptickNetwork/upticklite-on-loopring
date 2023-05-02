
all
===
    select taker_order.#use("cols")# from taker_order where taker_order.deleted = 0

sample
===
	select #use("cols")# from taker_order taker_order where  #use("condition")# #use("orderBy")#

cols
===
	taker_order.id,taker_order.maker_order_id,taker_order.valid_until,taker_order.maker_json,taker_order.signature,taker_order.contract_address,taker_order.nft_id,taker_order.signer,taker_order.create_time,taker_order.update_time,taker_order.deleted,taker_order.json,taker_order.weight
updateSample
===
	taker_order.id=#id#,taker_order.maker_order_id=#makerOrderId#,taker_order.valid_until=#validUntil#,taker_order.maker_json=#makerJson#,taker_order.signature=#signature#,taker_order.contract_address=#contractAddress#,taker_order.nft_id=#nftId#,taker_order.signer=#signer#,taker_order.create_time=#createTime#,taker_order.update_time=#updateTime#,taker_order.deleted=#deleted#,taker_order.json=#json#,taker_order.weight=#weight#
forceDeleteByIds
===
    delete from taker_order where taker_order.id in (#join(ids)#)

deleteByIds
===
    update taker_order set taker_order.deleted = 1 where taker_order.id in (#join(ids)#)

condition
===
    taker_order.deleted = 0
	@if(!isEmpty(id)){
	 and taker_order.id=#id#
	@}
	@if(!isEmpty(makerOrderId)){
	 and taker_order.maker_order_id=#makerOrderId#
	@}
	@if(!isEmpty(validUntil)){
	 and taker_order.valid_until=#validUntil#
	@}
	@if(!isEmpty(makerJson)){
	 and taker_order.maker_json=#makerJson#
	@}
	@if(!isEmpty(signature)){
	 and taker_order.signature=#signature#
	@}
	@if(!isEmpty(contractAddress)){
	 and taker_order.contract_address=#contractAddress#
	@}
	@if(!isEmpty(nftId)){
	 and taker_order.nft_id=#nftId#
	@}
	@if(!isEmpty(signer)){
	 and taker_order.signer=#signer#
	@}
	@if(!isEmpty(createTime)){
	 and taker_order.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and taker_order.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and taker_order.deleted=#deleted#
	@}
	@if(!isEmpty(json)){
	 and taker_order.json=#json#
	@}
	@if(!isEmpty(weight)){
	 and taker_order.weight=#weight#
	@}

orderBy
===
	order by taker_order.create_time desc

groupById
===
    group by taker_order.id
