package com.runjian.rbac.service.auth.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.config.AuthProperties;
import com.runjian.rbac.constant.MethodType;
import com.runjian.rbac.constant.ResourceType;
import com.runjian.rbac.dao.ResourceMapper;
import com.runjian.rbac.dao.UserMapper;
import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.service.auth.AuthSystemService;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.dto.CacheFuncDto;
import com.runjian.rbac.vo.dto.AuthUserDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
        authUserDto.setEnabled(!CommonEnum.getBoolean(userInfo.getDisabled()));
        authUserDto.setPassword(userInfo.getPassword());
        authUserDto.setUsingMfa(false);
        authUserDto.setAccountNonLocked(true);
        authUserDto.setCredentialsNonExpired(true);
        LocalDateTime nowTime = LocalDateTime.now();
        authUserDto.setAccountNonExpired(!nowTime.isBefore(userInfo.getExpiryStartTime()) && !nowTime.isAfter(userInfo.getExpiryEndTime()));
        Set<Long> roleIds = userRoleMapper.selectRoleIdByUserId(userInfo.getId());
        if (roleIds.size() == 0) {
            authUserDto.setAuthorities(Collections.EMPTY_SET);
            return authUserDto;
        }
        authUserDto.setAuthorities(roleIds.stream().map(String::valueOf).collect(Collectors.toSet()));
        setUserResourceCache(username, roleIds);

        return authUserDto;
    }

    /**
     * 设置用户资源缓存
     * @param username 用户名
     * @param roleIds 角色
     */
    private void setUserResourceCache(String username, Set<Long> roleIds) {
        cacheService.setUserRole(username, roleIds);
        Set<ResourceInfo> resourceInfos = resourceMapper.selectByRoleIds(roleIds);
        if (resourceInfos.size() > 0) {
            Set<String> resourceKeys = resourceInfos.stream().map(ResourceInfo::getResourceKey).collect(Collectors.toSet());
            Map<Integer, List<ResourceInfo>> typeMap = resourceInfos.stream().collect(Collectors.groupingBy(ResourceInfo::getResourceType));

            // 获取按照ResourceKey分类的目录数组
            Map<String, List<ResourceInfo>> catalogueKeyMap = typeMap.get(ResourceType.CATALOGUE.getCode()).stream().collect(Collectors.groupingBy(ResourceInfo::getResourceKey));
            // 获取按照ResourceKey分类的资源数组
            Map<String, List<ResourceInfo>> resourceKeyMap = typeMap.get(ResourceType.RESOURCE.getCode()).stream().collect(Collectors.groupingBy(ResourceInfo::getResourceKey));

            // 循环所有的资源组
            for (String resourceKey : resourceKeys) {

                // 资源检验数组
                List<ResourceInfo> validResourceList = resourceKeyMap.get(resourceKey);

                // 目录检验数组
                List<ResourceInfo> validCatalogueList = catalogueKeyMap.get(resourceKey);
                // 初始化资源List
                List<String> resourceValueList = new ArrayList<>();
                // 判断资源是否为空
                if (!CollectionUtils.isEmpty(validCatalogueList)) {
                    // 提取所有的level
                    Set<String> catalogueLevel = validCatalogueList.stream().map(ResourceInfo::getLevel).collect(Collectors.toSet());
                    // 判断资源数组是否为空
                    if (!CollectionUtils.isEmpty(validResourceList)) {
                        catalogueLevel.addAll(validResourceList.stream().map(ResourceInfo::getLevel).collect(Collectors.toSet()));
                    }
                    // 过滤所有的父类数据
                    List<String> childCatalogueLevelList = validCatalogueList.stream().filter(catalogueInfo -> {
                        for (String level : catalogueLevel) {
                            // 判断是否是相等的
                            if (catalogueInfo.getLevel().equals(level)) {
                                continue;
                            }
                            // 判断是否是父类
                            if (catalogueInfo.getLevel().startsWith(level)) {
                                return false;
                            }
                        }
                        return true;
                    }).map(ResourceInfo::getLevel).toList();

                    // 查询目录下的资源
                    if (childCatalogueLevelList.size() > 0) {
                        resourceValueList.addAll(
                                resourceMapper.selectAllByResourceKey(resourceKey, ResourceType.RESOURCE.getCode()).stream().filter(resourceInfo -> {
                                    for (String level : childCatalogueLevelList) {
                                        if (resourceInfo.getLevel().startsWith(level)) {
                                            return true;
                                        }
                                    }
                                    return false;
                                }).map(ResourceInfo::getResourceValue).toList());
                    }
                }
                // 判断资源数组是否为空，不为空的话，将资源添加进去
                if (!CollectionUtils.isEmpty(validResourceList)){
                    resourceValueList.addAll(validResourceList.stream().map(ResourceInfo::getResourceValue).toList());
                }
                cacheService.setUserResource(username, resourceKey, resourceValueList);
            }
        }
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
                String resourceCacheKey = MarkConstant.REDIS_AUTH_USER_RESOURCE + username + MarkConstant.MARK_SPLIT_SEMICOLON + key;
                authDataDto.getResourceKeyList().add(resourceCacheKey);
                List<String> userResource = cacheService.getUserResource(username, key);
                if (Objects.isNull(userResource)) {
                    setUserResourceCache(username, new HashSet<>(userRoles));
                }
            } else {
                JSONObject jsonObject;
                if (Objects.equals(reqMethod, MethodType.GET.getMsg()) || Objects.equals(reqMethod, MethodType.DELETE.getMsg())){
                    if (Objects.isNull(queryData)){
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
