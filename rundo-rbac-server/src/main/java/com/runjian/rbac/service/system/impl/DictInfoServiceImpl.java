package com.runjian.rbac.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.dto.page.PageSysDictDTO;
import com.runjian.rbac.domain.dto.system.QureySysDictDTO;
import com.runjian.rbac.domain.dto.system.SysDictDTO;
import com.runjian.rbac.domain.entity.DictInfo;
import com.runjian.rbac.domain.vo.system.SysDictVO;
import com.runjian.rbac.mapper.DictInfoMapper;
import com.runjian.rbac.service.system.DictInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class DictInfoServiceImpl extends ServiceImpl<DictInfoMapper, DictInfo> implements DictInfoService {

    @Autowired
    private DictInfoMapper dictInfoMapper;

    @Transactional
    @Override
    public void save(SysDictDTO dto) {
        DictInfo dictInfo = new DictInfo();
        BeanUtils.copyProperties(dto, dictInfo);
        dictInfoMapper.insert(dictInfo);
    }

    @Transactional
    @Override
    public void modifyById(SysDictDTO dto) {
        DictInfo dictInfo = dictInfoMapper.selectById(dto.getId());
        BeanUtils.copyProperties(dto, dictInfo);
        dictInfoMapper.updateById(dictInfo);
    }

    @Override
    public SysDictVO findById(Long id) {
        DictInfo dictInfo = dictInfoMapper.selectById(id);
        SysDictVO sysDictVO = new SysDictVO();
        BeanUtils.copyProperties(dictInfo, sysDictVO);
        return sysDictVO;
    }

    @Override
    public List<SysDictVO> findByList() {
        List<DictInfo> dictInfoList = dictInfoMapper.selectList(null);
        List<SysDictVO> sysDictVOList = new ArrayList<>();
        for (DictInfo dictInfo : dictInfoList) {
            SysDictVO sysDictVO = new SysDictVO();
            BeanUtils.copyProperties(dictInfo, sysDictVO);
            sysDictVOList.add(sysDictVO);
        }
        return sysDictVOList;
    }

    @Override
    public Page<SysDictVO> findByPage(QureySysDictDTO dto) {
        PageSysDictDTO page = new PageSysDictDTO();
        if (null != dto.getItemName() && !"".equals(dto.getItemName())) {
            page.setItemName(dto.getItemName());
        }
        if (null != dto.getItemValue() && !"".equals(dto.getItemValue())) {
            page.setItemValue(dto.getItemValue());
        }
        if (null != dto.getGroupName() && !"".equals(dto.getGroupName())) {
            page.setGroupName(dto.getGroupName());
        }
        if (null != dto.getGroupCode() && !"".equals(dto.getGroupCode())) {
            page.setGroupCode(dto.getGroupCode());
        }
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0) {
            page.setSize(dto.getPageSize());
        } else {
            page.setSize(20);
        }
        return dictInfoMapper.MySelectPage(page);
    }

    @Transactional
    @Override
    public void erasureById(Long id) {
        dictInfoMapper.deleteById(id);
    }

    @Override
    public List<SysDictVO> findByGroupCode(String groupCode) {
        LambdaQueryWrapper<DictInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictInfo::getGroupCode, groupCode);
        List<DictInfo> dictInfoList = dictInfoMapper.selectList(queryWrapper);
        return dictInfoList.stream().map(
                item -> {
                    SysDictVO sysDictVO = new SysDictVO();
                    BeanUtils.copyProperties(item, sysDictVO);
                    return sysDictVO;
                }
        ).collect(Collectors.toList());
    }
}
