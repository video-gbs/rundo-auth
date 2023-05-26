package com.runjian.rbac.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName OrgInfoVO
 * @Description
 * @date 2023-02-07 周二 10:29
 */
@Data
public class OrgInfoVO {

    @ApiModelProperty("所属部门编号")
    private Long orgId;

    @ApiModelProperty("所属部门名称")
    private String orgName;
}
