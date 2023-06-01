package com.runjian.rbac.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/5/30 15:06
 */
@Mapper
@Repository
public interface DictMapper {

    String DICT_TABLE_NAME = "rbac_dict";
}
