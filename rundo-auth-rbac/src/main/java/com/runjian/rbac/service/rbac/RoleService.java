package com.runjian.rbac.service.rbac;

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
     * @param page 页码
     * @param num 数量
     * @param roleName 角色名称
     * @param createBy 创建人
     * @param createTimeStart 创建开始时间
     * @param createTimeEnd 创建结束时间
     * @return GetRolePageRsp
     */
    PageInfo<GetRolePageRsp> getRolePage(int page, int num, String roleName, String createBy, LocalDateTime createTimeStart, LocalDateTime createTimeEnd);

    /**
     * 用户页面的角色查询
     * @param page 页码
     * @param num 数量
     * @param userId 用户id
     * @param roleName 角色名称
     * @return GetRolePageRsp
     */
    PageInfo<GetRolePageRsp> getRolePage(int page, int num, Long userId, String roleName);

    /**
     * 获取角色资源id
     * @param roleId 角色id
     * @return 资源id
     */
    Set<Long> getRoleResourceIds(Long roleId);

    /**
     * 获取角色菜单id
     * @param roleId 角色id
     * @return 菜单id
     */
    Set<Long> getRoleMenuIds(Long roleId);

    /**
     * 根据菜单id获取功能id
     * @param roleId 角色id
     * @param menuId 菜单id
     * @return 功能id
     */
    Set<Long> getRoleFuncIdByMenuId(Long roleId, Long menuId);

    /**
     * 获取角色功能id
     * @param roleId 角色id
     * @return 功能id
     */
    Set<Long> getRoleFuncId(Long roleId);


    /**
     * 添加角色
     * @param roleName 角色名称
     * @param roleDesc 角色描述
     * @param menuIds 菜单id数组
     * @param funcIds 功能id数组
     * @param resourceIds 资源id数组
     */
    void addRole(String roleName, String roleDesc, Set<Long> menuIds, Set<Long> funcIds, Set<Long> resourceIds);

    /**
     * 禁用启用用户
     * @param roleId 角色id
     * @param disabled 是否禁用
     */
    void updateDisabled(Long roleId, Integer disabled);

    /**
     * 编辑用户
     * @param roleId 角色id
     * @param roleName 角色名称
     * @param roleDesc 角色描述
     * @param menuIds 菜单id数组
     * @param funcIds 功能id数组
     * @param resourceIds 资源id数组
     */
    void updateRole(Long roleId, String roleName, String roleDesc, Set<Long> menuIds, Set<Long> funcIds, Set<Long> resourceIds);

    /**
     * 批量删除角色
     * @param roleIds 角色id数组
     */
    void batchDeleteRoles(Set<Long> roleIds);

    /**
     * 关联用户
     * @param roleId 角色id
     * @param userIds 用户id数组
     * @param isAdd 是否添加
     */
    void associateUser(Long roleId, Set<Long> userIds, Boolean isAdd);

}
