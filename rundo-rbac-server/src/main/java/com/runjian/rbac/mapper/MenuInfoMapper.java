package com.runjian.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.rbac.domain.entity.MenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface MenuInfoMapper extends BaseMapper<MenuInfo> {

    /**
     * 根据应用ID获取应用的菜单列表
     *
     * @param appId
     * @return
     */
    List<MenuInfo> selectByAppId(@Param("appId") Long appId);

    /**
     * 根据角色编码查询菜单权限
     *
     * @param roleCode
     * @return
     */
    List<MenuInfo> selectMenuByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据角色ID，获取角色已有的菜单ID列表
     *
     * @param roleId
     * @return
     */
    List<Long> findMenuIdListByRoleId(@Param("roleId") Long roleId);

    List<MenuInfo> selectMenuList(@Param("appId")Long appId, @Param("menuIds") List<Long> menuIds);
}
