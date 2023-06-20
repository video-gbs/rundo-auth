package com.runjian.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miracle
 * @date 2023/4/24 10:04
 */
@Data
@Configuration
public class AuthProperties {

    public final static String AUTHORIZATION_AUTH_FAILURE_NAME = "authFailure";

    public final static String AUTHORIZATION_AUTH_SUCCESS_NAME = "authSuccess";

    public final static String AUTHORIZATION_HEADER_TOKEN = "Authorization";

    public final static String REQUEST_BODY_NAME = "bodyData";

    /**
     * 鉴权地址
     */
    @Value("${auth.addr}")
    private String authAddr;

    /**
     * 授权前缀
     */
    @Value("${auth.prefix}")
    private String authPrefix;

    /**
     * 授权服务名称
     */
    @Value("${auth.server-name}")
    private String authServerName;

}
