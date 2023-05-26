package com.runjian.rbac.domain.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysUserInfoDTO
 * @Description 新建用户
 * @date 2023-01-10 周二 9:30
 */
@Data
@ApiModel(value = "新建用户", description = "用户信息")
public class ListSysUserInfoVO {

    @ApiModelProperty("用户编号")
    private Long id;
    @ApiModelProperty("用户账户")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("所属部门名称")
    private String orgName;

    @ApiModelProperty("角色")
    private String roleName;

    @ApiModelProperty("工号")
    private String jobNo;

    @ApiModelProperty(value = "禁用状态", notes = "0正常，1禁用")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
