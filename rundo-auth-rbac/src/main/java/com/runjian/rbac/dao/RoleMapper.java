package com.runjian.rbac.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/5/30 14:58
 */
@Mapper
@Repository
public interface RoleMapper {

    String ROLE_TABLE_NAME = "rbac_role";
}
