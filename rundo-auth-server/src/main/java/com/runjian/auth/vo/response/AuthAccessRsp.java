package com.runjian.auth.vo.response;

import lombok.Data;

/**
 * 授权返回体
 * @author Miracle
 * @date 2023/6/19 19:57
 */
@Data
public class AuthAccessRsp {

    /**
     * 授权token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 范围
     */
    private String scope;

    /**
     * token类型
     */
    private String tokenType;

    /**
     * 过期时间
     */
    private Long expiresIn;
}
