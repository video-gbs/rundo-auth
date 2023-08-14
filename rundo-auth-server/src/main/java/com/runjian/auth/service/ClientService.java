package com.runjian.auth.service;

import com.github.pagehelper.PageInfo;
import com.runjian.auth.vo.response.GetClientPage;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/8/14 9:42
 */
public interface ClientService {

    /**
     * 分页查询Client
     * @param clientId 客户端id
     * @param clientName 客户端名称
     * @return
     */
    PageInfo<GetClientPage> getClientPage(String clientId, String clientName);

    /**
     * 添加客户端
     * @param clientId 客户端id
     * @param clientSecret 客户端密码
     * @param authorizationGrantTypes 授权登录方式
     * @param scopes 授权范围
     * @param requireAuthorizationConsent 是否需要用户授权
     * @param accessTokenTimeToLiveSecond 授权token过期时间，单位：秒
     * @param refreshTokenTimeToLiveSecond 刷新token过期时间，单位：秒
     */
    void addClient(String clientId, String clientSecret, Set<Integer> authorizationGrantTypes, Set<String> scopes, Boolean requireAuthorizationConsent, Long accessTokenTimeToLiveSecond, Long refreshTokenTimeToLiveSecond);

    /**
     * 修改客户端
     * @param id 主键Id
     * @param clientId 客户端id
     * @param clientSecret 客户端密码
     * @param authorizationGrantTypes 授权登录方式
     * @param scopes 授权范围
     * @param requireAuthorizationConsent 是否需要用户授权
     * @param accessTokenTimeToLiveSecond 授权token过期时间， 单位：秒
     * @param refreshTokenTimeToLiveSecond 刷新token过期时间，单位：秒
     */
    void updateClient(Long id, String clientId, String clientSecret, Set<Integer> authorizationGrantTypes, Set<String> scopes, Boolean requireAuthorizationConsent, Long accessTokenTimeToLiveSecond, Long refreshTokenTimeToLiveSecond);

    /**
     * 删除客户端
     * @param id 主键id
     */
    void deleteClient(Long id);

}
