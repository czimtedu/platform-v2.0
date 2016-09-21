/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.sys.action;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.platform.framework.common.BaseAction;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.Param;
import com.platform.modules.sys.bean.SysArea;
import com.platform.modules.sys.service.AreaService;
import com.platform.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 区域Action
 *
 * @author lufengc
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaAction extends BaseAction<SysArea> {

    @Autowired
    private AreaService areaService;


    @Override
    @ModelAttribute
    protected SysArea get(@RequestParam(required = false) String id) throws Exception {
        if (StringUtils.isNotEmpty(id)) {
            return areaService.get(id);
        } else {
            return new SysArea();
        }
    }

    @Override
    @RequestMapping(value = {"list", ""})
    @RequiresPermissions("sys:area:view")
    protected String list(Model model, SysArea object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.addAttribute("list", UserUtils.getAreaList());
        return "modules/sys/areaList";
    }

    @Override
    @RequestMapping(value = "form")
    @RequiresPermissions("sys:area:view")
    protected String form(Model model, SysArea area) throws Exception {
        SysArea parent = areaService.get(area.getParentId());
        if (parent != null) {
            area.setParentName(parent.getName());
        }
        model.addAttribute("area", area);
        return "modules/sys/areaForm";
    }

    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("sys:area:edit")
    protected String save(Model model, SysArea area, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, area)) {
            return form(model, area);
        }
        areaService.save(area);
        addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
        return "redirect:" + adminPath + "/sys/area/";
    }

    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("sys:area:edit")
    protected String delete(Model model, SysArea object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        areaService.delete(param.getIds());
        addMessage(redirectAttributes, "删除区域成功");
        return "redirect:" + adminPath + "/sys/area/";
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        try {
            List<SysArea> list = UserUtils.getAreaList();
            for (SysArea e : list) {
                if (StringUtils.isBlank(extId)
                        || (!extId.equals(e.getId()) && !e.getParentIds().contains("," + extId + ","))) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("id", e.getId());
                    map.put("pId", e.getParentId());
                    map.put("name", e.getName());
                    mapList.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapList;
    }
}
