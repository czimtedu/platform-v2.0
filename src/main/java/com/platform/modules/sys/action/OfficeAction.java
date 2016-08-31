/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.sys.action;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Global;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.Param;
import com.platform.modules.sys.bean.SysOffice;
import com.platform.modules.sys.service.OfficeService;
import com.platform.modules.sys.utils.DictUtils;
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
 * 机构Controller
 *
 * @author jeeplus
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeAction extends BaseAction<SysOffice> {

    @Autowired
    private OfficeService officeService;


    @Override
    @ModelAttribute("office")
    protected SysOffice get(@RequestParam(required = false) String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            return officeService.get(SysOffice.class, id);
        } else {
            return new SysOffice();
        }
    }

    @RequestMapping(value = {""})
    public String index(SysOffice office, Model model) {
        return "modules/sys/officeIndex";
    }

    @Override
    @RequestMapping(value = {"list"})
    protected String list(Model model, SysOffice office, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (office == null || office.getParentIds() == null) {
            model.addAttribute("list", officeService.getList(false));
        } else {
            model.addAttribute("list", officeService.getByParentId(office.getId()));
        }
        return "modules/sys/officeList";
    }

    @Override
    @RequestMapping(value = "form")
    protected String form(Model model, SysOffice office) throws Exception {
        /*SysUser user = UserUtils.getUser();
        if (office.getParent()==null || office.getParent().getId()==null){
            office.setParent(user.getOffice());
        }
        office.setParent(officeService.get(office.getParent().getId()));
        if (office.getArea()==null){
            office.setArea(user.getOffice().getArea());
        }
        // 自动获取排序号
        if (StringUtils.isBlank(office.getId())&&office.getParent()!=null){
            int size = 0;
            List<Office> list = officeService.findAll();
            for (int i=0; i<list.size(); i++){
                Office e = list.get(i);
                if (e.getParent()!=null && e.getParent().getId()!=null
                        && e.getParent().getId().equals(office.getParent().getId())){
                    size++;
                }
            }
            office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size+1 : 1), 3, "0"));
        }*/
        model.addAttribute("office", office);
        return "modules/sys/officeForm";
    }

    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("sys:office:edit")
    protected String save(Model model, SysOffice office, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, office)) {
            return form(model, office);
        }
        officeService.save(office);

        if (office.getChildDeptList() != null) {
            SysOffice childOffice = null;
            for (String id : office.getChildDeptList()) {
                childOffice = new SysOffice();
                childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
                childOffice.setParentId(office.getId());
                childOffice.setAreaId(office.getAreaId());
                childOffice.setType(2);
                childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade()) + 1));
                childOffice.setUseable(Global.YES);
                officeService.save(childOffice);
            }
        }

        addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
        String id = "0".equals(office.getParentId()) ? "" : office.getParentId();
        return "redirect:" + adminPath + "/sys/office/list?id=" + id + "&parentIds=" + office.getParentIds();
    }

    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("sys:office:edit")
    protected String delete(Model model, SysOffice office, Param param, RedirectAttributes redirectAttributes) throws Exception {
        officeService.delete(param.getIds());
        addMessage(redirectAttributes, "删除机构成功");
        return "redirect:" + adminPath + "/sys/office/list?id=" + office.getParentId() + "&parentIds=" + office.getParentIds();
    }

    /**
     * 获取机构JSON数据。
     * @param extId 排除的ID
     * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
     * @param grade 显示级别
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
                                              @RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<SysOffice> list = officeService.getList(isAll);
        for (SysOffice e : list) {
            if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
                    && (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
                    && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
                    && Global.YES.equals(e.getUseable())) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("pIds", e.getParentIds());
                map.put("name", e.getName());
                if (type != null && "3".equals(type)) {
                    map.put("isParent", true);
                }
                mapList.add(map);
            }
        }
        return mapList;
    }
}
