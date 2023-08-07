package com.runjian.rbac.service.rbac;

import com.runjian.rbac.vo.response.GetMenuTreeRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/6/5 11:37
 */
public interface MenuService {

    /**
     * 获取菜单树
     * @param name 菜单名称
     * @param path 菜单地址
     * @return 菜单树
     */
    GetMenuTreeRsp getMenuList(String name, String path);

    /**
     * 添加菜单
     * @param menuPid 菜单父id
     * @param menuSort 菜单排序
     * @param menuType 菜单类型
     * @param path 跳转路径
     * @param component 前端组件
     * @param name 菜单名称
     * @param icon 菜单图标
     * @param description 描述
     * @param isFullScreen 是否全屏
     * @param disabled 是否禁用
     */
    void addMenu(Long menuPid, Integer menuSort, Integer menuType, String path, String component, String name, String icon, String description, Integer isFullScreen, Integer disabled);

    /**
     * 更改禁用状态
     * @param id 菜单id
     * @param disabled 禁用状态
     */
    void updateDisabled(Long id, Integer disabled);

    /**
     * 修改菜单
     * @param id 菜单id
     * @param menuSort 菜单排序
     * @param menuType 菜单类型
     * @param path 跳转url
     * @param component 前端组件
     * @param name 菜单名称
     * @param icon 菜单图标
     * @param description 描述
     * @param isFullScreen 是否全屏
     * @param disabled 是否禁用
     */
    void updateMenu(Long id, Integer menuSort, Integer menuType, String path, String component, String name, String icon, String description, Integer isFullScreen, Integer disabled);

    /**
     * 删除菜单
     * @param id 菜单id
     */
    void deleteMenu(Long id);
}
