package com.runjian.rbac;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Miracle
 * @date 2023/5/29 11:17
 */
@RefreshScope
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(value = {"com.runjian.*"})
public class RbacServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RbacServerApplication.class, args);
    }
}
