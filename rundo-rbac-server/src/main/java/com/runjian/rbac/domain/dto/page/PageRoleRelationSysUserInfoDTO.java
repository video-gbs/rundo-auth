package com.runjian.rbac.domain.dto.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName PageRelationSysUserInfoDTO
 * @Description 关联用户分页表
 * @date 2023-02-03 周五 10:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageRoleRelationSysUserInfoDTO extends Page {
    @ApiModelProperty("用户账户")
    private String userAccount;

    @ApiModelProperty("角色ID")
    private Long roleId;
}
