/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.cms.service.impl;

import com.platform.core.cms.bean.CmsArticle;
import com.platform.core.cms.bean.CmsArticleData;
import com.platform.core.cms.service.ArticleService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisBaseDaoImpl;
import com.platform.framework.util.Encodes;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色service实现类
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Service
@Transactional(readOnly = true)
public class ArticleServiceImpl extends BaseServiceImpl<CmsArticle> implements ArticleService {

    @Autowired
    private MybatisBaseDaoImpl mybatisBaseDaoImpl;

    /**
     * 保存
     *
     * @param object object
     * @return 保存的ID
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public void save(CmsArticle object, CmsArticleData articleData) throws Exception {
        if (StringUtils.isNotEmpty(object.getId())) {
            mybatisBaseDaoImpl.updateDbAndCache(object);
            mybatisBaseDaoImpl.updateDbAndCache(articleData);
        } else {
            String uuid = Encodes.uuid();
            object.setId(uuid);
            articleData.setId(uuid);
            mybatisBaseDaoImpl.saveDb(object);
            mybatisBaseDaoImpl.saveDb(articleData);
        }
    }

    @Override
    public String getContent(String id) {
        List<CmsArticleData> list = (List<CmsArticleData>) mybatisBaseDaoImpl.findFieldDbAndCacheByIds(CmsArticleData.class, id, "content");
        return list.get(0).getContent();
    }

    @Override
    public void updateArticle(CmsArticle object) {
        mybatisBaseDaoImpl.updateDbAndCache(object);
    }

    @Override
    public Long save(CmsArticle object) throws Exception {
        return null;
    }

    /**
     * 删除
     *
     * @param ids 删除的ID
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public Long delete(String ids) throws Exception {
        mybatisBaseDaoImpl.deleteDbAndCacheByIds(CmsArticle.class, ids);
        return 1L;
    }

}
