package com.runjian.rbac.service.auth.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.config.AuthProperties;
import com.runjian.rbac.constant.ResourceType;
import com.runjian.rbac.dao.ResourceMapper;
import com.runjian.rbac.dao.UserMapper;
import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.service.auth.AuthService;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.dto.CacheFuncDto;
import com.runjian.rbac.vo.dto.AuthUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final CacheService cacheService;

    private final ResourceMapper resourceMapper;

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
        authUserDto.setEnabled(CommonEnum.getBoolean(userInfo.getDisabled()));
        authUserDto.setPassword(userInfo.getPassword());
        authUserDto.setUsingMfa(false);
        authUserDto.setAccountNonLocked(true);
        authUserDto.setCredentialsNonExpired(true);
        LocalDateTime nowTime = LocalDateTime.now();
        authUserDto.setAccountNonExpired(!nowTime.isBefore(userInfo.getExpiryStartTime()) && !nowTime.isAfter(userInfo.getExpiryEndTime()));
        Set<Long> roleIds = userRoleMapper.selectRoleIdByUserId(userInfo.getId());
        if (roleIds.size() == 0){
            authUserDto.setAuthorities(Collections.EMPTY_SET);
            return authUserDto;
        }
        authUserDto.setAuthorities(roleIds.stream().map(String::valueOf).collect(Collectors.toSet()));
        cacheService.setUserRole(username, roleIds);
        Set<ResourceInfo> resourceInfos = resourceMapper.selectByRoleIds(roleIds);
        if (resourceInfos.size() > 0) {
            Set<String> resourceKeys = resourceInfos.stream().map(ResourceInfo::getResourceKey).collect(Collectors.toSet());
            Map<Integer, List<ResourceInfo>> typeMap = resourceInfos.stream().collect(Collectors.groupingBy(ResourceInfo::getResourceType));

            // 获取按照ResourceKey分类的目录数组
            Map<String, List<ResourceInfo>> catalogueKeyMap = typeMap.get(ResourceType.CATALOGUE.getCode()).stream().collect(Collectors.groupingBy(ResourceInfo::getResourceKey));
            // 获取按照ResourceKey分类的资源数组
            Map<String, List<ResourceInfo>> resourceKeyMap = typeMap.get(ResourceType.RESOURCE.getCode()).stream().collect(Collectors.groupingBy(ResourceInfo::getResourceKey));
            // 创建用资源层级数据
            Map<String, Set<String>> keyLevel = new HashMap<>(resourceKeys.size());
            for (String resourceKey : resourceKeys) {
                // 检验数组
                List<ResourceInfo> validList = resourceKeyMap.get(resourceKey);
                // 目录检验数组
                List<ResourceInfo> list = catalogueKeyMap.get(resourceKey);
                if (Objects.nonNull(validList)) {
                    // 先将资源数据加入过滤完成数组
                    keyLevel.put(resourceKey, validList.stream().map(ResourceInfo::getLevel).collect(Collectors.toSet()));
                    if (Objects.nonNull(list)) {
                        // 将目录数据加入到检验数组
                        validList.addAll(list);
                    }
                } else {
                    validList = list;
                }
                // 执行数据过滤
                if (Objects.nonNull(list) && list.size() > 0) {
                    for (ResourceInfo resourceInfo : list) {
                        validList = validList.stream().filter(validResource -> {
                            if (resourceInfo.getLevel().equals(validResource.getLevel())) {
                                return true;
                            }
                            return !validResource.getLevel().startsWith(resourceInfo.getLevel());
                        }).toList();
                    }
                    if (validList.size() > 0) {
                        keyLevel.put(resourceKey, validList.stream().map(ResourceInfo::getLevel).collect(Collectors.toSet()));
                    }
                }
            }
            cacheService.setUserResource(username, keyLevel);
        }

        return authUserDto;
    }

    @Override
    public AuthDataDto getAuthDataByUser(String username, String scope, String reqPath, String reqMethod, String jsonStr) {
        if (authProperties.getAdminUsername().equals(username)) {
            return authProperties.getAdminData();
        }
        AuthDataDto authDataDto = new AuthDataDto();
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
            authDataDto.setMsg(String.format("当前用户没有功能'%s'的权限", funcKey));
            authDataDto.setIsAuthorized(false);
            return authDataDto;
        }

        // 判断是否需要参数校验
        if (funcCache.getFuncResourceDataList().isEmpty()) {
            authDataDto.setIsAuthorized(true);
            return authDataDto;
        }

        // 遍历功能参数进行校验
        for (CacheFuncDto.FuncResourceData funcResourceData : funcCache.getFuncResourceDataList()) {
            String key = funcResourceData.getResourceKey();
            String param = funcResourceData.getValidateParam();
            // 判断参数是否为空，若为空交由API自己判断
            if (Objects.isNull(param)) {
                authDataDto.getResourceKeyList().add(key);
            } else {
                if (Objects.isNull(jsonStr)) {
                    authDataDto.setMsg(String.format("必要的参数权限校验失败，缺失参数'%s'", param));
                    authDataDto.setIsAuthorized(false);
                    return authDataDto;
                }
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                String value = jsonObject.getString(param);
                // 判断参数是否存在
                if (Objects.isNull(value)) {
                    authDataDto.setMsg(String.format("必要的参数权限校验失败，缺失参数'%s'", param));
                    authDataDto.setIsAuthorized(false);
                    return authDataDto;
                }

                String resourceLevel = cacheService.getResourceLevel(key + MarkConstant.MARK_SPLIT_SEMICOLON + value);

                // 判断是否有权限角色绑定
                if (Objects.nonNull(resourceLevel)) {
                    List<String> userResourceLevel = cacheService.getUserResource(username, key);
                    // 判断角色是否包含该资源的权限
                    for (String level : userResourceLevel) {
                        if (resourceLevel.startsWith(level)) {
                            authDataDto.setIsAuthorized(true);
                            return authDataDto;
                        }
                    }
                }
                authDataDto.setIsAuthorized(false);
                authDataDto.setMsg(String.format("当前用户没有资源'%s'的权限", value));
                return authDataDto;
            }
        }

        authDataDto.setIsAuthorized(true);
        return authDataDto;
    }

    @Override
    public AuthDataDto getAuthDataByClient(String scope, String reqPath, String reqMethod) {
        AuthDataDto authDataDto = new AuthDataDto();
        String funcKey = reqMethod + MarkConstant.MARK_SPLIT_SEMICOLON + reqPath;
        CacheFuncDto funcCache = cacheService.getFuncCache(funcKey);
        // 判断是否是保护的参数
        if (Objects.isNull(funcCache)) {
            authDataDto.setIsAuthorized(true);
            return authDataDto;
        }
        List<String> scopeList = Arrays.asList(scope.split(","));
        if (scopeList.isEmpty() || nonIntersection(scopeList, Arrays.asList("all", funcCache.getScope()))) {
            authDataDto.setMsg(String.format("当前客户端没有功能'%s'的权限", funcKey));
            authDataDto.setIsAuthorized(false);
            return authDataDto;
        }
        authDataDto.setIsAuthorized(true);
        return authDataDto;
    }

    /**
     * 判断两个数组存在交集
     *
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
