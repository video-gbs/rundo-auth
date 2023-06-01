package com.runjian.rbac.dao.relation;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/5/31 14:26
 */
@Mapper
@Repository
public interface RoleResourceMapper {

    String ROLE_RESOURCE_TABLE_NAME = "rbac_role_resource";
}
