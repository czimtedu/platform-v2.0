/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.action;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.platform.framework.common.BaseAction;
import com.platform.framework.util.Collections3;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.Param;
import com.platform.modules.sys.bean.SysPermission;
import com.platform.modules.sys.service.PermissionService;
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
import java.util.*;

/**
 * 权限管理
 *
 * @author lufengc
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/permission")
public class PermissionAction extends BaseAction<SysPermission> {

    @Autowired
    private PermissionService permissionService;

    /**
     * 数据绑定
     *
     * @param id ID
     * @return SysPermission
     * @throws Exception
     */
    @Override
    @ModelAttribute
    public SysPermission get(@RequestParam(required = false) String id) throws Exception {
        if (StringUtils.isNotEmpty(id)) {
            return permissionService.get(id);
        } else {
            return new SysPermission();
        }
    }

    /**
     * 权限列表页面
     *
     * @param model    Model
     * @param object   object
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception
     */
    @Override
    @RequestMapping(value = {"list", ""})
    @RequiresPermissions("sys:permission:view")
    public String list(Model model, SysPermission object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<SysPermission> sourcelist = UserUtils.getMenuList();
        List<SysPermission> list = Lists.newArrayList();
        Collections.sort(sourcelist, new Comparator<SysPermission>(){
            public int compare(SysPermission o1, SysPermission o2) {
                // 按sortId升序排序
                if(o1.getSortId() > o2.getSortId()){
                    return 1;
                } else if(Objects.equals(o1.getSortId(), o2.getSortId())){
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        SysPermission.sortList(list, sourcelist, SysPermission.getRootId(), true);
        model.addAttribute("list", list);
        return "modules/sys/permissionList";
    }

    /**
     * 权限编辑视图
     *
     * @param model  Model
     * @param object object
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "form")
    @RequiresPermissions("sys:permission:view")
    public String form(Model model, SysPermission object) throws Exception {
        if (object.getParentId() == null) {
            object.setParentId(SysPermission.getRootId());
        }
        if (object.getId() == null) {
            List<SysPermission> list = Lists.newArrayList();
            List<SysPermission> sourcelist = permissionService.getList(new SysPermission());
            SysPermission.sortList(list, sourcelist, object.getParentId(), false);
            if (list.size() > 0) {
                if (object.getParentId() == 0) {
                    object.setSortId(list.get(list.size() - 1).getSortId() + 10000);
                } else {
                    object.setSortId(list.get(list.size() - 1).getSortId() + 10);
                }
            } else {
                object.setSortId(permissionService.get(object.getParentId().toString()).getSortId() + 10);
            }
        }
        SysPermission parent = permissionService.get(object.getParentId().toString());
        if (parent != null) {
            object.setParentName(parent.getPermissionName());
        } else {
            object.setParentName("功能菜单");
        }
        model.addAttribute("sysPermission", object);
        return "modules/sys/permissionForm";
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
    @RequiresPermissions("sys:permission:edit")
    public String save(Model model, SysPermission object, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, object)) {
            return form(model, object);
        }
        permissionService.save(object);
        addMessage(redirectAttributes, "保存成功！");
        return "redirect:" + adminPath + "/sys/permission/";
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
    @RequiresPermissions("sys:permission:edit")
    public String delete(Model model, SysPermission object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        permissionService.delete(object.getId().toString());
        addMessage(redirectAttributes, "删除成功");
        return "redirect:" + adminPath + "/sys/permission/";
    }

    /**
     * 批量修改菜单排序
     */
    @RequestMapping(value = "updateSort")
    @RequiresPermissions("sys:permission:edit")
    public String updateSort(Integer[] ids, Integer[] sortIds, RedirectAttributes redirectAttributes) {
        permissionService.updatePermissionSort(ids, sortIds);
        addMessage(redirectAttributes, "保存菜单排序成功!");
        return "redirect:" + adminPath + "/sys/permission/";
    }

    /**
     * 权限树数据
     *
     * @param extId      id
     * @param isShowHide 是否可见
     * @return List
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) Integer extId,
                                              @RequestParam(required = false) Integer isShowHide) throws Exception {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<SysPermission> list = UserUtils.getMenuList();
        for (SysPermission bean : list) {
            if (extId == null || (!extId.equals(bean.getId()) && !bean.getParentIds().contains("," + extId + ","))) {
                if (isShowHide != null && isShowHide == 0 && bean.getIsShow() == 0) {
                    continue;
                }
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", bean.getId());
                map.put("pId", bean.getParentId());
                map.put("name", bean.getPermissionName());
                mapList.add(map);
            }
        }
        return mapList;
    }


}
