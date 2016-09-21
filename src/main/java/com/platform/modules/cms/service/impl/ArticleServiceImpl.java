/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.service.impl;

import com.google.common.collect.Lists;
import com.platform.framework.cache.JedisUtils;
import com.platform.framework.common.Page;
import com.platform.framework.common.PropertyFilter;
import com.platform.framework.util.StringUtils;
import com.platform.modules.cms.bean.CmsArticle;
import com.platform.modules.cms.bean.CmsArticleData;
import com.platform.modules.cms.service.ArticleService;
import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.Encodes;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 文章Service实现类
 *
 * @author lufengc
 * @version 2016-09-12
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<CmsArticle> implements ArticleService {

    @Autowired
    private MybatisDao mybatisDao;

    /**
     * 更新过期的权重，间隔时间为6个小时
     */
    @Override
    public void updateWeight() {
        // 更新过期的权重，间隔为“6”个小时
        Date updateExpiredWeightDate = (Date) JedisUtils.getObject("updateExpiredWeightDateByArticle");
        if (updateExpiredWeightDate == null || updateExpiredWeightDate.getTime() < new Date().getTime()) {
            String sql = "update cms_article SET weight = 0 WHERE weight > 0 AND weight_date < CURDATE()";
            mybatisDao.updateBySql(sql, null);
            JedisUtils.setObject("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6), 0);
        }
    }

    /**
     * 保存
     *
     * @param object object
     * @throws Exception
     */
    @Override
    public String save(CmsArticle object, CmsArticleData articleData) throws Exception {
        if (StringUtils.isNotBlank(object.getViewConfig())){
            object.setViewConfig(StringEscapeUtils.unescapeHtml4(object.getViewConfig()));
        }
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
     *
     * @param id 文章内容ID
     * @return 文章内容
     */
    @Override
    public CmsArticleData getArticleData(String id) {
        CmsArticleData data;
        List<CmsArticleData> list = mybatisDao.selectListByIds(CmsArticleData.class, id);
        if (list != null && list.size() > 0) {
            data = list.get(0);
        } else {
            data = new CmsArticleData();
        }
        return data;
    }

    /**
     * 更新文章信息（点击数）
     *
     * @param object 文章列表对象
     */
    @Override
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
    public int delete(String ids) throws Exception {
        mybatisDao.deleteByIds(CmsArticle.class, ids);
        mybatisDao.deleteByIds(CmsArticleData.class, ids);
        return 1;
    }

    @Override
    public String save(CmsArticle object) throws Exception {
        return null;
    }

    /**
     * 通过编号获取内容标题
     * @return new Object[]{栏目Id,文章Id,文章标题}
     */
    @Override
    public List<Object[]> getByIds(String ids) {
        List<Object[]> list = Lists.newArrayList();
        List<CmsArticle> articles = mybatisDao.selectListByIds(CmsArticle.class, ids);
        for (CmsArticle e : articles) {
            list.add(new Object[]{e.getCategoryId(),e.getId(), StringUtils.abbr(e.getTitle(),50)});
        }
        return list;
    }

}
