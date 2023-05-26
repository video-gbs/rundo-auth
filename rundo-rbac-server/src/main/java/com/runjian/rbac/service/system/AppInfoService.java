package com.runjian.rbac.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.rbac.domain.dto.system.QuerySysAppInfoDTO;
import com.runjian.rbac.domain.dto.system.StatusSysAppInfoDTO;
import com.runjian.rbac.domain.dto.system.SysAppInfoDTO;
import com.runjian.rbac.domain.entity.AppInfo;
import com.runjian.rbac.domain.vo.system.SysAppInfoVO;

import java.util.List;

/**
 * <p>
 * 应用信息 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface AppInfoService extends IService<AppInfo> {

    void save(SysAppInfoDTO dto);

    void modifyById(SysAppInfoDTO dto);

    SysAppInfoVO findById(Long id);

    List<SysAppInfoVO> findByList();

    void erasureById(Long id);

    Page<SysAppInfoVO> findByPage(QuerySysAppInfoDTO dto);

    void modifyByStatus(StatusSysAppInfoDTO dto);

    List<AppInfo> getAppByRoleCode(String roleCode);

    List<AppInfo> getAppByRoleCodelist(List<String> roleCodeList);

    List<Long> getAppIdListByRoleId(Long roleId);
}
