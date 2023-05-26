package com.runjian.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.rbac.domain.entity.RoleUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleUserMapper extends BaseMapper<RoleUser> {
}