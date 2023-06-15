package com.runjian.rbac.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.rbac.feign.AuthServerApi;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/6/15 10:57
 */
@Component
public class AuthServerFallback implements FallbackFactory<AuthServerApi> {
    @Override
    public AuthServerApi create(Throwable cause) {
        return new AuthServerApi() {
            @Override
            public CommonResponse<?> logout(String token) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<?> signOut(String username) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
