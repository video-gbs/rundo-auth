package com.runjian.gateway.filter;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.gateway.config.AuthProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;

import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/4/23 17:13
 */
public class AuthFailureFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        if (Objects.nonNull(response.getStatusCode())){
            if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED) || response.getStatusCode().equals(HttpStatus.FORBIDDEN)){
                Object authResponse = exchange.getAttributes().remove(AuthProperties.AUTHORIZATION_AUTH_FAILURE_NAME);
                if (Objects.nonNull(authResponse)){
                    return response.writeAndFlushWith(Mono.just(ByteBufMono.just(response.bufferFactory().wrap(JSONObject.toJSONString(authResponse).getBytes()))));
                }else {
                    return exchange.getResponse().setComplete();
                }
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }
}
