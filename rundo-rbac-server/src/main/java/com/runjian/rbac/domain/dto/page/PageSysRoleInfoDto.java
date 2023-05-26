package com.runjian.rbac.domain.dto.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName PageSysRoleInfoDto
 * @Description 分页参数
 * @date 2023-02-06 周一 13:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageSysRoleInfoDto extends Page {
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("创建人账户")
    private String userAccount;

    @ApiModelProperty("创建开始时间")
    private LocalDateTime createdTimeStart;

    @ApiModelProperty("创建时间结束")
    private LocalDateTime createdTimeEnd;
}
