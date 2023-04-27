package com.runjian.auth.domain.vo.request;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/4/20 11:42
 */
@Data
public class PostAuthReq {

    /**
     * 请求地址
     */
    private String reqUrl;

    /**
     * 请求方式
     */
    private String reqMethod;

    /**
     * json数据
     */
    private String jsonStr;
}
