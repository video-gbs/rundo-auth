package com.runjian.auth.controller;

import com.runjian.auth.vo.response.AuthDataRsp;
import com.runjian.auth.vo.request.PostAuthReq;
import com.runjian.auth.service.AuthService;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/4/13 16:52
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth-server")
public class AuthController {

    private final ValidatorService validatorService;

    private final AuthService authService;

    /**
     * 鉴权
     * @param req 鉴权请求体
     * @return
     */
    @PostMapping("/request")
    public CommonResponse<AuthDataRsp> authRequest(@RequestBody PostAuthReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(authService.authenticate(req.getReqUrl(), req.getReqMethod(), req.getQueryData(), req.getBodyData()));
    }

    /**
     * 登出系统
     * @param token jwtToken
     * @return
     */
    @DeleteMapping("/logout")
    public CommonResponse<?> logout(@RequestParam String token){
        authService.logout(token);
        return CommonResponse.success();
    }

    /**
     * 注销指定用户
     * @param username 用户
     * @return
     */
    @DeleteMapping("/sign-out")
    public CommonResponse<?> signOut(@RequestParam String username){
        authService.signOut(username);
        return CommonResponse.success();
    }


    @GetMapping("/oauth2")
    public CommonResponse<String> authRequest(@RequestParam String code){
        log.info("接收到验证code:" + code);
        return CommonResponse.success(code);
    }

}
