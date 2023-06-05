package com.runjian.rbac.service;

import com.github.pagehelper.PageInfo;
import com.runjian.rbac.vo.response.GetRolePageRsp;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/2 9:42
 */
public interface RoleService {

    /**
     * 分页查询角色
     */
    PageInfo<GetRolePageRsp> getRolePage(int page, int num, String roleName, String createBy, LocalDateTime createTimeStart, LocalDateTime createTimeEnd);

    /**
     * 用户页面的角色查询
     */
    PageInfo<GetRolePageRsp> getRolePage(int page, int num, Long userId, String roleName);

    /**
     * 禁用启用用户
     */
    void disabled(String authUser, Long roleId, Integer disabled);

    /**
     * 添加角色
     */
    void addRole(String authUser, String roleName, String roleDesc, Set<Long> menuIds, Set<Long> funcIds, Set<Long> resourceIds);

    /**
     * 编辑用户
     */
    void updateRole(String authUser, Long roleId, String roleName, String roleDesc, Set<Long> menuIds, Set<Long> funcIds, Set<Long> resourceIds);

    /**
     * 批量删除角色
     */
    void batchDeleteRoles(String authUser, Set<Long> roleIds);

    /**
     * 关联用户
     */
    void associateUser(String authUser, Long roleId, Set<Long> userIds);
}
