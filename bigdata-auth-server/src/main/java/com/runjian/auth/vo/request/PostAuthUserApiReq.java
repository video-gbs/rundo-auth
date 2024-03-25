package com.runjian.auth.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miracle
 * @date 2023/4/20 11:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * 参数数据
     */
    private String queryData;

    /**
     * body的json数据
     */
    private String bodyData;
}
