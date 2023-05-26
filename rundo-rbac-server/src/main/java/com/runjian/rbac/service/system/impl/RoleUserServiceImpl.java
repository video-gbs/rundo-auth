package com.runjian.rbac.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.entity.RoleUser;
import com.runjian.rbac.mapper.RoleUserMapper;
import com.runjian.rbac.service.system.RoleUserService;
import org.springframework.stereotype.Service;

@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {
}
