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
 * @ClassName StatusSysRoleInfoDTO
 * @Description 角色状态切换
 * @date 2023-02-09 周四 6:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "角色状态切换", description = "状态切换")
public class StatusSysRoleInfoDTO {
    @ApiModelProperty("编号ID")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "禁用状态", notes = "0正常，1禁用")
    private Integer status;
}
