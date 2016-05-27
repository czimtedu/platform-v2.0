/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.sys.service.impl;

import com.platform.core.sys.bean.AddressCity;
import com.platform.core.sys.bean.AddressCounty;
import com.platform.core.sys.bean.AddressProvince;
import com.platform.core.sys.service.AddressService;
import com.platform.framework.common.MybatisBaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典表service实现类
 *
 * @author lufengc
 * @date 2016/1/16 14:05
 */
@Service
@Transactional(readOnly = true)
public class addressServiceImpl implements AddressService {
    @Autowired
    private MybatisBaseDaoImpl mybatisBaseDaoImpl;

    /**
     * 获取省份列表
     *
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<AddressProvince> getProvinceList() {
        return mybatisBaseDaoImpl.findListDbAndCacheByConditions(AddressProvince.class, "");
    }

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceId 省份ID
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<AddressCity> getCityList(String provinceId) {
        return mybatisBaseDaoImpl.findListDbAndCacheByConditions(AddressCity.class, "province_id = " + provinceId);
    }

    /**
     * 根据城市ID获取区县列表
     *
     * @param cityId 城市ID
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<AddressCounty> getCountyList(String cityId) {
        return mybatisBaseDaoImpl.findListDbAndCacheByConditions(AddressCounty.class, "city_id = " + cityId);
    }
}
