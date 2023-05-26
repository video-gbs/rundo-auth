package com.runjian.rbac.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.dto.system.SysConfigDTO;
import com.runjian.rbac.domain.entity.ConfigInfo;
import com.runjian.rbac.domain.vo.system.SysConfigVO;
import com.runjian.rbac.mapper.ConfigInfoMapper;
import com.runjian.rbac.service.system.ConfigInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统全局参数配置 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class ConfigInfoServiceImpl extends ServiceImpl<ConfigInfoMapper, ConfigInfo> implements ConfigInfoService {

    @Autowired
    private ConfigInfoMapper configInfoMapper;

    @Transactional
    @Override
    public void save(SysConfigDTO dto) {
        ConfigInfo configInfo = new ConfigInfo();
        BeanUtils.copyProperties(dto, configInfo);
        configInfoMapper.insert(configInfo);
    }

    @Transactional
    @Override
    public void modifyById(SysConfigDTO dto) {
        ConfigInfo configInfo = new ConfigInfo();
        BeanUtils.copyProperties(dto, configInfo);
        configInfoMapper.updateById(configInfo);
    }

    @Override
    public SysConfigVO findById(Long id) {
        SysConfigVO sysConfigVO = new SysConfigVO();
        ConfigInfo configInfo = configInfoMapper.selectById(id);
        BeanUtils.copyProperties(configInfo, sysConfigVO);
        return sysConfigVO;
    }

    @Override
    public List<SysConfigVO> findByList() {

        return configInfoMapper.selectList(null).stream().map(
                item -> {
                    SysConfigVO sysConfigVO = new SysConfigVO();
                    BeanUtils.copyProperties(item, sysConfigVO);
                    return sysConfigVO;
                }
        ).collect(Collectors.toList());
    }
}
