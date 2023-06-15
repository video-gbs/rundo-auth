package com.runjian.auth.vo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 鉴权请求体
 * @author Miracle
 * @date 2023/4/20 11:42
 */
@Data
public class PostAuthReq {

    /**
     * 请求地址
     */
    @NotBlank(message = "请求地址不能为空")
    @Size(max = 999, message = "请求地址过长")
    private String reqUrl;

    /**
     * 请求方式
     */
    @NotBlank(message = "请求方式不能为空")
    @Size(max = 20, message = "非法请求方式")
    private String reqMethod;

    /**
     * json数据
     */
    @Size(max = 999999, message = "json数据体过大，不支持数据验证")
    private String jsonStr;
}
