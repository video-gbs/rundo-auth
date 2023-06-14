package com.runjian.auth.service.impl;

import com.runjian.auth.feign.AuthRbacApi;
import com.runjian.auth.vo.response.AuthDataRsp;
import com.runjian.auth.vo.request.PostAuthUserApiReq;
import com.runjian.auth.service.AuthService;
import com.runjian.auth.vo.response.AuthJwtRsp;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/4/20 10:39
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRbacApi authRbacApi;

    private final HttpServletRequest request;

    private final OAuth2AuthorizationService authorizationService;

    @Override
    public AuthDataRsp authenticate(String reqPath, String reqMethod, String jsonStr) {
        String jwtToken = request.getHeader(CommonConstant.AUTHORIZATION).split(" ")[1];
        OAuth2Authorization authorization = this.authorizationService.findByToken(jwtToken, null);
        if (Objects.isNull(authorization)){
            return AuthDataRsp.getFailureRsp("非法token,请重新登录");
        }
        OAuth2Authorization.Token<OAuth2Token> authorizedToken = authorization.getToken(jwtToken);
        if (Objects.isNull(authorizedToken) || !authorizedToken.isActive()){
            return AuthDataRsp.getFailureRsp("非法token,请重新登录");
        }

        CommonResponse<AuthDataRsp> authDataDtoCommonResponse = authRbacApi.authUserApi(new PostAuthUserApiReq(authorization.getPrincipalName(), String.join(",", authorization.getAuthorizedScopes()), reqMethod, reqPath, jsonStr));
        if (authDataDtoCommonResponse.isError()){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        }
        AuthDataRsp authDataRsp = authDataDtoCommonResponse.getData();
        authDataRsp.setClientId(authorization.getRegisteredClientId());
        return authDataRsp;
    }
}
