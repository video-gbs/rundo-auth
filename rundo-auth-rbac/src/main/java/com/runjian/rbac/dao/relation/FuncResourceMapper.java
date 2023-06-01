package com.runjian.rbac.dao.relation;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/5/31 14:37
 */
@Mapper
@Repository
public interface FuncResourceMapper {

    String FUNC_RESOURCE_TABLE_NAME = "rbac_func_resource";

}
