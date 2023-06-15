package com.runjian.rbac.vo.response;

import com.runjian.common.constant.CommonEnum;
import com.runjian.rbac.constant.MenuType;
import lombok.Data;

import java.util.List;

/**
 * 菜单树返回体
 * @author Miracle
 * @date 2023/6/5 11:46
 */
@Data
public class GetMenuTreeRsp {

    private Long id;

    /**
     * 菜单id
     */
    private Long menuPid;

    /**
     * 菜单排序
     */
    private Integer menuSort;

    /**
     * 菜单类型
     */
    private Integer menuType;

    /**
     * 路径
     */
    private String path;

    /**
     * 组件名称
     */
    private String component;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 描述
     */
    private String description;

    /**
     * 层级
     */
    private String level;

    /**
     * 是否隐藏
     */
    private Integer hidden;

    /**
     * 是否禁用
     */
    private Integer disabled;

    /**
     * 子数据
     */
    private List<GetMenuTreeRsp> childList;

    /**
     * 获取虚拟根节点
     * @return
     */
    public static GetMenuTreeRsp getRootMenuTree(){
        GetMenuTreeRsp getMenuTreeRsp = new GetMenuTreeRsp();
        getMenuTreeRsp.setId(0L);
        getMenuTreeRsp.setMenuPid(null);
        getMenuTreeRsp.setMenuSort(0);
        getMenuTreeRsp.setMenuType(MenuType.DIRECTORY.getCode());
        getMenuTreeRsp.setLevel("0");
        getMenuTreeRsp.setName("根节点");
        getMenuTreeRsp.setIcon(null);
        getMenuTreeRsp.setHidden(CommonEnum.DISABLE.getCode());
        getMenuTreeRsp.setDisabled(CommonEnum.DISABLE.getCode());
        return getMenuTreeRsp;
    }
}
