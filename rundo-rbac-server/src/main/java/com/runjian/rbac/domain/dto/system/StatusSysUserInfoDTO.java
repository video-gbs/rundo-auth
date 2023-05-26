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
 * @ClassName StatusSysUserInfoDTO
 * @Description
 * @date 2023-02-02 周四 17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户状态切换", description = "用户状态切换")
public class StatusSysUserInfoDTO {
    @ApiModelProperty("编号ID")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "禁用状态", notes = "0正常，1禁用")
    private Integer status;
}
