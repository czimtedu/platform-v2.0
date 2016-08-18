/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.bean;

import com.platform.framework.cache.DataCached;

import java.io.Serializable;

/**
 * 区县
 * @author lufengc
 * @date 2016/2/18 11:49
 */
@DataCached(type = DataCached.CachedType.REDIS_CACHED)
public class AddressCounty implements Serializable {

    private static final long serialVersionUID = 1L;
    private String countyId;
    private String county;
    private String cityId;
    public String getCountyId() {
        return countyId;
    }
    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }
    public String getCounty() {
        return county;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    public String getCityId() {
        return cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
