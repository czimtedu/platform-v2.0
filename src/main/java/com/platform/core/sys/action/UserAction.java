/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.platform.core.sys.bean.Param;
import com.platform.core.sys.bean.SysRole;
import com.platform.core.sys.bean.SysUser;
import com.platform.core.sys.service.RoleService;
import com.platform.core.sys.service.UserService;
import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import com.platform.framework.common.SysConfigManager;
import com.platform.framework.mapper.AjaxJson;
import com.platform.framework.security.UserUtils;
import com.platform.framework.util.*;
import com.platform.framework.util.excel.ExportExcel;
import com.platform.framework.util.excel.ImportExcel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户管理
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserAction extends BaseAction<SysUser> {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 数据绑定
     *
     * @param id ID
     * @return SysUser
     * @throws Exception
     */
    @Override
    @ModelAttribute
    public SysUser get(@RequestParam(required = false) String id) throws Exception {
        SysUser sysUser;
        if (id != null) {
            sysUser = userService.get(SysUser.class, id);
            if(sysUser != null) {
                List<SysRole> roleList = roleService.getByUserId(StringUtils.toInteger(id));
                List<Integer> roleIdList = Lists.newArrayList();
                for (SysRole sysRole : roleList) {
                    roleIdList.add(sysRole.getId());
                }
                sysUser.setRoleIdList(roleIdList);
            } else {
                sysUser = new SysUser();
            }
        } else {
            sysUser = new SysUser();
        }
        return sysUser;
    }

    /**
     * 用户列表
     *
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = {"list", ""})
    public String list(Model model, SysUser object, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Page<SysUser> page = userService.getPage(new Page<SysUser>(request, response), object, "status <> 3");
        model.addAttribute("page", page);
        return "sys/userList";
    }

    /**
     * 表单输入页面
     *
     * @return String
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "form")
    public String form(Model model, SysUser user) throws Exception {
        return "sys/userForm";
    }

    /**
     * 保存
     *
     * @return String
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("sys:user:edit")
    public String save(Model model, SysUser user, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, user)) {
            return form(model, user);
        }
        userService.save(user);
        // 清除当前用户缓存
        if (user.getUsername().equals(UserUtils.getUser().getUsername())) {
            UserUtils.clearCache();
        }
        addMessage(redirectAttributes, "保存用户'" + user.getUsername() + "'成功");
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 删除
     *
     * @return String
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("sys:user:del")
    public String delete(Model model, SysUser user, Param param, RedirectAttributes redirectAttributes) throws Exception {
        userService.delete(param.getIds());
        addMessage(redirectAttributes, "删除用户成功");
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 验证用户名是否有效
     *
     * @param oldUsername 用户id
     * @param username    用户名
     * @return String
     */
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "checkUsername")
    public String checkLoginName(String oldUsername, String username) {
        if (username != null && username.equals(oldUsername)) {
            return "true";
        } else if (username != null && userService.getByUsername(username) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 导出用户数据
     *
     * @param user
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(SysUser user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<SysUser> page = userService.getPage(new Page<SysUser>(request, response), user, "status <> 3");
            new ExportExcel("用户数据", SysUser.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 导入用户数据
     *
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel importExcel = new ImportExcel(file, 1, 0);
            List<SysUser> list = importExcel.getDataList(SysUser.class);
            for (SysUser user : list) {
                try {
                    if ("true".equals(checkLoginName("", user.getUsername()))) {
                        user.setPassword(Encodes.entryptPassword("123456"));
                        BeanValidators.validateWithException(validator, user);
                        userService.save(user);
                        successNum++;
                    } else {
                        failureMsg.append("<br/>登录名 " + user.getUsername() + " 已存在; ");
                        failureNum++;
                    }
                } catch (ConstraintViolationException ex) {
                    failureMsg.append("<br/>登录名 " + user.getUsername() + " 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList) {
                        failureMsg.append(message + "; ");
                        failureNum++;
                    }
                } catch (Exception ex) {
                    failureMsg.append("<br/>登录名 " + user.getUsername() + " 导入失败：" + ex.getMessage());
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条用户，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条用户" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入用户失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 下载导入用户数据模板
     *
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "用户数据导入模板.xlsx";
            List<SysUser> list = Lists.newArrayList();
            list.add(UserUtils.getUser());
            new ExportExcel("用户数据", SysUser.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 用户头像显示编辑保存
     *
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "imageEdit")
    public String imageEdit(SysUser user, boolean __ajax, HttpServletResponse response, Model model) {
        SysUser currentUser = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getRealName())) {
            if (user.getPhoto() != null) {
                currentUser.setPhoto(user.getPhoto());
            }
            userService.updateUserInfo(currentUser);
            if (__ajax) {//手机访问
                AjaxJson j = new AjaxJson();
                j.setSuccess(true);
                j.setMsg("修改个人头像成功!");
                return renderString(response, j);
            }
            model.addAttribute("message", "保存用户信息成功");
            return "sys/userInfo";
        }
        model.addAttribute("user", currentUser);
        return "sys/userImageEdit";
    }

    /**
     * 用户头像显示编辑保存
     *
     * @return
     * @throws Exception
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "imageUpload")
    public String imageUpload(HttpServletRequest request, MultipartFile file) throws Exception{
        SysUser currentUser = UserUtils.getUser();
        // 判断文件是否为空
        if (!file.isEmpty()) {
            String uploadFileName = file.getOriginalFilename();
            int index = uploadFileName.lastIndexOf(".");
            String filetype = "";
            if (index > -1) {
                filetype = uploadFileName.substring(index + 1);
            }
            // 文件保存路径
            String path = SysConfigManager.getFileUploadPath();
            String realPath = "userid_" + UserUtils.getPrincipal() + "/images/"
                    + simpleDateFormat.format(new Date()) + "/";
            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + filetype;
            // 转存文件
            FileUtils.createDirectory(path + realPath);
            file.transferTo(new File(path + realPath + fileName));
            currentUser.setPhoto(realPath + file.getOriginalFilename());
            userService.updateUserInfo(currentUser);
        }
        return "sys/userImageEdit";
    }

    /**
     * 当前用户信息显示
     * @param model
     * @return
     */
    @RequestMapping(value = "info")
    public String info(Model model) {
        SysUser currentUser = UserUtils.getUser();
        model.addAttribute("sysUser", currentUser);
        return "sys/userInfo";
    }

    /**************************
     * app api test
     ********************************/

    @ResponseBody
    @RequestMapping(value = "/appLogin", produces = "text/html;charset=UTF-8")
    public String appLogin(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        String username = request.getParameter("username");
        json.put("username", username);
        return json.toString();
    }

}
