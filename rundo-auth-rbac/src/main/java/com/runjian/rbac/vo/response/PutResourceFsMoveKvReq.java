package com.runjian.rbac.vo.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Miracle
 * @date 2023/7/18 19:28
 */
@Data
public class PutResourceFsMoveKvReq {

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
     * 资源value
     */
    @NotBlank(message = "父资源value不能为空")
    private String pResourceValue;
}
