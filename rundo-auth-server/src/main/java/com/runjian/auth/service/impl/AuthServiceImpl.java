package com.runjian.auth.service.impl;

import com.runjian.auth.domain.vo.response.AuthorizeData;
import com.runjian.auth.service.AuthService;
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
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthorizeData authenticate(Authentication authentication, String reqUrl, String reqMethod, String jsonStr) {
        Jwt principal = (Jwt) authentication.getPrincipal();
        Map<String, Object> claimMap = principal.getClaims();
        claimMap.get("aud");
        Object scopeOb = claimMap.get("scope");
        String scope = null;
        if (Objects.nonNull(scopeOb)){
            scope = scopeOb.toString();
        }
        return new AuthorizeData(true, authentication.getName(), claimMap.get("aud").toString(), scope, "111");
    }
}
