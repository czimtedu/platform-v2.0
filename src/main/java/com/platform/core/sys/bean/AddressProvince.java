/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.sys.bean;

import com.platform.framework.cache.DataCached;

import java.io.Serializable;

/**
 * 省份
 * @author lufengc
 * @date 2016/2/18 11:50
 */
@DataCached(type = DataCached.CachedType.REDIS_CACHED)
public class AddressProvince implements Serializable {

    private static final long serialVersionUID = 1L;
    private String provinceId;
    private String province;
    public String getProvinceId() {
        return provinceId;
    }
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
}
