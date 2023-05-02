
all
===
    select user.#use("cols")# from user where user.deleted = 0

sample
===
	select #use("cols")# from user user where  #use("condition")# #use("orderBy")#

cols
===
	user.id,user.create_time,user.update_time,user.deleted,user.json,user.email,user.name,user.description,user.profile_photo,user.background,user.address,user.account_id,user.account_status
updateSample
===
	user.id=#id#,user.create_time=#createTime#,user.update_time=#updateTime#,user.deleted=#deleted#,user.json=#json#,user.email=#email#,user.name=#name#,user.description=#description#,user.profile_photo=#profilePhoto#,user.background=#background#,user.address=#address#,user.account_id=#accountId#,user.account_status=#accountStatus#
forceDeleteByIds
===
    delete from user where user.id in (#join(ids)#)

deleteByIds
===
    update user set user.deleted = 1 where user.id in (#join(ids)#)

condition
===
    user.deleted = 0
	@if(!isEmpty(id)){
	 and user.id=#id#
	@}
	@if(!isEmpty(createTime)){
	 and user.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and user.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and user.deleted=#deleted#
	@}
	@if(!isEmpty(json)){
	 and user.json=#json#
	@}
	@if(!isEmpty(email)){
	 and user.email=#email#
	@}
	@if(!isEmpty(name)){
	 and user.name like #'%'+name+'%'#
	@}
	@if(!isEmpty(description)){
	 and user.description=#description#
	@}
	@if(!isEmpty(profilePhoto)){
	 and user.profile_photo=#profilePhoto#
	@}
	@if(!isEmpty(background)){
	 and user.background=#background#
	@}
	@if(!isEmpty(address)){
	 and user.address=#address#
	@}
	@if(!isEmpty(accountId)){
	 and user.account_id=#accountId#
	@}
	@if(!isEmpty(accountStatus)){
	 and user.account_status=#accountStatus#
	@}

orderBy
===
	order by user.create_time desc

groupById
===
    group by user.id

templatePage
===
    select @pageTag(){
        t.*
    @}
    from (
        select #use("cols")#
        from user
        where  #use("condition")#
        order by user.create_time desc
    ) t
