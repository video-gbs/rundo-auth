package com.runjian.rbac.service.auth.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.vo.dto.CacheFuncDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/7 14:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final RedissonClient redissonClient;


    @Override
    public List<Long> getUserRole(String username) {
        return redissonClient.getList(MarkConstant.REDIS_AUTH_USER_ROLE + username).readAll().stream().map(roleId -> Long.parseLong(roleId.toString())).toList();
    }

    @Override
    public void setUserRole(String username, Set<Long> roleIds) {
        RList<Object> rList = redissonClient.getList(MarkConstant.REDIS_AUTH_USER_ROLE + username);
        rList.clear();
        rList.addAll(roleIds);
    }

    @Override
    public void removeUserRole(String username) {
        redissonClient.getList(MarkConstant.REDIS_AUTH_USER_ROLE + username).clear();
    }

    @Override
    public CacheFuncDto getFuncCache(String methodPath) {
        return JSONObject.parseObject(redissonClient.getMap(MarkConstant.REDIS_AUTH_FUNC).get(methodPath).toString(), CacheFuncDto.class);
    }

    @Override
    public void setFuncCache(String methodPath, CacheFuncDto funcCache) {
        redissonClient.getMap(MarkConstant.REDIS_AUTH_FUNC).put(methodPath, JSONObject.toJSONString(funcCache));
    }

    @Override
    public void removeFuncCache(String methodPath) {
        redissonClient.getMap(MarkConstant.REDIS_AUTH_FUNC).remove(methodPath);
    }

    @Override
    public String getResourceLevel(String keyValue) {
        return redissonClient.getMap(MarkConstant.REDIS_AUTH_RESOURCE_ROLE).get(keyValue).toString();
    }

    @Override
    public void setResourceLevel(String keyValue, String level) {
        redissonClient.getMap(MarkConstant.REDIS_AUTH_RESOURCE_ROLE).put(keyValue, level);
    }

    @Override
    public void removeResourceLevel(String keyValue) {
        redissonClient.getMap(MarkConstant.REDIS_AUTH_RESOURCE_ROLE).remove(keyValue);
    }

    @Override
    public List<String> getUserResource(String username, String resourceKey){
       return redissonClient.getList(MarkConstant.REDIS_AUTH_USER_RESOURCE + username+ MarkConstant.MARK_SPLIT_SEMICOLON + resourceKey).readAll().stream().map(Object::toString).toList();
    }

    @Override
    public void setUserResource(String username, Map<String, Set<String>> resourceLevel) {
        for (Map.Entry<String, Set<String>> entry : resourceLevel.entrySet()){
            RList<Object> rList = redissonClient.getList(MarkConstant.REDIS_AUTH_USER_RESOURCE + username + MarkConstant.MARK_SPLIT_SEMICOLON + entry.getKey());
            rList.clear();
            rList.addAll(entry.getValue());
        }
    }
}
