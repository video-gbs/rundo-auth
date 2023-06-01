package com.runjian.rbac.dao.relation;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/5/31 14:24
 */
@Mapper
@Repository
public interface RoleFuncMapper {

    String ROLE_FUNC_TABLE_NAME = "rbac_role_func";
}
