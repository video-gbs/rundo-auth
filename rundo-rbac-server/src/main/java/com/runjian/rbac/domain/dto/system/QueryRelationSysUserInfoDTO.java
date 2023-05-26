package com.runjian.rbac.domain.dto.system;

import com.runjian.rbac.domain.dto.common.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RelationSysUserInfoDTO
 * @Description 关联用户
 * @date 2023-02-03 周五 9:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "关联用户查询参数", description = "关联用户查询参数")
public class QueryRelationSysUserInfoDTO extends CommonPage {
    @ApiModelProperty("用户账户")
    private String userAccount;
}
