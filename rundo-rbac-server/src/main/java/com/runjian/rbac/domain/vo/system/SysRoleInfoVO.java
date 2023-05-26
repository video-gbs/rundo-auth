package com.runjian.rbac.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysRoleInfoVO
 * @Description
 * @date 2023-02-02 周四 9:37
 */
@Data
@ApiModel(value = "角色管理列表", description = "角色管理列表")
public class SysRoleInfoVO {
    @ApiModelProperty("主键ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("创建人ID")
    private Long createdBy;

    @ApiModelProperty("创建人账户")
    private String userAccount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    @ApiModelProperty("是否禁用;0:启用(否）,1:禁用(是)")
    private Integer status;
}
