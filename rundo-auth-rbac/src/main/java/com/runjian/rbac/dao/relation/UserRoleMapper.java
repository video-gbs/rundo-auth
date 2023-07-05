package com.runjian.rbac.dao.relation;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    void saveAllByRoleIds(Long userId, Set<Long> roleIds, String createBy, LocalDateTime createTime);

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

    @Select(" <script> " +
            " SELECT user_id FROM " + USER_ROLE_TABLE_NAME +
            " WHERE role_id IN <foreach collection='roleIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    Set<Long> selectUserIdByRoleIds(Set<Long> roleIds);

    @Insert(" <script> " +
            " INSERT INTO " + USER_ROLE_TABLE_NAME +
            " (user_id, role_id, create_by, create_time) VALUES " +
            " <foreach collection='userIds' item='item' separator=','>(#{item}, #{roleId}, #{createBy}, #{createTime})</foreach> " +
            " </script>")
    void saveAllUserIds(Long roleId, Set<Long> userIds, String createBy, LocalDateTime createTime);
}
