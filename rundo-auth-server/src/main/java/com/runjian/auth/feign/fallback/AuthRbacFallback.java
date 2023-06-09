package com.runjian.auth.feign.fallback;

import com.runjian.auth.feign.AuthRbacApi;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/6/8 17:18
 */
@Component
public class AuthRbacFallback implements FallbackFactory<AuthRbacApi> {
    @Override
    public AuthRbacApi create(Throwable cause) {
        return null;
    }
}
