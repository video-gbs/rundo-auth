package com.runjian.rbac.domain.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName EditUserSysRoleInfoVO
 * @Description 新增编辑用户时的角色列表
 * @date 2023-02-03 周五 10:11
 */

@Data
@ApiModel(value = "新增编辑用户时的角色信息", description = "角色信息")
public class EditUserSysRoleInfoVO {

    @ApiModelProperty("编号ID")
    private Long id;
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    @ApiModelProperty("是否禁用;0:启用(否）,1:禁用(是)")
    private Integer status;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("创建人账户")
    private String userAccount;
}
