package com.runjian.rbac.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UserInfoVo
 * @Description 用户详情
 * @date 2023-01-10 周二 14:48
 */
@Data
@ApiModel(value = "用户详细信息", description = "用户信息")
public class UserInfoVo {

    @ApiModelProperty("用户账户")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("有效期起")
    private Date expiryDateStart;

    @ApiModelProperty("有效期终")
    private Date expiryDateEnd;

    @ApiModelProperty("直属部门")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orgId;

    @ApiModelProperty("直属部门")
    private String orgName;

    @ApiModelProperty("间接所属部门")
    private String orgPIds;

    @ApiModelProperty("间接所属部门")
    private List<String> orgNameStr;

    // 详细信息
    @ApiModelProperty("用户工号")
    private String jobNo;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("所有角色ID列表")
    private List<Long> roleIds;

    @ApiModelProperty("所有角色名称")
    private List<String> roleNames;

}
