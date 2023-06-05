package com.runjian.rbac.service;

import com.runjian.rbac.entity.*;

/**
 * @author Miracle
 * @date 2023/6/1 9:15
 */
public interface DataBaseService {

    /**
     * 查询部门
     */
    SectionInfo getSectionInfo(Long id);

    /**
     * 查询用户
     * @param id
     * @return
     */
    UserInfo getUserInfo(Long id);

    /**
     * 查询角色
     */
    RoleInfo getRoleInfo(Long id);

    /**
     * 查询字典
     */
    DictInfo getDictInfo(Long id);

    /**
     * 获取菜单
     * @param id
     * @return
     */
    MenuInfo getMenuInfo(Long id);
}
