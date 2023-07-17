package com.runjian.rbac.service.rbac.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.constant.MethodType;
import com.runjian.rbac.dao.FuncMapper;
import com.runjian.rbac.dao.RoleMapper;
import com.runjian.rbac.dao.relation.RoleFuncMapper;
import com.runjian.rbac.dao.relation.RoleMenuMapper;
import com.runjian.rbac.dao.relation.RoleResourceMapper;
import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.FuncInfo;
import com.runjian.rbac.entity.RoleInfo;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.service.rbac.RoleService;
import com.runjian.rbac.utils.AuthUtils;
import com.runjian.rbac.vo.dto.CacheFuncDto;
import com.runjian.rbac.vo.response.GetRolePageRsp;
import com.runjian.rbac.vo.response.GetUserRolePageRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Miracle
 * @date 2023/6/2 11:10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    private final UserRoleMapper userRoleMapper;

    private final RoleMenuMapper roleMenuMapper;

    private final RoleResourceMapper roleResourceMapper;

    private final RoleFuncMapper roleFuncMapper;

    private final DataBaseService dataBaseService;

    private final AuthUtils authUtils;

    private final CacheService cacheService;

    private final FuncMapper funcMapper;

    @Override
    public PageInfo<GetRolePageRsp> getRolePage(int page, int num, String roleName, String createBy, LocalDateTime createTimeStart, LocalDateTime createTimeEnd) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(roleMapper.selectPageByRoleNameAndCreateByAndCreateTime(page, num, roleName, createBy, createTimeStart, createTimeEnd));
    }

    @Override
    public PageInfo<GetUserRolePageRsp> getRolePage(int page, int num, Long userId, String roleName) {
        PageHelper.startPage(page, num);
        if (Objects.isNull(userId)){
            return new PageInfo<>(roleMapper.selectPageByRoleName(roleName));
        }
        return new PageInfo<>(roleMapper.selectPageByUserIdAndRoleName(userId, roleName));
    }

    @Override
    public Set<Long> getRoleResourceIds(Long roleId) {
        return roleResourceMapper.selectResourceIdByRoleId(roleId);
    }

    @Override
    public Set<Long> getRoleMenuIds(Long roleId) {
        return roleMenuMapper.selectMenuIdByRoleId(roleId);
    }

    @Override
    public Set<Long> getRoleFuncIdByMenuId(Long roleId, Long menuId) {
        return roleFuncMapper.selectFuncIdByRoleIdAndMenuId(roleId, menuId);
    }

    @Override
    public Set<Long> getRoleFuncId(Long roleId) {
        return roleFuncMapper.selectFuncIdByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(String roleName, String roleDesc, Set<Long> menuIds, Set<Long> funcIds, Set<Long> resourceIds) {
        Optional<RoleInfo> roleInfoOp = roleMapper.selectByRoleName(roleName);
        if (roleInfoOp.isPresent()){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("角色名 %s 已存在，请重新填写", roleName));
        }
        String authUser = authUtils.getAuthData().getUsername();
        LocalDateTime nowTime = LocalDateTime.now();
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleName(roleName);
        roleInfo.setRoleDesc(roleDesc);
        roleInfo.setCreateBy(authUser);
        roleInfo.setCreateTime(nowTime);
        roleInfo.setUpdateTime(nowTime);
        roleMapper.save(roleInfo);

        if (menuIds.size() > 0){
            roleMenuMapper.saveAll(roleInfo.getId(), menuIds, authUser, nowTime);
        }

        if (funcIds.size() > 0){
            roleFuncMapper.saveAll(roleInfo.getId(), funcIds, authUser, nowTime);
        }

        if (resourceIds.size() > 0){
            roleResourceMapper.saveAll(roleInfo.getId(), resourceIds, authUser, nowTime);
        }

        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "角色服务", "添加角色成功", String.format("用户'%s' 执行添加 角色'%s'", authUser, roleInfo));
    }

    @Override
    public void updateDisabled(Long roleId, Integer disabled) {
        RoleInfo roleInfo = dataBaseService.getRoleInfo(roleId);
        roleInfo.setDisabled(disabled);
        roleInfo.setUpdateTime(LocalDateTime.now());
        roleMapper.updateDisabled(roleInfo);
        Set<Long> userIds = userRoleMapper.selectUserIdByRoleId(roleId);
        for (Long userId : userIds){
            UserInfo userInfo = dataBaseService.getUserInfo(userId);
            Set<Long> roleIds = userRoleMapper.selectRoleIdByUserId(userId);
            if (disabled.equals(CommonEnum.DISABLE.getCode())){
                roleIds.remove(roleId);
            }else {
                roleIds.add(roleId);
            }
            cacheService.setUserRole(userInfo.getUsername(), roleIds);
        }
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "角色服务", "禁用角色成功", String.format("用户'%s' 执行禁用 角色'%s'", authUtils.getAuthData().getUsername(), roleInfo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Long roleId, String roleName, String roleDesc, Set<Long> menuIds, Set<Long> funcIds, Set<Long> resourceIds) {
        RoleInfo roleInfo = dataBaseService.getRoleInfo(roleId);
        if (!roleName.equals(roleInfo.getRoleName())){
            Optional<RoleInfo> roleInfoOp = roleMapper.selectByRoleName(roleName);
            if (roleInfoOp.isPresent()){
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("角色名 %s 已存在，请重新填写", roleName));
            }
        }
        String authUser = authUtils.getAuthData().getUsername();
        LocalDateTime nowTime = LocalDateTime.now();
        roleInfo.setRoleName(roleName);
        roleInfo.setRoleDesc(roleDesc);
        roleInfo.setUpdateTime(nowTime);
        roleMapper.update(roleInfo);
        if (Objects.nonNull(menuIds)){
            if (menuIds.size() == 0){
                roleMenuMapper.deleteAllByRoleId(roleId);
            }else {
                Set<Long> existMenuIds = roleMenuMapper.selectMenuIdByRoleId(roleId);
                if (existMenuIds.size() > 0){
                    Set<Long> difference = new HashSet<>(existMenuIds);
                    difference.retainAll(menuIds);
                    existMenuIds.removeAll(difference);
                    menuIds.removeAll(difference);
                    if (existMenuIds.size() > 0){
                        roleMenuMapper.deleteAllByRoleIdAndMenuIds(roleId, existMenuIds);
                    }
                }
                if (menuIds.size() > 0){
                    roleMenuMapper.saveAll(roleId, menuIds, authUser, nowTime);
                }
            }
        }

        if (Objects.nonNull(funcIds)){
            Set<Long> existFuncIds = roleFuncMapper.selectFuncIdByRoleId(roleId);
            List<FuncInfo> delFuncInfoList = null;
            List<FuncInfo> addFuncInfoList = null;
            if (funcIds.size() == 0){
                roleFuncMapper.deleteAllByRoleId(roleId);
                delFuncInfoList = funcMapper.selectAllByIds(existFuncIds);
            }else {
                if (existFuncIds.size() > 0){
                    Set<Long> difference = new HashSet<>(existFuncIds);
                    difference.retainAll(funcIds);
                    existFuncIds.removeAll(difference);
                    funcIds.removeAll(difference);
                    if (existFuncIds.size() > 0){
                        roleFuncMapper.deleteAllByRoleIdAndFuncIds(roleId, existFuncIds);
                        delFuncInfoList = funcMapper.selectAllByIds(existFuncIds);
                    }
                }
                if (funcIds.size() > 0){
                    roleFuncMapper.saveAll(roleId, funcIds, authUser, nowTime);
                    addFuncInfoList = funcMapper.selectAllByIds(funcIds);

                }
            }
            // 功能缓存角色删除处理
            if (Objects.nonNull(delFuncInfoList)){
                for (FuncInfo funcInfo : delFuncInfoList){
                    String key = MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath();
                    CacheFuncDto funcCache = cacheService.getFuncCache(key);
                    funcCache.getRoleIds().remove(roleId);
                    cacheService.setFuncCache(key, funcCache);
                }
            }
            // 功能缓存角色添加处理
            if (Objects.nonNull(addFuncInfoList)){
                for (FuncInfo funcInfo : addFuncInfoList){
                    String key = MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath();
                    CacheFuncDto funcCache = cacheService.getFuncCache(key);
                    funcCache.getRoleIds().add(roleId);
                    cacheService.setFuncCache(key, funcCache);
                }
            }
        }

        if (Objects.nonNull(resourceIds)){
            if (resourceIds.size() == 0){
                roleResourceMapper.deleteAllByRoleId(roleId);
            }else {
                Set<Long> existSourceIds = roleResourceMapper.selectResourceIdByRoleId(roleId);
                if (existSourceIds.size() > 0){
                    Set<Long> difference = new HashSet<>(existSourceIds);
                    difference.retainAll(resourceIds);
                    existSourceIds.removeAll(difference);
                    resourceIds.removeAll(difference);
                    if (existSourceIds.size() > 0){
                        roleResourceMapper.deleteAllByRoleIdAndResourceIds(roleId, existSourceIds);
                    }
                }
                if (resourceIds.size() > 0){
                    roleResourceMapper.saveAll(roleId, resourceIds, authUser, nowTime);
                }
            }
        }

        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "角色服务", "修改角色", String.format("用户'%s' 执行修改角色'%s'", authUser, roleInfo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRoles(Set<Long> roleIds) {
        if (roleIds.size() == 0){
            return;
        }
        roleMapper.batchUpdateDeleted(roleIds, CommonEnum.ENABLE.getCode(), LocalDateTime.now());
        Set<Long> userIds = userRoleMapper.selectUserIdByRoleIds(roleIds);
        for (Long userId : userIds){
            UserInfo userInfo = dataBaseService.getUserInfo(userId);
            cacheService.setUserRole(userInfo.getUsername(), userRoleMapper.selectRoleIdByUserId(userId));
        }
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "角色服务", "批量删除用户成功", String.format("用户'%s' 执行删除 角色'%s'", authUtils.getAuthData().getUsername(), roleIds));
    }

    @Override
    public void associateUser(Long roleId, Set<Long> userIds, Boolean isAdd) {
        RoleInfo roleInfo = dataBaseService.getRoleInfo(roleId);
        String authUser = authUtils.getAuthData().getUsername();
        if (isAdd){
            userRoleMapper.saveAllUserIds(roleId, userIds, authUser, LocalDateTime.now());
        }else {
            userRoleMapper.deleteAllByRoleIdAndUserIds(roleId, userIds);
        }
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "角色服务", "角色关联用户", String.format("用户'%s' 执行角色'%s' 关联用户 用户'%s'", authUser, roleInfo.getRoleName(), userIds));
    }

}
