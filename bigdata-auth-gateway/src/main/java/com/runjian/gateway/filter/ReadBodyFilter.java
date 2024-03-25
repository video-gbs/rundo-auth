package com.runjian.gateway.filter;

import com.runjian.gateway.config.AuthProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 读取body数据
 * @author Miracle
 * @date 2023/4/24 16:20
 */
@Component
public class ReadBodyFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        // 检查请求是否包含请求体
        boolean hasBody = headers.getContentLength() > 0;

        if (hasBody){
            // 从请求体中获取参数
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        DataBufferUtils.retain(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux
                                .defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }

                        };
                        exchange.getAttributes().put(AuthProperties.REQUEST_BODY_NAME, cachedFlux);

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    });
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
