package com.runjian.rbac.dao;

import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.RoleInfo;
import com.runjian.rbac.vo.response.GetRolePageRsp;
import com.runjian.rbac.vo.response.GetUserRolePageRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/30 14:58
 */
@Mapper
@Repository
public interface RoleMapper {

    String ROLE_TABLE_NAME = "rbac_role";

    @Select(" <script> " +
            " SELECT * FROM " + ROLE_TABLE_NAME +
            " WHERE deleted =  0 " +
            " <if test=\"roleName != null\" > AND role_name LIKE CONCAT('%', #{roleName}, '%') </if> " +
            " <if test=\"createBy != null\" > AND create_by LIKE CONCAT('%', #{createBy}, '%') </if> " +
            " <if test=\"createTimeStart != null\" > AND create_time &gt;= #{createTimeStart} </if> " +
            " <if test=\"createTimeEnd != null\" > AND create_time &lt;= #{createTimeEnd} </if> " +
            " </script> ")
    List<GetRolePageRsp> selectPageByRoleNameAndCreateByAndCreateTime(int page, int num, String roleName, String createBy, LocalDateTime createTimeStart, LocalDateTime createTimeEnd);

    @Select(" <script> " +
            " SELECT * FROM " + ROLE_TABLE_NAME +
            " WHERE deleted =  0 " +
            " <if test=\"roleName != null\" >  AND rt.role_name LIKE CONCAT('%', #{roleName}, '%') </if> " +
            " </script> ")
    List<GetUserRolePageRsp> selectPageByRoleName(String roleName);

    @Select(" <script> " +
            " SELECT rt.*, ur.user_id  FROM " + ROLE_TABLE_NAME + " rt " +
            " LEFT JOIN " + UserRoleMapper.USER_ROLE_TABLE_NAME + " ur ON rt.id = ur.role_id AND ur.user_id = #{userId} " +
            " WHERE rt.deleted = 0 " +
            " <if test=\"roleName != null\" >  AND rt.role_name LIKE CONCAT('%', #{roleName}, '%') </if> " +
            " ORDER BY ur.id desc " +
            " </script> ")
    List<GetUserRolePageRsp> selectPageByUserIdAndRoleName(Long userId, String roleName);

    @Update(" <script> " +
            " <foreach collection='roleIds' item='item' separator=';'> " +
            " UPDATE " + ROLE_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , deleted = #{deleted} " +
            " WHERE id = #{item} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateDeleted(Set<Long> roleIds, Integer deleted, LocalDateTime updateTime);

    @Select(" SELECT * FROM " + ROLE_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<RoleInfo> selectById(Long id);

    @Update(" UPDATE "  + ROLE_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " disabled = #{disabled} " +
            " WHERE id = #{id} ")
    void updateDisabled(RoleInfo roleInfo);

    @Select(" SELECT * FROM " + ROLE_TABLE_NAME +
            " WHERE role_name = #{roleName} ")
    Optional<RoleInfo> selectByRoleName(String roleName);

    @Insert(" INSERT INTO " + ROLE_TABLE_NAME +
            " (role_name, role_desc, create_by, create_time, update_time) " +
            " VALUES " +
            " (#{roleName}, #{roleDesc}, #{createBy}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(RoleInfo roleInfo);

    @Select(" <script> " +
            " SELECT rt.* FROM " + ROLE_TABLE_NAME + " rt " +
            " <if test=\"userId != null\" > RIGHT JOIN " + UserRoleMapper.USER_ROLE_TABLE_NAME + " ur ON rt.id = ur.role_id </if> " +
            " WHERE rt.deleted =  0 " +
            " AND ur.user_id = #{userId} " +
            " </script> ")
    List<RoleInfo> selectByUserId(Long userId);

    @Update(" <script> " +
            " UPDATE "  + ROLE_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test=\"roleName != null\" > , role_name = #{roleName} </if> " +
            " <if test=\"roleDesc != null\" > , role_desc = #{roleDesc} </if> " +
            " <if test=\"roleName != null\" > , role_name = #{roleName} </if> " +
            " WHERE id = #{id} " +
            " </script> ")
    void update(RoleInfo roleInfo);

    @Select(" <script> " +
            " SELECT role_name FROM " + ROLE_TABLE_NAME +
            " WHERE id IN <foreach collection='roleIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    Set<String> selectRoleNameByIds(Set<Long> roleIds);
}
