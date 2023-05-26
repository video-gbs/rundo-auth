package com.runjian.rbac.domain.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UserInfoDTO
 * @Description 登录参数
 * @date 2023-01-05 周四 17:11
 */
@Data
@ApiModel(value = "登录参数", description = "登录参数")
public class UserInfoDTO {

    @ApiModelProperty(value = "用户名", required = true, position = 1)
    @NotNull(message = "用户名不能为空")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true, position = 2)
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    private String password;
}
