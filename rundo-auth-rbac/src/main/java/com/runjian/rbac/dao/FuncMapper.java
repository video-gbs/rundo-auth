package com.runjian.rbac.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/5/31 10:32
 */

@Mapper
@Repository
public interface FuncMapper {

    String FUNC_TABLE_NAME = "rbac_func";
}
