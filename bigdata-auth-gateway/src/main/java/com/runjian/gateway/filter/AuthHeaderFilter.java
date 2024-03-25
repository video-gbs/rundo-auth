package com.runjian.gateway.filter;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.gateway.common.BusinessErrorEnums;
import com.runjian.gateway.common.CommonResponse;
import com.runjian.gateway.config.AuthProperties;
import com.runjian.gateway.vo.AuthDataDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/6/8 16:31
 */
@Component
public class AuthHeaderFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Object successData = exchange.getAttribute(AuthProperties.AUTHORIZATION_AUTH_SUCCESS_NAME);
        if (Objects.isNull(successData)){
            return chain.filter(exchange);
        }
        AuthDataDto authDataDto = JSONObject.parseObject(JSONObject.toJSONString(successData), AuthDataDto.class);
        HttpStatus authHttpStatus = HttpStatus.valueOf(authDataDto.getStatusCode());
        if (!HttpStatus.OK.equals(authHttpStatus)){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(authHttpStatus);
            return response.writeAndFlushWith(Mono.just(ByteBufMono.just(response.bufferFactory().wrap(JSONObject.toJSONString(CommonResponse.failure(BusinessErrorEnums.USER_NO_AUTH, authDataDto.getMsg())).getBytes()))));
        }
        exchange.getRequest().mutate().headers(httpHeaders -> {
            httpHeaders.add("Username", authDataDto.getUsername());
            httpHeaders.add("Client-Id", authDataDto.getClientId());
            httpHeaders.add("Is-Admin", authDataDto.getIsAdmin().toString());
            if (!authDataDto.getIsAdmin()){
                httpHeaders.add("Role-Ids", authDataDto.getRoleIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
                if (Objects.nonNull(authDataDto.getResourceKey())){
                    httpHeaders.add("Resource-Key", authDataDto.getResourceKey());
                }else {
                    httpHeaders.remove("Resource-Key");
                }
            }
        });
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 3;
    }
}
