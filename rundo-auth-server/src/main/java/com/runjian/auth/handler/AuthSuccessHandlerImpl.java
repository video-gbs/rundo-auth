package com.runjian.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runjian.common.config.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 授权成功处理器
 * @author Miracle
 * @date 2023/4/11 15:38
 */
@Slf4j
@Component
public class AuthSuccessHandlerImpl extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest == null) {
            // todo 后续返回用户的前端路由表
            var ObjectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            var errData = Map.of(
                    "title", "认证成功",
                    "details", "欢迎用户：" + authentication.getName()
            );
            CommonResponse<Map<String, String>> commonResponse = CommonResponse.success(errData);
            response.getWriter().println(ObjectMapper.writeValueAsString(commonResponse));
            clearAuthenticationAttributes(request);
            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            this.requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, savedRequest.getRedirectUrl());
        this.requestCache.removeRequest(request, response);
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}
