package com.runjian.rbac.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/8/7 17:32
 */
@Configuration
@AllArgsConstructor
public class DruidConfig {

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
