package com.runjian.rbac.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/7/17 20:00
 */
@Data
public class GetCatalogueResourceRsp {

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源id
     */
    private String resourceValue;

    /**
     * 层级名称
     */
    private String levelName;
}
