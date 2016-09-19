/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.cms.service.impl;

import com.platform.framework.common.BaseServiceImpl;
import com.platform.modules.cms.bean.CmsComment;
import com.platform.modules.cms.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * 评论Service
 *
 * @author lufengc
 * @version 2016-09-12
 */
@Service
public class CommentServiceImpl extends BaseServiceImpl<CmsComment> implements CommentService {

    @Override
    public String save(CmsComment object) throws Exception {
        return null;
    }
}
