package com.runjian.rbac.config.redis;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.NettyCustomizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName ClientConfig
 * @Description 通过netty心跳机制来补充lettuce断链问题
 * @date 2023-03-09 周四 16:17
 */
@Configuration
public class ClientConfig {

    @Bean
    public ClientResources clientResources() {
        NettyCustomizer nettyCustomizer = new NettyCustomizer() {
            @Override
            public void afterChannelInitialized(Channel channel) {
                channel.pipeline().addLast(
                        new IdleStateHandler(40, 0, 0)
                );
                channel.pipeline().addLast(new ChannelDuplexHandler() {
                    @Override
                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                        if (evt instanceof IdleStateEvent) {
                            ctx.disconnect();
                        }
                    }
                });
                NettyCustomizer.super.afterChannelInitialized(channel);
            }

            @Override
            public void afterBootstrapInitialized(Bootstrap bootstrap) {
            }
        };
        return ClientResources.builder().nettyCustomizer(nettyCustomizer).build();
    }
}
