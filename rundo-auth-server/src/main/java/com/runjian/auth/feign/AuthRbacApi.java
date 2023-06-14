package com.runjian.auth.feign;


import com.runjian.auth.feign.fallback.AuthRbacFallback;
import com.runjian.auth.vo.dto.AuthUserDto;
import com.runjian.auth.vo.request.PostAuthClientApiReq;
import com.runjian.auth.vo.request.PostAuthUserApiReq;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.runjian.auth.vo.dto.AuthDataDto;

/**
 * @author Miracle
 * @date 2023/6/8 17:16
 */
@FeignClient(value = "auth-rbac", fallbackFactory = AuthRbacFallback.class, dismiss404 = true)
public interface AuthRbacApi {

    /**
     * 获取授权用户
     * @param username 用户名
     * @return AuthUserDto
     */
    @GetMapping("/auth-rbac/user")
    CommonResponse<AuthUserDto> getAuthUser(@RequestParam String username);

    /**
     * 校验用户请求
     * @param req PostAuthUserApiReq
     * @return AuthDataDto
     */
    @PostMapping("/auth-rbac/api/user")
    CommonResponse<AuthDataDto> authUserApi(@RequestBody PostAuthUserApiReq req);

    /**
     * 校验客户端请求
     * @param req PostAuthClientApiReq
     * @return AuthDataDto
     */
    @PostMapping("/auth-rbac/api/client")
    CommonResponse<AuthDataDto> authClientApi(@RequestBody PostAuthClientApiReq req);
}
