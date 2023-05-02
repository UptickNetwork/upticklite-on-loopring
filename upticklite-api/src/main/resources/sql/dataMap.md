
all
===
    select data_map.#use("cols")# from data_map where data_map.deleted = 0

sample
===
	select #use("cols")# from data_map data_map where  #use("condition")# #use("orderBy")#

cols
===
	data_map.id,data_map.name,data_map.value,data_map.json,data_map.description,data_map.create_time,data_map.update_time,data_map.deleted,data_map.weight
updateSample
===
	data_map.id=#id#,data_map.name=#name#,data_map.value=#value#,data_map.json=#json#,data_map.description=#description#,data_map.create_time=#createTime#,data_map.update_time=#updateTime#,data_map.deleted=#deleted#,data_map.weight=#weight#
forceDeleteByIds
===
    delete from data_map where data_map.id in (#join(ids)#)

deleteByIds
===
    update data_map set data_map.deleted = 1 where data_map.id in (#join(ids)#)

condition
===
    data_map.deleted = 0
	@if(!isEmpty(id)){
	 and data_map.id=#id#
	@}
	@if(!isEmpty(name)){
	 and data_map.name=#name#
	@}
	@if(!isEmpty(value)){
	 and data_map.value=#value#
	@}
	@if(!isEmpty(json)){
	 and data_map.json=#json#
	@}
	@if(!isEmpty(description)){
	 and data_map.description=#description#
	@}
	@if(!isEmpty(createTime)){
	 and data_map.create_time=#createTime#
	@}
	@if(!isEmpty(updateTime)){
	 and data_map.update_time=#updateTime#
	@}
	@if(!isEmpty(deleted)){
	 and data_map.deleted=#deleted#
	@}
	@if(!isEmpty(weight)){
	 and data_map.weight=#weight#
	@}

orderBy
===
	order by data_map.create_time desc

groupById
===
    group by data_map.id
