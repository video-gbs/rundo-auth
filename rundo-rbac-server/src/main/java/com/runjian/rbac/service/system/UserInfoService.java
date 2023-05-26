package com.runjian.rbac.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.rbac.domain.dto.system.QueryRelationSysUserInfoDTO;
import com.runjian.rbac.domain.dto.system.QuerySysUserInfoDTO;
import com.runjian.rbac.domain.dto.system.StatusSysUserInfoDTO;
import com.runjian.rbac.domain.dto.system.SysUserInfoDTO;
import com.runjian.rbac.domain.entity.UserInfo;
import com.runjian.rbac.domain.vo.system.EditSysUserInfoVO;
import com.runjian.rbac.domain.vo.system.ListSysUserInfoVO;
import com.runjian.rbac.domain.vo.system.RelationSysUserInfoVO;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 自定义分页条件查询
     *
     * @param dto
     * @return
     */
    Page<ListSysUserInfoVO> findByPage(QuerySysUserInfoDTO dto);

    /**
     * 新增用户
     *
     * @param dto
     */
    void save(SysUserInfoDTO dto);

    /**
     * 修改用户
     *
     * @param dto
     */
    void modifyById(SysUserInfoDTO dto);

    /**
     * 用户启用停用
     *
     * @param dto
     */
    void modifyByStatus(StatusSysUserInfoDTO dto);

    /**
     * 删除用户
     *
     * @param id
     */
    void erasureById(Long id);

    /**
     * 批量删除用户
     *
     * @param ids
     */
    void erasureBatch(List<Long> ids);

    /**
     * 根据ID获取编辑前用户信息
     *
     * @param id
     * @return
     */
    EditSysUserInfoVO findById(Long id);

    /**
     * 关联用户中查看单个用户信息
     *
     * @param id
     * @return
     */
    RelationSysUserInfoVO findRelationById(Long id);


    /**
     * 关联用户列表
     *
     * @param dto
     * @return
     */
    Page<RelationSysUserInfoVO> findRelationList(QueryRelationSysUserInfoDTO dto);

    /**
     * 通过角色ID，查询该角色已关联的用户ID
     *
     * @param roleId
     * @return
     */
    List<Long> getUserIdListByRoleId(Long roleId);
}
