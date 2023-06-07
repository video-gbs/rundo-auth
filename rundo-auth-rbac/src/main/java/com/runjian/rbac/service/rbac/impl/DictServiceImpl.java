package com.runjian.rbac.service.rbac.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.rbac.dao.DictMapper;
import com.runjian.rbac.entity.DictInfo;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.service.rbac.DictService;
import com.runjian.rbac.vo.response.GetDictGroupRsp;
import com.runjian.rbac.vo.response.GetDictPageRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/5 10:17
 */
@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final DictMapper dictMapper;

    private final DataBaseService dataBaseService;

    @Override
    public PageInfo<GetDictPageRsp> getDictPageRsp(int page, int num, String groupName, String itemName) {
        PageHelper.startPage(page, num);

        return new PageInfo<>(dictMapper.selectByGroupNameAndItemName(groupName, itemName));
    }

    @Override
    public List<GetDictGroupRsp> getDictByGroupName(String groupCode) {
        return dictMapper.selectByGroupCode(groupCode);
    }

    @Override
    public void addDict(String groupName, String groupCode, String itemName, String itemValue, String description) {
        LocalDateTime nowTime = LocalDateTime.now();
        DictInfo dictInfo = new DictInfo();
        dictInfo.setGroupName(groupName);
        dictInfo.setGroupCode(groupCode);
        dictInfo.setItemName(itemName);
        dictInfo.setItemValue(itemValue);
        dictInfo.setDescription(description);
        dictInfo.setCreateTime(nowTime);
        dictInfo.setUpdateTime(nowTime);
        dictMapper.save(dictInfo);
    }

    @Override
    public void batchDelete(Set<Long> dictIds) {
        if (dictIds.size() == 0){
            return;
        }
        dictMapper.batchDeleted(dictIds);
    }

    @Override
    public void updateDict(Long dictId, String groupName, String groupCode, String itemName, String itemValue, String description) {
        DictInfo dictInfo = dataBaseService.getDictInfo(dictId);
        dictInfo.setGroupName(groupName);
        dictInfo.setGroupCode(groupCode);
        dictInfo.setItemName(itemName);
        dictInfo.setItemValue(itemValue);
        dictInfo.setDescription(description);
        dictInfo.setUpdateTime(LocalDateTime.now());
        dictMapper.update(dictInfo);
    }
}
