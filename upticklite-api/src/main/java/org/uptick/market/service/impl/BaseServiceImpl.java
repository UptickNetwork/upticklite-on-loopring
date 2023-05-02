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
package org.uptick.market.service.impl;


import org.uptick.market.dao.BaseDao;
import org.uptick.market.entity.CrudEntity;
import org.uptick.market.service.BaseService;
import org.uptick.market.util.BeanUtils;
import org.uptick.market.util.GenericsUtils;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.annotatoin.Table;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class BaseServiceImpl<T extends CrudEntity, D extends BaseDao<T>> implements BaseService<T> {
    private String tableName;
    private T entity;
    private D entityDao;
    @Autowired
    private ApplicationContext applicationContext;

    private T getEntity() {
        if (entity != null) {
            return entity;
        }
        Class<T> entityType = GenericsUtils.getSuperClassGenricType(getClass(), 0);
        try {
            entity = entityType.newInstance();
            Table annotation = entity.getClass().getAnnotation(Table.class);
            tableName = annotation.name();
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("error: {}", e.getMessage(), e);
            return null;
        }
    }


    private String getDefinedEntityTableName() {
        if (!StringUtils.isEmpty(tableName)) {
            return tableName;
        }
        T entity = getEntity();
        Class<?> aClass = entity.getClass();
        String name = aClass.getAnnotation(Table.class).name();
        return StringUtils.isEmpty(name) ? aClass.getSimpleName() : name;
    }


    protected D getEntityDao() {
        try {
            if (entityDao != null) {
                return entityDao;
            }
            Class<D> daoType = GenericsUtils.getSuperClassGenricType(getClass(), 1);
            List<Field> fields = BeanUtils.getFieldsByType(this, daoType);
            if (fields != null && fields.size() > 0) {
                entityDao = (D) BeanUtils.getDeclaredProperty(this, fields.get(0).getName());
            } else {
                entityDao = applicationContext.getBean(daoType);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return entityDao;
    }

    @Override
    @Transactional
    public T save(T t) {
        if (t.getCreateTime() == null) {
            t.setCreateTime(new Date());
        }
        t.setDeleted(false);
        KeyHolder keyHolder = getEntityDao().insertReturnKey(t);
        long aLong = keyHolder.getLong();
        t.setId(aLong);
        return t;
    }


    @Override
    @Transactional
    public void saveTemplate(T t) {
        if (t.getCreateTime() == null) {
            t.setCreateTime(new Date());
        }
        t.setDeleted(false);
        getEntityDao().insertTemplate(t);
    }


    @Override
    @Transactional
    public void saveBatch(List<T> list) {
        if (list.isEmpty()) {
            return;
        }
        for (T t : list) {
            if (t.getCreateTime() == null) {
                t.setCreateTime(new Date());
            }
            t.setId(null);
            t.setDeleted(false);
        }

        List<List<T>> lists = splitBigList(list);
        for (List<T> ts : lists) {
            long l = System.currentTimeMillis();
            getEntityDao().insertBatch(ts);
            log.info("insertBatch, {} , time-consuming-minutes:{}", ts.size(), (System.currentTimeMillis() - l) / 1000);
        }
    }


    private List<List<T>> splitBigList(List<T> source) {
        Integer maximum = 1000;
        List<List<T>> list = new ArrayList<>();
        List<T> set = new ArrayList<>();
        for (int i = 1; i <= source.size(); i++) {
            T t = source.get(i - 1);
            if (i % maximum > 0) {
                set.add((t));
            }
            if (i % maximum == 0) {
                set.add((t));
                list.add(set);
                set = new ArrayList<>();
            }
        }
        if (!set.isEmpty()) {
            list.add(set);
        }
        return list;
    }

    @Override
    @Transactional
    public int updateById(T t) {
        t.setUpdateTime(new Date());
        return getEntityDao().updateById(t);
    }

    @Override
    @Transactional
    public int updateTemplateById(T t) {
        t.setUpdateTime(new Date());
        return getEntityDao().updateTemplateById(t);
    }

    @Override
    public T unique(Long id) {
        return getEntityDao().unique(id);
    }

    @Override
    @Transactional
    public int deleteById(T entity) {
        return deleteById(entity.getId());
    }


    @Override
    @Transactional
    public int deleteById(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return getEntityDao().deleteByIds(ids);
    }

    @Override
    @Transactional
    public int deleteByIds(List<Long> ids) {
        return getEntityDao().deleteByIds(ids);
    }

    @Override
    @Transactional
    public int forceDeleteById(T entity) {
        return forceDeleteById(entity.getId());
    }

    @Override
    @Transactional
    public int forceDeleteById(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return this.getEntityDao().forceDeleteByIds(ids);
    }

    @Override
    @Transactional
    public int forceDeleteByIds(List<Long> ids) {
        return this.getEntityDao().forceDeleteByIds(ids);
    }


    @Override
    public List<T> all() {
        T entity = getEntity();
        resetQuery(entity);
        return getEntityDao().template(entity);
    }


    @Override
    public List<T> template(T t) {
        resetQuery(t);
        return getEntityDao().template(t);
    }

    @Override
    public T templateOne(T t) {
        if (t.getId() != null) {
            return getEntityDao().single(t.getId());
        }
        resetQuery(t);
        return getEntityDao().templateOne(t);
    }

    @Override
    public List<T> template(T t, int i, int i1) {
        resetQuery(t);
        return getEntityDao().template(t, i, i1);
    }

    @Override
    public void templatePage(PageQuery<T> pageQuery) {
        if (pageQuery.getPageNumber() < 1) {
            pageQuery.setPageNumber(1);
        }
        getEntityDao().templatePage(pageQuery);
    }


    @Override
    public long templateCount(T t) {
        resetQuery(t);
        return getEntityDao().templateCount(t);
    }

    @Override
    @Transactional
    public List<T> batchUpdate(List<T> list) {
        for (T t : list) {
            this.updateTemplateById(t);
        }
        return list;
    }

    private void resetQuery(T t) {
        if (t == null) {
            return;
        }
        t.setCreateTime(null);
        t.setUpdateTime(null);
        t.setDeleted(false);
    }


    @Override
    public T single(Long id) {
        if (id == null) {
            return null;
        }
        return getEntityDao().single(id);
    }

    @Override
    public List<T> sample(T t) {
        return this.getEntityDao().sample(t);
    }

}
