package com.runjian.rbac.feign;

import com.runjian.rbac.feign.fallback.ExpansionClientFallbackFactory;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName ExpansionClient
 * @Description
 * @date 2023-02-20 周一 14:58
 */
@FeignClient(name = "device-expansion", fallbackFactory = ExpansionClientFallbackFactory.class)
public interface ExpansionClient {

    @GetMapping("/internalPower/videoArea/bindCheck")
    CommonResponse<Boolean> videoAreaBindCheck(@RequestParam Long areaId);
}
