package com.runjian.rbac.domain.dto.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName PageSysUserInfoDTO
 * @Description 分页参数
 * @date 2023-02-02 周四 18:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageSysUserInfoDTO extends Page {
    @ApiModelProperty("部门ID")
    private Long orgId;

    @ApiModelProperty("用户账户")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;
}
