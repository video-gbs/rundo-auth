package com.runjian.rbac.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.entity.RoleApi;
import com.runjian.rbac.mapper.RoleApiMapper;
import com.runjian.rbac.service.system.RoleApiService;
import org.springframework.stereotype.Service;

@Service
public class RoleApiServiceImpl extends ServiceImpl<RoleApiMapper, RoleApi> implements RoleApiService {
}
