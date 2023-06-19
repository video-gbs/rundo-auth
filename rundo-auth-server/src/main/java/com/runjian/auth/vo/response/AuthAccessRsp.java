package com.runjian.auth.vo.response;

import lombok.Data;

/**
 *
 * @author Miracle
 * @date 2023/6/19 19:57
 */
@Data
public class AuthAccessRsp {

    private String accessToken;

    private String refreshToken;

    private String scope;

    private String tokenType;

    private Long expiresIn;
}
