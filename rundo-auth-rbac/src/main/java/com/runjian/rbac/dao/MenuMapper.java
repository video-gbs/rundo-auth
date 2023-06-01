package com.runjian.rbac.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/5/30 15:53
 */
@Mapper
@Repository
public interface MenuMapper {

    String MENU_TABLE_NAME = "rbac_menu";
}
