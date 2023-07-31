package com.runjian.rbac.service.auth.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.constant.ResourceType;
import com.runjian.rbac.dao.ResourceMapper;
import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.vo.dto.CacheFuncDto;
import com.runjian.rbac.vo.response.GetResourceRootRsp;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

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
    public void setAllFuncCache(Map<String, String> funcCacheMap){
        RMap<Object, Object> redisMap = redissonClient.getMap(MarkConstant.REDIS_AUTH_FUNC);
        redisMap.delete();
        redisMap.putAll(funcCacheMap);
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
    public boolean isUserResourceExist(String resourceKey) {
        return redissonClient.getMap(MarkConstant.REDIS_AUTH_USER_RESOURCE + resourceKey).isExists();
    }

    @Override
    public List<String> getUserResource(String username, String resourceKey){
        Object data = redissonClient.getMap(MarkConstant.REDIS_AUTH_USER_RESOURCE + resourceKey).get(username);
        if (Objects.isNull(data)){
            return null;
        }
        return JSONArray.parseArray(data.toString()).toList(String.class);
    }

    @Override
    public void setUserResourceCache(String username, Set<Long> roleIds) {
        this.removeUserResourceByUsername(username);
        if (CollectionUtils.isEmpty(roleIds)){
            return;
        }
        Set<ResourceInfo> resourceInfos = resourceMapper.selectByRoleIds(roleIds);
        if (!resourceInfos.isEmpty()) {
            Set<String> resourceKeys = resourceInfos.stream().map(ResourceInfo::getResourceKey).collect(Collectors.toSet());
            Map<Integer, List<ResourceInfo>> typeMap = resourceInfos.stream().collect(Collectors.groupingBy(ResourceInfo::getResourceType));

            // 获取按照ResourceKey分类的目录数组
            List<ResourceInfo> catalogueList = typeMap.get(ResourceType.CATALOGUE.getCode());
            Map<String, List<ResourceInfo>> catalogueKeyMap = null;
            if (!CollectionUtils.isEmpty(catalogueList)){
                catalogueKeyMap = catalogueList.stream().collect(Collectors.groupingBy(ResourceInfo::getResourceKey));
            }
            // 获取按照ResourceKey分类的资源数组
            List<ResourceInfo> resourceList = typeMap.get(ResourceType.RESOURCE.getCode());
            Map<String, List<ResourceInfo>> resourceKeyMap = null;
            if (!CollectionUtils.isEmpty(resourceList)){
                resourceKeyMap = resourceList.stream().collect(Collectors.groupingBy(ResourceInfo::getResourceKey));
            }
            // 循环所有的资源组
            for (String resourceKey : resourceKeys) {
                List<String> resourceValueList = getResourceValueList(resourceKey,
                        Objects.nonNull(catalogueKeyMap) ? catalogueKeyMap.getOrDefault(resourceKey, null) : null,
                        Objects.nonNull(resourceKeyMap) ? resourceKeyMap.getOrDefault(resourceKey, null) : null
                );
                redissonClient.getMap(MarkConstant.REDIS_AUTH_USER_RESOURCE + resourceKey).put(username, JSONArray.toJSONString(resourceValueList));
            }
        }
    }

    @Override
    public List<String> setUserResourceCache(String username, Set<Long> roleIds, String resourceKey) {
        if (CollectionUtils.isEmpty(roleIds)){
            return null;
        }
        Set<ResourceInfo> resourceInfos = resourceMapper.selectByRoleIdsAndResourceKey(roleIds, resourceKey);
        if (!resourceInfos.isEmpty()){
            List<ResourceInfo> catalogueList = resourceInfos.stream().filter(resourceInfo -> resourceInfo.getResourceType().equals(ResourceType.CATALOGUE.getCode())).toList();
            List<ResourceInfo> resourceList = resourceInfos.stream().filter(resourceInfo -> resourceInfo.getResourceType().equals(ResourceType.RESOURCE.getCode())).toList();
            List<String> resourceValueList = getResourceValueList(resourceKey, catalogueList, resourceList);
            redissonClient.getMap(MarkConstant.REDIS_AUTH_USER_RESOURCE + resourceKey).put(username, JSONArray.toJSONString(resourceValueList));
            return resourceValueList;
        }
        return null;
    }

    private List<String> getResourceValueList(String resourceKey, List<ResourceInfo> catalogueList, List<ResourceInfo> resourceList) {
        List<String> resourceValueList = new ArrayList<>();
        // 判断资源是否为空
        if (!CollectionUtils.isEmpty(catalogueList)) {
            // 提取所有的Pid
            Set<Long> cataloguePids = catalogueList.stream().map(ResourceInfo::getResourcePid).collect(Collectors.toSet());
            // 判断资源数组是否为空
            if (!CollectionUtils.isEmpty(resourceList)) {
                cataloguePids.addAll(resourceList.stream().map(ResourceInfo::getResourcePid).collect(Collectors.toSet()));
            }
            // 过滤所有的父类数据
            List<ResourceInfo> childCatalogueList = catalogueList.stream().filter(catalogueInfo -> !cataloguePids.contains(catalogueInfo.getId())).toList();
            // 添加目录节点
            resourceValueList.addAll(childCatalogueList.stream().map(ResourceInfo::getResourceValue).toList());
            // 获取level
            List<String> childCatalogueLevelList = childCatalogueList.stream().map(resourceInfo -> resourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + resourceInfo.getId()).toList();
            // 查询目录下的资源
            if (!childCatalogueLevelList.isEmpty()) {
                for (String level : childCatalogueLevelList) {
                    resourceValueList.addAll(resourceMapper.selectResourceValueByResourceKeyAndLevelLike(resourceKey, level));
                }
            }
        }
        // 判断资源数组是否为空，不为空的话，将资源添加进去
        if (!CollectionUtils.isEmpty(resourceList)){
            resourceValueList.addAll(resourceList.stream().map(ResourceInfo::getResourceValue).toList());
        }

        return resourceValueList;
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
