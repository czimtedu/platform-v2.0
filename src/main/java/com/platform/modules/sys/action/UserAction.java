/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import com.platform.framework.common.PropertyFilter;
import com.platform.framework.common.SysConfigManager;
import com.platform.framework.mapper.AjaxJson;
import com.platform.framework.util.*;
import com.platform.framework.util.excel.ExportExcel;
import com.platform.framework.util.excel.ImportExcel;
import com.platform.modules.sys.bean.Param;
import com.platform.modules.sys.bean.SysOffice;
import com.platform.modules.sys.bean.SysRole;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.service.OfficeService;
import com.platform.modules.sys.service.RoleService;
import com.platform.modules.sys.service.UserService;
import com.platform.modules.sys.utils.UserUtils;
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
import java.util.*;

/**
 * 用户管理
 *
 * @author lufengc
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserAction extends BaseAction<SysUser> {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OfficeService officeService;

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
        if (StringUtils.isNotEmpty(id)) {
            sysUser = userService.get(id);
            if (sysUser != null) {
                List<SysRole> roleList = roleService.getByUserId(StringUtils.toInteger(id));
                sysUser.setRoleList(roleList);
                List<Integer> roleIdList = Lists.newArrayList();
                for (SysRole role : roleList) {
                    roleIdList.add(role.getId());
                }
                sysUser.setRoleIdList(roleIdList);
                SysOffice office = officeService.get(sysUser.getOfficeId());
                sysUser.setOfficeName(office.getName());
                SysOffice company = officeService.get(sysUser.getCompanyId());
                sysUser.setCompanyName(company.getName());
            } else {
                sysUser = new SysUser();
            }
        } else {
            sysUser = new SysUser();
        }
        return sysUser;
    }

    /**
     * 用户首页
     *
     * @param office SysOffice
     * @param model  Model
     * @return ModelAndView
     */
    @RequestMapping(value = {""})
    @RequiresPermissions("sys:user:view")
    public String index(SysOffice office, Model model) {
        return "modules/sys/userIndex";
    }

    /**
     * 用户列表
     *
     * @return ModelAndView
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "list")
    @RequiresPermissions("sys:user:view")
    public String list(Model model, SysUser object, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //String conditions = "status <> 0";
        //根据当前组织机构ID查询数据
        List<PropertyFilter> propertyFilters = new ArrayList<>();
        if (StringUtils.isNotEmpty(object.getOfficeId())) {
            String ids = object.getOfficeId();
            List<SysOffice> offices = officeService.getByParentIdsLike(object.getOfficeId());
            if (offices != null && offices.size() > 0) {
                for (SysOffice office : offices) {
                    ids += "," + office.getId();
                }
            }
            //conditions += " AND office_id in(" + StringUtils.idsToString(ids) + ")";
            PropertyFilter propertyFilter = new PropertyFilter();
            propertyFilter.setMatchType(PropertyFilter.MatchType.IN);
            propertyFilter.setPropertyName("officeId");
            propertyFilter.setMatchValue(StringUtils.idsToString(ids));
            propertyFilters.add(propertyFilter);
        }
        object.setOfficeId(null);
        Page<SysUser> page = userService.getPage(new Page<SysUser>(request, response), object, propertyFilters, "status <> 0");
        model.addAttribute("page", page);
        return "modules/sys/userList";
    }

    /**
     * 表单输入页面
     *
     * @return ModelAndView
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "form")
    @RequiresPermissions("sys:user:view")
    public String form(Model model, SysUser user) throws Exception {
        return "modules/sys/userForm";
    }

    /**
     * 保存
     *
     * @return ModelAndView
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
     * @return ModelAndView
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("sys:user:edit")
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
     * @param user               SysUser
     * @param request            HttpServletRequest
     * @param response           HttpServletResponse
     * @param redirectAttributes RedirectAttributes
     * @return ModelAndView
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(SysUser user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<SysUser> page = userService.getPage(new Page<SysUser>(request, response), user, null, "status <> 0");
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
     * @param file               MultipartFile
     * @param redirectAttributes RedirectAttributes
     * @return ModelAndView
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
     * @param response           HttpServletResponse
     * @param redirectAttributes RedirectAttributes
     * @return ModelAndView
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
     * @param user  SysUser
     * @param model Model
     * @return ModelAndView
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
            return "modules/sys/userInfo";
        }
        model.addAttribute("user", currentUser);
        return "modules/sys/userImageEdit";
    }

    /**
     * 用户头像显示编辑保存
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "imageUpload")
    public String imageUpload(HttpServletRequest request, MultipartFile file) throws Exception {
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
        return "modules/sys/userImageEdit";
    }

    /**
     * 当前用户信息显示
     *
     * @param model Model
     * @return ModelAndView
     */
    @RequestMapping(value = "info")
    @RequiresPermissions("sys:user:view")
    public String info(Model model) {
        SysUser currentUser = UserUtils.getUser();
        model.addAttribute("sysUser", currentUser);
        return "modules/sys/userInfo";
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String officeId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<SysUser> list = userService.getUserByOfficeId(officeId);
        if (list != null && list.size() > 0) {
            for (SysUser e : list) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", "u_" + e.getId());
                map.put("pId", officeId);
                map.put("name", StringUtils.replace(e.getRealName(), " ", ""));
                mapList.add(map);
            }
        }
        return mapList;
    }

    /***************************
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
