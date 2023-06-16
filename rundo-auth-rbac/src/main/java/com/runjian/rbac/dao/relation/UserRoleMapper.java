package com.runjian.rbac.dao.relation;

import com.runjian.rbac.dao.UserMapper;
import com.runjian.rbac.entity.relation.UserRoleRel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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


    @Select(" SELECT role_id FROM " + USER_ROLE_TABLE_NAME +
            " WHERE user_id = #{userId} ")
    Set<Long> selectRoleIdByUserId(Long userId);

    @Delete(" DELETE FROM " + USER_ROLE_TABLE_NAME +
            " WHERE user_id = #{userId} " +
            " AND role_id IN <foreach collection='roleIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> "
    )
    void deleteAllByUserIdAndRoleIds(Long userId, Set<Long> roleIds);

    @Insert(" <script> " +
            " INSERT INTO " + USER_ROLE_TABLE_NAME +
            " (user_id, role_id, create_by, create_time) VALUES " +
            " <foreach collection='roleIds' item='item' separator=','>(#{userId}, #{item}, #{createBy}, #{createTime})</foreach> " +
            " </script>")
    void saveAll(Long userId, Set<Long> roleIds, String createBy, LocalDateTime createTime);

    @Select(" SELECT user_id FROM " + USER_ROLE_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    Set<Long> selectUserIdByRoleId(Long roleId);

    @Delete(" DELETE FROM " + USER_ROLE_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    void deleteAllByRoleId(Long roleId);

    @Delete(" DELETE FROM " + USER_ROLE_TABLE_NAME +
            " WHERE user_id = #{userId} ")
    void deleteAllByUserId(Long userId);

    @Delete(" <script> " +
            " DELETE FROM " + USER_ROLE_TABLE_NAME +
            " WHERE role_id = #{roleId} " +
            " AND user_id IN <foreach collection='userIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    void deleteAllByRoleIdAndUserIds(Long roleId, Set<Long> userIds);

    @Select(" SELECT role_id FROM " + USER_ROLE_TABLE_NAME + " urt " +
            " LEFT JOIN " + UserMapper.USER_TABLE_NAME + " ut ON ut.id = urt.user_id " +
            " WHERE ut.username = #{username} ")
    Set<Long> selectRoleIdByUsername(String username);

    @Select(" SELECT user_id FROM " + USER_ROLE_TABLE_NAME +
            " WHERE role_id IN <foreach collection='userIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> ")
    Set<Long> selectUserIdByRoleIds(Set<Long> roleIds);
}
