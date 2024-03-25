package com.runjian.gateway.config;

import com.runjian.gateway.filter.AuthBodyFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miracle
 * @date 2023/4/24 15:10
 */
@Configuration
public class AuthBodyFilterFactoryConfig extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return new AuthBodyFilter();
    }

    @Override
    public String name() {
        return "AuthBody";
    }
}
