/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.sys.action;

import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.Param;
import com.platform.modules.sys.bean.SysPermission;
import com.platform.modules.sys.bean.SysRole;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.service.PermissionService;
import com.platform.modules.sys.service.RoleService;
import com.platform.modules.sys.service.UserService;
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
import java.util.List;

/**
 * 角色action
 *
 * @author lufengc
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleAction extends BaseAction<SysRole> {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;

    /**
     * 数据绑定
     *
     * @param id ID
     * @return SysRole
     * @throws Exception
     */
    @Override
    @ModelAttribute
    public SysRole get(@RequestParam(required = false) String id) throws Exception {
        SysRole sysRole;
        if (StringUtils.isNotEmpty(id)) {
            sysRole = roleService.get(id);
            StringBuilder permissionIds = new StringBuilder();
            List<SysPermission> permissionList = permissionService.getByRoleId(sysRole.getId());
            for (SysPermission sysPermission : permissionList) {
                if (permissionIds.length() == 0) {
                    permissionIds.append(sysPermission.getId());
                } else {
                    permissionIds.append(",").append(sysPermission.getId());
                }
            }
            sysRole.setPermissionIds(permissionIds.toString());
        } else {
            sysRole = new SysRole();
        }
        return sysRole;
    }

    /**
     * 角色列表
     *
     * @param model    Model
     * @param object   object
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return ModelAndView
     * @throws Exception
     */
    @Override
    @RequestMapping(value = {"list", ""})
    @RequiresPermissions("sys:role:view")
    public String list(Model model, SysRole object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*Page<SysRole> page = roleService.getPage(new Page<SysRole>(request, response), object, "");
        model.addAttribute("page", page);*/
        model.addAttribute("roleList", UserUtils.getRoleList());
        return "modules/sys/roleList";
    }

    /**
     * 角色编辑视图
     *
     * @param model  Model
     * @param object object
     * @return ModelAndView
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "form")
    @RequiresPermissions("sys:role:view")
    public String form(Model model, SysRole object) throws Exception {
        return "modules/sys/roleForm";
    }

    /**
     * 保存角色信息
     *
     * @param model  Model
     * @param object object
     * @return ModelAndView
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("sys:role:edit")
    public String save(Model model, SysRole object, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, object)) {
            return form(model, object);
        }
        roleService.save(object);
        addMessage(redirectAttributes, "保存角色'" + object.getRoleName() + "'成功");
        return "redirect:" + adminPath + "/sys/role/list?repage";
    }

    /**
     * 删除角色信息
     *
     * @param model  Model
     * @param object object
     * @return ModelAndView
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("sys:role:edit")
    public String delete(Model model, SysRole object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        roleService.delete(param.getIds());
        addMessage(redirectAttributes, "删除角色成功");
        return "redirect:" + adminPath + "/sys/role/list?repage";
    }

    /**
     * 授权设置页面
     *
     * @param role  SysRole
     * @param model Model
     * @return ModelAndView
     * @throws Exception
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "auth")
    public String auth(SysRole role, Model model) throws Exception {
        model.addAttribute("permissionList", UserUtils.getMenuList());
        return "modules/sys/roleAuth";
    }

    /**
     * 角色分配页面
     *
     * @param role  SysRole
     * @param model Model
     * @return ModelAndView
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "assign")
    public String assign(SysRole role, Model model) {
        List<SysUser> userList = userService.getByRoleId(role.getId());
        model.addAttribute("userList", userList);
        return "modules/sys/roleAssign";
    }

    /**
     * 角色分配 -- 打开角色分配对话框
     *
     * @param role SysRole
     * @param model Model
     * @return ModelAndView
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "selectUser")
    public String selectUser(SysRole role, SysUser user, Model model, HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        user.setId(null);
        Page<SysUser> page = userService.getPage(new Page<SysUser>(request, response), user, "status <> 0");
        model.addAttribute("page", page);
        return "modules/sys/selectUser";
    }

    /**
     * 角色分配
     *
     * @param role SysRole
     * @param ids 用户ids
     * @param redirectAttributes RedirectAttributes
     * @return ModelAndView
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "assignRole")
    public String assignRole(SysRole role, String ids, RedirectAttributes redirectAttributes) {
        roleService.assignUserToRole(role, ids);
        addMessage(redirectAttributes, "已成功分配 " + ids.split(",").length + " 个用户");
        return "redirect:" + adminPath + "/sys/role/assign?id=" + role.getId();
    }

    /**
     * 角色分配 -- 从角色中移除用户
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @param redirectAttributes RedirectAttributes
     * @return ModelAndView
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "outRole")
    public String outRole(Long userId, Long roleId, RedirectAttributes redirectAttributes) throws Exception {
        SysUser user = userService.get(userId.toString());
        SysRole role = roleService.get(roleId.toString());
        if (UserUtils.getUser().getId().equals(userId.intValue())) {
            addMessage(redirectAttributes, "无法从角色【" + role.getRoleName() + "】中移除用户【" + user.getRealName() + "】自己！");
        } else {
            boolean flag = roleService.outUserInRole(role, user);
            if (!flag) {
                addMessage(redirectAttributes, "用户【" + user.getRealName() + "】从角色【" + role.getRoleName() + "】中移除失败！");
            } else {
                addMessage(redirectAttributes, "用户【" + user.getRealName() + "】从角色【" + role.getRoleName() + "】中移除成功！");
            }
        }
        return "redirect:" + adminPath + "/sys/role/assign?id=" + role.getId();
    }
}
