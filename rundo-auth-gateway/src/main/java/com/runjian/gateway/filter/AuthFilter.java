package com.runjian.gateway.filter;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.gateway.common.BusinessErrorEnums;
import com.runjian.gateway.common.CommonResponse;
import com.runjian.gateway.config.AuthProperties;
import com.runjian.gateway.config.NacosUrlConfig;
import com.runjian.gateway.utils.CheckUtils;
import com.runjian.gateway.vo.PostAuthReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Miracle
 * @date 2023/4/23 9:27
 */
@Slf4j
@RequiredArgsConstructor
public class AuthFilter implements GatewayFilter, Ordered {

    private final NacosUrlConfig nacosUrlConfig;

    private final AuthProperties authProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        if (CheckUtils.checkPath(uri.getPath(), authProperties.getAuthPrefix())){
            return chain.filter(exchange);
        }
        String authToken = request.getHeaders().getFirst(AuthProperties.AUTHORIZATION_HEADER_TOKEN);

        if (Objects.isNull(authToken)){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatusCode.valueOf(BusinessErrorEnums.USER_AUTH_ERROR.getState()));
            return response.writeAndFlushWith(Mono.just(ByteBufMono.just(response.bufferFactory().wrap(JSONObject.toJSONString(CommonResponse.failure(BusinessErrorEnums.USER_AUTH_ERROR)).getBytes()))));
        }
        Flux<DataBuffer> bodyData = exchange.getAttribute(AuthProperties.REQUEST_BODY_NAME);
        PostAuthReq postAuthReq;
        if (Objects.nonNull(bodyData)){
            String raw = toRaw(bodyData);
            postAuthReq = new PostAuthReq(uri.getPath(), exchange.getRequest().getMethod().name(), JSONObject.toJSONString(raw));
        }else {
            postAuthReq = new PostAuthReq(uri.getPath(), exchange.getRequest().getMethod().name(), null);
        }

        return WebClient.builder()
                .baseUrl(nacosUrlConfig.getServiceIpPort(authProperties.getAuthServerName()))
                .build()
                .post()
                .uri(authProperties.getAuthAddr())
                .header(AuthProperties.AUTHORIZATION_HEADER_TOKEN, authToken)
                //.body(BodyInserters.fromMultipartData(map))
                .body(Mono.just(postAuthReq), PostAuthReq.class)
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    CommonResponse<?> commonResponse = switch (HttpStatus.valueOf(ex.getStatusCode().value())){
                        case UNAUTHORIZED -> CommonResponse.failure(BusinessErrorEnums.USER_AUTH_ERROR);
                        case FORBIDDEN -> CommonResponse.failure(BusinessErrorEnums.USER_NO_AUTH);
                        default -> CommonResponse.failure(BusinessErrorEnums.USER_AUTH_ERROR, ex.getMessage());
                    };
                    return Mono.just(commonResponse);
                }).doOnSuccess(commonResponse -> {
                    if (commonResponse.isError()){
                        ServerHttpResponse response = exchange.getResponse();
                        BusinessErrorEnums errorEnums = BusinessErrorEnums.getByCode(commonResponse.getCode());
                        response.setStatusCode(HttpStatusCode.valueOf(errorEnums.getState()));
                        exchange.getAttributes().put(AuthProperties.AUTHORIZATION_AUTH_FAILURE_NAME, commonResponse);
                    }else {
                        exchange.getAttributes().put(AuthProperties.AUTHORIZATION_AUTH_SUCCESS_NAME, commonResponse.getData());
                    }
                })
                .then(chain.filter(exchange));

    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

    private static String toRaw(Flux<DataBuffer> body) {
        AtomicReference<String> rawRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            rawRef.set(Strings.fromUTF8ByteArray(bytes));
        });
        return rawRef.get();
    }
}
