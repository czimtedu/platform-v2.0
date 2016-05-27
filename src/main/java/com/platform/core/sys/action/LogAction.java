/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.action;

import com.platform.core.sys.bean.Param;
import com.platform.core.sys.bean.SysLog;
import com.platform.core.sys.service.LogService;
import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
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

/**
 * 日志action
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogAction extends BaseAction<SysLog> {

    @Autowired
    private LogService logService;

    @Override
    @ModelAttribute
    public SysLog get(@RequestParam(required = false) String id) throws Exception {
        if (id != null) {
            return logService.get(SysLog.class, id);
        } else {
            return new SysLog();
        }
    }

    /**
     * 列表页面
     *
     * @param model    model
     * @param object   object
     * @param request HttpServletRequest
     * @param response @return String
     * @throws Exception
     */
    @Override
    @RequestMapping(value = {"list", ""})
    public String list(Model model, SysLog object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<SysLog> page = logService.getPage(new Page<SysLog>(request, response), object, "");
        model.addAttribute("page", page);
        return "sys/logList";
    }

    /**
     * 表单输入页面
     *
     * @param model  model
     * @param object object
     * @return String
     * @throws Exception
     */
    @Override
    public String form(Model model, SysLog object) throws Exception {
        return null;
    }

    /**
     * 保存
     *
     * @param model  model
     * @param object object
     * @return String
     * @throws Exception
     */
    @Override
    public String save(Model model, SysLog object, RedirectAttributes redirectAttributes) throws Exception {
        return null;
    }

    /**
     * 删除
     *
     * @param model  model
     * @param object object
     * @return String
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("sys:log:del")
    public String delete(Model model, SysLog object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        logService.delete(param.getIds());
        addMessage(redirectAttributes, "删除成功");
        return "redirect:" + adminPath + "/sys/log/list?repage";
    }

    /**
     * 清空
     */
    @RequiresPermissions("sys:log:del")
    @RequestMapping(value = "empty")
    public String empty(RedirectAttributes redirectAttributes) {
        logService.empty();
        addMessage(redirectAttributes, "清空日志成功");
        return "redirect:"+ adminPath +"/sys/log/?repage";
    }

}
