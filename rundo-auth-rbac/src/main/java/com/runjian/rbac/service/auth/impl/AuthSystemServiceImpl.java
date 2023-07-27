package com.runjian.rbac.service.auth.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.config.AuthProperties;
import com.runjian.rbac.constant.MethodType;
import com.runjian.rbac.dao.UserMapper;
import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.service.auth.AuthSystemService;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.dto.CacheFuncDto;
import com.runjian.rbac.vo.dto.AuthUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/6/6 16:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthSystemServiceImpl implements AuthSystemService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final CacheService cacheService;

    private final AuthProperties authProperties;

    @Override
    public AuthUserDto getUserAuth(String username) {
        if (authProperties.getAdminUsername().equals(username)) {
            return authProperties.getAdminUser();
        }
        Optional<UserInfo> userInfoOp = userMapper.selectByUsername(username);
        if (userInfoOp.isEmpty() || CommonEnum.getBoolean(userInfoOp.get().getDeleted())) {
            return null;
        }
        UserInfo userInfo = userInfoOp.get();
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setUsername(username);
        authUserDto.setEnabled(!CommonEnum.getBoolean(userInfo.getDisabled()));
        authUserDto.setPassword(userInfo.getPassword());
        authUserDto.setUsingMfa(false);
        authUserDto.setAccountNonLocked(true);
        authUserDto.setCredentialsNonExpired(true);
        LocalDateTime nowTime = LocalDateTime.now();
        if (Objects.nonNull(userInfo.getExpiryEndTime())){
            authUserDto.setAccountNonExpired(!nowTime.isBefore(userInfo.getExpiryStartTime()) && !nowTime.isAfter(userInfo.getExpiryEndTime()));
        }else {
            authUserDto.setAccountNonExpired(true);
        }
        Set<Long> roleIds = userRoleMapper.selectRoleIdByUserId(userInfo.getId());
        if (roleIds.size() == 0) {
            authUserDto.setAuthorities(Collections.EMPTY_SET);
            return authUserDto;
        }
        authUserDto.setAuthorities(roleIds.stream().map(String::valueOf).collect(Collectors.toSet()));
        cacheService.setUserRole(userInfo.getUsername(), roleIds);
        return authUserDto;
    }


    @Override
    public AuthDataDto getAuthDataByUser(String username, String scope, String reqPath, String reqMethod, String queryData, String bodyData) {
        if (authProperties.getAdminUsername().equals(username)) {
            return authProperties.getAdminData();
        }
        AuthDataDto authDataDto = new AuthDataDto();
        authDataDto.setIsAdmin(false);
        authDataDto.setUsername(username);
        List<Long> userRoles = cacheService.getUserRole(username);

        if (Objects.isNull(userRoles) || userRoles.isEmpty()) {
            authDataDto.setMsg("当前用户没有任何角色的权限");
            authDataDto.setIsAuthorized(false);
            return authDataDto;
        }
        authDataDto.setRoleIds(userRoles);
        String funcKey = reqMethod + MarkConstant.MARK_SPLIT_SEMICOLON + reqPath;
        CacheFuncDto funcCache = cacheService.getFuncCache(funcKey);

        // 判断是否是保护的参数
        if (Objects.isNull(funcCache)) {
            authDataDto.setIsAuthorized(true);
            return authDataDto;
        }

        List<String> scopeList = Arrays.asList(scope.split(","));
        // 判断客户端是否有系统领域权限 || 判断客户端是否有当前接口的系统服务权限 || 判断该功能是否有权限角色绑定 || 判断角色是否包含该功能的权限
        if (scopeList.isEmpty() || nonIntersection(scopeList, Arrays.asList("all", funcCache.getScope())) || funcCache.getRoleIds().isEmpty() || nonIntersection(userRoles, funcCache.getRoleIds())) {
            authDataDto.setMsg(String.format("当前用户没有功能'%s'的权限", funcCache.getFuncName()));
            authDataDto.setIsAuthorized(false);
            return authDataDto;
        }

        // 判断是否需要参数校验
        if (CollectionUtils.isEmpty(funcCache.getFuncResourceDataList())) {
            authDataDto.setIsAuthorized(true);
            return authDataDto;
        }

        // 遍历功能参数进行校验
        for (CacheFuncDto.FuncResourceData funcResourceData : funcCache.getFuncResourceDataList()) {
            String key = funcResourceData.getResourceKey();
            String param = funcResourceData.getValidateParam();
            // 判断参数是否为空，若为空交由API自己判断
            if (Objects.isNull(param)) {
                String resourceCacheKey = MarkConstant.REDIS_AUTH_USER_RESOURCE + username + MarkConstant.MARK_SPLIT_SEMICOLON + key;
                authDataDto.getResourceKeyList().add(resourceCacheKey);
                List<String> userResource = cacheService.getUserResource(username, key);
                if (CollectionUtils.isEmpty(userResource)) {
                    cacheService.setUserResourceCache(username, new HashSet<>(userRoles), key);
                }
            } else {
                JSONObject jsonObject;
                if (Objects.equals(reqMethod, MethodType.GET.getMsg()) || Objects.equals(reqMethod, MethodType.DELETE.getMsg())){
                    if (Objects.isNull(queryData) || !StringUtils.hasText(queryData)){
                        authDataDto.setMsg(String.format("必要的参数权限校验失败，缺失参数'%s'", param));
                        authDataDto.setIsAuthorized(false);
                        return authDataDto;
                    }
                    jsonObject = JSONObject.parseObject(queryData);
                }else {
                    if (Objects.isNull(bodyData)) {
                        authDataDto.setMsg(String.format("必要的参数权限校验失败，缺失参数'%s'", param));
                        authDataDto.setIsAuthorized(false);
                        return authDataDto;
                    }
                    jsonObject = JSONObject.parseObject(bodyData);
                }

                String value = jsonObject.getString(param);
                // 判断参数是否存在
                if (Objects.isNull(value)) {
                    authDataDto.setMsg(String.format("必要的参数权限校验失败，缺失参数'%s'", param));
                    authDataDto.setIsAuthorized(false);
                    return authDataDto;
                }

                // 获取角色绑定的资源
                List<String> userResourceValue = cacheService.getUserResource(username, key);
                if (CollectionUtils.isEmpty(userResourceValue)){
                    userResourceValue = cacheService.setUserResourceCache(username, new HashSet<>(userRoles), key);
                    if (CollectionUtils.isEmpty(userResourceValue)){
                        authDataDto.setIsAuthorized(false);
                        authDataDto.setMsg(String.format("当前用户没有资源'%s'的权限", value));
                        return authDataDto;
                    }
                }

                // 判断数据是否是数组
                if (value.startsWith("[") && value.endsWith("]")){
                    List<String> values = jsonObject.getList(param, String.class);
                    // 判断用户是否包含该资源的权限
                    if (!new HashSet<>(userResourceValue).containsAll(values)){
                        authDataDto.setIsAuthorized(false);
                        authDataDto.setMsg(String.format("当前用户没有资源'%s'的权限", value));
                        return authDataDto;
                    }
                } else {
                    // 判断用户是否包含该资源的权限
                    if (!userResourceValue.contains(value)){
                        authDataDto.setIsAuthorized(false);
                        authDataDto.setMsg(String.format("当前用户没有资源'%s'的权限", value));
                        return authDataDto;
                    }
                }
            }
        }
        authDataDto.setIsAuthorized(true);
        return authDataDto;
    }

    @Override
    public AuthDataDto getAuthDataByClient(String scope, String reqPath, String reqMethod) {
        AuthDataDto authDataDto = new AuthDataDto();
        Boolean result = checkFuncAuth(scope, reqPath, reqMethod);
        if (Objects.isNull(result) || result){
            authDataDto.setIsAuthorized(true);
            return authDataDto;
        }
        authDataDto.setIsAuthorized(false);
        authDataDto.setMsg("客户端无访问该功能的权限");
        return authDataDto;
    }

    private Boolean checkFuncAuth(String scope, String reqPath, String reqMethod){
        String funcKey = reqMethod + MarkConstant.MARK_SPLIT_SEMICOLON + reqPath;
        CacheFuncDto funcCache = cacheService.getFuncCache(funcKey);
        // 判断是否是保护的参数
        if (Objects.isNull(funcCache)) {
            return true;
        }
        List<String> scopeList = Arrays.asList(scope.split(","));
        if (scopeList.isEmpty() || nonIntersection(scopeList, Arrays.asList("all", funcCache.getScope()))) {
            return false;
        }
        return null;
    }


    /**
     * 判断两个数组存在交集
     * @param aList 数组a
     * @param bList 数组b
     * @return 布尔
     */
    private static <T> boolean nonIntersection(List<T> aList, List<T> bList) {
        for (T data : aList) {
            if (bList.contains(data)) {
                return false;
            }
        }
        return true;
    }

}
