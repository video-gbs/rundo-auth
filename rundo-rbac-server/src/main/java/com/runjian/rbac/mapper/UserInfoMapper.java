package com.runjian.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.rbac.domain.dto.page.PageRelationSysUserInfoDTO;
import com.runjian.rbac.domain.dto.page.PageSysUserInfoDTO;
import com.runjian.rbac.domain.entity.UserInfo;
import com.runjian.rbac.domain.vo.system.ListSysUserInfoVO;
import com.runjian.rbac.domain.vo.system.RelationSysUserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-29 20:01:25
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 自定义分页查询
     *
     * @param page
     * @param orgIds
     * @return
     */
    Page<ListSysUserInfoVO> MySelectPageByContain(@Param("page") PageSysUserInfoDTO page, @Param("orgIds") List<Long> orgIds);

    /**
     * 关联用户用户列表
     *
     * @param page
     * @return
     */
    Page<RelationSysUserInfoVO> relationSysUserInfoPage(PageRelationSysUserInfoDTO page);

    /**
     * 关联用户查看单个用户信息
     *
     * @param userId
     * @return
     */
    RelationSysUserInfoVO selectRelationSysUserInfoById(@Param("userId") Long userId);

    /**
     * 通过角色ID，获取已授权的用户ID
     *
     * @param roleId
     * @return
     */
    List<Long> selectUserIdListByRoleId(@Param("roleId") Long roleId);

    /**
     * 通过用户账号获取用户
     * @param userAccount
     * @return
     */
    Optional<UserInfo> selectByAccount(@Param("user_account") String userAccount);
}
