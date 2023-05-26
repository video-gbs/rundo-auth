package com.runjian.rbac.service.system;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.rbac.domain.dto.system.*;
import com.runjian.rbac.domain.entity.RoleInfo;
import com.runjian.rbac.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.rbac.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.rbac.domain.vo.system.RoleDetailVO;
import com.runjian.rbac.domain.vo.system.SysRoleInfoVO;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface RoleInfoService extends IService<RoleInfo> {

    /**
     * 自定义分页条件查询
     *
     * @param dto
     * @return
     */
    Page<SysRoleInfoVO> findByPage(QuerySysRoleInfoDTO dto);

    /**
     * 新建角色
     *
     * @param dto
     */
    void save(SysRoleInfoDTO dto);

    /**
     * 编辑角色
     *
     * @param dto
     */
    void modifyById(SysRoleInfoDTO dto);

    /**
     * 删除角色
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除角色
     *
     * @param ids
     */
    void erasureBatch(List<Long> ids);

    /**
     * 编辑角色时回显详细信息
     *
     * @param id
     * @return
     */
    RoleDetailVO getRoleDetailById(Long id);

    /**
     * 角色启用停用
     *
     * @param dto
     */
    void modifyByStatus(StatusSysRoleInfoDTO dto);

    /**
     * 用户新增获取角色表格
     *
     * @param dto
     * @return
     */
    Page<EditUserSysRoleInfoVO> getEditUserSysRoleInfoList(QueryEditUserSysRoleInfoDTO dto);


    /**
     * 新建角色时获取应用类相关ID列表
     *
     * @param appType
     * @return
     */
    List<Tree<Long>> getAppMenuApiTree(Integer appType);

    /**
     * 提交关联用户列表
     *
     * @param dto
     */
    void addRelationUser(RoleRelationUserDTO dto);

    /**
     * 查询已关联用户列表
     *
     * @param dto
     * @return
     */
    Page<RelationSysUserInfoVO> listRelationUser(QueryRoleRelationSysUserInfoDTO dto);

    /**
     * 右移从关联用户列表中移除用户
     *
     * @param dto
     */
    void rightRelationUser(RoleRelationUserDTO dto);

    /**
     * 左移提交用户到已关联用户列表
     *
     * @param dto
     */
    void leftRelationUser(RoleRelationUserDTO dto);

    /**
     * 添加角色用户映射
     *
     * @param roleId
     * @param userId
     */
    void saveRoleUser(Long roleId, Long userId);

    /**
     * 删除角色用户映射
     *
     * @param roleId
     * @param userId
     */
    void removeRoleUser(Long roleId, Long userId);

    /**
     * 通过用户ID，获取用户已有的角色ID
     *
     * @param userId
     * @return
     */
    List<Long> getRoleByUserId(Long userId);

    /**
     * 通过用户ID，获取用户已有的角色编码
     *
     * @param userId
     * @return
     */
    List<String> getRoleCodeByUserId(Long userId);

}
