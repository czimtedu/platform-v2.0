/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.service.impl;

import com.platform.modules.cms.bean.CmsArticle;
import com.platform.modules.cms.bean.CmsArticleData;
import com.platform.modules.cms.service.ArticleService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
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
    private MybatisDao mybatisDao;

    /**
     * 保存
     *
     * @param object object
     * @throws Exception
     */
    @Override
    @Transactional()
    public String save(CmsArticle object, CmsArticleData articleData) throws Exception {
        if (StringUtils.isNotEmpty(object.getId())) {
            mybatisDao.update(object);
            mybatisDao.update(articleData);
        } else {
            String uuid = Encodes.uuid();
            object.setId(uuid);
            articleData.setId(uuid);
            mybatisDao.insert(object);
            mybatisDao.insert(articleData);
        }
        return object.getId();
    }

    /**
     * 获取文章内容
     * @param id 文章内容ID
     * @return 文章内容
     */
    @Override
    public String getContent(String id) {
        String content;
        List<CmsArticleData> list = mybatisDao.selectFieldByIds(CmsArticleData.class, id, "content");
        if(list != null && list.size() > 0){
            content = list.get(0).getContent();
        } else {
            content = "暂无内容...";
        }
        return content;
    }

    /**
     * 更新文章信息（点击数）
     * @param object 文章列表对象
     */
    @Override
    @Transactional()
    public void updateArticle(CmsArticle object) {
        mybatisDao.update(object);
    }

    /**
     * 删除
     *
     * @param ids 删除的ID
     * @throws Exception
     */
    @Override
    @Transactional()
    public String delete(String ids) throws Exception {
        mybatisDao.deleteByIds(CmsArticle.class, ids);
        mybatisDao.deleteByIds(CmsArticleData.class, ids);
        return "";
    }

    @Override
    public String save(CmsArticle object) throws Exception {
        return null;
    }
}
