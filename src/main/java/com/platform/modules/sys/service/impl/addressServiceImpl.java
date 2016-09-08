/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.framework.common.MybatisDao;
import com.platform.modules.sys.bean.AddressCity;
import com.platform.modules.sys.bean.AddressCounty;
import com.platform.modules.sys.bean.AddressProvince;
import com.platform.modules.sys.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典表service实现类
 *
 * @author lufengc
 * @date 2016/1/16 14:05
 */
@Service
public class addressServiceImpl implements AddressService {

    @Autowired
    private MybatisDao mybatisDao;

    /**
     * 获取省份列表
     *
     * @return List<AddressProvince>
     */
    @Override
    public List<AddressProvince> getProvinceList() {
        return mybatisDao.selectListByConditions(AddressProvince.class, "");
    }

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceId 省份ID
     * @return List<AddressCity>
     */
    @Override
    public List<AddressCity> getCityList(String provinceId) {
        return mybatisDao.selectListByConditions(AddressCity.class, "province_id = " + provinceId);
    }

    /**
     * 根据城市ID获取区县列表
     *
     * @param cityId 城市ID
     * @return List<AddressCounty>
     */
    @Override
    public List<AddressCounty> getCountyList(String cityId) {
        return mybatisDao.selectListByConditions(AddressCounty.class, "city_id = " + cityId);
    }
}
