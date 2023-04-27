package com.runjian.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Miracle
 * @date 2023/4/23 11:17
 */
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(value = {"com.runjian.*"})
public class AuthResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthResourceApplication.class);
    }
}
