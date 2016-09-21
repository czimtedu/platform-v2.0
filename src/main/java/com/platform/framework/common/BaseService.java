/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.common;


import java.util.List;

/**
 * service基类
 *
 * @author lufengc
 * @date 2016-01-15 09:56:22
 */
public interface BaseService<T> {

    /**
     * 数据插入或更新操作
     * 由业务类实现
     *
     * @param object T
     */
    String save(T object) throws Exception;

    /**
     * 新增entity
     *
     * @param object T
     * @return 主键ID
     * @throws Exception
     */
    String insert(T object) throws Exception;

    /**
     * 修改entity
     *
     * @param object T
     * @throws Exception
     */
    void update(T object) throws Exception;

    /**
     * 删除entity
     *
     * @param ids 删除的ID
     * @return 删除数量
     * @throws Exception
     */
    int delete(String ids) throws Exception;

    /**
     * 获取对象
     *
     * @param id 主键ID
     * @return T
     * @throws Exception
     */
    T get(String id) throws Exception;

    /**
     * 获取列表
     *
     * @param object 要查询的对象
     * @return List<T>
     * @throws Exception
     */
    List<T> getList(T object) throws Exception;

    /**
     * @param page            分页信息
     * @param object          分页对象
     * @return Page<T>
     * @throws Exception
     */
    Page<T> getPage(Page<T> page, T object, String conditions) throws Exception;


}
