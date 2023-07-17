package com.runjian.rbac.service.auth.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.dao.*;
import com.runjian.rbac.dao.relation.RoleFuncMapper;
import com.runjian.rbac.dao.relation.RoleMenuMapper;
import com.runjian.rbac.dao.relation.RoleResourceMapper;
import com.runjian.rbac.entity.RoleInfo;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.feign.AuthServerApi;
import com.runjian.rbac.service.auth.AuthUserService;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.utils.AuthUtils;
import com.runjian.rbac.vo.AbstractTreeInfo;
import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.response.GetFuncRsp;
import com.runjian.rbac.vo.response.GetMenuTreeRsp;
import com.runjian.rbac.vo.response.GetResourceTreeRsp;
import com.runjian.rbac.vo.response.GetUserRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/6/15 17:33
 */
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

    private final RoleResourceMapper roleResourceMapper;

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
        getUserRsp.setRoleNames(roleMapper.selectByUserId(userInfo.getId()).stream().map(RoleInfo::getRoleName).collect(Collectors.toSet()));
        if (Objects.equals(userInfo.getSectionId(), 0L)){
            getUserRsp.setSectionName("根节点");
        }else {
            getUserRsp.setSectionName(dataBaseService.getSectionInfo(userInfo.getSectionId()).getSectionName());
        }
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
            if (authData.getRoleIds().size() == 0){
                return Collections.EMPTY_LIST;
            }
            Set<Long> menuIds = roleMenuMapper.selectMenuIdByRoleIds(authData.getRoleIds());
            cmenuTreeRspList = menuMapper.selectAllByLevelNumStartAndLevelNumEndAndMenuIdsIn(levelNumStart, levelNumEnd, menuIds);
        }
        if (cmenuTreeRspList.size() == 0){
            return Collections.EMPTY_LIST;
        }
        int i = levelNumStart;
        while (pMenuTreeRspList.size() == 0 && i <= levelNumEnd){
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
        if (authData.getRoleIds().size() == 0){
            return Collections.EMPTY_LIST;
        }
        List<Long> funcIds = roleFuncMapper.selectFuncIdByRoleIds(authData.getRoleIds());
        if (funcIds.size() == 0){
            return Collections.EMPTY_LIST;
        }
        return funcMapper.selectAllByMenuIdAndFuncIds(menuId, funcIds);
    }

    @Override
    public GetResourceTreeRsp getResource(String resourceKey, Boolean isIncludeResource) {
        AuthDataDto authData = authUtils.getAuthData();
        List<GetResourceTreeRsp> resourceInfoList;
        GetResourceTreeRsp root = GetResourceTreeRsp.getRoot(resourceKey);
        if (authData.getIsAdmin()){
            resourceInfoList = resourceMapper.selectAllByResourceKeyAndResourceType(resourceKey, isIncludeResource);
        }else {
            if (authData.getRoleIds().size() == 0){
                return root;
            }
            Set<Long> resourceIds = roleResourceMapper.selectResourceIdByRoleIds(authData.getRoleIds());
            resourceInfoList = resourceMapper.selectAllByResourceKeyAndResourceTypeAndResourceIdsIn(resourceKey, isIncludeResource, resourceIds);
        }
        if (resourceInfoList.size() == 0){
            return root;
        }
        root.setChildList(root.recursionData(resourceInfoList, root.getLevel()));
        return root;
    }
}
