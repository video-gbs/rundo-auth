package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Miracle
 * @date 2023/7/19 14:44
 */
@Data
public class PutResourceKvReq {

    /**
     * 资源key
     */
    @NotBlank(message = "资源key不能为空")
    private String resourceKey;

    /**
     * 资源value
     */
    @NotBlank(message = "资源value不能为空")
    private String resourceValue;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    @Size(max = 999, message = "资源名称过长")
    private String resourceName;
}
