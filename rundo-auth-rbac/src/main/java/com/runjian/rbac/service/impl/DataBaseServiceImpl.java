package com.runjian.rbac.service.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.rbac.dao.SectionMapper;
import com.runjian.rbac.dao.UserMapper;
import com.runjian.rbac.entity.SectionInfo;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.service.DataBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/6/1 9:16
 */
@Service
@RequiredArgsConstructor
public class DataBaseServiceImpl implements DataBaseService {

    private final SectionMapper sectionMapper;

    private final UserMapper userMapper;

    @Override
    public SectionInfo getSectionInfo(Long id) {
        Optional<SectionInfo> sectionInfoOp = sectionMapper.selectById(id);
        if (sectionInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("部门 %s 不存在,请刷新后重试", id));
        }
        return sectionInfoOp.get();
    }

    @Override
    public UserInfo getUserInfo(Long id) {
        Optional<UserInfo> userInfoOp = userMapper.selectById(id);
        if (userInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("用户 %s 不存在,请刷新后重试", id));
        }
        return userInfoOp.get();
    }
}
