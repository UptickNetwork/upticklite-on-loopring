
all
===
    select order_details.#use("cols")# from order_details where order_details.deleted = 0

sample
===
	select #use("cols")# from order_details order_details where  #use("condition")# #use("orderBy")#

cols
===
	order_details.id,order_details.order_no,order_details.level,order_details.pay_price,order_details.token_type,order_details.order_status,order_details.sender,order_details.recipient,order_details.tx_id,order_details.contract_address,order_details.asset_id,order_details.nft_id,order_details.create_time,order_details.update_time,order_details.deleted,order_details.weight,order_details.json
updateSample
===
	order_details.id=#id#,order_details.order_no=#orderNo#,order_details.level=#level#,order_details.pay_price=#payPrice#,order_details.token_type=#tokenType#,order_details.order_status=#orderStatus#,order_details.sender=#sender#,order_details.recipient=#recipient#,order_details.tx_id=#txId#,order_details.contract_address=#contractAddress#,order_details.asset_id=#assetId#,order_details.nft_id=#nftId#,order_details.create_time=#createTime#,order_details.update_time=#updateTime#,order_details.deleted=#deleted#,order_details.weight=#weight#,order_details.json=#json#
forceDeleteByIds
===
    delete from order_details where order_details.id in (#join(ids)#)

deleteByIds
===
    update order_details set order_details.deleted = 1 where order_details.id in (#join(ids)#)

condition
===
    order_details.deleted = 0
	@if(!isEmpty(id)){
	 and order_details.id=#id#
	@}
	@if(!isEmpty(orderNo)){
	 and order_details.order_no=#orderNo#
	@}
	@if(!isEmpty(level)){
	 and order_details.level=#level#
	@}
	@if(!isEmpty(payPrice)){
	 and order_details.pay_price=#payPrice#
	@}
	@if(!isEmpty(tokenType)){
	 and order_details.token_type=#tokenType#
	@}
	@if(!isEmpty(orderStatus)){
	 and order_details.order_status=#orderStatus#
	@}
	@if(!isEmpty(sender)){
	 and order_details.sender=#sender#
	@}
	@if(!isEmpty(recipient)){
	 and order_details.recipient=#recipient#
	@}
	@if(!isEmpty(txId)){
	 and order_details.tx_id=#txId#
	@}
	@if(!isEmpty(contractAddress)){
	 and order_details.contract_address=#contractAddress#
	@}
	@if(!isEmpty(assetId)){
	 and order_details.asset_id=#assetId#
	@}
	@if(!isEmpty(nftId)){
	 and order_details.nft_id=#nftId#
	@}
	@if(!isEmpty(createTime)){
	 and order_details.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and order_details.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and order_details.deleted=#deleted#
	@}
	@if(!isEmpty(weight)){
	 and order_details.weight=#weight#
	@}
	@if(!isEmpty(json)){
	 and order_details.json=#json#
	@}

orderBy
===
	order by order_details.create_time desc

groupById
===
    group by order_details.id
