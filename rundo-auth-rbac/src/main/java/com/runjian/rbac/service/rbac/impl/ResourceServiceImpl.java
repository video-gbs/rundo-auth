package com.runjian.rbac.service.rbac.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.constant.ResourceType;
import com.runjian.rbac.dao.relation.FuncResourceMapper;
import com.runjian.rbac.dao.ResourceMapper;
import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.service.rbac.ResourceService;
import com.runjian.rbac.vo.AbstractTreeInfo;
import com.runjian.rbac.vo.response.GetResourceTreeRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

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



    @Override
    public GetResourceTreeRsp getResourceTree(String resourceKey, Boolean isIncludeResource) {
        List<GetResourceTreeRsp> resourceInfoList = resourceMapper.selectAllByResourceKeyAndResourceType(resourceKey, isIncludeResource);
        GetResourceTreeRsp root = GetResourceTreeRsp.getRoot(resourceKey);
        root.setChildList(root.recursionData(resourceInfoList, root.getLevel()));
        return root;
    }


    @Override
    public void batchAddResource(Long resourcePid, Integer resourceType, String resourceName, String resourceKey, Set<String> resourceValues) {
        if (resourceValues.size() == 0){
            return;
        }
        ResourceInfo pResourceInfo;
        if (resourcePid.equals(0L)){
            pResourceInfo = new ResourceInfo();
            pResourceInfo.setLevel("0");
        }else {
            pResourceInfo = dataBaseService.getResourceInfo(resourcePid);
        }
        Set<String> existValues = resourceMapper.selectResourceValueByResourceKeyAndResourceValueIn(resourceKey, resourceValues);
        if (existValues.size() > 0){
            resourceValues.removeAll(existValues);
        }
        List<ResourceInfo> resourceInfoList = new ArrayList<>(resourceValues.size());
        LocalDateTime nowTime = LocalDateTime.now();
        long sort = nowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        for (String resourceValue : resourceValues){
            ResourceInfo resourceInfo = new ResourceInfo();
            resourceInfo.setResourcePid(resourcePid);
            resourceInfo.setResourceType(resourceType);
            resourceInfo.setResourceName(resourceName);
            resourceInfo.setResourceKey(resourceKey);
            resourceInfo.setResourceValue(resourceValue);
            resourceInfo.setSort(sort++);
            resourceInfo.setUpdateTime(nowTime);
            resourceInfo.setCreateTime(nowTime);
            if (resourcePid.equals(0L)){
                resourceInfo.setLevel("0");
            }else {
                resourceInfo.setLevel(pResourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId());
            }
        }
        resourceMapper.batchAdd(resourceInfoList);
    }

    @Override
    public void updateResource(Long resourceId, String resourceName, String resourceValue) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(resourceId);
        resourceInfo.setResourceName(resourceName);
        resourceInfo.setResourceValue(resourceValue);
        resourceInfo.setUpdateTime(LocalDateTime.now());
        resourceMapper.update(resourceInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Set<Long> resourceIds) {
        if (resourceIds.size() == 0){
            return;
        }
        funcResourceMapper.deleteAllByResourceIds(resourceIds);
        resourceMapper.batchDelete(resourceIds);
    }

    @Override
    public void fsMove(Long id, Long pid) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(id);
        ResourceInfo pResourceInfo;
        String level;
        if (pid.equals(0L)){
            pResourceInfo = new ResourceInfo();
            pResourceInfo.setId(0L);
            pResourceInfo.setLevel("0");
            pResourceInfo.setResourceType(ResourceType.CATALOGUE.getCode());
            level = pResourceInfo.getLevel();
        }else {
            pResourceInfo = dataBaseService.getResourceInfo(pid);
            level = pResourceInfo.getLevel()+ MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId();
        }
        if (pResourceInfo.getResourcePid().equals(resourceInfo.getResourcePid())){
            return;
        }
        if (pResourceInfo.getResourceType().equals(ResourceType.RESOURCE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "资源只能移动在目录下");
        }
        if (pResourceInfo.getLevel().startsWith(resourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + resourceInfo.getId())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不可将父部门移动到子部门");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        String oldLevel = resourceInfo.getLevel();
        resourceInfo.setLevel(level);
        resourceInfo.setResourcePid(pid);
        resourceInfo.setSort(nowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        resourceInfo.setUpdateTime(nowTime);
        List<ResourceInfo> childList = resourceMapper.selectAllLikeByLevel(oldLevel + MarkConstant.MARK_SPLIT_RAIL + resourceInfo.getId());
        for (ResourceInfo child : childList){
            child.setUpdateTime(nowTime);
            child.setLevel(resourceInfo.getLevel() + child.getLevel().substring(0, oldLevel.length()));
        }
        childList.add(resourceInfo);
        resourceMapper.updateAll(childList);
    }

    @Override
    public void btMove(Long id, Integer moveOp) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(id);
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
            }
        }
    }
}
