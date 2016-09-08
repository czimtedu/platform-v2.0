/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service;

import com.platform.modules.sys.bean.AddressCity;
import com.platform.modules.sys.bean.AddressCounty;
import com.platform.modules.sys.bean.AddressProvince;

import java.util.List;

/**
 * 字典service
 *
 * @author lufengc
 * @date 2016/1/16 14:03
 */
public interface AddressService {

    /**
     * 获取省份列表
     *
     * @return List
     */
    List<AddressProvince> getProvinceList();

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceId 省份ID
     * @return List
     */
    List<AddressCity> getCityList(String provinceId);

    /**
     * 根据城市ID获取区县列表
     *
     * @param cityId 城市ID
     * @return List
     */
    List<AddressCounty> getCountyList(String cityId);
}
