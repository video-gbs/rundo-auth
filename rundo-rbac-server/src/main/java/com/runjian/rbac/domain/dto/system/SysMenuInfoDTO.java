package com.runjian.rbac.domain.dto.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.runjian.rbac.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysMenuInfoDTO
 * @Description 添加菜单
 * @date 2023-01-10 周二 10:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "菜单信息", description = "菜单信息")
public class SysMenuInfoDTO {

    @ApiModelProperty("编号ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;

    @ApiModelProperty("所属应用ID")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long appId;

    @ApiModelProperty("所属应用名称")
    @NotNull
    private String appName;

    @ApiModelProperty("上级菜单ID")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuPid;

    @ApiModelProperty("排序")
    @NotNull
    @Min(value = 0)
    @Max(100)
    private Integer menuSort;

    @ApiModelProperty("排序")
    @NotNull
    @Min(value = 1)
    @Max(value = 3)
    private Integer menuType;

    @ApiModelProperty("跳转URL")
    private String path;

    @ApiModelProperty("前端组件import路径")
    private String component;

    @ApiModelProperty("重定向地址")
    private String redirect;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("菜单标题")
    private String title;

    @ApiModelProperty(value = "禁用状态", notes = "0正常，1禁用")
    private Integer status;

    @ApiModelProperty(value = "隐藏状态", notes = "0正常，1禁用")
    private Integer hidden;
}
