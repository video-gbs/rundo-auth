package com.runjian.rbac.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.rbac.feign.fallback.AuthServerFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 授权系统API接口
 * @author Miracle
 * @date 2023/6/15 10:52
 */
@FeignClient(value = "auth-server", fallbackFactory = AuthServerFallback.class, dismiss404 = true)
public interface AuthServerApi {

    /**
     * 登出系统
     * @param token jwtToken
     * @return
     */
    @DeleteMapping("/auth-server/logout")
    CommonResponse<?> logout(@RequestParam String token);

    /**
     * 注销指定用户
     * @param username 用户
     * @return
     */
    @DeleteMapping("/auth-server/sign-out")
    CommonResponse<?> signOut(@RequestParam String username);
}
