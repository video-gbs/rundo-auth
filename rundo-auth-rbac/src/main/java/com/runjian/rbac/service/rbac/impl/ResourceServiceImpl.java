package com.runjian.rbac.service.rbac.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.constant.ResourceType;
import com.runjian.rbac.dao.relation.FuncResourceMapper;
import com.runjian.rbac.dao.ResourceMapper;
import com.runjian.rbac.dao.relation.RoleResourceMapper;
import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.service.rbac.ResourceService;
import com.runjian.rbac.vo.response.GetResourceRootRsp;
import com.runjian.rbac.vo.response.GetResourceTreeRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

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

    private final DataSourceTransactionManager dataSourceTransactionManager;

    private final RoleResourceMapper roleResourceMapper;

    private final TransactionDefinition transactionDefinition;

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
        cacheService.removeUserResourceByResourceKey(resourceKey);
    }

    @Override
    public List<GetResourceRootRsp> getResourceRoot() {
        return resourceMapper.selectAllRoot();
    }


    @Override
    public void batchAddResource(Long resourcePid, Integer resourceType, Map<String, String> resourceMap) {
        if (resourceMap.isEmpty()){
            return;
        }
        addResource(dataBaseService.getResourceInfo(resourcePid), resourceType, resourceMap);

    }

    @Override
    public void batchAddResourceByKv(String resourceKey, String pResourceValue, Integer resourceType, Map<String, String> resourceMap) {
        if (resourceMap.isEmpty()){
            return;
        }
        addResource(dataBaseService.getResourceInfo(resourceKey, pResourceValue), resourceType, resourceMap);
    }

    private void addResource(ResourceInfo pResourceInfo, Integer resourceType, Map<String, String> resourceMap) {
        if (pResourceInfo.getResourceType().equals(ResourceType.RESOURCE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不能在资源下添加资源");
        }
        Set<String> existValues = resourceMapper.selectResourceValueByResourceKeyAndResourceValueIn(pResourceInfo.getResourceKey(), resourceMap.keySet());
        if (!existValues.isEmpty()){
            for (String resourceValue : existValues){
                resourceMap.remove(resourceValue);
            }
        }
        List<ResourceInfo> resourceInfoList = new ArrayList<>(resourceMap.size());
        LocalDateTime nowTime = LocalDateTime.now();
        long sort = nowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        for (Map.Entry<String, String> resource: resourceMap.entrySet()){
            ResourceInfo resourceInfo = new ResourceInfo();
            resourceInfo.setResourcePid(pResourceInfo.getId());
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
        if (!resourceInfoList.isEmpty()){
            TransactionStatus transaction = dataSourceTransactionManager.getTransaction(transactionDefinition);
            try {
                resourceMapper.batchAdd(resourceInfoList);
                cacheService.removeUserResourceByResourceKey(pResourceInfo.getResourceKey());
                dataSourceTransactionManager.commit(transaction);
            }catch (Exception exception){
                log.error(LogTemplate.ERROR_LOG_TEMPLATE, "资源服务", "批量添加失败", exception);
                dataSourceTransactionManager.rollback(transaction);
            }
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
        delete(pResourceInfo);
    }

    private void delete(ResourceInfo pResourceInfo) {
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            List<ResourceInfo> resourceInfoList = resourceMapper.selectAllLikeByLevel(pResourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId());
            resourceInfoList.add(pResourceInfo);
            Set<Long> resourceIds = resourceInfoList.stream().filter(resourceInfo -> Objects.equals(resourceInfo.getResourceType(), ResourceType.CATALOGUE.getCode())).map(ResourceInfo::getId).collect(Collectors.toSet());
            if (resourceInfoList.size() > resourceIds.size()){
                throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "删除失败，该节点下或者下级节点下存在资源");
            }
            funcResourceMapper.deleteAllByResourceIds(resourceIds);
            resourceMapper.batchDelete(resourceIds);
            cacheService.removeUserResourceByResourceKey(pResourceInfo.getResourceKey());
            dataSourceTransactionManager.commit(transaction);
        }catch (Exception exception){
            log.error(LogTemplate.ERROR_LOG_TEMPLATE, "资源服务", "删除失败", exception);
            dataSourceTransactionManager.rollback(transaction);
        }
    }

    @Override
    public void fsMove(Long id, Long pid) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(id);
        ResourceInfo pResourceInfo = dataBaseService.getResourceInfo(pid);
        fsMove(resourceInfo, pResourceInfo);
    }

    private void fsMove(ResourceInfo resourceInfo, ResourceInfo pResourceInfo) {
        String level = pResourceInfo.getLevel()+ MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId();
        if (resourceInfo.getResourcePid().equals(0L)){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不能移动根节点");
        }
        if (pResourceInfo.getResourceType().equals(ResourceType.RESOURCE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "资源只能移动在目录下");
        }
        if (pResourceInfo.getLevel().startsWith(resourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + resourceInfo.getId())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不可将父节点移动到子节点下");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        String oldLevel = resourceInfo.getLevel();
        resourceInfo.setLevel(level);
        resourceInfo.setResourcePid(pResourceInfo.getId());
        resourceInfo.setSort(nowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        resourceInfo.setUpdateTime(nowTime);
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try{
            List<ResourceInfo> childList = resourceMapper.selectAllLikeByLevel(oldLevel + MarkConstant.MARK_SPLIT_RAIL + resourceInfo.getId());
            childList.add(resourceInfo);
            resourceMapper.updateAll(childList);
            roleResourceMapper.deleteAllByResourceId(resourceInfo.getId());
            cacheService.removeUserResourceByResourceKey(pResourceInfo.getResourceKey());
            dataSourceTransactionManager.commit(transaction);
        }catch (Exception exception){
            log.error(LogTemplate.ERROR_LOG_TEMPLATE, "资源服务", "父子移动失败", exception);
            dataSourceTransactionManager.rollback(transaction);
        }
    }

    @Override
    public void btMove(Long id, Integer moveOp) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(id);
        btMove(resourceInfo, moveOp);
    }

    @Override
    public void updateResourceByKv(String resourceKey, String resourceValue, String resourceName) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(resourceKey, resourceValue);
        resourceInfo.setResourceName(resourceName);
        resourceInfo.setUpdateTime(LocalDateTime.now());
        resourceMapper.update(resourceInfo);
    }

    private void btMove(ResourceInfo resourceInfo, Integer moveOp) {
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
            if (pointData.getId().equals(resourceInfo.getId())){
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

    @Override
    public void deleteByResourceByKv(String resourceKey, String resourceValue) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(resourceKey, resourceValue);
        delete(resourceInfo);
    }

    @Override
    public void fsMoveByKv(String resourceKey, String resourceValue, String pResourceValue) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(resourceKey, resourceValue);
        ResourceInfo pResourceInfo = dataBaseService.getResourceInfo(resourceKey, pResourceValue);
        fsMove(resourceInfo, pResourceInfo);
    }

    @Override
    public void btMoveByKv(String resourceKey, String resourceValue, Integer moveOp) {
        ResourceInfo resourceInfo = dataBaseService.getResourceInfo(resourceKey, resourceValue);
        btMove(resourceInfo, moveOp);
    }
}
