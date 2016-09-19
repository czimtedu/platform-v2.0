/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.cms.service.impl;

import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.StringUtils;
import com.platform.modules.cms.bean.CmsSite;
import com.platform.modules.cms.service.SiteService;
import com.platform.modules.cms.utils.CmsUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

/**
 * 站点Service
 *
 * @author lufengc
 * @version 2016-09-12
 */
@Service
public class SiteServiceImpl extends BaseServiceImpl<CmsSite> implements SiteService {

    @Override
    public String save(CmsSite site) throws Exception {
        if (site.getCopyright() != null) {
            site.setCopyright(StringEscapeUtils.unescapeHtml4(site.getCopyright()));
        }
        if (StringUtils.isNotEmpty(site.getId())) {
            super.update(site);
        } else {
            site.setId(Encodes.uuid());
            super.insert(site);
        }
        CmsUtils.removeCache("site_" + site.getId());
        CmsUtils.removeCache("siteList");
        return site.getId();
    }

    @Override
    public int delete(String ids) throws Exception {
        super.delete(ids);
        CmsUtils.removeCache("site_" + ids);
        CmsUtils.removeCache("siteList");
        return 1;
    }

}
