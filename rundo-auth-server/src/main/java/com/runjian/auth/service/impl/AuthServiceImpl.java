package com.runjian.auth.service.impl;

import com.runjian.auth.feign.AuthRbacApi;
import com.runjian.auth.vo.dto.AuthDataDto;
import com.runjian.auth.vo.request.PostAuthUserApiReq;
import com.runjian.auth.vo.response.AuthorizeData;
import com.runjian.auth.service.AuthService;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/4/20 10:39
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRbacApi authRbacApi;

    @Override
    public AuthDataDto authenticate(Authentication authentication, String reqPath, String reqMethod, String jsonStr) {
        Jwt principal = (Jwt) authentication.getPrincipal();
        Map<String, Object> claimMap = principal.getClaims();
        Object scopeOb = claimMap.get("scope");
        String scope = null;
        if (Objects.nonNull(scopeOb)){
            scope = scopeOb.toString();
        }
        CommonResponse<AuthDataDto> authDataDtoCommonResponse = authRbacApi.authUserApi(new PostAuthUserApiReq(authentication.getName(), scope, reqMethod, reqPath, jsonStr));
        if (authDataDtoCommonResponse.isError()){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        }
        AuthDataDto authDataDto = authDataDtoCommonResponse.getData();
        authDataDto.setClientId(claimMap.get("aud").toString());
        return authDataDto;
    }
}
