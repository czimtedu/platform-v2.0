/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.action;

import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import com.platform.framework.util.StringUtils;
import com.platform.modules.cms.bean.CmsComment;
import com.platform.modules.cms.service.CommentService;
import com.platform.modules.sys.bean.Param;
import com.platform.modules.sys.utils.DictUtils;
import com.platform.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 站点
 *
 * @author lufengc
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/comment")
public class CommentAction extends BaseAction<CmsComment> {

    @Autowired
    private CommentService commentService;

    /**
     * 数据绑定
     *
     * @param id ID
     * @return CmsComment
     * @throws Exception
     */
    @Override
    @ModelAttribute
    public CmsComment get(@RequestParam(required = false) String id) throws Exception {
        CmsComment cmsComment;
        if (StringUtils.isNotEmpty(id)) {
            cmsComment = commentService.get(id);
        } else {
            cmsComment = new CmsComment();
        }
        return cmsComment;
    }

    /**
     * 列表
     *
     * @param model    Model
     * @param object   object
     * @param request  request
     * @param response @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = {"list", ""})
    @RequiresPermissions("cms:comment:view")
    public String list(Model model, CmsComment object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("orderBy", "create_date desc");
        Page<CmsComment> page = commentService.getPage(new Page<CmsComment>(request, response), object, "");
        model.addAttribute("page", page);
        return "modules/cms/commentList";
    }

    /**
     * 编辑视图
     *
     * @param model  Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "form")
    @RequiresPermissions("cms:comment:edit")
    public String form(Model model, CmsComment object) throws Exception {
        return "";
    }

    /**
     * 保存
     *
     * @param model  Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("cms:comment:edit")
    protected String save(Model model, CmsComment object, RedirectAttributes redirectAttributes) throws Exception {
        if (beanValidator(redirectAttributes, object)) {
            if (object.getAuditUserId() == null) {
                object.setAuditUserId(UserUtils.getUserId());
                object.setAuditDate(new Date());
            }
            commentService.save(object);
            addMessage(redirectAttributes, DictUtils.getDictLabel(object.getDelFlag(), "cms_status", "保存")
                    + "评论'" + StringUtils.abbr(StringUtils.replaceHtml(object.getContent()), 50) + "'成功");
        }
        addMessage(redirectAttributes, "保存成功");
        return "redirect:" + adminPath + "/cms/comment/list?repage";
    }

    /**
     * 删除
     *
     * @param model  Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("cms:comment:edit")
    public String delete(Model model, CmsComment object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        commentService.delete(object.getId());
        addMessage(redirectAttributes, "删除成功");
        return "redirect:" + adminPath + "/cms/comment/list?repage";
    }

}
