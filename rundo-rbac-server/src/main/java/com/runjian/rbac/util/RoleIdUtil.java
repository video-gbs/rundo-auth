package com.runjian.rbac.util;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.rbac.domain.entity.RoleInfo;
import com.runjian.rbac.domain.entity.RoleOrg;
import com.runjian.rbac.service.system.RoleInfoService;
import com.runjian.rbac.service.system.RoleOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleIdUtil {

    @Lazy
    @Autowired
    private RoleInfoService roleInfoService;

    @Lazy
    @Autowired
    private RoleOrgService roleOrgService;


    public List<Long> getRoleIdList() {
        List<String> roleCodeList = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        LambdaQueryWrapper<RoleInfo> roleInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleInfoLambdaQueryWrapper.in(RoleInfo::getRoleCode, roleCodeList);
        return roleInfoService.list(roleInfoLambdaQueryWrapper).stream().map(RoleInfo::getId).collect(Collectors.toList());
    }

    public List<Long> getRoleOrgIdList(List<Long> roleIds){
        LambdaQueryWrapper<RoleOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleOrg::getRoleId, roleIds);
        return roleOrgService.list(queryWrapper).stream().map(RoleOrg::getOrgId).collect(Collectors.toList());
    }
}
