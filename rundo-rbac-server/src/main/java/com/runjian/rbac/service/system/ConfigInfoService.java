package com.runjian.rbac.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.rbac.domain.dto.system.SysConfigDTO;
import com.runjian.rbac.domain.entity.ConfigInfo;
import com.runjian.rbac.domain.vo.system.SysConfigVO;

import java.util.List;

/**
 * <p>
 * 系统全局参数配置 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface ConfigInfoService extends IService<ConfigInfo> {
    void save(SysConfigDTO dto);

    void modifyById(SysConfigDTO dto);

    SysConfigVO findById(Long id);

    List<SysConfigVO> findByList();
}
