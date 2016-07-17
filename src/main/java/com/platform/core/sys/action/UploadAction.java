/*
 * Copyright &copy; <a href="https://www.bjldwx.cn">bjldwx</a> All rights reserved.
 */

package com.platform.core.sys.action;

import com.alibaba.fastjson.JSONObject;
import com.platform.framework.common.SysConfigManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传action
 *
 * @author lufengcheng
 * @date 2016-01-15 09:56:22
 */
@Controller
@RequestMapping(value = "${adminPath}")
public class UploadAction {

    private static final String ALLOW_TYPE_IMG = "jpg|gif|jpeg|png|bmp";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 上传图片
     *
     * @param upload 上传文件
     * @param request HttpServletRequest
     * @throws IOException
     * @return json
     */
    @ResponseBody
    @RequestMapping("/uploadImage")
    public String uploadImage(@RequestParam(value = "upload", required = false) MultipartFile upload,
                            HttpServletRequest request) throws IOException {
        JSONObject jsonObject = new JSONObject();
        String fileName = "";
        int status = -1;
        try {
            if (upload != null) {
                String uploadFileName = request.getParameter("fileName");
                int index = uploadFileName.lastIndexOf(".");
                String filetype = "";
                if (index > -1) {
                    filetype = uploadFileName.substring(index + 1);
                }
                // 验证扩展名
                boolean allowUpload = false;
                if (!"".equals(filetype)) {
                    filetype = filetype.toLowerCase();
                    if (ALLOW_TYPE_IMG.contains(filetype)) {
                        allowUpload = true;
                    }
                }
                if (allowUpload) {
                    String path = SysConfigManager.getFileUploadPath();
                    fileName = simpleDateFormat.format(new Date())
                            + "/" + UUID.randomUUID().toString().replace("-", "") + "." + filetype;
                    File file = new File(path + fileName);
                    if (!file.exists()) {
                        boolean mkdirs = file.mkdirs();
                        if(mkdirs){
                            upload.transferTo(file);
                            status = 0;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("status", status);
        jsonObject.put("fileName", fileName);
        return jsonObject.toJSONString();
    }


}