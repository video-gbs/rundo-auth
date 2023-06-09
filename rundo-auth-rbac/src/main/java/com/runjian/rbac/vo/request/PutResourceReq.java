package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改资源请求体
 * @author Miracle
 * @date 2023/6/9 17:11
 */
@Data
public class PutResourceReq {

    /**
     * 资源id
     */
    @NotNull(message = "资源id不能为空")
    @Min(value = 1, message = "非法资源id")
    private Long resourceId;

    /**
     * 资源组名称
     */
    @NotBlank(message = "资源组名称不能为空")
    @Size(max = 100, message = "资源组名称的范围在1~100")
    private String groupName;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    @Size(max = 100, message = "资源名称的范围在1~100")
    private String resourceName;

    /**
     * 资源Key
     */
    @NotBlank(message = "资源key不能为空")
    @Size(max = 50, message = "资源key的范围在1~50")
    private String resourceKey;

    /**
     * 资源value
     */
    @NotBlank(message = "资源value不能为空")
    @Size(max = 50, message = "资源value的范围在1~50")
    private String resourceValue;
}
