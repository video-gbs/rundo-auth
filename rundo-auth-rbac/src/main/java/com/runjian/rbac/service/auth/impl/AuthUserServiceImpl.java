package com.runjian.rbac.service.auth.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.constant.ResourceType;
import com.runjian.rbac.dao.*;
import com.runjian.rbac.dao.relation.RoleFuncMapper;
import com.runjian.rbac.dao.relation.RoleMenuMapper;
import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.feign.AuthServerApi;
import com.runjian.rbac.service.auth.AuthUserService;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.utils.AuthUtils;
import com.runjian.rbac.vo.AbstractTreeInfo;
import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/6/15 17:33
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {


    private final AuthUtils authUtils;

    private final AuthServerApi authServerApi;

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final DataBaseService dataBaseService;

    private final MenuMapper menuMapper;

    private final RoleMenuMapper roleMenuMapper;

    private final RoleFuncMapper roleFuncMapper;

    private final FuncMapper funcMapper;

    private final ResourceMapper resourceMapper;

    private final CacheService cacheService;

    @Override
    public void logout() {
        authServerApi.logout(authUtils.getAuthToken());
    }

    @Override
    public GetUserRsp getUser() {
        AuthDataDto authData = authUtils.getAuthData();
        GetUserRsp getUserRsp = new GetUserRsp();
        getUserRsp.setUsername(authData.getUsername());
        if (authData.getIsAdmin()){
            getUserRsp.setRoleNames(Set.of("超级系统管理员"));
            getUserRsp.setWorkName(authData.getUsername());
            getUserRsp.setSectionName("系统管理");
            return getUserRsp;
        }
        Optional<UserInfo> userInfoOp = userMapper.selectByUsername(authData.getUsername());
        if (userInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("用户%s不存在", authData.getUsername()));
        }
        UserInfo userInfo = userInfoOp.get();
        getUserRsp.setWorkName(userInfo.getWorkName());
        getUserRsp.setWorkNum(userInfo.getWorkNum());
        getUserRsp.setPhone(userInfo.getPhone());
        getUserRsp.setExpiryEndTime(userInfo.getExpiryEndTime());
        Set<Long> roleIds = new HashSet<>(cacheService.getUserRole(userInfo.getUsername()));
        getUserRsp.setRoleNames(roleMapper.selectRoleNameByIds(roleIds));
        if (Objects.equals(userInfo.getSectionId(), 0L)){
            getUserRsp.setSectionName("根节点");
        }else {
            getUserRsp.setSectionName(dataBaseService.getSectionInfo(userInfo.getSectionId()).getSectionName());
        }
        cacheService.setUserResourceCache(userInfo.getUsername(), roleIds);
        return getUserRsp;
    }

    @Override
    public List<GetMenuTreeRsp> getMenu(Integer levelNumStart, Integer levelNumEnd) {
        if (levelNumStart.equals(0)){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "无法选择层级0进行查询");
        }
        AuthDataDto authData = authUtils.getAuthData();

        List<GetMenuTreeRsp> pMenuTreeRspList = new ArrayList<>();
        List<GetMenuTreeRsp> cmenuTreeRspList;
        if (authData.getIsAdmin()){
            cmenuTreeRspList = menuMapper.selectAllByLevelNumStartAndLevelNumEnd(levelNumStart, levelNumEnd);
        }else {
            if (authData.getRoleIds().isEmpty()){
                return Collections.EMPTY_LIST;
            }
            Set<Long> menuIds = roleMenuMapper.selectMenuIdByRoleIds(authData.getRoleIds());
            if (menuIds.isEmpty()){
                return Collections.EMPTY_LIST;
            }
            cmenuTreeRspList = menuMapper.selectAllByLevelNumStartAndLevelNumEndAndMenuIdsIn(levelNumStart, levelNumEnd, menuIds);
        }
        if (cmenuTreeRspList.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        int i = levelNumStart;
        while (pMenuTreeRspList.isEmpty() && i <= levelNumEnd){
            int finalI = i;
            pMenuTreeRspList = cmenuTreeRspList.stream().filter(getMenuTreeRsp -> getMenuTreeRsp.getLevelNum().equals(finalI)).toList();
            i++;
        }
        cmenuTreeRspList.removeAll(pMenuTreeRspList);
        for (GetMenuTreeRsp root : pMenuTreeRspList){
            String level = root.getLevel() + MarkConstant.MARK_SPLIT_RAIL + root.getId();
            root.setChildList(root.recursionData(cmenuTreeRspList.stream().filter(cRsp -> cRsp.getLevel().startsWith(level)).toList(), level));
        }
        return pMenuTreeRspList.stream().sorted(Comparator.comparing(AbstractTreeInfo::getSort)).toList();
    }

    @Override
    public List<GetFuncRsp> getFunc(Integer menuId) {
        AuthDataDto authData = authUtils.getAuthData();
        if (authData.getIsAdmin()){
            return funcMapper.selectAllByMenuId(menuId);
        }
        if (authData.getRoleIds().isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<Long> funcIds = roleFuncMapper.selectFuncIdByRoleIds(authData.getRoleIds());
        if (funcIds.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return funcMapper.selectAllByMenuIdAndFuncIds(menuId, funcIds);
    }

    @Override
    public GetResourceTreeRsp getCatalogueResource(String resourceKey) {
        AuthDataDto authData = authUtils.getAuthData();
        List<GetResourceTreeRsp> resourceInfoList;
        List<GetResourceTreeRsp> allResourceList;
        Optional<GetResourceTreeRsp> rootOp = resourceMapper.selectRootByResourceKey(resourceKey);
        if (rootOp.isEmpty()){
            return null;
        }
        GetResourceTreeRsp root = rootOp.get();
        if (authData.getIsAdmin()){
            List<GetResourceTreeRsp> resourceInfos = resourceMapper.selectAllByResourceKey(resourceKey);
            resourceInfoList = resourceInfos.stream().filter(catalogueInfo -> catalogueInfo.getResourceType().equals(ResourceType.CATALOGUE.getCode())).toList();
            allResourceList = resourceInfos.stream().filter(catalogueInfo -> catalogueInfo.getResourceType().equals(ResourceType.RESOURCE.getCode())).toList();
        }else {
            if (CollectionUtils.isEmpty(authData.getRoleIds())){
                return null;
            }
            // 刷新缓存
            cacheService.refreshUserResourceRefreshMark(resourceKey, authData.getUsername(), new HashSet<>(authData.getRoleIds()));

            List<GetResourceTreeRsp> roleResourceRsp = resourceMapper.selectByRoleIdsAndResourceKeyAndResourceType(new HashSet<>(authData.getRoleIds()), resourceKey);
            resourceInfoList = new ArrayList<>(roleResourceRsp.stream().filter(catalogueInfo -> catalogueInfo.getResourceType().equals(ResourceType.CATALOGUE.getCode())).toList());
            if (CollectionUtils.isEmpty(roleResourceRsp)){
                return null;
            }
            Set<Long> cataloguePids = roleResourceRsp.stream().map(GetResourceTreeRsp::getResourcePid).collect(Collectors.toSet());
            // 过滤所有的父类数据
            List<GetResourceTreeRsp> catalogueRoleResourceRsp = roleResourceRsp.stream()
                    .filter(catalogueInfo -> !cataloguePids.contains(catalogueInfo.getId()))
                    .filter(catalogueInfo -> catalogueInfo.getResourceType().equals(ResourceType.CATALOGUE.getCode()))
                    .toList();

            for (GetResourceTreeRsp rsp : catalogueRoleResourceRsp){
                resourceInfoList.addAll(resourceMapper.selectByResourceKeyAndResourceTypeAndLevelLike(resourceKey, ResourceType.CATALOGUE.getCode(), rsp.getLevel() + MarkConstant.MARK_SPLIT_RAIL + rsp.getId()));
            }
            allResourceList = roleResourceRsp.stream().filter(catalogueInfo -> catalogueInfo.getResourceType().equals(ResourceType.RESOURCE.getCode())).toList();

        }
        log.warn("allResourceList:{}", allResourceList);
        for (GetResourceTreeRsp rsp : resourceInfoList){
            rsp.setResourceNum(allResourceList.stream().filter(resourceInfo -> {
                if (rsp.getLevel().equals("0")){
                    return true;
                } else return resourceInfo.getLevel().startsWith(rsp.getLevel() + MarkConstant.MARK_SPLIT_RAIL + rsp.getId());
            }).count());
        }
        log.warn("getCatalogueResource Rsp:{}", resourceInfoList);

        root.setChildList(root.recursionData(resourceInfoList, root.getLevel() + MarkConstant.MARK_SPLIT_RAIL + root.getId()));
        return root;
    }

    @Override
    public List<GetCatalogueResourceRsp> getResourceByCatalogue(Long pid, Boolean isIncludeChild) {
        AuthDataDto authData = authUtils.getAuthData();
        ResourceInfo pResourceInfo = dataBaseService.getResourceInfo(pid);
        if (!Objects.equals(pResourceInfo.getResourceType(), ResourceType.CATALOGUE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, String.format("节点 %s 不是目录", pResourceInfo.getResourceName()));
        }
        List<ResourceInfo> resourceInfoList;
        if (authData.getIsAdmin()){
            if (isIncludeChild){
                resourceInfoList = resourceMapper.selectByLevelLikeAndResourceType(pResourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId());
            }else {
                resourceInfoList = resourceMapper.selectByPidAndResourceType(pid);
            }
        }else {
            // 刷新缓存
            cacheService.refreshUserResourceRefreshMark(pResourceInfo.getResourceKey(), authData.getUsername(), new HashSet<>(authData.getRoleIds()));
            List<String> userResourceList = cacheService.getUserResource(authData.getUsername(), pResourceInfo.getResourceKey());
            if (!CollectionUtils.isEmpty(userResourceList)){
                if (isIncludeChild){
                    resourceInfoList = resourceMapper.selectByLevelLikeAndResourceValueIn(pResourceInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pResourceInfo.getId(), userResourceList);
                }else {
                    resourceInfoList = resourceMapper.selectByPidAndResourceValueIn(pid, userResourceList);
                }
            }else {
                return Collections.EMPTY_LIST;
            }
        }

        if (CollectionUtils.isEmpty(resourceInfoList)){
            return Collections.EMPTY_LIST;
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
            getCatalogueResourceRsp.setResourceId(resourceInfo.getId());
            getCatalogueResourceRsp.setResourceName(resourceInfo.getResourceName());
            getCatalogueResourceRsp.setResourceValue(resourceInfo.getResourceValue());
            getCatalogueResourceRsp.setParentResourceId(resourceInfo.getResourcePid());
            List<Long> ids = levelMap.get(resourceInfo.getId());
            StringBuilder stringBuilder = new StringBuilder();
            for (Long id : ids){
                if (id.equals(0L)){
                    if (ids.size() == 1){
                        stringBuilder.append("/");
                        stringBuilder.append(resourceInfo.getResourceName());
                    }else {
                        continue;
                    }
                }
                stringBuilder.append("/");
                stringBuilder.append(resourceNameMap.get(id));
            }
            getCatalogueResourceRsp.setLevelName(stringBuilder.toString());
            getCatalogueResourceRspList.add(getCatalogueResourceRsp);
        }
        return getCatalogueResourceRspList;
    }

    @Override
    public void refreshUserResource(String resourceKey) {
        AuthDataDto authData = authUtils.getAuthData();
        if (authData.getIsAdmin()){
            return;
        }
        cacheService.refreshUserResourceRefreshMark(resourceKey, authData.getUsername(), new HashSet<>(authData.getRoleIds()));
    }

    @Override
    public List<String> getUserResource(String resourceKey) {
        AuthDataDto authData = authUtils.getAuthData();
        if (authData.getIsAdmin()){
            return resourceMapper.selectResourceValueByResourceKeyAndResourceType(resourceKey, ResourceType.RESOURCE.getCode());
        }
        return cacheService.getUserResource(authData.getUsername(), resourceKey);
    }

}
