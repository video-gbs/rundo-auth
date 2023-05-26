package com.runjian.rbac.domain.dto.system;

import com.runjian.rbac.domain.dto.common.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName PageRelationSysUserInfoDTO
 * @Description 关联用户分页表
 * @date 2023-02-03 周五 10:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "查询已关联用户查询参数", description = "关联用户查询参数")
public class QueryRoleRelationSysUserInfoDTO extends CommonPage {
    @ApiModelProperty("角色ID")
    @NotNull
    private Long roleId;
    @ApiModelProperty("用户账户")
    private String userAccount;

}
