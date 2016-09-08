/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.modules.sys.service.impl;

import com.platform.framework.common.BaseServiceImpl;
import com.platform.framework.common.MybatisDao;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.ObjectUtils;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.bean.SysArea;
import com.platform.modules.sys.bean.SysOffice;
import com.platform.modules.sys.bean.SysPermission;
import com.platform.modules.sys.service.AreaService;
import com.platform.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 区域Service
 *
 * @author jeeplus
 * @version 2014-05-16
 */
@Service
public class AreaServiceImpl extends BaseServiceImpl<SysArea> implements AreaService {

    @Autowired
    private MybatisDao mybatisDao;

    /**
     * 保存或更新操作
     *
     * @param object Object
     * @return 主键ID
     * @throws Exception
     */
    @Override
    public String save(SysArea object) throws Exception {
        String oldParentIds = object.getParentIds();
        SysArea parent = get(object.getParentId());
        if (parent != null) {
            object.setParentIds(parent.getParentIds() + parent.getId() + ",");
        } else {
            object.setParentIds(SysPermission.getRootId().toString());
        }
        if (StringUtils.isNotEmpty(object.getId())) {
            mybatisDao.update(object);
            // 更新子节点parentIds
            List<SysArea> list = mybatisDao.selectListByConditions(SysArea.class, "parent_id like '%," + object.getId() + ",%'");
            if (list != null && list.size() > 0) {
                for (SysArea p : list) {
                    p.setParentIds(p.getParentIds().replace(oldParentIds, object.getParentIds()));
                    mybatisDao.update(p);
                }
            }
        } else {
            List<SysArea> areaList = UserUtils.getAreaList();
            int sort = 0;
            for (SysArea area : areaList) {
                if(area.getSort() > sort){
                    sort = area.getSort();
                }
            }
            object.setSort(sort);
            object.setId(Encodes.uuid());
            mybatisDao.insert(object);
        }
        UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
        return object.getId();
    }

    /**
     * 删除操作
     *
     * @param ids 删除的ID
     * @return String
     * @throws Exception
     */
    @Override
    public int delete(String ids) throws Exception {
        super.delete(ids);
        UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
        return 1;
    }
}
