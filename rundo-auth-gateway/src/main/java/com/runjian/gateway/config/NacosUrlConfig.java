package com.runjian.gateway.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miracle
 * @date 2023/4/23 15:35
 */
@Configuration
@RequiredArgsConstructor
public class NacosUrlConfig {

    private final NacosServiceManager nacosServiceManager;

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    private final static String HTTP_PREFIX = "http://";

    private final static String PORT_SPILT = ":";

    public String getServiceIpPort(String instanceName) {
        NamingService namingService = nacosServiceManager.getNamingService();
        Instance instance = null;
        try {
            instance = namingService.selectOneHealthyInstance(instanceName, nacosDiscoveryProperties.getGroup());
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return HTTP_PREFIX + instance.getIp() + PORT_SPILT + instance.getPort();
    }
}
