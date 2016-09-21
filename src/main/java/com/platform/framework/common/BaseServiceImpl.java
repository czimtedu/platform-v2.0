/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.common;

import com.platform.framework.exception.CommonException;
import com.platform.framework.util.DaoUtils;
import com.platform.framework.util.Reflections;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.NoDbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * service基类实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    private MybatisDao mybatisDao;

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 插入
     *
     * @param object T
     * @throws Exception
     */
    @Override
    public String insert(T object) throws Exception {
        return mybatisDao.insert(object);
    }

    /**
     * 更新
     *
     * @param object T
     * @throws Exception
     */
    @Override
    public void update(T object) throws Exception {
        mybatisDao.update(object);
    }

    /**
     * 删除
     *
     * @param ids 删除的ID
     * @return 删除数量
     * @throws Exception
     */
    @Override
    public int delete(String ids) throws Exception {
        @SuppressWarnings("unchecked")
        Class<T> entityClass = Reflections.getClassGenricType(getClass());
        return mybatisDao.deleteByIds(entityClass, ids);
    }

    /**
     * 根据ID查询
     *
     * @param id 对象id
     * @return Object
     * @throws Exception
     */
    public T get(String id) throws Exception {
        @SuppressWarnings("unchecked")
        Class<T> entityClass = Reflections.getClassGenricType(getClass());
        T entity = null;
        if (StringUtils.isNotEmpty(id)) {
            List<T> list = mybatisDao.selectListByIds(entityClass, id);
            if (list != null && list.size() > 0) {
                entity = list.get(0);
            }
        }
        if (entity == null) {
            entity = entityClass.newInstance();
        }
        return entity;
    }

    /**
     * 查询列表,
     * 参数为空对象，则查询所有，如：getList(new SysUser())
     *
     * @param object 要查询的对象
     * @return List<T>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<T> getList(T object) throws Exception {
        if (object == null) {
            throw new CommonException("Object is null");
        }
        StringBuffer conditions = new StringBuffer();
        List<PropertyFilter> propertyFilterList = new ArrayList<>();
        PropertyFilter propertyFilter;
        Field[] fields = Reflections.getField(object.getClass(), null);
        for (Field field : fields) {
            field.setAccessible(true);
            NoDbColumn annotation = field.getAnnotation(NoDbColumn.class);
            if (annotation != null) {
                continue;
            }
            Object value = field.get(object);
            if (!"serialVersionUID".equals(field.getName()) && value != null && !"".equals(value)) {
                propertyFilter = new PropertyFilter();
                propertyFilter.setPropertyClass(field.getType());
                propertyFilter.setMatchType(PropertyFilter.MatchType.EQ);
                propertyFilter.setPropertyName(field.getName());
                propertyFilter.setMatchValue(value);
                propertyFilterList.add(propertyFilter);
            }
        }
        DaoUtils.getWhereClauseBuf(conditions, propertyFilterList);
        int index = conditions.toString().toUpperCase().indexOf("WHERE");
        String conditionStr = "";
        if (index > 0) {
            conditionStr = conditions.substring(index + 6, conditions.length());
        }
        List<T> list = (List<T>) mybatisDao.selectListByConditions(object.getClass(), conditionStr);
        if (list != null) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 分页查询
     *
     * @param page            分页信息
     * @param object          分页对象
     * @return Page<T>
     * @throws Exception
     */
    @Override
    @SuppressWarnings("unchecked")
    public Page<T> getPage(Page<T> page, T object, String conditions) throws Exception {
        PropertyFilter propertyFilter;
        Field[] fields = Reflections.getField(object.getClass(), null);
        for (Field field : fields) {
            field.setAccessible(true);
            NoDbColumn annotation = field.getAnnotation(NoDbColumn.class);
            if (annotation != null) {
                continue;
            }
            Object value = field.get(object);
            if (!"serialVersionUID".equals(field.getName()) && value != null && !value.equals("")) {
                propertyFilter = new PropertyFilter();
                propertyFilter.setPropertyClass(field.getType());
                if("String".equals(field.getType().getName()) || "java.lang.String".equals(field.getType().getName())){
                    propertyFilter.setMatchType(PropertyFilter.MatchType.LIKE);
                } else {
                    propertyFilter.setMatchType(PropertyFilter.MatchType.EQ);
                }
                propertyFilter.setPropertyName(field.getName());
                propertyFilter.setMatchValue(value);
                page.addPropertyFilter(propertyFilter);
            }
        }
        List<T> list = (List<T>) mybatisDao.selectPageByConditions(object.getClass(), conditions, page);
        page.setList(list);
        return page;
    }
}
