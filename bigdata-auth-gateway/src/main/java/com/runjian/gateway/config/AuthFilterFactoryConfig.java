package com.runjian.gateway.config;

import com.runjian.gateway.filter.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/4/23 9:41
 */

@Component
@RequiredArgsConstructor
public class AuthFilterFactoryConfig extends AbstractGatewayFilterFactory<Object> {

    private final NacosUrlConfig nacosUrlConfig;

    private final AuthProperties authProperties;

    @Override
    public GatewayFilter apply(Object config) {
        return new AuthFilter(nacosUrlConfig, authProperties);
    }

    @Override
    public String name() {
        return "Auth";
    }


}
