package com.runjian.rbac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName com.runjian.rbac.AuthServerApplication
 * @Description 应用启动类
 * @date 2022-12-22 周四 14:41
 */
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
@MapperScan(basePackages = "com.runjian.rbac.mapper")
@ComponentScan(value = {"com.runjian.*"})
public class RbacServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RbacServerApplication.class, args);
    }
}
