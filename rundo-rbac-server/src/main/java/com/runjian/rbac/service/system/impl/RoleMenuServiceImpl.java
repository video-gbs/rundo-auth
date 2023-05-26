package com.runjian.rbac.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.entity.RoleMenu;
import com.runjian.rbac.mapper.RoleMenuMapper;
import com.runjian.rbac.service.system.RoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
