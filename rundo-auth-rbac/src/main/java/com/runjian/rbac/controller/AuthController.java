package com.runjian.rbac.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.auth.AuthService;
import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.dto.AuthUserDto;
import com.runjian.rbac.vo.request.PostAuthUserApiReq;
import com.runjian.rbac.vo.request.PostAuthClientApiReq;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 授权接口
 * @author Miracle
 * @date 2023/6/8 9:39
 */
@Slf4j
@RestController
@RequestMapping("/auth-rbac")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final ValidatorService validatorService;

    /**
     * 获取授权用户（前端不对接）
     * @param username 用户名
     * @return AuthUserDto
     */
    @GetMapping("/user")
    @ApiDoc(result = AuthUserDto.class)
    public CommonResponse<AuthUserDto> getAuthUser(@RequestParam String username){
        return CommonResponse.success(authService.getUserAuth(username));
    }

    /**
     * 校验用户请求（前端不对接）
     * @param req PostAuthUserApiReq
     * @return AuthDataDto
     */
    @PostMapping("/api/user")
    @ApiDoc(result = AuthDataDto.class)
    public CommonResponse<AuthDataDto> authUserApi(@RequestBody PostAuthUserApiReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(authService.getAuthDataByUser(req.getUsername(), req.getScope(), req.getReqPath(), req.getReqMethod(), req.getJsonStr()));
    }

    /**
     * 校验客户端请求（前端不对接）
     * @param req PostAuthClientApiReq
     * @return AuthDataDto
     */
    @PostMapping("/api/client")
    @ApiDoc(result = AuthDataDto.class)
    public CommonResponse<AuthDataDto> authClientApi(@RequestBody PostAuthClientApiReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(authService.getAuthDataByClient(req.getScope(), req.getReqPath(), req.getReqMethod()));
    }

    /**
     * 登出系统
     * @return
     */
    @DeleteMapping("/logout")
    public CommonResponse<?> logout(){
        authService.logout();
        return CommonResponse.success();
    }
}
