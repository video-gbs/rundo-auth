package com.runjian.rbac.service.rbac.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.constant.ResourceType;
import com.runjian.rbac.dao.relation.FuncResourceMapper;
import com.runjian.rbac.dao.ResourceMapper;
import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.service.rbac.ResourceService;
import com.runjian.rbac.vo.AbstractTreeInfo;
import com.runjian.rbac.vo.response.GetResourceRootRsp;
import com.runjian.rbac.vo.response.GetResourceTreeRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/6/6 11:11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceMapper resourceMapper;

    private final FuncResourceMapper funcResourceMapper;

    private final DataBaseService dataBaseService;

    private final CacheService cacheService;

    @Override
    public GetResourceTreeRsp getResourceTree(String resourceKey, Boolean isIncludeResource) {
        Optional<GetResourceTreeRsp> rootOp = resourceMapper.selectRootByResourceKey(resourceKey);
        if (rootOp.isEmpty()){
            return null;
        }
        GetResourceTreeRsp root = rootOp.get();
        List<GetResourceTreeRsp> resourceInfoList = resourceMapper.selectChildByResourceKeyAndResourceType(resourceKey, isIncludeResource);
        root.setChildList(root.recursionData(resourceInfoList, root.getLevel() + MarkConstant.MARK_SPLIT_RAIL + root.getId()));
        return root;
    }

    @Override
    public void addResourceRoot(String resourceKey, String resourceName, String resourceValue){
        Optional<GetResourceTreeRsp> rootOp = resourceMapper.selectRootByResourceKey(resourceKey);
        if (rootOp.isPresent()){
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, "根节点已存在，不能重复创建");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setResourceType(ResourceType.CATALOGUE.getCode());
        resourceInfo.setLevel("0");
        resourceInfo.setResourcePid(0L);
        resourceInfo.setSort(0L);
        resourceInfo.setResourceName(resourceName);
        resourceInfo.setResourceKey(resourceKey);
        resourceInfo.setResourceValue(resourceValue);
        resourceInfo.setUpdateTime(nowTime);
        resourceInfo.setCreateTime(nowTime);
        resourceMapper.save(resourceInfo);
    }

    @Override
    public List<GetResourceRootRsp> getResourceRoot() {
        return resourceMapper.selectAllRoot();
    }


    @Override
    public void batchAddResource(Long resourcePid, Integer resourceType, Map<String, String> resourceMap) {
        if (resourceMap.size() == 0){
            return;
        }

        ResourceInfo pResourceInfo = dataBaseService.getResourceInfo(resourcePid);
        if (pResourceInfo.getResourceType().equals(ResourceType.RESOURCE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不能在资源下添加资源");
        }
        Set<String> existValues = resourceMapper.selectResourceValueByResourceKeyAndResourceValueIn(pResourceInfo.getResourceKey(), resourceMap.keySet());
        if (existValues.size() > 0){
            for (Map.Entry<String, String> resource: resourceMap.entrySet()){
                if (existValues.contains(resource.getKey())){
                    resourceMap.remove(resource.getValue());
                }
            }
        }
        List<ResourceInfo> resourceInfoList = new ArrayList<>(resourceMap.size());
        LocalDateTime nowTime = LocalDateTime.now();
        long sort = nowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        for (Map.Entry<String, String> resource: resourceMap.entrySet()){
            ResourceInfo resourceInfo = new ResourceInfo();
            resourceInfo.setResourcePid(resourcePid);
            resourceInfo.setResourceType(resourceType);
            resourceInfo.setResourceName(resource.getValue());
            resourceInfo.setResourceKey(pResourceInfo.getResourceKey());
            resourceInfo.setResourceValue(resource.getKey());
            resourceInfo.setSort(sort++);
            resourceInfo.setUpdateTime(nowTime);
            resourceInfo.setCreateTime(nowTime);
            resourceInfo.setLevel(pResourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId());
            cacheService.setResourceLevel(resourceInfo.getResourceKey() + MarkConstant.MARK_SPLIT_SEMICOLON + resourceInfo.getResourceValue(), resourceInfo.getLevel());
            resourceInfoList.add(resourceInfo);
        }
        if (resourceInfoList.size() > 0){
            resourceMapper.batchAdd(resourceInfoList);
        }

    }

    @Override
    public void updateResource(Long resourceId, String resourceName, String resourceValue) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(resourceId);
        String key = resourceInfo.getResourceKey() + MarkConstant.MARK_SPLIT_SEMICOLON + resourceInfo.getResourceValue();
        cacheService.removeResourceLevel(key);
        resourceInfo.setResourceName(resourceName);
        resourceInfo.setResourceValue(resourceValue);
        resourceInfo.setUpdateTime(LocalDateTime.now());
        resourceMapper.update(resourceInfo);
        cacheService.setResourceLevel(key, resourceInfo.getLevel());
    }

    @Override
    public void delete(Long resourceId) {
        ResourceInfo pResourceInfo = dataBaseService.getResourceInfo(resourceId);
        List<ResourceInfo> resourceInfoList = resourceMapper.selectAllLikeByLevel(pResourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId());
        resourceInfoList.add(pResourceInfo);
        for (ResourceInfo resourceInfo : resourceInfoList){
            cacheService.removeResourceLevel(resourceInfo.getResourceKey() + MarkConstant.MARK_SPLIT_SEMICOLON + resourceInfo.getResourceValue());
        }
        Set<Long> resourceIds = resourceInfoList.stream().map(ResourceInfo::getId).collect(Collectors.toSet());
        funcResourceMapper.deleteAllByResourceIds(resourceIds);
        resourceMapper.batchDelete(resourceIds);
    }

    @Override
    public void fsMove(Long id, Long pid) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(id);
        ResourceInfo pResourceInfo = dataBaseService.getResourceInfo(pid);
        String level = pResourceInfo.getLevel()+ MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId();
        if (resourceInfo.getResourcePid().equals(0L)){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不能移动根节点");
        }
        if (pResourceInfo.getResourceType().equals(ResourceType.RESOURCE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "资源只能移动在目录下");
        }
        if (resourceInfo.getLevel().startsWith(pResourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不可将父节点移动到子节点下");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        String oldLevel = resourceInfo.getLevel();
        resourceInfo.setLevel(level);
        resourceInfo.setResourcePid(pid);
        resourceInfo.setSort(nowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        resourceInfo.setUpdateTime(nowTime);
        cacheService.setResourceLevel(resourceInfo.getResourceKey() + MarkConstant.MARK_SPLIT_SEMICOLON + resourceInfo.getResourceValue(), resourceInfo.getLevel());
        List<ResourceInfo> childList = resourceMapper.selectAllLikeByLevel(oldLevel + MarkConstant.MARK_SPLIT_RAIL + resourceInfo.getId());
        for (ResourceInfo child : childList){
            child.setUpdateTime(nowTime);
            child.setLevel(resourceInfo.getLevel() + child.getLevel().substring(0, oldLevel.length()));
            cacheService.setResourceLevel(child.getResourceKey() + MarkConstant.MARK_SPLIT_SEMICOLON + child.getResourceValue(), child.getLevel());
        }
        childList.add(resourceInfo);
        resourceMapper.updateAll(childList);
    }

    @Override
    public void btMove(Long id, Integer moveOp) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(id);
        if (resourceInfo.getResourcePid().equals(0L)){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不能移动根节点");
        }
        List<ResourceInfo> brotherList = resourceMapper.selectChildByPid(resourceInfo.getResourcePid());
        if (brotherList.size() == 1){
            return;
        }
        brotherList.sort(Comparator.comparing(ResourceInfo::getSort));
        for (int i = 0; i < brotherList.size(); i++){
            ResourceInfo pointData = brotherList.get(i);
            if (pointData.getId().equals(id)){
                ResourceInfo brother;
                if (moveOp.equals(1)){
                    if (i == 0){
                        return;
                    }
                    brother = brotherList.get(i - 1);
                }else if (moveOp.equals(0)){
                    if (i == brotherList.size() - 1){
                        return;
                    }
                    brother = brotherList.get(i + 1);
                } else {
                    throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "未知的移动操作");
                }
                LocalDateTime nowTime = LocalDateTime.now();
                Long oldSort = brother.getSort();
                brother.setSort(pointData.getSort());
                pointData.setSort(oldSort);
                brother.setUpdateTime(nowTime);
                pointData.setUpdateTime(nowTime);
                resourceMapper.updateAll(Arrays.asList(brother, pointData));
                return;
            }
        }
    }
}
