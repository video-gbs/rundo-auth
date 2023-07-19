package com.runjian.rbac.service.rbac;

import com.runjian.rbac.entity.*;

/**
 * @author Miracle
 * @date 2023/6/1 9:15
 */
public interface DataBaseService {

    /**
     * 查询部门
     * @param id 主键id
     * @return
     */
    SectionInfo getSectionInfo(Long id);

    /**
     * 查询用户
     * @param id 主键id
     * @return
     */
    UserInfo getUserInfo(Long id);

    /**
     * 查询角色
     * @param id 主键id
     * @return
     */
    RoleInfo getRoleInfo(Long id);

    /**
     * 查询字典
     * @param id 主键id
     * @return
     */
    DictInfo getDictInfo(Long id);

    /**
     * 获取菜单
     * @param id 主键id
     * @return
     */
    MenuInfo getMenuInfo(Long id);


    /**
     * 获取功能接口信息
     * @param id 主键id
     * @return
     */
    FuncInfo getFuncInfo(Long id);

    /**
     * 获取资源接口信息
     * @param id 主键id
     * @return
     */
    ResourceInfo getResourceInfo(Long id);

    /**
     * 获取资源接口信息
     * @param resourceKey 资源Key
     * @param resourceValue 资源Value
     * @return
     */
    ResourceInfo getResourceInfo(String resourceKey, String resourceValue);
}
