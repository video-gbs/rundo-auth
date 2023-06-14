package com.runjian.auth.feign.fallback;

import com.runjian.auth.feign.AuthRbacApi;
import com.runjian.auth.vo.response.AuthDataRsp;
import com.runjian.auth.vo.response.AuthUserRsp;
import com.runjian.auth.vo.request.PostAuthClientApiReq;
import com.runjian.auth.vo.request.PostAuthUserApiReq;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
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
        return new AuthRbacApi() {
            @Override
            public CommonResponse<AuthUserRsp> getAuthUser(String username) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<AuthDataRsp> authUserApi(PostAuthUserApiReq req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<AuthDataRsp> authClientApi(PostAuthClientApiReq req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
