package com.runjian.auth.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 客户端分页
 * @author Miracle
 * @date 2023/8/14 9:56
 */
@Data
@Builder
public class GetAuthClientPage {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clientIdIssuedAt;

    /**
     * 客户端密码过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    private Set<String> redirectUris;

    /**
     * 授权范围
     */
    private Set<String> scopes;

    /**
     * 是否需要用户授权
     */
    private Boolean requireAuthConsent;

    /**
     * 授权token的过期时间 单位：秒
     */
    private Long accessTokenTimeToLiveSecond;

    /**
     * 刷新token的过期时间 单位：秒
     */
    private Long refreshTokenTimeToLiveSecond;

    /**
     * 刷新token的过期时间 单位：秒
     */
    private Long authCodeTimeToLiveSecond;

    /**
     * 客户端授权方式
     */
    private Set<Integer> clientAuthenticationMethods;



}
