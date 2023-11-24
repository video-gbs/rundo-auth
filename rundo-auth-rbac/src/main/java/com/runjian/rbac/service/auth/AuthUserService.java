package com.runjian.rbac.service.auth;

import com.runjian.rbac.vo.response.*;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/6/15 17:32
 */
public interface AuthUserService {

    /**
     * 登出
     */
    void logout();

    /**
     * 获取用户基础信息
     * @return GetUserRsp
     */
    GetUserRsp getUser();

    /**
     * 通过层级获取菜单树
     * @param levelNumStart 开始层级
     * @param levelNumEnd 结束层级
     * @return
     */
    List<GetMenuTreeRsp> getMenu(Integer levelNumStart, Integer levelNumEnd);

    /**
     * 通过菜单id获取功能列表
     * @param menuId 菜单id
     * @return
     */
    List<GetFuncRsp> getFunc(Integer menuId);

    /**
     * 获取资源
     * @param resourceKey 资源key
     */
    GetResourceTreeRsp getCatalogueResource(String resourceKey);

    /**
     * 获取目录下的资源信息
     * @param pid
     * @return
     */
    List<GetCatalogueResourceRsp> getResourceByCatalogue(Long pid, Boolean isIncludeChild);

    /**
     * 刷新用户缓存
     * @param resourceKey 资源key
     */
    void refreshUserResource(String resourceKey);

    /**
     * 获取用户所有资源
     * @return
     */
    List<String> getUserResource(String resourceKey);
}
