package com.runjian.rbac.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.entity.RoleOrg;
import com.runjian.rbac.mapper.RoleOrgMapper;
import com.runjian.rbac.service.system.RoleOrgService;
import org.springframework.stereotype.Service;

@Service
public class RoleOrgServiceImpl extends ServiceImpl<RoleOrgMapper, RoleOrg> implements RoleOrgService {
}
