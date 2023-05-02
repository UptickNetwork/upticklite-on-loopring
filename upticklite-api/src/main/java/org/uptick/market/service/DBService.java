package org.uptick.market.service;

import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/11/9 19:49
 */
public interface DBService  {

    <T>void save(T entity);

    <T>void saveAll(List<T> list,Class<T> tClass);

    <T> List<T> find(Map<String, Object> map, Class<T> tClass);

    <T> T findOne(Map<String, Object> map, Class<T> tClass);

    <T> void update(String id, Map<String, Object> map, Class<T> tClass);

    <T>void delete(String id, Class<T> tClass);

    <T> List<T> findByPage(Integer offset, Integer limit, Map<String, Object> map, List<String> properties, Class<T> tClass);

    <T> List<T> customFind(Query query, Class<T> tClass);
}
