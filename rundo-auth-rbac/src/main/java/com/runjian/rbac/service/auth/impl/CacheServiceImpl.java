package com.runjian.rbac.service.auth.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.vo.dto.FuncCacheDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
        redissonClient.getList(MarkConstant.REDIS_AUTH_USER_ROLE + username).addAll(roleIds);
    }

    @Override
    public FuncCacheDto getFuncCache(String methodPath) {
        return JSONObject.parseObject(redissonClient.getMap(MarkConstant.REDIS_AUTH_FUNC).get(methodPath).toString(), FuncCacheDto.class);
    }

    @Override
    public List<Long> getResourceRole(String keyValue) {
        return JSONArray.parseArray(redissonClient.getMap(MarkConstant.REDIS_AUTH_RESOURCE_ROLE).get(keyValue).toString()).toList(Long.class);
    }
}
