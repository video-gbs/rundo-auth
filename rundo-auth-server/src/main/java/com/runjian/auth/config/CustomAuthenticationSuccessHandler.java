package com.runjian.auth.config;

import com.runjian.auth.vo.response.AuthAccessRsp;
import com.runjian.common.config.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/6/19 19:22
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final HttpMessageConverter<Object> accessTokenHttpResponseConverter = new MappingJackson2HttpMessageConverter();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();

        AuthAccessRsp authAccessRsp = new AuthAccessRsp();
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            authAccessRsp.setExpiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            authAccessRsp.setRefreshToken(refreshToken.getTokenValue());
        }

        authAccessRsp.setAccessToken(accessToken.getTokenValue());
        authAccessRsp.setTokenType(accessToken.getTokenType().getValue());
        authAccessRsp.setScope(String.join(",", accessToken.getScopes()));


        //custom R
        this.accessTokenHttpResponseConverter.write(CommonResponse.success(authAccessRsp), null, new ServletServerHttpResponse(response));

    }
}
