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
 * @ClassName QuerySysUserInfoDTO
 * @Description 用户管理查询条件
 * @date 2023-02-02 周四 17:16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "应用分页查询", description = "应用信息查询条件")
public class QuerySysUserInfoDTO extends CommonPage {
    @ApiModelProperty("部门ID")
    private Long orgId;

    @ApiModelProperty("是否包含下级：1包含，0不包含")
    private Integer contain;

    @ApiModelProperty("用户账户")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;
}
