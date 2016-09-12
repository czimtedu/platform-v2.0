/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */
package com.platform.modules.sys.service;

import com.platform.framework.common.BaseService;
import com.platform.modules.sys.bean.SysOffice;

import java.util.List;

/**
 * 机构Service
 *
 * @author jeeplus
 * @version 2014-05-16
 */
public interface OfficeService extends BaseService<SysOffice> {


    /**
     * 获取结构列表
     *
     * @param isAll true：所有机构数据，false：只查询当前用户拥有的机构数据
     * @return List<SysOffice>
     */
    List<SysOffice> getList(Boolean isAll);

    /**
     * 根据用户ID获取机构列表
     *
     * @param userId 用户id
     * @return List<SysOffice>
     */
    List<SysOffice> getByUserId(int userId);

    /**
     * 根据parentId查询该机构下所有的子列表数据
     *
     * @param parentIds parentIds
     * @return List<SysOffice>
     */
    List<SysOffice> getByParentIdsLike(String parentIds);

    /**
     * 获取某个机构下的所有子机构
     *
     * @param parentId 父id
     * @return List<SysOffice>
     * @throws Exception
     */
    List<SysOffice> getByParentId(String parentId) throws Exception;

    /**
     * 保存
     *
     * @param object SysOffice
     * @return 主键id
     * @throws Exception
     */
    String save(SysOffice object) throws Exception;
}
