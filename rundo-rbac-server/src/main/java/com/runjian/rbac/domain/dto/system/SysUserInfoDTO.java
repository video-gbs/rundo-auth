package com.runjian.rbac.domain.dto.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.runjian.rbac.constant.AddGroup;
import com.runjian.rbac.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysUserInfoDTO
 * @Description 新建用户
 * @date 2023-01-10 周二 9:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "新建用户", description = "用户信息")
public class SysUserInfoDTO {

    @ApiModelProperty("编号ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;

    @ApiModelProperty("用户账户")
    @NotBlank(message = "用户账户不能为空")
    @Pattern(regexp = "[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户密码")
    @NotBlank(groups = {AddGroup.class}, message = "用户密码不能为空")
    private String password;

    @ApiModelProperty("确认密码")
    @NotBlank(groups = {AddGroup.class}, message = "确认密码不能为空")
    private String rePassword;

    @ApiModelProperty("有效期起")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "有效期起不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDateStart;

    @ApiModelProperty("有效期终")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDateEnd;

    @ApiModelProperty("所属部门编号")
    @NotNull(message = "所属部门编号不能为空")
    private Long orgId;

    @ApiModelProperty("工号")
    private String jobNo;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("描述信息")
    private String description;

    @ApiModelProperty("角色编号列表")
    private List<Long> roleIds;

}
