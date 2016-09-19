/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.service.impl;

import com.platform.modules.cms.bean.CmsGuestbook;
import com.platform.modules.cms.service.GuestbookService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.Encodes;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 留言板service实现类
 *
 * @author lufengc
 * @date 2016-01-15 09:56:22
 */
@Service
public class GuestbookServiceImpl extends BaseServiceImpl<CmsGuestbook> implements GuestbookService {

    @Autowired
    private MybatisDao mybatisDao;

    @Override
    public String save(CmsGuestbook object) throws Exception {
        if (StringUtils.isNotEmpty(object.getId())) {
            mybatisDao.update(object);
        } else {
            object.setId(Encodes.uuid());
            mybatisDao.insert(object);
        }
        return object.getId();
    }

}
