package com.runjian.rbac.service.auth;

import com.runjian.rbac.vo.dto.FuncCacheDto;

import java.util.List;
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
     */
    FuncCacheDto getFuncCache(String methodPath);

    /**
     * 获取资源的角色信息
     */
    List<Long> getResourceRole(String keyValue);

}
