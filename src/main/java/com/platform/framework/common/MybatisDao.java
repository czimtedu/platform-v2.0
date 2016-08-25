/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.common;

import java.lang.reflect.Field;
import java.util.*;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.platform.modules.sys.bean.NoDbColumn;
import com.platform.framework.cache.JedisCachedOrignal;
import com.platform.framework.exception.CommonException;
import com.platform.framework.security.UserUtils;
import com.platform.framework.util.BeanToTable;
import com.platform.framework.util.DaoUtils;
import com.platform.framework.util.ObjectUtils;
import com.platform.framework.util.Reflections;
import com.platform.framework.util.StringUtils;

/**
 * mybatis接口实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Repository
public class MybatisDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String[] basicTypes = {"int", "String", "long", "date", "BigDecimal", "BigInteger"};
    private final String[] objectTypes = {"java.lang.Integer", "java.lang.String", "java.lang.Long", "java.util.Date", "java.math.BigDecimal", "java.math.BigInteger"};

    /**
     * 根据指定表名实体类、id获取数据集
     *
     * @param clazz 实体类Class 不能为空
     * @param ids         ids
     * @return 数据集
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> selectListByIds(Class<T> clazz, String ids) {
        if (clazz == null) {
            throw new CommonException("clazz is null");
        }
        String name = StringUtils.substringAfterLast(clazz.getName(), ".");
        String tableName = BeanToTable.beanToTable(name);
        List list = new ArrayList<>();
        String noneCacheIds = ids;
        // 检查是否缓存
        if (JedisCachedOrignal.isCached(clazz)) {
            //根据id通过cache查找对象
            Object[] objArray = JedisCachedOrignal.findListObject(clazz.getName(), ids);
            list = (List) objArray[0];
            noneCacheIds = (String) objArray[1];
        }
        if (!"".equals(noneCacheIds)) {
            //cache中没有找到的对象,根据id通过数据库查找对象
            ResultAndParam result = new ResultAndParam();
            result.setTableName(tableName);
            result.setConditions(StringUtils.idsToString(noneCacheIds));
            List<Map> listArray = sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectByIds", result);
            if (listArray != null && listArray.size() > 0) {
                for (Map map : listArray) {
                    //把map转换成对应的实体对象
                    Object obj = ObjectUtils.mapToObject(clazz, map);
                    if (JedisCachedOrignal.isCached(clazz)) {
                        //把cache中没有的对象保存到cache中
                        JedisCachedOrignal.saveObject(clazz.getName(), obj);
                    }
                    list.add(obj);
                }
            }
        }
        return list;
    }

    /**
     * 根据指定表名实体类、id、表字段名 获取数据集
     *
     * @param clazz 实体类Class 非空
     * @param ids         id
     * @param fields      表字段名
     * @return 数据集
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> selectFieldByIds(Class<T> clazz, String ids, String fields) {
        if (clazz == null) {
            throw new CommonException("clazz is null");
        }
        String name = StringUtils.substringAfterLast(clazz.getName(), ".");
        String tableName = BeanToTable.beanToTable(name);
        List list = new ArrayList<>();
        String noneCacheIds = ids;
        // 检查是否缓存
        if (JedisCachedOrignal.isCached(clazz)) {
            //根据id通过cache查找对象
            Object[] objArray = JedisCachedOrignal.findListObject(clazz.getName(), ids);
            list = (List) objArray[0];
            noneCacheIds = (String) objArray[1];
        }
        if (!"".equals(noneCacheIds)) {
            //cache中没有找到的对象,根据id通过数据库查找对象
            ResultAndParam result = new ResultAndParam();
            result.setTableName(tableName);
            result.setConditions(StringUtils.idsToString(noneCacheIds));
            result.setParam(fields);
            List<Map> listArray = sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectFieldByIds", result);
            if (listArray != null && listArray.size() > 0) {
                for (Map map : listArray) {
                    //把map转换成对应的实体对象
                    Object obj = ObjectUtils.mapToObject(clazz, map);
                    list.add(obj);
                }
            }
        }
        return list;
    }


    /**
     * 根据指定表名实体类、id、表字段名、筛选条件 获取数据集
     *
     * @param clazz 实体类Class 非空
     * @param conditions  筛选条件
     * @param fields      表字段名
     * @return 数据集
     */
    public <T> List<T> selectFieldByConditions(Class<T> clazz, String conditions, String fields) {
        if (clazz == null) {
            throw new CommonException("clazz is null");
        }
        String name = StringUtils.substringAfterLast(clazz.getName(), ".");
        String tableName = BeanToTable.beanToTable(name);
        if (StringUtils.isEmpty(conditions)) {
            conditions = "1=1";
        }
        //根据查询条件通过数据库查找ids
        ResultAndParam result = new ResultAndParam();
        result.setTableName(tableName);
        result.setConditions(conditions);
        List<String> idsList = sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectIdsByConditions", result);
        if (idsList == null || idsList.size() <= 0) {
            return null;
        }
        StringBuilder ids = new StringBuilder();
        for (String id : idsList) {
            if (ids.length() == 0) {
                ids.append(id);
            } else {
                ids.append(",").append(ids);
            }
        }
        return selectFieldByIds(clazz, ids.toString(), fields);
    }

    /**
     * 根据指定表名实体类、筛选条件 获取数据集
     *
     * @param clazz 实体类Class 非空
     * @param conditions  筛选条件
     * @return 数据集
     */
    public <T> List<T> selectListByConditions(Class<T> clazz, String conditions) {
        if (clazz == null) {
            throw new CommonException("clazz is null");
        }
        String name = StringUtils.substringAfterLast(clazz.getName(), ".");
        String tableName = BeanToTable.beanToTable(name);
        if (StringUtils.isEmpty(conditions)) {
            conditions = "1=1";
        }
        //根据查询条件通过数据库查找ids
        ResultAndParam result = new ResultAndParam();
        result.setTableName(tableName);
        result.setConditions(conditions);
        List<String> idsList = sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectIdsByConditions", result);
        if (idsList == null || idsList.size() <= 0) {
            return null;
        }
        StringBuilder ids = new StringBuilder();
        for (String id : idsList) {
            if (ids.length() == 0) {
                ids.append(id);
            } else {
                ids.append(",").append(id);
            }
        }
        return selectListByIds(clazz, ids.toString());
    }

    /**
     * 根据指定表名实体类、筛选条件 获取数据集(分页功能)
     *
     * @param clazz 实体类Class 非空
     * @param conditions  筛选条件
     * @param page        分页参数
     * @return 数据集
     */
    public <T> List<T> selectPageByConditions(Class<T> clazz, String conditions, Page<?> page) {
        if (clazz == null) {
            throw new CommonException("clazz is null");
        }
        String name = StringUtils.substringAfterLast(clazz.getName(), ".");
        String tableName = BeanToTable.beanToTable(name);

        //表中的总记录数
        String countSql = "SELECT COUNT(0) FROM " + tableName;
        page.setCount(selectCountBySql(countSql));

        if (StringUtils.isEmpty(conditions)) {
            conditions = "1=1";
        }
        StringBuffer jpql = new StringBuffer();
        jpql.append("select id from ").append(tableName).append(" where ").append(conditions);
        //拼接where语句
        DaoUtils.getWhereClauseBuf(jpql, page.getPropertyFilterList());
        //根据筛选条件查询总条数
        List<String> listArray = sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectStringBySql", jpql.toString());
        int count = listArray.size();
        //计算总页数
        int i = count % page.getPageSize();
        int totalPage = count / page.getPageSize();
        if (i > 0) {
            totalPage++;
        }
        //判断总页数与当前页的关系
        if (totalPage > 0 && totalPage < page.getPageNo()) {
            page.setPageNo(totalPage + 1);
        }
        //拼接查询顺序
        DaoUtils.getOrderBy(jpql, page.getOrderBy(), page.getDirection());
        if (page.getPageSize() != -1) {
            int start = 0;
            //查询分页信息
            if (page.getPageNo() != 0) {//=0则查询全部
                start = (page.getPageNo() - 1) * page.getPageSize();
            }
            jpql.append(" limit ").append(start).append(",").append(page.getPageSize());
        }
        //当前筛选条件的分页后的总记录数
        List<String> idList = sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectStringBySql", jpql.toString());
        page.setDisplayCount(count);
        StringBuilder ids = new StringBuilder();
        for (String id : idList) {
            if (ids.length() == 0) {
                ids.append(id);
            } else {
                ids.append(",").append(id);
            }
        }
        return selectListByIds(clazz, ids.toString());
    }

    /**
     * 更新对象
     *
     * @param obj 更新对象
     */
    public void update(Object obj) {
        if (obj == null) {
            throw new CommonException("Object is null");
        }
        // 检查是否缓存
        if (JedisCachedOrignal.isCached(obj.getClass())) {
            // 删除缓存
            JedisCachedOrignal.deleteObject(obj.getClass().getName(), obj);
        }
        String name = StringUtils.substringAfterLast(obj.getClass().getName(), ".");
        String tableName = BeanToTable.beanToTable(name);
        //自动添加更新时间
        try {
            Reflections.setFieldValue(obj, "updateBy", UserUtils.getUserId());
        } catch (Exception ignored) {
        }
        try {
            Reflections.setFieldValue(obj, "updateTime", new java.sql.Timestamp(new Date().getTime()));
        } catch (Exception ignored) {
        }
        String conditions = "id=";
        String setFields = "";//对象的字段名=字段值
        Field[] fields = Reflections.getField(obj.getClass(), null);
        for (Field field : fields) {
            String param = "";//对象的有值的字段名
            String value = "";//对象的有值的字段值
            field.setAccessible(true);
            NoDbColumn annotation = field.getAnnotation(NoDbColumn.class);
            if (annotation != null) {
                continue;
            }
            // 字段值
            for (int i = 0; i < basicTypes.length; i++) {
                if (field.getType().getName().equalsIgnoreCase(basicTypes[i])
                        || field.getType().getName().equalsIgnoreCase(objectTypes[i])) {
                    try {
                        if ("serialVersionUID".equals(field.getName())) {
                            continue;
                        }
                        if (field.get(obj) != null) {
                            if (field.getType().getName().equals(String.class.getName())
                                    || field.getType().getName().equals(Date.class.getName())) {
                                value = "'" + field.get(obj) + "',";
                            } else {
                                value = field.get(obj) + ",";
                            }
                            if ((field.getName()).equals("id")) {
                                conditions += "'" + field.get(obj).toString() + "'";//对象的id值
                            } else {
                                //字段名
                                param = BeanToTable.beanToTable(field.getName()) + "=";
                                setFields += param + value;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        setFields = setFields.substring(0, setFields.length() - 1);
        ResultAndParam result = new ResultAndParam();
        result.setTableName(tableName);
        result.setConditions(conditions);
        result.setParam(setFields);
        sqlSession.update("com.platform.framework.common.MybatisBaseDao.update", result);
    }

    /**
     * 根据条件更新数据库和删除缓存：
     *
     * @param clazz 要更新的表对应的实体类 非空
     * @param setfields   要更新的字段（例如“userName='a', password='1'”）
     * @param conditions  更新条件（例如“userName='a' and password='1'”）
     */
    public <T> void updateByConditions(Class<T> clazz, String setfields, String conditions) {
        if (clazz == null || setfields == null) {
            throw new CommonException("clazz or setfields is null");
        }
        // 检查是否缓存
        if (JedisCachedOrignal.isCached(clazz)) {
            List<T> list = selectListByConditions(clazz, conditions);//根据条件查找对象
            for (Object obj : list) {
                // 删除缓存
                JedisCachedOrignal.deleteObject(clazz.getName(), obj);
            }
        }
        String name = StringUtils.substringAfterLast(clazz.getName(), ".");
        String tableName = BeanToTable.beanToTable(name);
        if (StringUtils.isEmpty(conditions)) {
            conditions = "1=1";
        }
        // 添加修改时间和修改人
        Field updateTime = null;
        try {
            updateTime = Reflections.getAccessibleField(clazz.newInstance(), "updateTime");
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
        if(updateTime != null){ // 存在该属性
            setfields += ",update_time='" + new java.sql.Timestamp(new Date().getTime()) + "'";
        }
        Field updateBy = null;
        try {
            updateBy = Reflections.getAccessibleField(clazz.newInstance(), "updateBy");
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
        if(updateBy != null){ // 存在该属性
            setfields += ",update_by=" + UserUtils.getUserId();
        }
        ResultAndParam result = new ResultAndParam();
        result.setTableName(tableName);
        result.setConditions(conditions);
        result.setParam(setfields);
        // 更新数据库
        sqlSession.update("com.platform.framework.common.MybatisBaseDao.update", result);
    }

    /**
     * 保存数据
     *
     * @param obj 保存对象
     * @return 保存的数据ID
     */
    public String insert(Object obj) {
        if (obj == null) {
            throw new CommonException("Object is null");
        }
        String name = StringUtils.substringAfterLast(obj.getClass().getName(), ".");
        String tableName = BeanToTable.beanToTable(name);
        try {
            Reflections.setFieldValue(obj, "createBy", UserUtils.getUserId());
        } catch (Exception ignored) {
        }
        try {
            Reflections.setFieldValue(obj, "createTime", new java.sql.Timestamp(new Date().getTime()));
        } catch (Exception ignored) {
        }
        String param = "";//对象的有值的字段名
        String value = "";//对象的有值的字段值
        Field[] fields = Reflections.getField(obj.getClass(), null);
        for (Field field : fields) {
            field.setAccessible(true);
            NoDbColumn annotation = field.getAnnotation(NoDbColumn.class);
            if (annotation != null) {
                continue;
            }
            // 字段值
            for (int i = 0; i < basicTypes.length; i++) {
                if (field.getType().getName().equalsIgnoreCase(basicTypes[i])
                        || field.getType().getName().equalsIgnoreCase(objectTypes[i])) {
                    try {
                        if ("serialVersionUID".equals(field.getName())) {
                            continue;
                        }
                        if (field.get(obj) != null) {
                            if (field.getType().getName().equals(String.class.getName())
                                    || field.getType().getName().equals(Date.class.getName())) {
                                value += "'" + field.get(obj) + "',";
                            } else {
                                value += field.get(obj) + ",";
                            }
                            param += BeanToTable.beanToTable(field.getName()) + ",";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        String params = param.substring(0, param.length() - 1);
        String conditions = value.substring(0, value.length() - 1);
        ResultAndParam result = new ResultAndParam();
        result.setTableName(tableName);
        result.setConditions(conditions);
        result.setParam(params);
        sqlSession.insert("com.platform.framework.common.MybatisBaseDao.insert", result);
        return result.getId();
    }

    /**
     * 更新对象
     *
     * @param clazz 实体类
     * @param ids         ids
     */
    public void deleteByIds(Class clazz, String ids) {
        if (clazz == null || ids == null) {
            throw new CommonException("clazz or ids is null");
        }
        // 检查是否缓存
        if (JedisCachedOrignal.isCached(clazz)) {
            JedisCachedOrignal.deleteCache(clazz.getName(), ids);
        }
        String name = StringUtils.substringAfterLast(clazz.getName(), ".");
        String tableName = BeanToTable.beanToTable(name);
        ResultAndParam result = new ResultAndParam();
        result.setTableName(tableName);
        result.setConditions(StringUtils.idsToString(ids));
        // 删除数据库数据
        sqlSession.delete("com.platform.framework.common.MybatisBaseDao.deleteByIds", result);
    }


    /**
     * 根据sql语句查询对象数据集(无缓存)
     *
     * @param sql SQL语句
     * @return 对象数据集
     */
    public <T> List<T> selectListBySql(Class<T> clazz, String sql) {
        if (clazz == null) {
            throw new CommonException("clazz is null");
        }
        List<T> list = new ArrayList<>();
        List<Map> listArray = sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectMapBySql", sql);
        if (listArray != null && listArray.size() > 0) {
            for (Map map : listArray) {
                list.add(ObjectUtils.mapToObject(clazz, map));
            }
        }
        return list;
    }

    /**
     * 根据sql语句查询单列数据集(无缓存)
     *
     * @param sql SQL语句
     * @return 单列数据集
     */
    public List<String> selectStringBySql(String sql) {
        return sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectStringBySql", sql);
    }

    /**
     * 根据sql语句查询Map数据级(无缓存)
     *
     * @param sql SQL语句
     * @return Map数据集
     */
    public List<Map> selectMapBySql(String sql) {
        return sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectMapBySql", sql);
    }

    /**
     * 根据指定sql查询数量
     *
     * @param sql sql查询语句
     * @return 数量
     */
    public int selectCountBySql(String sql) {
        //查询总条数
        List<Integer> listArray = sqlSession.selectList("com.platform.framework.common.MybatisBaseDao.selectCountBySql", sql);
        int count = 0;
        if (listArray != null && listArray.size() > 0) {
            count = listArray.get(0);
        }
        return count;
    }

    /**
     * 根据sql语句删除
     *
     * @param sql SQL语句
     * @param clazz 实体类 可以为空，为空则不删除缓存
     */
    public void deleteBySql(String sql, Class clazz) {
        if(clazz != null){
            // 检查是否缓存
            if (JedisCachedOrignal.isCached(clazz)) {
                JedisCachedOrignal.deleteObjectLike(clazz.getName());
            }
        }
        sqlSession.delete("com.platform.framework.common.MybatisBaseDao.deleteBySql", sql);
    }

    /**
     * 根据sql语句保存
     *
     * @param sql SQL语句
     */
    public void insertBySql(String sql) {
        sqlSession.insert("com.platform.framework.common.MybatisBaseDao.insertBySql", sql);
    }

    /**
     * 根据sql语句更新
     *
     * @param sql SQL语句
     * @param clazz 实体类 可以为空，为空则不删除缓存
     */
    public void updateBySql(String sql, Class clazz) {
        if(clazz != null){
            // 检查是否缓存
            if (JedisCachedOrignal.isCached(clazz)) {
                JedisCachedOrignal.deleteObjectLike(clazz.getName());
            }
        }
        sqlSession.update("com.platform.framework.common.MybatisBaseDao.updateBySql", sql);
    }

}
