package com.runjian.rbac.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.rbac.dao.RoleMapper;
import com.runjian.rbac.dao.relation.RoleFuncMapper;
import com.runjian.rbac.dao.relation.RoleMenuMapper;
import com.runjian.rbac.dao.relation.RoleResourceMapper;
import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.RoleInfo;
import com.runjian.rbac.service.DataBaseService;
import com.runjian.rbac.service.RoleService;
import com.runjian.rbac.vo.response.GetRolePageRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public PageInfo<GetRolePageRsp> getRolePage(int page, int num, String roleName, String createBy, LocalDateTime createTimeStart, LocalDateTime createTimeEnd) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(roleMapper.selectPageByRoleNameAndCreateByAndCreateTime(page, num, roleName, createBy, createTimeStart, createTimeEnd));
    }

    @Override
    public PageInfo<GetRolePageRsp> getRolePage(int page, int num, Long userId, String roleName) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(roleMapper.selectPageByUserIdAndRoleName(userId, roleName));
    }

    @Override
    public void disabled(String authUser, Long roleId, Integer disabled) {
        RoleInfo roleInfo = dataBaseService.getRoleInfo(roleId);
        roleInfo.setDisabled(disabled);
        roleInfo.setUpdateTime(LocalDateTime.now());
        roleMapper.updateDisabled(roleInfo);
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "角色服务", "禁用角色成功", String.format("用户'%s' 执行禁用 角色'%s'", authUser, roleInfo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(String authUser, String roleName, String roleDesc, Set<Long> menuIds, Set<Long> funcIds, Set<Long> resourceIds) {
        Optional<RoleInfo> roleInfoOp = roleMapper.selectByRoleName(roleName);
        if (roleInfoOp.isPresent()){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("角色名 %s 已存在，请重新填写", roleName));
        }
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
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(String authUser, Long roleId, String roleName, String roleDesc, Set<Long> menuIds, Set<Long> funcIds, Set<Long> resourceIds) {
        RoleInfo roleInfo = dataBaseService.getRoleInfo(roleId);
        if (!roleName.equals(roleInfo.getRoleName())){
            Optional<RoleInfo> roleInfoOp = roleMapper.selectByRoleName(roleName);
            if (roleInfoOp.isPresent()){
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("角色名 %s 已存在，请重新填写", roleName));
            }
        }
        LocalDateTime nowTime = LocalDateTime.now();
        roleInfo.setRoleName(roleName);
        roleInfo.setRoleDesc(roleDesc);
        roleInfo.setUpdateTime(nowTime);

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
            if (funcIds.size() == 0){
                roleFuncMapper.deleteAllByRoleId(roleId);
            }else {
                Set<Long> existFuncIds = roleFuncMapper.selectMenuIdByFuncId(roleId);
                if (existFuncIds.size() > 0){
                    Set<Long> difference = new HashSet<>(existFuncIds);
                    difference.retainAll(funcIds);
                    existFuncIds.removeAll(difference);
                    funcIds.removeAll(difference);
                    if (existFuncIds.size() > 0){
                        roleFuncMapper.deleteAllByRoleIdAndFuncIds(roleId, existFuncIds);
                    }
                }
                if (funcIds.size() > 0){
                    roleFuncMapper.saveAll(roleId, funcIds, authUser, nowTime);
                }
            }
        }

        if (Objects.nonNull(resourceIds)){
            if (resourceIds.size() == 0){
                roleResourceMapper.deleteAllByRoleId(roleId);
            }else {
                Set<Long> existSourceIds = roleResourceMapper.selectResourceIdByResourceId(roleId);
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
    public void batchDeleteRoles(String authUser, Set<Long> roleIds) {
        if (roleIds.size() == 0){
            return;
        }
        roleMapper.batchUpdateDeleted(roleIds, CommonEnum.DISABLE.getCode(), LocalDateTime.now());
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "角色服务", "批量删除用户成功", String.format("用户'%s' 执行删除 角色'%s'", authUser, roleIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associateUser(String authUser, Long roleId, Set<Long> userIds) {
        RoleInfo roleInfo = dataBaseService.getRoleInfo(roleId);
        if (Objects.isNull(userIds)){
            return;
        }
        if (userIds.size() == 0){
            userRoleMapper.deleteAllByRoleId(roleId);
        }else {
            Set<Long> existUserIds = userRoleMapper.selectUserIdByRoleId(roleId);
            if (existUserIds.size() > 0){
                Set<Long> difference = new HashSet<>(existUserIds);
                // 求交集
                difference.retainAll(userIds);
                // 移除交集获得被删除的数据
                existUserIds.removeAll(difference);
                // 移除交集获得增加的数据
                userIds.removeAll(difference);
                // 删除去除的角色
                if (existUserIds.size() > 0){
                    userRoleMapper.deleteAllByRoleIdAndUserIds(roleId, existUserIds);
                }
            }
            // 保存新的角色
            if (userIds.size() > 0){
                userRoleMapper.saveAll(roleId, userIds, authUser, LocalDateTime.now());
            }
        }
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "角色服务", "角色关联用户", String.format("用户'%s' 执行角色'%s' 关联用户 用户'%s'", authUser, roleInfo.getRoleName(), userIds));
    }
}
