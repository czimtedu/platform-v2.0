/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.action;

import com.platform.framework.common.PropertyFilter;
import com.platform.framework.security.UserUtils;
import com.platform.framework.util.BeanToTable;
import com.platform.framework.util.DateUtils;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.Param;
import com.platform.modules.sys.bean.SysLog;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.service.LogService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        ArrayList<PropertyFilter> propertyFilters = new ArrayList<>();
        String conditions = "";

        //用户名称查询条件
        String createName = object.getCreateName();
        if(StringUtils.isNotEmpty(createName)){
            List<SysUser> userList = UserUtils.getByRealName(createName);
            String ids = "";
            for (SysUser sysUser : userList) {
                if(StringUtils.isEmpty(ids)) {
                    ids += sysUser.getId();
                } else {
                    ids += sysUser.getId() + ",";
                }
            }
            conditions = "create_by in(" + ids + ")";
        }

        //时间范围查询条件
        String createTimeRange = object.getCreateTimeRange();
        if(StringUtils.isNotEmpty(createTimeRange)){
            String[] split = createTimeRange.split(" - ");
            String start = DateUtils.formatDate(DateUtils.parseDate(split[0])) + " 00:00:00";
            String end = DateUtils.formatDate(DateUtils.parseDate(split[1])) + " 23:59:59";
            if(StringUtils.isNotEmpty(conditions)){
                conditions += " and ";
            }
            conditions += "create_time >= '" + start + "' and create_time <= '" + end + "'";
        }
        Page<SysLog> page = logService.getPage(new Page<SysLog>(request, response), object, null, conditions);
        model.addAttribute("page", page);
        return "modules/sys/logList";
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
