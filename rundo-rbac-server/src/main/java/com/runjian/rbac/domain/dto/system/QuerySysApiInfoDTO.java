package com.runjian.rbac.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName QuerySysApiInfoDTO
 * @Description 接口查询
 * @date 2023-01-31 周二 16:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "接口查询", description = "接口信息查询条件")
public class QuerySysApiInfoDTO {

    @ApiModelProperty("所属应用")
    private Long appId;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("跳转链接")
    private String url;
}
