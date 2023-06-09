package com.runjian.gateway.config;

import com.runjian.gateway.filter.AuthHeaderFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miracle
 * @date 2023/4/24 15:10
 */
@Configuration
public class AuthHeaderFilterFactoryConfig extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return new AuthHeaderFilter();
    }

    @Override
    public String name() {
        return "AuthHeader";
    }
}
