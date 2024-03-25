package com.runjian.rbac.vo.response;

import lombok.Data;

/**
 * 获取资源根节点
 * @author Miracle
 * @date 2023/7/4 19:56
 */
@Data
public class GetResourceRootRsp {

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源key
     */
    private String resourceKey;

    /**
     * 资源value
     */
    private String resourceValue;

}
