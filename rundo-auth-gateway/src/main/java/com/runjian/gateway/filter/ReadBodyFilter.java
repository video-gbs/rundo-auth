package com.runjian.gateway.filter;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
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
        boolean hasBody = headers.getContentLength() > 0 || headers.getContentType() != null;

        if (hasBody){
            // 从请求体中获取参数
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer ->{
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        exchange.getAttributes().put("bodyData", new String(bytes, StandardCharsets.UTF_8));
                        return chain.filter(exchange);
                    });
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
