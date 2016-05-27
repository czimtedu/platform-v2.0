/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.core.sys.action;

import com.platform.core.sys.bean.Param;
import com.platform.core.sys.bean.SysDict;
import com.platform.core.sys.service.DictService;
import com.platform.framework.common.BaseAction;
import com.platform.framework.common.Page;
import com.platform.framework.util.DictUtils;
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
 * 字典Action
 *
 * @author lufengc
 * @date 2016/1/16 14:01
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class dictAction extends BaseAction<SysDict> {

    @Autowired
    private DictService dictService;

    /**
     * 数据绑定
     *
     * @param id id
     * @return SysDict
     * @throws Exception
     */
    @Override
    @ModelAttribute
    protected SysDict get(@RequestParam(required = false) String id) throws Exception {
        SysDict sysDict;
        if (id != null) {
            sysDict = dictService.get(SysDict.class, id);
        } else {
            sysDict = new SysDict();
        }
        return sysDict;
    }

    /**
     * 字典列表页
     *
     * @return view
     * @throws Exception
     */
    @Override
    @RequestMapping(value = {"list", ""})
    protected String list(Model model, SysDict object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<SysDict> page = dictService.getPage(new Page<SysDict>(request, response), object, "");
        model.addAttribute("page", page);
        return "sys/dictList";
    }

    /**
     * 表单输入页面
     *
     * @return String
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "form")
    protected String form(Model model, SysDict sysDict) throws Exception {
        if(sysDict.getActionType() != null && sysDict.getActionType() == 2){
            sysDict.setId(null);
            sysDict.setLabel(null);
            sysDict.setValue(null);
            List<SysDict> dictList = DictUtils.getDictList(sysDict.getEnName());
            Integer maxSortId = sysDict.getSortId();
            for (SysDict dict : dictList) {
                if(dict.getSortId() > maxSortId){
                    maxSortId = dict.getSortId();
                }
            }
            sysDict.setSortId(maxSortId + 10);
        }
        return "sys/dictForm";
    }

    /**
     * 保存
     *
     * @return String
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "save")
    @RequiresPermissions("sys:dict:edit")
    protected String save(Model model, SysDict object, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, object)) {
            return form(model, object);
        }
        dictService.save(object);
        addMessage(redirectAttributes, "保存字典成功");
        return "redirect:" + adminPath + "/sys/dict/list?repage";
    }

    /**
     * 删除
     *
     * @return String
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "delete")
    @RequiresPermissions("sys:dict:delete")
    protected String delete(Model model, SysDict object, Param param, RedirectAttributes redirectAttributes) throws Exception {
        dictService.delete(param.getIds());
        addMessage(redirectAttributes, "删除字典成功");
        return "redirect:" + adminPath + "/sys/dict/list?repage";
    }
}
