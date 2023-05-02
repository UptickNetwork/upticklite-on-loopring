package org.uptick.market.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.uptick.market.service.DBService;

import java.util.List;
import java.util.Map;

@Service
public class DBServiceImpl implements DBService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存
     *
     * @param entity
     */
    @Override
    public <T> void save(T entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public <T> void saveAll(List<T> list,Class<T> tClass) {
        mongoTemplate.insert(list,tClass);
    }


    /**
     * 查询对象
     *
     * @param map
     * @param tClass
     * @return
     */
    @Override
    public <T> List<T> find(Map<String, Object> map, Class<T> tClass) {
        Query query = new Query();
        if (map!=null){
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
            }
        }
        List<T> user = mongoTemplate.find(query, tClass);
        return user;
    }

    @Override
    public <T> T findOne(Map<String, Object> map, Class<T> tClass) {
        List<T> list= findByPage( 0,  1,  map,  null ,  tClass);
        if (list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 更新对象
     *
     * @param map
     * @param tClass
     */
    @Override
    public <T> void update(String id, Map<String, Object> map, Class<T> tClass) {

        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            update.set(entry.getKey(), entry.getValue());

        }
        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query, update, tClass);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,tClass);
    }

    /**
     * 删除对象
     *
     * @param id
     * @param tClass
     */
    @Override
    public <T> void delete(String id, Class<T> tClass) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, tClass);
    }


    /**
     * 分页查询（）
     * @param offset 启始页码
     * @param limit 页码限制
     * @param map 查询条件
     * @param properties  排序字段
     * @param tClass
     * @return
     */
    public <T> List<T> findByPage(Integer offset, Integer limit, Map<String, Object> map,  List<String> properties, Class<T> tClass) {
        Query query = new Query();
       if (map!=null){
           //条件
           for (Map.Entry<String, Object> entry : map.entrySet()) {
               query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
           }
       }

       if (properties!=null && properties.size()>0){
           //排序
           Sort sort = new Sort(Sort.Direction.ASC, properties);//多条件DEVID、time
           query.with(sort);
       }

        //分页
        query.skip(offset).limit(limit);

        return mongoTemplate.find(query, tClass);
    }


    /**
     * 自定义查询
     *
     * @param query
     * @param tClass
     * @return
     */
    @Override
    public <T> List<T> customFind(Query query, Class<T> tClass) {
        return mongoTemplate.find(query, tClass);
    }

}
