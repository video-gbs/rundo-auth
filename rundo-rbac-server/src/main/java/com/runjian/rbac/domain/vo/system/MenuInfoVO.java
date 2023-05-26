package com.runjian.rbac.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysMenuInfoVO
 * @Description 菜单
 * @date 2023-01-11 周三 18:37
 */
@Data
public class MenuInfoVO {
    @ApiModelProperty("主键ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    @ApiModelProperty("所属应用ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long appId;

    @ApiModelProperty("所属应用名称")
    private String appName;

    @ApiModelProperty("直接父级菜单ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long menuPid;

    @ApiModelProperty("父级菜单名称")
    private String parentName;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("跳转URL")
    private String path;

    @ApiModelProperty("path去掉/之后")
    private String name;

    @ApiModelProperty("菜单名称")
    private String title;

    @ApiModelProperty("菜单顺序")
    private Integer menuSort;

    @ApiModelProperty("菜单类型")
    private Integer menuType;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("前端组件import路径")
    private String component;

    @ApiModelProperty("禁用状态")
    private Integer status;

    @ApiModelProperty("是否隐藏")
    private Integer hidden;

    @ApiModelProperty("跳转")
    private String redirect;

    @ApiModelProperty("meta")
    private MyMetaClass meta;
}
