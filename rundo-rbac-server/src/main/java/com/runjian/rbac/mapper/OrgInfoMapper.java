package com.runjian.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.rbac.domain.entity.OrgInfo;
import com.runjian.rbac.domain.vo.system.OrgInfoVO;
import com.runjian.rbac.domain.vo.system.SysOrgVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组织机构表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface OrgInfoMapper extends BaseMapper<OrgInfo> {


    /**
     * 根据部门ID，当前部门及其下级部门的id
     *
     * @param orgId
     * @return
     */
    List<Long> mySelectOrg(@Param("orgId") Long orgId);

    /**
     * 根据部门ID，当前部门及其下级部门信息
     *
     * @param orgId
     * @return
     */
    List<SysOrgVO> mySelectListById(@Param("orgId") Long orgId);

    /**
     * 通过角色编码，查取角色已有部门信息
     *
     * @param roleCode
     * @return
     */
    List<OrgInfo> selectOrgInfoByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据角色ID，获取角色已有的部门ID列表
     *
     * @param roleId
     * @return
     */
    List<Long> findOrgIdListByRoleId(@Param("roleId") Long roleId);

    /**
     * 通过用户ID，查询用户直属的部门
     *
     * @param userId
     * @return
     */
    OrgInfoVO selectOrgInfoByUserId(@Param("userId") Long userId);

    List<OrgInfo> selectOrgList(@Param("orgIdList") List<Long> orgIdList);
}
