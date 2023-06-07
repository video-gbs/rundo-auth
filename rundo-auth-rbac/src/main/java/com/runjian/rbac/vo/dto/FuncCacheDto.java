package com.runjian.rbac.vo.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/7 14:24
 */
@Data
public class FuncCacheDto {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 角色数组
     */
    private List<Long> roleIds;

    /**
     * 资源组
     */
    private List<FuncResourceData> funcResourceDataList;


    @Data
    public static class FuncResourceData{

        /**
         * 资源组
         */
        private String resourceKey;

        /**
         * 校验的参数
         */
        private String validateParam;

    }
}
