package com.runjian.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author Miracle
 * @date 2023/4/19 17:39
 */
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
public class AuthGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthGatewayApplication.class, args);
    }
}
