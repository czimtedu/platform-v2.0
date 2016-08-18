/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.bean;

import com.platform.framework.cache.DataCached;

import java.io.Serializable;

/**
 * 城市
 * @author lufengc
 * @date 2016/2/18 11:49
 */
@DataCached(type = DataCached.CachedType.REDIS_CACHED)
public class AddressCity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cityId;
    private String city;
    private String provinceId;
    public String getCityId() {
        return cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getProvinceId() {
        return provinceId;
    }
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
}
