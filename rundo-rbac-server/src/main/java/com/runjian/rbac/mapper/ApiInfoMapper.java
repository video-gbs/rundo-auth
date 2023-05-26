package com.runjian.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.rbac.domain.entity.ApiInfo;
import com.runjian.rbac.domain.vo.system.SysApiInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 接口信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface ApiInfoMapper extends BaseMapper<ApiInfo> {

    /**
     * 分页查询接口
     *
     * @param page
     * @return
     */
    Page<SysApiInfoVO> MySelectPage(Page<SysApiInfoVO> page);

    /**
     * 通过角色编码，查取角色已有的接口
     *
     * @param roleCode
     * @return
     */
    List<ApiInfo> selectApiInfoByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编码，查取角色已有的API URL
     *
     * @param roleCode
     * @return
     */
    List<String> selectApiUrlByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色ID，获取角色已授权的ApiId
     *
     * @param roleId
     * @return
     */
    List<Long> findApiIdListByRoleId(@Param("roleId") Long roleId);
}
