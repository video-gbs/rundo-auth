package com.runjian.auth.config;


import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * druid的拦截器配置
 * @author Miracle
 * @date 2023/8/7 17:32
 */
@Configuration
@AllArgsConstructor
public class DruidFilterConfig {

    @Bean
    public WallFilter wallFilter() {
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        config.setNoneBaseStatementAllow(true);
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(config);
        return wallFilter;
    }
}
