/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.modules.cms.service.impl;

import com.platform.modules.cms.bean.CmsGuestbook;
import com.platform.modules.cms.service.GuestbookService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisBaseDaoImpl;
import com.platform.framework.util.Encodes;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统角色service实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Service
@Transactional(readOnly = true)
public class GuestbookServiceImpl extends BaseServiceImpl<CmsGuestbook> implements GuestbookService {

    @Autowired
    private MybatisBaseDaoImpl mybatisBaseDaoImpl;


    @Override
    @Transactional()
    public String save(CmsGuestbook object) throws Exception {
        if (StringUtils.isNotEmpty(object.getId())) {
            mybatisBaseDaoImpl.update(object);
        } else {
            object.setId(Encodes.uuid());
            mybatisBaseDaoImpl.insert(object);
        }
        return object.getId();
    }

    @Override
    @Transactional()
    public String delete(String ids) throws Exception {
        return null;
    }
}
