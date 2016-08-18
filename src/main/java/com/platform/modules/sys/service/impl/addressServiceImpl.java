/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.modules.sys.bean.AddressCity;
import com.platform.modules.sys.bean.AddressCounty;
import com.platform.modules.sys.bean.AddressProvince;
import com.platform.modules.sys.service.AddressService;
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
    public List<AddressProvince> getProvinceList() {
        return mybatisBaseDaoImpl.selectListByConditions(AddressProvince.class, "");
    }

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceId 省份ID
     * @return List
     */
    public List<AddressCity> getCityList(String provinceId) {
        return mybatisBaseDaoImpl.selectListByConditions(AddressCity.class, "province_id = " + provinceId);
    }

    /**
     * 根据城市ID获取区县列表
     *
     * @param cityId 城市ID
     * @return List
     */
    public List<AddressCounty> getCountyList(String cityId) {
        return mybatisBaseDaoImpl.selectListByConditions(AddressCounty.class, "city_id = " + cityId);
    }
}
