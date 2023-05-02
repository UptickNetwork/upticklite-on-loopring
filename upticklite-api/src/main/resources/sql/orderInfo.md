
all
===
    select order_info.#use("cols")# from order_info where order_info.deleted = 0

sample
===
	select #use("cols")# from order_info order_info where  #use("condition")# #use("orderBy")#

cols
===
	order_info.id,order_info.order_no,order_info.description,order_info.nft_name,order_info.nft_img,order_info.order_price,order_info.token_type,order_info.quantity,order_info.level,order_info.order_status,order_info.sender,order_info.recipient,order_info.contract_address,order_info.tx_id,order_info.create_time,order_info.update_time,order_info.deleted,order_info.weight,order_info.json,order_info.nft_id
updateSample
===
	order_info.id=#id#,order_info.order_no=#orderNo#,order_info.description=#description#,order_info.nft_name=#nftName#,order_info.nft_img=#nftImg#,order_info.order_price=#orderPrice#,order_info.token_type=#tokenType#,order_info.quantity=#quantity#,order_info.level=#level#,order_info.order_status=#orderStatus#,order_info.sender=#sender#,order_info.recipient=#recipient#,order_info.contract_address=#contractAddress#,order_info.tx_id=#txId#,order_info.create_time=#createTime#,order_info.update_time=#updateTime#,order_info.deleted=#deleted#,order_info.weight=#weight#,order_info.json=#json#,order_info.nft_id=#nftId#
forceDeleteByIds
===
    delete from order_info where order_info.id in (#join(ids)#)

deleteByIds
===
    update order_info set order_info.deleted = 1 where order_info.id in (#join(ids)#)

condition
===
    order_info.deleted = 0
	@if(!isEmpty(id)){
	 and order_info.id=#id#
	@}
	@if(!isEmpty(orderNo)){
	 and order_info.order_no=#orderNo#
	@}
	@if(!isEmpty(description)){
	 and order_info.description=#description#
	@}
	@if(!isEmpty(nftName)){
	 and order_info.nft_name like #'%'+nftName+'%'#
	@}
	@if(!isEmpty(nftImg)){
	 and order_info.nft_img=#nftImg#
	@}
	@if(!isEmpty(orderPrice)){
	 and order_info.order_price=#orderPrice#
	@}
	@if(!isEmpty(tokenType)){
	 and order_info.token_type=#tokenType#
	@}
	@if(!isEmpty(quantity)){
	 and order_info.quantity=#quantity#
	@}
	@if(!isEmpty(level)){
	 and order_info.level=#level#
	@}
	@if(!isEmpty(orderStatus)){
	 and order_info.order_status=#orderStatus#
	@}
	@if(!isEmpty(sender)){
	 and order_info.sender=#sender#
	@}
	@if(!isEmpty(recipient)){
	 and order_info.recipient=#recipient#
	@}
	@if(!isEmpty(contractAddress)){
	 and order_info.contract_address=#contractAddress#
	@}
	@if(!isEmpty(txId)){
	 and order_info.tx_id=#txId#
	@}
	@if(!isEmpty(createTime)){
	 and order_info.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and order_info.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and order_info.deleted=#deleted#
	@}
	@if(!isEmpty(weight)){
	 and order_info.weight=#weight#
	@}
	@if(!isEmpty(json)){
	 and order_info.json=#json#
	@}
	@if(!isEmpty(nftId)){
	 and order_info.nft_id=#nftId#
	@}

orderBy
===
	order by order_info.create_time desc

groupById
===
    group by order_info.id




getOrderPage
===
    select @pageTag(){
        t.*,sender.name as senderName,sender.profile_photo as senderPhoto,recipient.name as recipientName,recipient.profile_photo as recipientPhoto
    @}
    from (
        select #use("cols")#
        from order_info
        where  #use("condition")#
        order by order_info.create_time desc
    ) t
    left join user sender on sender.address=t.sender
    left join user recipient on recipient.address=t.recipient
    order by t.create_time desc



getOrderData
===
    select count(id) as orderNumber,sum(quantity) as nftNumber,token_type as tokenType,sum(order_price) as amount
    from order_info
    where order_info.deleted = 0
	@if(!isEmpty(level)){
	 and order_info.level=#level#
	@}
	@if(!isEmpty(orderStatus)){
	 and order_info.order_status=#orderStatus#
	@}
    group by token_type