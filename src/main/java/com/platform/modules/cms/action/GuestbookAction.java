/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.action;

import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import com.platform.framework.util.StringUtils;
import com.platform.modules.cms.bean.CmsGuestbook;
import com.platform.modules.cms.service.GuestbookService;
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
 * 留言
 *
 * @author lufengc
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/guestbook")
public class GuestbookAction extends BaseAction<CmsGuestbook> {

    @Autowired
    private GuestbookService guestbookService;

    /**
     * 数据绑定
     *
     * @param id ID
     * @return CmsGuestbook
     * @throws Exception
     */
    @Override
    @ModelAttribute
    public CmsGuestbook get(@RequestParam(required = false) String id) throws Exception {
        if (StringUtils.isNotEmpty(id)) {
            return guestbookService.get(id);
        } else {
            return new CmsGuestbook();
        }
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
    @RequiresPermissions("cms:guestbook:view")
    public String list(Model model, CmsGuestbook object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("orderBy", "create_date desc");
        Page<CmsGuestbook> page = guestbookService.getPage(new Page<CmsGuestbook>(request, response), object, "");
        model.addAttribute("page", page);
        return "modules/cms/guestbookList";
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
    @RequiresPermissions("cms:guestbook:edit")
    public String form(Model model, CmsGuestbook object) throws Exception {
        return "modules/cms/guestbookForm";
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
    @RequiresPermissions("cms:guestbook:edit")
    protected String save(Model model, CmsGuestbook object, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, object)){
            return form(model, object);
        }
        if (object.getReUserId() == null){
            object.setReUserId(UserUtils.getUserId());
            object.setReDate(new Date());
        }
        guestbookService.save(object);
        addMessage(redirectAttributes, DictUtils.getDictLabel(object.getDelFlag(), "cms_del_flag", "保存")
                +"留言'" + object.getName() + "'成功");
        return "redirect:" + adminPath + "/cms/guestbook/?repage&status=2";
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
    @RequiresPermissions("cms:guestbook:edit")
    public String delete(Model model, CmsGuestbook object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        guestbookService.delete(object.getId());
        addMessage(redirectAttributes, "删除成功");
        return "redirect:" + adminPath + "/cms/guestbook/list?repage&status=2";
    }

}
