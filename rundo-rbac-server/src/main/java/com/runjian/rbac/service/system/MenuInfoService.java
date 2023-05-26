package com.runjian.rbac.service.system;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.rbac.domain.dto.system.HiddenChangeDTO;
import com.runjian.rbac.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.rbac.domain.dto.system.StatusChangeDTO;
import com.runjian.rbac.domain.dto.system.SysMenuInfoDTO;
import com.runjian.rbac.domain.entity.MenuInfo;
import com.runjian.rbac.domain.vo.system.MenuInfoVO;
import com.runjian.rbac.domain.vo.tree.MenuInfoTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface MenuInfoService extends IService<MenuInfo> {

    List<Tree<Long>> findByTree(QuerySysMenuInfoDTO dto);

    void save(SysMenuInfoDTO dto);

    void modifyById(SysMenuInfoDTO dto);

    MenuInfoVO findById(Long id);

    void erasureById(Long id);

    List<MenuInfoTree> findByTreeByAppType(Integer appType);

    void modifyByStatus(StatusChangeDTO dto);

    void modifyByHidden(HiddenChangeDTO dto);

    List<Tree<Long>> getTreeByAppId(Long appId);

    List<MenuInfo> getMenuByRoleCode(String roleCode);

    List<Long> getMenuIdListByRoleId(@Param("roleId") Long roleId);
}
