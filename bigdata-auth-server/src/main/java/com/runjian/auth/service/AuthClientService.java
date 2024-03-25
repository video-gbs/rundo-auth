package com.runjian.auth.service;

import com.github.pagehelper.PageInfo;
import com.runjian.auth.vo.response.AuthClientPage;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/8/14 9:42
 */
public interface AuthClientService {

    /**
     * 分页查询Client
     * @param clientId 客户端id
     * @param clientName 客户端名称
     * @return
     */
    PageInfo<AuthClientPage> getClientPage(int page, int num, String clientId, String clientName);

    /**
     * @param clientId 客户端id
     * @param clientSecret 客户端密码
     * @param clientName 客户端名称
     * @param clientAuthenticationMethods 客户端授权方式
     * @param authorizationGrantTypes 用户授权登录方式
     * @param scopes 授权范围
     * @param redirectUris 授权跳转url
     * @param requireAuthorizationConsent 是否需要用户授权
     * @param accessTokenTimeToLiveSecond 授权token持续时间，单位：秒
     * @param refreshTokenTimeToLiveSecond 刷新token持续时间，单位：秒
     * @param authCodeTimeToLiveSecond 授权code持续时间
     * @param clientSecretExpiresAt 密码过期时间
     */
    void addClient(String clientId, String clientSecret,
                   String clientName, Set<Integer> clientAuthenticationMethods,
                   Set<Integer> authorizationGrantTypes, Set<String> scopes,
                   Set<String> redirectUris, Boolean requireAuthorizationConsent,
                   Long accessTokenTimeToLiveSecond, Long refreshTokenTimeToLiveSecond,
                   Long authCodeTimeToLiveSecond, LocalDateTime clientSecretExpiresAt);

    /**
     *
     * @param id 主键Id
     * @param clientSecret 客户端密码
     * @param clientName 客户端名称
     * @param clientAuthenticationMethods 客户端授权方式
     * @param authorizationGrantTypes 用户授权登录方式
     * @param scopes 授权范围
     * @param redirectUris 授权跳转url
     * @param requireAuthorizationConsent 是否需要用户授权
     * @param accessTokenTimeToLiveSecond 授权token持续时间，单位：秒
     * @param refreshTokenTimeToLiveSecond 刷新token持续时间，单位：秒
     * @param authCodeTimeToLiveSecond 授权code持续时间
     * @param clientSecretExpiresAt 密码过期时间
     */
    void updateClient(String id, String clientSecret,
                      String clientName, Set<Integer> clientAuthenticationMethods,
                      Set<Integer> authorizationGrantTypes, Set<String> scopes,
                      Set<String> redirectUris, Boolean requireAuthorizationConsent,
                      Long accessTokenTimeToLiveSecond, Long refreshTokenTimeToLiveSecond,
                      Long authCodeTimeToLiveSecond, LocalDateTime clientSecretExpiresAt);

    /**
     * 删除客户端
     * @param id 主键id
     */
    void deleteClient(String id);

}
