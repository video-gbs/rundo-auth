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
    private Long menuPid;

    /**
     * 菜单排序
     */
    private Integer sort;

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
     * 层级数
     */
    private Integer levelNum;

    /**
     * 是否隐藏
     */
    private Integer hidden;

    /**
     * 是否禁用
     */
    private Integer disabled;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
