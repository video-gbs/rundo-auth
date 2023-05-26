package com.runjian.rbac.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.entity.RoleApp;
import com.runjian.rbac.mapper.RoleAppMapper;
import com.runjian.rbac.service.system.RoleAppService;
import org.springframework.stereotype.Service;

@Service
public class RoleAppServiceImpl extends ServiceImpl<RoleAppMapper, RoleApp> implements RoleAppService {
}
