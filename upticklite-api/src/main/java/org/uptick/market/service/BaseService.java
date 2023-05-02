// +---------------------------------------------------------------------------- 
// | UptickLite is the community version of Uptick NFT marketpalce, including    
// | three major components - service, api and web, powered by Uptick Network    
// | and Loopring. It is available to the Uptick and Loopring dev community      
// | through the open source license.                                            
// | Uptick Network is a busienss grade NFT infrastructure for NFT applications  
// | with multichian and interchain support. Loopring is a zkRollup layer2 on top
// | of Ethereum.                                                                
// +---------------------------------------------------------------------------- 
// | Copyright (c) 2022~2099 Uptick Network (https://uptick.network/)            
// | All rights reserved.                                                        
// +---------------------------------------------------------------------------- 
// | Licensed ( https://www.gnu.org/licenses/gpl-3.0.en.html )                   
// +---------------------------------------------------------------------------- 
// | Author: Uptick Network development team tech@uptickproject.com              
// +---------------------------------------------------------------------------- 
package org.uptick.market.service;


import org.uptick.market.entity.CrudEntity;
import org.beetl.sql.core.engine.PageQuery;

import java.util.List;

public interface BaseService<T extends CrudEntity> {
    T save(T t);

    void saveTemplate(T t);

    void saveBatch(List<T> t);


    /**
     * The object is updated according to the primary key, so the properties are all involved in the update. You can also use the primary key ColumnIgnore to control that this field is ignored during updates
     *
     * @param entity
     * @return
     */
    int updateById(T entity);

    /**
     * The object is updated according to the primary key, and only properties that are not null participate in the update
     *
     * @param entity
     * @return
     */
    int updateTemplateById(T entity);

    /**
     * Delete an object based on its primary key. If the object is a composite primary key, pass in the object's native key
     *
     * @param entity
     * @return
     */
    int deleteById(T entity);

    int deleteById(Long id);

    int deleteByIds(List<Long> list);

    int forceDeleteById(T entity);

    /**
     * Gets the object based on the primary key and throws a Runtime exception if the object does not exist
     *
     * @param id
     * @return
     */
    T unique(Long id);


    int forceDeleteById(Long id);

    int forceDeleteByIds(List<Long> list);

    /**
     * Returns all database records corresponding to the entity
     *
     * @return
     */
    List<T> all();


    /**
     * Template query returns all results that match the template. beetlsql will take out the non-null values (excluding the date type) and find the exact matching result set from the database
     *
     * @param entity
     * @return
     */
    List<T> template(T entity);


    /**
     * Template query, returns one result, or null if none
     *
     * @param entity
     * @return
     */
    T templateOne(T entity);

    List<T> template(T entity, int start, int size);

    void templatePage(PageQuery<T> query);

    /**
     * The number of matches the template
     *
     * @param entity
     * @return
     */
    long templateCount(T entity);


    List<T> batchUpdate(List<T> list);

    T single(Long id);

    List<T> sample(T t);
}
