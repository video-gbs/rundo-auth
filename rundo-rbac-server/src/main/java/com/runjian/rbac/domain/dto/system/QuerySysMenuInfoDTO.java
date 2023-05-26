package com.runjian.rbac.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName QuerySysMenuInfoDTO
 * @Description 菜单查询参数
 * @date 2023-02-06 周一 16:45
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "菜单查询参数", description = "菜单查询参数")
public class QuerySysMenuInfoDTO {

    @ApiModelProperty("应用ID")
    private Long appId;

    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("跳转URL")
    private String url;

}
