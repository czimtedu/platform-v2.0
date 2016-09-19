/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.SysDict;
import com.platform.modules.sys.service.DictService;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.SpringContextHolder;

/**
 * 字典工具类
 *
 * @author lufengc
 * @date 2016/1/16 9:37
 */
public class DictUtils {

    private static DictService dictService = SpringContextHolder.getBean(DictService.class);

    public static final String CACHE_DICT_MAP = "dictMap";

    /**
     * 根据多个字典值和字典名获取对应的标签
     *
     * @param value        字典名对应的值
     * @param enName       字典名
     * @param defaultValue 默认值
     * @return 字典标签
     */
    public static String getDictLabel(String value, String enName, String defaultValue) {
        if (StringUtils.isNotBlank(enName) && StringUtils.isNotBlank(value)) {
            for (SysDict dict : getDictList(enName)) {
                if (enName.equals(dict.getEnName()) && value.equals(dict.getValue())) {
                    return dict.getLabel();
                }
            }
        }
        return defaultValue;
    }

    /**
     * 根据多个字典值和字典名获取对应的标签（多个以逗号分隔）
     *
     * @param values       字典名对应的值（多个以逗号分隔）
     * @param enName       字典名
     * @param defaultValue 默认值
     * @return 字典标签
     */
    public static String getDictLabels(String values, String enName, String defaultValue) {
        if (StringUtils.isNotBlank(enName) && StringUtils.isNotBlank(values)) {
            List<String> valueList = Lists.newArrayList();
            for (String value : StringUtils.split(values, ",")) {
                valueList.add(getDictLabel(value, enName, defaultValue));
            }
            return StringUtils.join(valueList, ",");
        }
        return defaultValue;
    }

    /**
     * 根据某个字典label和字典类型获取值
     *
     * @param label        字典名对应的标签
     * @param enName       字典名
     * @param defaultLabel 默认标签
     * @return 字典值
     */
    public static String getDictValue(String label, String enName, String defaultLabel) {
        if (StringUtils.isNotBlank(enName) && StringUtils.isNotBlank(label)) {
            for (SysDict dict : getDictList(enName)) {
                if (enName.equals(dict.getEnName()) && label.equals(dict.getLabel())) {
                    return dict.getValue();
                }
            }
        }
        return defaultLabel;
    }

    /**
     * 根据字典名获取字典列表
     *
     * @param enName 字典名 为空查询所有
     * @return List
     */
    public static List<SysDict> getDictList(String enName) {
        List<SysDict> dictList = null;
        Map<String, List<SysDict>> dictMap = getDictMap();
        if (dictMap != null) {
            if (enName != null) {
                dictList = dictMap.get(enName);
            } else {
                dictList = new ArrayList<>();
                for (List<SysDict> sysDicts : dictMap.values()) {
                    dictList.addAll(sysDicts);
                }
            }
        }
        if (dictList == null) {
            dictList = Lists.newArrayList();
        }
        return dictList;
    }

    /**
     * 返回字典列表（JSON）
     *
     * @param enName 字典名
     * @return json
     */
    public static String getDictListJson(String enName) {
        return JSON.toJSONString(getDictList(enName));
    }

    /**
     * 获取字典MAP
     *
     * @return Map
     */
    public static Map<String, List<SysDict>> getDictMap() {
        try {
            List<SysDict> dictList;
            @SuppressWarnings("unchecked")
            Map<String, List<SysDict>> dictMap = (Map<String, List<SysDict>>) JedisUtils.getObject(CACHE_DICT_MAP);
            if (dictMap == null) {
                dictMap = Maps.newHashMap();
                for (SysDict dict : dictService.getList(new SysDict())) {
                    dictList = dictMap.get(dict.getEnName());
                    if (dictList != null) {
                        dictList.add(dict);
                    } else {
                        dictMap.put(dict.getEnName(), Lists.newArrayList(dict));
                    }
                }
                JedisUtils.setObject(CACHE_DICT_MAP, dictMap, 0);
            }
            return dictMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回字典列表（JSON）
     *
     * @return json
     */
    public static String getDictMapJson() {
        return JSON.toJSONString(getDictMap());
    }
}
