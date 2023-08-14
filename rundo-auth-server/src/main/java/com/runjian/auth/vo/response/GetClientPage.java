package com.runjian.auth.vo.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 客户端分页
 * @author Miracle
 * @date 2023/8/14 9:56
 */
@Data
public class GetClientPage {

    /**
     * 主键id
     */
    private String id;

    /**
     * 客户端Id
     */
    private String clientId;

    /**
     * 客户端id发布时间
     */
    private LocalDateTime clientIdIssuedAt;

    /**
     * 客户端密码过期时间
     */
    private LocalDateTime clientSecretExpiresAt;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 授权方式
     */
    private Set<Integer> authorizationGrantTypes;

    /**
     * 回调url
     */
    private String redirectUris;

    /**
     * 授权范围
     */
    private Set<String> scopes;

    /**
     * 是否需要用户授权
     */
    private Boolean requireAuthorizationConsent;

    /**
     * 授权token的过期时间 单位：秒
     */
    private Long accessTokenTimeToLiveSecond;

    /**
     * 刷新token的过期时间 单位：秒
     */
    private Long refreshTokenTimeToLiveSecond;
}
