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
import com.runjian.rbac.vo.response.GetCatalogueResourceRsp;
import com.runjian.rbac.vo.response.GetResourceRootRsp;
import com.runjian.rbac.vo.response.GetResourceTreeRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public List<GetCatalogueResourceRsp> getCatalogueResource(Long pid, Boolean isIncludeChild) {
        ResourceInfo pResourceInfo = dataBaseService.getResourceInfo(pid);
        if (!Objects.equals(pResourceInfo.getResourceType(), ResourceType.CATALOGUE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, String.format("节点 %s 不是目录", pResourceInfo.getResourceName()));
        }
        List<ResourceInfo> resourceInfoList;
        if (isIncludeChild){
            resourceInfoList = resourceMapper.selectAllLikeByLevel(pResourceInfo.getLevel());
        }else {
            resourceInfoList = resourceMapper.selectChildByPid(pid);
        }

        if (CollectionUtils.isEmpty(resourceInfoList)){
            return null;
        }
        Map<Long, List<Long>> levelMap = new HashMap<>(resourceInfoList.size());
        for (ResourceInfo resourceInfo: resourceInfoList){
            levelMap.put(resourceInfo.getId(), Arrays.stream(resourceInfo.getLevel().split(MarkConstant.MARK_SPLIT_RAIL)).map(Long::parseLong).toList());
        }
        Set<Long> levelIds = new HashSet<>();
        for (List<Long> idList : levelMap.values()){
            levelIds.addAll(idList);
        }
        Map<Long, String> resourceNameMap = resourceMapper.selectAllByIdIn(levelIds).stream().collect(Collectors.toMap(ResourceInfo::getId, ResourceInfo::getResourceName));

        List<GetCatalogueResourceRsp> getCatalogueResourceRspList = new ArrayList<>(resourceInfoList.size());
        for (ResourceInfo resourceInfo : resourceInfoList){
            GetCatalogueResourceRsp getCatalogueResourceRsp = new GetCatalogueResourceRsp();
            getCatalogueResourceRsp.setResourceValue(resourceInfo.getResourceValue());
            List<Long> ids = levelMap.get(resourceInfo.getId());
            StringBuilder stringBuilder = new StringBuilder();
            for (Long id : ids){
                stringBuilder.append("/");
                stringBuilder.append(resourceNameMap.get(id));
            }
            getCatalogueResourceRsp.setLevelName(stringBuilder.toString());
            getCatalogueResourceRspList.add(getCatalogueResourceRsp);
        }
        return getCatalogueResourceRspList;
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
            resourceInfoList.add(resourceInfo);
        }
        if (resourceInfoList.size() > 0){
            resourceMapper.batchAdd(resourceInfoList);
            cacheService.removeUserResourceByResourceKey(pResourceInfo.getResourceKey());
        }

    }

    @Override
    public void updateResource(Long resourceId, String resourceName, String resourceValue) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(resourceId);
        resourceInfo.setResourceName(resourceName);
        resourceInfo.setResourceValue(resourceValue);
        resourceInfo.setUpdateTime(LocalDateTime.now());
        resourceMapper.update(resourceInfo);
        cacheService.removeUserResourceByResourceKey(resourceInfo.getResourceKey());
    }

    @Override
    public void delete(Long resourceId) {
        ResourceInfo pResourceInfo = dataBaseService.getResourceInfo(resourceId);
        List<ResourceInfo> resourceInfoList = resourceMapper.selectAllLikeByLevel(pResourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId());
        resourceInfoList.add(pResourceInfo);
        Set<Long> resourceIds = resourceInfoList.stream().map(ResourceInfo::getId).collect(Collectors.toSet());
        funcResourceMapper.deleteAllByResourceIds(resourceIds);
        resourceMapper.batchDelete(resourceIds);
        cacheService.removeUserResourceByResourceKey(pResourceInfo.getResourceKey());
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
        List<ResourceInfo> childList = resourceMapper.selectAllLikeByLevel(oldLevel + MarkConstant.MARK_SPLIT_RAIL + resourceInfo.getId());
        childList.add(resourceInfo);
        resourceMapper.updateAll(childList);
        cacheService.removeUserResourceByResourceKey(pResourceInfo.getResourceKey());
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
