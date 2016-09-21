/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.cms.service.impl;

import com.google.common.collect.Lists;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.common.Page;
import com.platform.framework.util.StringUtils;
import com.platform.modules.cms.bean.CmsLink;
import com.platform.modules.cms.service.LinkService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 链接Service
 *
 * @author lufengc
 * @version 2016-09-12
 */
@Service
@Transactional(readOnly = true)
public class LinkServiceImpl extends BaseServiceImpl<CmsLink> implements LinkService {

    @Autowired
    MybatisDao mybatisDao;

    @Override
    public Page<CmsLink> getPage(Page<CmsLink> page, CmsLink object,
                                 String conditions) throws Exception {
        // 更新过期的权重，间隔为“6”个小时
        Date updateExpiredWeightDate = (Date) JedisUtils.getObject("updateExpiredWeightDateByLink");
        if (updateExpiredWeightDate == null || updateExpiredWeightDate.getTime() < new Date().getTime()) {
            String sql = "update cms_link SET weight = 0 WHERE weight > 0 AND weight_date < CURDATE()";
            mybatisDao.updateBySql(sql, null);
            JedisUtils.setObject("updateExpiredWeightDateByLink", DateUtils.addHours(new Date(), 6), 0);
        }

        return super.getPage(page, object, conditions);
    }

    @Override
    public String save(CmsLink object) throws Exception {
        return null;
    }

    @Override
    public int delete(String ids) throws Exception {
        super.delete(ids);
        return 1;
    }

    /**
     * 通过编号获取内容标题
     */
    @Override
    public List<Object[]> findByIds(String ids) {
        List<Object[]> list = Lists.newArrayList();
        List<CmsLink> cmsLinks = mybatisDao.selectListByIds(CmsLink.class, ids);
        for (CmsLink e : cmsLinks) {
            list.add(new Object[]{e.getId(), StringUtils.abbr(e.getTitle(), 50)});
        }
        return list;
    }

}
