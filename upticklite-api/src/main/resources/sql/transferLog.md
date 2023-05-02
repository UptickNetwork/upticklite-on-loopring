
all
===
    select transfer_log.#use("cols")# from transfer_log where transfer_log.deleted = 0

sample
===
	select #use("cols")# from transfer_log transfer_log where  #use("condition")# #use("orderBy")#

cols
===
	transfer_log.id,transfer_log.contract_address,transfer_log.nft_id,transfer_log.asset_id,transfer_log.tx_id,transfer_log.from,transfer_log.to,transfer_log.amount,transfer_log.tx_type,transfer_log.serial_no,transfer_log.create_time,transfer_log.update_time,transfer_log.deleted,transfer_log.weight,transfer_log.json
updateSample
===
	transfer_log.id=#id#,transfer_log.contract_address=#contractAddress#,transfer_log.nft_id=#nftId#,transfer_log.asset_id=#assetId#,transfer_log.tx_id=#txId#,transfer_log.from=#from#,transfer_log.to=#to#,transfer_log.amount=#amount#,transfer_log.tx_type=#txType#,transfer_log.serial_no=#serialNo#,transfer_log.create_time=#createTime#,transfer_log.update_time=#updateTime#,transfer_log.deleted=#deleted#,transfer_log.weight=#weight#,transfer_log.json=#json#
forceDeleteByIds
===
    delete from transfer_log where transfer_log.id in (#join(ids)#)

deleteByIds
===
    update transfer_log set transfer_log.deleted = 1 where transfer_log.id in (#join(ids)#)

condition
===
    transfer_log.deleted = 0
	@if(!isEmpty(id)){
	 and transfer_log.id=#id#
	@}
	@if(!isEmpty(contractAddress)){
	 and transfer_log.contract_address=#contractAddress#
	@}
	@if(!isEmpty(nftId)){
	 and transfer_log.nft_id=#nftId#
	@}
	@if(!isEmpty(assetId)){
	 and transfer_log.asset_id=#assetId#
	@}
	@if(!isEmpty(txId)){
	 and transfer_log.tx_id=#txId#
	@}
	@if(!isEmpty(from)){
	 and transfer_log.from=#from#
	@}
	@if(!isEmpty(to)){
	 and transfer_log.to=#to#
	@}
	@if(!isEmpty(amount)){
	 and transfer_log.amount=#amount#
	@}
	@if(!isEmpty(txType)){
	 and transfer_log.tx_type=#txType#
	@}
	@if(!isEmpty(serialNo)){
	 and transfer_log.serial_no=#serialNo#
	@}
	@if(!isEmpty(createTime)){
	 and transfer_log.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and transfer_log.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and transfer_log.deleted=#deleted#
	@}
	@if(!isEmpty(weight)){
	 and transfer_log.weight=#weight#
	@}
	@if(!isEmpty(json)){
	 and transfer_log.json=#json#
	@}

orderBy
===
	order by transfer_log.create_time desc

groupById
===
    group by transfer_log.id

nftHistoryPage
===
    select @pageTag(){
        t.*,senderUser.name as senderName,senderUser.profile_photo as senderPhoto,recipientUser.name as recipientName,recipientUser.profile_photo as recipientPhoto
    @}
    from (
        select #use("cols")# 
        from transfer_log
        where  #use("condition")# 
        group by transfer_log.serial_no
        #use("orderBy")#
    ) t
    left join user senderUser on senderUser.address=t.from
    left join user recipientUser on recipientUser.address=t.to
    order by t.create_time desc

nftHistory
===
    select
        t.*,senderUser.name as senderName,senderUser.profile_photo as senderPhoto,recipientUser.name as recipientName,recipientUser.profile_photo as recipientPhoto
    from (
        select #use("cols")# 
        from transfer_log
        where  #use("condition")# 
        and transfer_log.tx_type != 4
        group by transfer_log.serial_no
        #use("orderBy")#
    ) t
    left join user senderUser on senderUser.address=t.from
    left join user recipientUser on recipientUser.address=t.to
    order by t.create_time desc