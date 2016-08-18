/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.bean;

import java.io.Serializable;

/**
 * 系统日志bean
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
public class CmsArticleData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;		// 编号
    private String content;	// 内容
    private String copyfrom;// 来源
    private String relation;// 相关文章
    private Integer allowComment;// 是否允许评论

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCopyfrom() {
        return copyfrom;
    }

    public void setCopyfrom(String copyfrom) {
        this.copyfrom = copyfrom;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Integer getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }

}
