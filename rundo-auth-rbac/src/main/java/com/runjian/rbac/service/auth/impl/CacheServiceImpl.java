package com.runjian.rbac.service.auth.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.dao.ResourceMapper;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.vo.dto.CacheFuncDto;
import com.runjian.rbac.vo.response.GetResourceRootRsp;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private final ResourceMapper resourceMapper;


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
        Object cacheData = redissonClient.getMap(MarkConstant.REDIS_AUTH_FUNC).get(methodPath);
        if (Objects.isNull(cacheData)){
            return null;
        }
        return JSONObject.parseObject(cacheData.toString(), CacheFuncDto.class);
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
    public List<String> getUserResource(String username, String resourceKey){
        Object data = redissonClient.getMap(MarkConstant.REDIS_AUTH_USER_RESOURCE + resourceKey).get(username);
        if (Objects.isNull(data)){
            return null;
        }
        return JSONArray.parseArray(JSONArray.toJSONString(data)).toList(String.class);
    }

    @Override
    public void setUserResource(String username, String resourceKey, List<String> resourceValue){
        redissonClient.getMap(MarkConstant.REDIS_AUTH_USER_RESOURCE + resourceKey).put(username, JSONArray.toJSONString(resourceValue));
    }

    @Override
    public void removeUserResourceByUsername(String username) {
        List<GetResourceRootRsp> getResourceRootRsps = resourceMapper.selectAllRoot();
        for (GetResourceRootRsp root :getResourceRootRsps){
            redissonClient.getMap(MarkConstant.REDIS_AUTH_USER_RESOURCE + root.getResourceKey()).remove(username);
        }
    }

    @Override
    public void removeUserResourceByResourceKey(String resourceKey) {
        redissonClient.getMap(MarkConstant.REDIS_AUTH_USER_RESOURCE + resourceKey).delete();
    }

}
