package com.runjian.gateway.config;

import com.runjian.gateway.filter.AuthFailureFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/4/23 17:15
 */
@Component
public class AuthFailureFilterFactoryConfig extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return new AuthFailureFilter();
    }

    @Override
    public String name() {
        return "AuthFailure";
    }
}
