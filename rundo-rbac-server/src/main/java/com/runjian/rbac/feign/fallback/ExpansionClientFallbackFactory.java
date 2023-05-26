package com.runjian.rbac.feign.fallback;

import com.runjian.rbac.feign.ExpansionClient;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName ExpansionClientCallBack
 * @Description
 * @date 2023-02-20 周一 15:03
 */
@Slf4j
@Component
public class ExpansionClientFallbackFactory implements FallbackFactory<ExpansionClient> {

    @Override
    public ExpansionClient create(Throwable cause) {
        return areaId -> {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "扩展服务", "feign--查询区域绑定状态失败", areaId, cause);
            return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
        };
    }
}
