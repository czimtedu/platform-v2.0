/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.common;


import java.util.List;

/**
 * service基类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public interface BaseService<T> {

    /**
     * 新增或修改entity
     *
     * @param object object
     * @return 保存的ID
     * @throws Exception
     */
    String save(T object) throws Exception;

    /**
     * 删除entity
     *
     * @param ids 删除的ID
     * @return 值
     * @throws Exception
     */
    String delete(String ids) throws Exception;

    /**
     * 获取对象
     *
     * @param id 主键ID
     * @return 对象
     * @throws Exception
     */
    T get(Class<T> clazz, String id) throws Exception;

    /**
     * 获取列表
     *
     * @param object 要查询的对象
     * @return list
     * @throws Exception
     */
    List<T> getList(T object) throws Exception;

    /**
     * @param page   分页信息
     * @param object 分页对象
     * @param propertyFilterList
     * @return Page
     * @throws Exception
     */
    Page<T> getPage(Page<T> page, T object, List<PropertyFilter> propertyFilterList, String conditions) throws Exception;


}
