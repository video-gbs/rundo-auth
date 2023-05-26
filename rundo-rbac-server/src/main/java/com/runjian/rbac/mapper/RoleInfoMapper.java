package com.runjian.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.rbac.domain.dto.page.PageEditUserSysRoleInfoDTO;
import com.runjian.rbac.domain.dto.page.PageRoleRelationSysUserInfoDTO;
import com.runjian.rbac.domain.dto.page.PageSysRoleInfoDto;
import com.runjian.rbac.domain.entity.RoleInfo;
import com.runjian.rbac.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.rbac.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.rbac.domain.vo.system.SysRoleInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface RoleInfoMapper extends BaseMapper<RoleInfo> {

    /**
     * 根据用户ID查询,用户角色编码
     *
     * @param userId
     * @return
     */
    List<String> selectRoleCodeByUserId(@Param("userId") Long userId);

    Page<SysRoleInfoVO> MySelectPage(PageSysRoleInfoDto page);

    Page<EditUserSysRoleInfoVO> selectEditUserSysRoleInfoPage(PageEditUserSysRoleInfoDTO page);

    Page<RelationSysUserInfoVO> relationSysUserInfoPage(PageRoleRelationSysUserInfoDTO page);
}
