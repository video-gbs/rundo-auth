package com.runjian.rbac.service.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.rbac.dao.*;
import com.runjian.rbac.entity.*;
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

    private final RoleMapper roleMapper;

    private final DictMapper dictMapper;

    private final MenuMapper menuMapper;

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

    @Override
    public RoleInfo getRoleInfo(Long id) {
        Optional<RoleInfo> roleInfoOp = roleMapper.selectById(id);
        if (roleInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("角色 %s 不存在,请刷新后重试", id));
        }
        return roleInfoOp.get();
    }

    @Override
    public DictInfo getDictInfo(Long id) {
        Optional<DictInfo> dictInfoOp = dictMapper.selectById(id);
        if (dictInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("字典 %s 不存在，请刷新后重试", id));
        }
        return dictInfoOp.get();
    }

    @Override
    public MenuInfo getMenuInfo(Long id) {
        Optional<MenuInfo> menuInfoOp = menuMapper.selectById(id);
        if (menuInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("菜单 %s 不存在，请刷新后重试", id));
        }
        return menuInfoOp.get();
    }
}
