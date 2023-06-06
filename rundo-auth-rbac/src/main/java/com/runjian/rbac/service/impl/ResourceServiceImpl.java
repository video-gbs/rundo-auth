package com.runjian.rbac.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.rbac.dao.relation.FuncResourceMapper;
import com.runjian.rbac.dao.ResourceMapper;
import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.service.DataBaseService;
import com.runjian.rbac.service.ResourceService;
import com.runjian.rbac.vo.response.GetResourcePageRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

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
    public PageInfo<GetResourcePageRsp> getResourcePage(int page, int num, String name, String resourceKey) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(resourceMapper.selectByNameAndResourceKeyLike(name, resourceKey));
    }

    @Override
    public void batchAddResource(String groupName, String resourceName, String resourceKey, Set<String> resourceValues) {
        if (resourceValues.size() == 0){
            return;
        }
        Set<String> existValues = resourceMapper.selectResourceValueByResourceKeyAndResourceValueIn(resourceKey, resourceValues);
        if (existValues.size() > 0){
            resourceValues.removeAll(existValues);
        }
        resourceMapper.batchAdd(groupName, resourceName, resourceKey, resourceValues, LocalDateTime.now());
    }

    @Override
    public void updateResource(Long resourceId, String groupName, String resourceName, String resourceKey, String resourceValue) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(resourceId);
        resourceInfo.setGroupName(groupName);
        resourceInfo.setResourceName(resourceName);
        resourceInfo.setResourceKey(resourceKey);
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
}
