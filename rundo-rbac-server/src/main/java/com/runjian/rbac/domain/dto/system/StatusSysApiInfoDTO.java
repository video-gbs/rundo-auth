package com.runjian.rbac.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName StatusSysApiInfoDTO
 * @Description 接口状态切换
 * @date 2023-01-31 周二 16:12
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "接口状态切换", description = "状态切换")
public class StatusSysApiInfoDTO {
    @ApiModelProperty("编号ID")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "禁用状态", notes = "0正常，1禁用")
    private Integer status;
}
