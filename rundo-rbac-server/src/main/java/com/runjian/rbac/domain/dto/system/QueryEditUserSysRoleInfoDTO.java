package com.runjian.rbac.domain.dto.system;

import com.runjian.rbac.domain.dto.common.CommonPage;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName QueryEditUserSysRoleInfoDTO
 * @Description 新增编辑用户时的角色分页参数
 * @date 2023-02-03 周五 10:16
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "新增编辑用户时的角色分页参数", description = "新增编辑用户时的角色分页参数")
public class QueryEditUserSysRoleInfoDTO extends CommonPage {
}
