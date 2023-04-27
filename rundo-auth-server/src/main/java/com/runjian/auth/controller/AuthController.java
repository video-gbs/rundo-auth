package com.runjian.auth.controller;

import com.runjian.auth.domain.vo.request.PostAuthReq;
import com.runjian.auth.domain.vo.response.AuthorizeData;
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
@RequestMapping("/auth")
public class AuthController {

    private final ValidatorService validatorService;

    private final AuthService authService;

    @GetMapping("/me")
    public CommonResponse<String> getAuth(Authentication authentication){
        return CommonResponse.success(authentication.getName());
    }

    @PostMapping("/request")
    public CommonResponse<AuthorizeData> authRequest(@RequestBody PostAuthReq req, Authentication authentication){
        validatorService.validateRequest(req);
        return CommonResponse.success(authService.authenticate(authentication, req.getReqUrl(), req.getReqMethod(), req.getJsonStr()));
    }

    @GetMapping("/oauth2")
    public CommonResponse<String> authRequest(@RequestParam String code){
        log.info("接收到验证code:" + code);
        return CommonResponse.success(code);
    }

}
