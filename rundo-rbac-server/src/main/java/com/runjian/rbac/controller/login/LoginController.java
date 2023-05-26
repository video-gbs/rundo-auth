package com.runjian.rbac.controller.login;


import com.runjian.common.config.response.CommonResponse;
import com.runjian.rbac.domain.dto.login.UserInfoDTO;
import com.runjian.rbac.service.login.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName LoginController
 * @Description 登录登出
 * @date 2023-01-05 周四 17:09
 */
@Api(tags = "登录注销")
@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody @Valid UserInfoDTO userInfoDTO) {
        return loginService.login(userInfoDTO);
    }

    @ApiOperation(value = "验证码")
    @GetMapping("/code")
    public CommonResponse<?> getCode() {
        return loginService.getCode();
    }

    @ApiOperation(value = "登出接口")
    @PostMapping("/logout")
    public CommonResponse<?> logout() {
        loginService.logout();
        return CommonResponse.success();
    }

}
