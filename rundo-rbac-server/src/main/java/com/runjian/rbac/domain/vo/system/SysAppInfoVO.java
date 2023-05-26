package com.runjian.rbac.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysAppInfoVO
 * @Description 应用详情
 * @date 2023-01-30 周一 14:12
 */
@Data
public class SysAppInfoVO {

    @ApiModelProperty("主键ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "应用分类")
    private Integer appType;

    @ApiModelProperty("应用所在IP")
    private String appIp;

    @ApiModelProperty("应用服务端口")
    private Integer appPort;

    @ApiModelProperty(value = "应用名称")
    private String appIcon;

    @ApiModelProperty("url")
    private String appUrl;

    @ApiModelProperty("应用简介")
    private String appDesc;

    @ApiModelProperty(value = "禁用状态", notes = "0正常，1禁用")
    private Integer status;
}
