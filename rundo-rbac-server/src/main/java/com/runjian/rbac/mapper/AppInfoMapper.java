package com.runjian.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.rbac.domain.dto.page.PageSysAppInfoDTO;
import com.runjian.rbac.domain.entity.AppInfo;
import com.runjian.rbac.domain.vo.system.SysAppInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 应用信息 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface AppInfoMapper extends BaseMapper<AppInfo> {

    /**
     * 分页查询应用
     *
     * @param dto
     * @return
     */
    Page<SysAppInfoVO> MySelectPage(PageSysAppInfoDTO dto);

    /**
     * 根据角色编码查询应用权限
     *
     * @param roleCode
     * @return
     */
    List<AppInfo> selectAppByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据角色编码列表查询应用权限
     *
     * @param roleList
     * @return
     */
    List<AppInfo> selectAppByRoleCodeList(@Param("roleList") List<String> roleList);

    /**
     * 通过角色ID，获取角色已有的应用ID
     * @param roleId
     * @return
     */
    List<Long> findAppIdListByRoleId(@Param("roleId") Long roleId);
}
