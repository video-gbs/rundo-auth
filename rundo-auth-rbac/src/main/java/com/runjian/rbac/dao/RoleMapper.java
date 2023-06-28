package com.runjian.rbac.dao;

import com.runjian.common.constant.CommonEnum;
import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.RoleInfo;
import com.runjian.rbac.vo.response.GetRolePageRsp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
            " WHERE deleted !=  1 " +
            " <if test=\"roleName != null\" > AND role_name LIKE CONCAT('%', #{roleName}, '%') </if> " +
            " <if test=\"createBy != null\" > AND create_by LIKE CONCAT('%', #{createBy}, '%') </if> " +
            " <if test=\"createTimeStart != null\" > AND create_time &gt;= #{createTimeStart} </if> " +
            " <if test=\"createTimeEnd != null\" > AND create_time &lt;= #{createTimeEnd} </if> " +
            " </script> ")
    List<GetRolePageRsp> selectPageByRoleNameAndCreateByAndCreateTime(int page, int num, String roleName, String createBy, LocalDateTime createTimeStart, LocalDateTime createTimeEnd);

    @Select(" <script> " +
            " SELECT rt.* FROM " + ROLE_TABLE_NAME + " rt " +
            " <if test=\"userId != null\" > RIGHT JOIN " + UserRoleMapper.USER_ROLE_TABLE_NAME + " ur ON rt.id = ur.role_id </if> " +
            " WHERE rt.deleted !=  1 " +
            " <if test=\"userId != null\" > AND ur.user_id = #{userId} </if> " +
            " <if test=\"roleName != null\" >  AND rt.role_name LIKE CONCAT('%', #{roleName}, '%') </if> " +
            " </script> ")
    List<GetRolePageRsp> selectPageByUserIdAndRoleName(Long userId, String roleName);

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
            " (role_name, role_desc, create_time, update_time) " +
            " VALUES " +
            " (#{role_name}, #{role_desc}, #{createTime}, #{updateTime})")
    void save(RoleInfo roleInfo);

    @Select(" <script> " +
            " SELECT rt.* FROM " + ROLE_TABLE_NAME + " rt " +
            " <if test=\"userId != null\" > RIGHT JOIN " + UserRoleMapper.USER_ROLE_TABLE_NAME + " ur ON rt.id = ur.role_id </if> " +
            " WHERE rt.deleted !=  1 " +
            " AND ur.user_id = #{userId} " +
            " </script> ")
    List<RoleInfo> selectByUserId(Long userId);
}
