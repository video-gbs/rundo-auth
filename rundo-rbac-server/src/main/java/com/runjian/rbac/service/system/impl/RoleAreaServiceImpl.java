package com.runjian.rbac.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.entity.RoleArea;
import com.runjian.rbac.mapper.RoleAreaMapper;
import com.runjian.rbac.service.system.RoleAreaService;
import org.springframework.stereotype.Service;

@Service
public class RoleAreaServiceImpl  extends ServiceImpl<RoleAreaMapper, RoleArea> implements RoleAreaService {
}
