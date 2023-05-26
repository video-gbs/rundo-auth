package com.runjian.rbac.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RelationSysUserInfoVO
 * @Description 关联用户
 * @date 2023-02-03 周五 8:51
 */
@Data
public class RelationSysUserInfoVO {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("用户账户")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("有效期起")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDateStart;

    @ApiModelProperty("有效期终")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDateEnd;

    @ApiModelProperty("工号")
    private String jobNo;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("功能角色")
    private String roleName;

    @ApiModelProperty("安防区域名称")
    private String areaName;

    @ApiModelProperty("所属部门")
    private String orgName;

    @ApiModelProperty("描述信息")
    private String description;
}
