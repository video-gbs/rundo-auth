package com.runjian.rbac.service.auth;

import com.runjian.rbac.vo.dto.CacheFuncDto;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Miracle
 * @date 2023/6/7 9:44
 */
public interface CacheService {

    /**
     * 获取用户的角色信息
     * @param username 用户名
     */
    List<Long> getUserRole(String username);

    /**
     * 设置用户角色信息
     * @param username 用户名
     * @param roleIds 角色id数组
     */
    void setUserRole(String username, Set<Long> roleIds);

    /**
     * 删除缓存
     * @param username 用户
     */
    void removeUserRole(String username);

    /**
     * 获取功能信息
     * @param methodPath 方法路径key
     */
    CacheFuncDto getFuncCache(String methodPath);

    /**
     * 设置全部功能缓存
     * @param funcCacheMap 功能缓存
     */
    void setAllFuncCache(Map<String, String> funcCacheMap);

    /**
     * 设置功能信息
     * @param methodPath 方法路径key
     * @param funcCache 缓存数据
     */
    void setFuncCache(String methodPath, CacheFuncDto funcCache);

    /**
     * 移除功能缓存
     * @param methodPath 方法路径key
     */
    void removeFuncCache(String methodPath);

    /**
     * 获取用户资源
     * @param username 用户名
     * @param resourceKey 资源分组
     * @return 资源
     */
    List<String> getUserResource(String username, String resourceKey);

    /**
     * 设置用户资源
     * @param username 用户名
     * @param resourceKey 资源分组
     * @param resourceValue 资源
     */
    void setUserResource(String username, String resourceKey, List<String> resourceValue);

    /**
     * 移除用户资源缓存
     * @param username 用户名
     */
    void removeUserResourceByUsername(String username);

    /**
     * 移除用户资源缓存
     * @param resourceKey 资源key
     */
    void removeUserResourceByResourceKey(String resourceKey);
}
