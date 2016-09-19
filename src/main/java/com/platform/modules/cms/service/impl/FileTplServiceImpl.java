/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.cms.service.impl;

import com.platform.framework.common.BaseServiceImpl;
import com.platform.modules.cms.bean.CmsFileTpl;
import com.platform.modules.cms.service.FileTplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件模版Service实现类
 *
 * @author lufengc
 * @version 2016-09-12
 */
@Service
public class FileTplServiceImpl extends BaseServiceImpl<CmsFileTpl> implements FileTplService {

    @Autowired
    ServletContext context;

    @Override
    public List<String> getNameListByPrefix(String path) {
        List<CmsFileTpl> list = getListByPath(path, false);
        List<String> result = new ArrayList<>(list.size());
        for (CmsFileTpl tpl : list) {
            result.add(tpl.getName());
        }
        return result;
    }

    @Override
    public List<CmsFileTpl> getListByPath(String path, boolean directory) {
        File f = new File(context.getRealPath(path));
        if (f.exists()) {
            File[] files = f.listFiles();
            if (files != null) {
                List<CmsFileTpl> list = new ArrayList<>();
                for (File file : files) {
                    if (file.isFile() || directory)
                        list.add(new CmsFileTpl(file, context.getRealPath("")));
                }
                return list;
            } else {
                return new ArrayList<>(0);
            }
        } else {
            return new ArrayList<>(0);
        }
    }

    @Override
    public List<CmsFileTpl> getListForEdit(String path) {
        List<CmsFileTpl> list = getListByPath(path, true);
        List<CmsFileTpl> result = new ArrayList<>();
        result.add(new CmsFileTpl(new File(context.getRealPath(path)), context.getRealPath("")));
        getAllDirectory(result, list);
        return result;
    }

    @Override
    public void getAllDirectory(List<CmsFileTpl> result, List<CmsFileTpl> list) {
        for (CmsFileTpl tpl : list) {
            result.add(tpl);
            if (tpl.isDirectory()) {
                getAllDirectory(result, getListByPath(tpl.getName(), true));
            }
        }
    }

    @Override
    public CmsFileTpl getFileTpl(String name) {
        File f = new File(context.getRealPath(name));
        if (f.exists()) {
            return new CmsFileTpl(f, "");
        } else {
            return null;
        }
    }

    @Override
    public String save(CmsFileTpl object) throws Exception {
        return null;
    }
}
