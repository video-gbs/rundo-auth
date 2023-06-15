package com.runjian.rbac.service.auth.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.dao.MenuMapper;
import com.runjian.rbac.dao.RoleMapper;
import com.runjian.rbac.dao.UserMapper;
import com.runjian.rbac.entity.RoleInfo;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.feign.AuthServerApi;
import com.runjian.rbac.service.auth.AuthUserService;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.utils.AuthUtils;
import com.runjian.rbac.vo.AbstractTreeInfo;
import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.response.GetMenuTreeRsp;
import com.runjian.rbac.vo.response.GetUserRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        getUserRsp.setSectionName(dataBaseService.getSectionInfo(userInfo.getId()).getSectionName());
        return getUserRsp;
    }

    @Override
    public List<GetMenuTreeRsp> getMenu(Integer levelNumStart, Integer levelNumEnd) {
        if (levelNumStart.equals(0)){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "无法选择层级0进行查询");
        }
        AuthDataDto authData = authUtils.getAuthData();
        String username;
        if (authData.getIsAdmin()){
            username = null;
        }else {
            username = authData.getUsername();
        }
        List<GetMenuTreeRsp> pMenuTreeRspList = menuMapper.selectAllByUsernameAndLevelNum(username, levelNumStart);
        List<String> pLevelList = pMenuTreeRspList.stream().map(GetMenuTreeRsp::getLevel).toList();
        List<GetMenuTreeRsp> cMenuTreeRspList = menuMapper.selectAllByLevelLikeAndLevelNum(pLevelList, levelNumEnd);
        for (GetMenuTreeRsp root : pMenuTreeRspList){
            String level = root.getLevel() + MarkConstant.MARK_SPLIT_RAIL + root.getId();
            root.recursionData(cMenuTreeRspList.stream().filter(cRsp -> cRsp.getLevel().startsWith(root.getLevel())).toList(), level);
        }
        return pMenuTreeRspList;
    }
}
