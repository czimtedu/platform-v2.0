/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.common;

import com.platform.modules.sys.bean.NoDbColumn;
import com.platform.framework.exception.CommonException;
import com.platform.framework.util.BeanToTable;
import com.platform.framework.util.DaoUtils;
import com.platform.framework.util.Reflections;
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
     * 根据ID查询entity
     *
     * @param clazz OBJECT
     * @param id    对象id
     * @return OBJECT
     * @throws Exception
     */
    public T get(Class<T> clazz, String id) throws Exception {
        List<T> list = mybatisDao.selectListByIds(clazz, id);
        T entity = null;
        if (list != null && list.size() > 0) {
            entity = list.get(0);
        }
        return entity;
    }

    /**
     * 查询列表,参数为空 则查询所有
     *
     * @param object 要查询的对象
     * @return list
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
        String column;
        Field[] fields = Reflections.getField(object.getClass(), null);
        for (Field field : fields) {
            field.setAccessible(true);
            NoDbColumn annotation = field.getAnnotation(NoDbColumn.class);
            if (annotation != null) {
                continue;
            }
            column = BeanToTable.beanToTable(field.getName());
            Object value = field.get(object);
            if (!"serialVersionUID".equals(field.getName()) && value != null) {
                propertyFilter = new PropertyFilter();
                propertyFilter.setPropertyClass(field.getType());
                propertyFilter.setMatchType(PropertyFilter.MatchType.EQ);
                propertyFilter.setPropertyName(column);
                propertyFilter.setMatchValue("'" + value + "'");
                propertyFilterList.add(propertyFilter);
            }
        }
        DaoUtils.getWhereClauseBuf(conditions, propertyFilterList);
        int index = conditions.toString().toUpperCase().indexOf("WHERE");
        String conditionStr = "";
        if (index > 0) {
            conditionStr = conditions.substring(index + 6, conditions.length());
        }
        return (List<T>) mybatisDao.selectListByConditions(object.getClass(), conditionStr);
    }

    /**
     * 分页查询
     *
     * @param page   分页信息
     * @param object 分页对象
     * @param propertyFilters
     * @return Page
     * @throws Exception
     */
    @Override
    @SuppressWarnings("unchecked")
    public Page<T> getPage(Page<T> page, T object, List<PropertyFilter> propertyFilters, String conditions) throws Exception {
        if(propertyFilters == null){
            propertyFilters = new ArrayList<>();
        }
        PropertyFilter propertyFilter;
        String column;
        Field[] fields = Reflections.getField(object.getClass(), null);
        for (Field field : fields) {
            field.setAccessible(true);
            NoDbColumn annotation = field.getAnnotation(NoDbColumn.class);
            if (annotation != null) {
                continue;
            }
            column = BeanToTable.beanToTable(field.getName());
            Object value = field.get(object);
            if (!"serialVersionUID".equals(field.getName()) && value != null && !value.equals("")) {
                propertyFilter = new PropertyFilter();
                propertyFilter.setPropertyClass(field.getType());
                propertyFilter.setMatchType(PropertyFilter.MatchType.LIKE);
                propertyFilter.setPropertyName(column);
                propertyFilter.setMatchValue(value);
                propertyFilters.add(propertyFilter);
            }
        }
        page.setPropertyFilterList(propertyFilters);
        List<T> list = (List<T>) mybatisDao.selectPageByConditions(object.getClass(), conditions, page);
        page.setList(list);
        return page;
    }
}
