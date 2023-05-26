package com.runjian.rbac.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.rbac.domain.dto.system.QureySysDictDTO;
import com.runjian.rbac.domain.dto.system.SysDictDTO;
import com.runjian.rbac.domain.entity.DictInfo;
import com.runjian.rbac.domain.vo.system.SysDictVO;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface DictInfoService extends IService<DictInfo> {

    void save(SysDictDTO dto);

    void modifyById(SysDictDTO dto);

    SysDictVO findById(Long id);

    List<SysDictVO> findByList();

    Page<SysDictVO> findByPage(QureySysDictDTO dto);

    void erasureById(Long id);

    List<SysDictVO> findByGroupCode(String groupCode);
}
