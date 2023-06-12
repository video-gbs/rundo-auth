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
     * 获取功能信息
     * @param methodPath 方法路径key
     */
    CacheFuncDto getFuncCache(String methodPath);

    /**
     * 设置功能信息
     * @param methodPath 方法路径key
     * @param funcCache 缓存数据
     */
    void setFuncCache(String methodPath, CacheFuncDto funcCache);

    /**
     * 获取资源层级关系
     * @param keyValue 资源keyValue
     */
    String getResourceLevel(String keyValue);

    /**
     * 设置资源与资源层级关系
     * @param keyValue 资源keyValue
     * @param level 资源层级
     */
    void setResourceLevel(String keyValue, String level);

    /**
     * 获取用户资源层级信息
     * @param username 用户名
     */
    List<String> getUserResource(String username, String resourceKey);

    /**
     * 设置用户资源缓存
     * @param key
     * @param resources
     */
    void setUserResource(String key, Map<String, Set<String>> resources);

}
