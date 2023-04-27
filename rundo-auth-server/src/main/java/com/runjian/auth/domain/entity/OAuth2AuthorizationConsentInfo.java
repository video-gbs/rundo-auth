package com.runjian.auth.domain.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Miracle
 * @date 2023/4/26 11:05
 */

@Data
public class OAuth2AuthorizationConsentInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 客户端id
     */
    private String registeredClientId;

    /**
     * 授权名称
     */
    private String principalName;

    /**
     * 授权资源
     */
    private String authorities;

}
