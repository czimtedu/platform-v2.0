/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.gen.service.impl;

import com.platform.core.gen.bean.GenTemplate;
import com.platform.core.gen.service.GenTemplateService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisBaseDaoImpl;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 代码模板Service实现类
 *
 * @author lufengc
 * @date 2016/7/28 23:54
 */
@Service
@Transactional(readOnly = true)
public class GenTemplateServiceImpl extends BaseServiceImpl<GenTemplate> implements GenTemplateService {

    @Autowired
    private MybatisBaseDaoImpl mybatisBaseDaoImpl;

    @Override
    @Transactional
    public String save(GenTemplate object) throws Exception {
        if (object.getContent() != null) {
            object.setContent(StringEscapeUtils.unescapeHtml4(object.getContent()));
        }
        if (StringUtils.isBlank(object.getId())) {
            object.setId(Encodes.uuid());
            mybatisBaseDaoImpl.insert(object);
        } else {
            mybatisBaseDaoImpl.update(object);
        }
        return object.getId();
    }

    @Override
    @Transactional
    public String delete(String ids) throws Exception {
        mybatisBaseDaoImpl.deleteByIds(GenTemplate.class, ids);
        return "";
    }
}
