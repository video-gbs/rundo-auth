package com.runjian.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

/**
 * 授权失败处理器
 * @author Miracle
 * @date 2023/4/11 15:47
 */
@Slf4j
@Component
public class AuthFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // todo 返回401异常
        var ObjectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        var errData = Map.of(
                "title", "认证失败",
                "details", exception.getMessage()
        );
        CommonResponse<Map<String, String>> commonResponse = CommonResponse.failure(BusinessErrorEnums.USER_AUTH_ERROR);
        response.getWriter().println(ObjectMapper.writeValueAsString(commonResponse));
    }
}
