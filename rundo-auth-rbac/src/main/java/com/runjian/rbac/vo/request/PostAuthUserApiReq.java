package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Miracle
 * @date 2023/4/20 11:42
 */
@Data
public class PostAuthUserApiReq {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 范围
     */
    @NotBlank(message = "授权范围不能为空")
    private String scope;

    /**
     * 请求方式
     */
    @NotBlank(message = "请求方法不能为空")
    private String reqMethod;

    /**
     * 请求地址
     */
    @NotBlank(message = "请求路径不能为空")
    private String reqPath;

    /**
     * json数据
     */
    private String jsonStr;
}
