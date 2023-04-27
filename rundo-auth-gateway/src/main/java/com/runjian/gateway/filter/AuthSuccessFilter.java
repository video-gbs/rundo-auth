package com.runjian.gateway.filter;

import com.runjian.gateway.config.AuthProperties;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/4/24 14:43
 */
@Component
public class AuthSuccessFilter implements GatewayFilter, Ordered {
    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Object successData = exchange.getAttribute(AuthProperties.AUTHORIZATION_AUTH_SUCCESS_NAME);
        if (Objects.isNull(successData)){
            return chain.filter(exchange);
        }
        ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);
        ModifyRequestBodyGatewayFilterFactory.Config config = new ModifyRequestBodyGatewayFilterFactory.Config();
        config.setInClass(Map.class);
        config.setOutClass(Map.class);
        Class inClass = config.getInClass();
        Class outClass = config.getOutClass();
        config.setRewriteFunction(inClass, outClass, new BodyRewrite());

        Mono<?> modifiedBody = serverRequest.bodyToMono(inClass)
                .flatMap(originalBody -> config.getRewriteFunction().apply(exchange, originalBody))
                .switchIfEmpty(Mono.defer(() -> (Mono) config.getRewriteFunction().apply(exchange, null)));
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, config.getOutClass());
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        if (config.getContentType() != null) {
            headers.set(HttpHeaders.CONTENT_TYPE, config.getContentType());
        }
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
                    return chain.filter(exchange.mutate().request(decorator).build());
                }));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 3;
    }

    private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(headers);
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    private class BodyRewrite implements RewriteFunction<Map, Map> {
        @SuppressWarnings("unchecked")
        @Override
        public Publisher<Map> apply(ServerWebExchange t, Map u) {
            //在这里添加插入的数据
            if (Objects.isNull(u)){
                u = new HashMap<String, Object>();
            }
            u.put("authorizeData", t.getAttributes().get(AuthProperties.AUTHORIZATION_AUTH_SUCCESS_NAME));
            return Mono.just(u);
        }
    }
}
