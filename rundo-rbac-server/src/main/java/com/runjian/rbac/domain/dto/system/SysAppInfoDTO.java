package com.runjian.rbac.domain.dto.system;

import com.runjian.rbac.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysAppInfoDTO
 * @Description 添加应用
 * @date 2023-01-10 周二 10:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "应用信息", description = "应用信息")
public class SysAppInfoDTO {

    @ApiModelProperty("编号ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "应用分类", required = true)
    @NotBlank
    private Integer appType;

    @ApiModelProperty(value = "应用名称", required = true)
    @NotBlank
    private String appName;

    @ApiModelProperty(value = "应用图标")
    private String appIcon;

    @ApiModelProperty(value = "应用所在IP", required = true)
    @NotBlank
    private String appIp;

    @ApiModelProperty(value = "应用服务端口", required = true)
    @NotBlank
    private Integer appPort;

    @ApiModelProperty(value = "应用跳转路由", required = true)
    @NotBlank
    private String appUrl;

    @ApiModelProperty(value = "前端组件import路径")
    private String component;

    @ApiModelProperty(value = "重定向地址")
    private String redirect;

    @ApiModelProperty("应用简介")
    private String appDesc;

    @ApiModelProperty(value = "禁用状态", notes = "0正常，1禁用")
    private Integer status;

}
