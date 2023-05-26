package com.runjian.rbac.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单信息表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-04 15:16:33
 */
@Data
@EqualsAndHashCode
@TableName("menu_info")
@ApiModel(value = "MenuInfo对象", description = "菜单信息表")
public class MenuInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("所属应用ID")
    @TableField("app_id")
    private Long appId;

    @ApiModelProperty("所属应用名称")
    @TableField("app_name")
    private String appName;

    @ApiModelProperty("父菜单ID")
    @TableField("menu_pid")
    private Long menuPid;

    @ApiModelProperty("父级菜单名称")
    @TableField("parent_name")
    private String parentName;
    @ApiModelProperty("当前菜单所有父菜单")
    @TableField("menu_pids")
    private String menuPids;

    @ApiModelProperty("排序")
    @TableField("menu_sort")
    private Integer menuSort;

    @ApiModelProperty("排序")
    @TableField("menu_type")
    private Integer menuType;

    @ApiModelProperty("是否子节点")
    @TableField("leaf")
    private Integer leaf;

    @ApiModelProperty("跳转URL")
    @TableField("path")
    private String path;

    @ApiModelProperty("前端组件import路径")
    @TableField("component")
    private String component;
    @ApiModelProperty("前端组件import路径")
    @TableField("redirect")
    private String redirect;

    @ApiModelProperty("path字段去掉/之后的值")
    @TableField("name")
    private String name;

    @ApiModelProperty("图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty("菜单名称")
    @TableField("title")
    private String title;

    @ApiModelProperty("层级")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("是否隐藏;0:显示(否）,1:隐藏(是)")
    @TableField("hidden")
    private Integer hidden;

    @ApiModelProperty("是否禁用;0:启用(否）,1:禁用(是)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @ApiModelProperty("创建人")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    @ApiModelProperty("更新人")
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;


}
