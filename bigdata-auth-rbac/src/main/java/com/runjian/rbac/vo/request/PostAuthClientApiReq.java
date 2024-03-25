package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Miracle
 * @date 2023/6/8 17:07
 */
@Data
public class PostAuthClientApiReq {

    /**
     * 请求范围
     */
    @NotBlank(message = "授权范围不能为空")
    private String scope;

    /**
     * 请求地址
     */
    @NotBlank(message = "请求路径不能为空")
    private String reqPath;

    /**
     * 请求方法
     */
    @NotBlank(message = "请求方法不能为空")
    private String reqMethod;
}
