package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/30 15:07
 */
@Data
public class MenuInfo {

    private Long id;

    /**
     * 菜单id
     */
    private Long menuId;

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
    private Integer level;

    /**
     * 是否隐藏
     */
    private Integer hidden;

    /**
     * 是否禁用
     */
    private Integer disabled;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
