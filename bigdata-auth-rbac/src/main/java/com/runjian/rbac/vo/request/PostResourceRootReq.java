package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 添加资源根节点请求体
 * @author Miracle
 * @date 2023/7/4 19:34
 */
@Data
public class PostResourceRootReq {

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    private String resourceName;

    /**
     * 资源key
     */
    @NotBlank(message = "资源key不能为空")
    private String resourceKey;

    /**
     * 资源值
     */
    @NotBlank(message = "资源Value不能为空")
    private String resourceValue;
}
