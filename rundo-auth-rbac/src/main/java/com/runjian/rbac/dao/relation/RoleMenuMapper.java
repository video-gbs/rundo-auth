package com.runjian.rbac.dao.relation;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/5/31 14:25
 */
@Mapper
@Repository
public interface RoleMenuMapper {

    String ROLE_MENU_TABLE_NAME = "rbac_role_menu";
}
