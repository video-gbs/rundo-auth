package com.runjian.rbac.dao.relation;

import com.runjian.rbac.entity.relation.UserRoleRel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/31 14:28
 */
@Mapper
@Repository
public interface UserRoleMapper {

    String USER_ROLE_TABLE_NAME = "rbac_user_role";

    void saveAll(List<UserRoleRel> userRoleRels);

    void updateAll(List<UserRoleRel> userRoleRels);

    List<Long> selectRoleIdByUserIdAndRoleIds(Long userId, Set<Long> roleIds);
}
