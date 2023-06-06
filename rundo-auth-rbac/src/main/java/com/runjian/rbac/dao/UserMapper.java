package com.runjian.rbac.dao;

import com.github.pagehelper.PageInfo;
import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.vo.response.GetUserPageRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/30 14:54
 */
@Mapper
@Repository
public interface UserMapper {

    String USER_TABLE_NAME = "rbac_user";

    @Select(" SELECT count(*) FROM " + USER_TABLE_NAME +
            " WHERE section_id = #{sectionId} ")
    Integer selectCountBySectionId(Long sectionId);

    @Select(" <script> " +
            " SELECT ut.* FROM " + SectionMapper.SECTION_TABLE_NAME + " st " +
            " LEFT JOIN " + USER_TABLE_NAME + " ut ON ut.section_id = st.id " +
            " WHERE deleted != 1 " +
            " AND st.level LIKE CONCAT(#{sectionLevel}, '%') " +
            " AND <if test=\"username != null\" > username LIKE CONCAT('%', #{username}, '%') </if> " +
            " AND <if test=\"workName != null\" > work_name LIKE CONCAT('%', #{workName}, '%') </if> " +
            " </script>")
    List<GetUserPageRsp> selectAllUserBySectionLevelLikeAndUsernameAndWorkName(String sectionLevel, String username, String workName);

    @Select(" <script> " +
            " SELECT * FROM " + USER_TABLE_NAME +
            " WHERE deleted != 1 " +
            " AND section_id = #{sectionId} " +
            " AND <if test=\"username != null\" > username LIKE CONCAT('%', #{username}, '%') </if> " +
            " AND <if test=\"workName != null\" > work_name LIKE CONCAT('%', #{workName}, '%') </if> " +
            " </script>"
    )
    List<GetUserPageRsp> selectAllUserBySectionIdAndUsernameAndWorkName(Long sectionId, String username, String workName);

    @Select(" SELECT * FROM " + USER_TABLE_NAME +
            " WHERE id = #{userId} ")
    Optional<UserInfo> selectById(Long userId);

    @Update(" UPDATE "  + USER_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " disabled = #{disabled} " +
            " WHERE id = #{id} ")
    void updateDisabled(UserInfo userInfo);

    @Select(" SELECT * FROM " + USER_TABLE_NAME +
            " WHERE username = #{username} ")
    Optional<UserInfo> selectByUsername(String username);

    @Insert(" INSERT INTO " + USER_TABLE_NAME +
            " (username, password, work_name, phone, work_num, section_id, address, expiry_start_time, expiry_end_time, description, create_time, update_time) " +
            " VALUES " +
            " (#{username}, #{password}, #{workName}, #{phone}, #{workNum}, #{sectionId}, #{address}, #{expiryStartTime}, #{expiryEndTime}, #{description},#{createTime}, #{updateTime}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(UserInfo userInfo);

    @Update(" <script> " +
            " UPDATE "  + USER_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " AND section_id = #{sectionId} " +
            " AND <if test=\"password != null\" > password = #{password} </if> " +
            " AND <if test=\"workName != null\" > work_name = #{workName} </if> " +
            " AND <if test=\"phone != null\" > phone = #{phone} </if> " +
            " AND <if test=\"workNum != null\" > work_num = #{workNum} </if> " +
            " AND <if test=\"address != null\" > address = #{address} </if> " +
            " AND <if test=\"expiryStartTime != null\" > work_name = #{expiryStartTime} </if> " +
            " AND <if test=\"workName != null\" > work_name = #{workName} </if> " +
            " AND <if test=\"description != null\" > description = #{description} </if> " +
            " WHERE id = #{id} " +
            " </script> ")
    void update(UserInfo userInfo);

    @Update(" <script> " +
            " <foreach collection='userIds' item='item' separator=';'> " +
            " UPDATE " + USER_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , deleted = #{deleted} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateDeleted(Set<Long> userIds, Integer deleted);

    @Select(" <script> " +
            " SELECT ut.* FROM " + UserRoleMapper.USER_ROLE_TABLE_NAME + " ur" +
            " LEFT JOIN " + USER_TABLE_NAME + " ut ON " + " ur.user_id = ut.id" +
            " WHERE " +
            " <if test=\"username != null\" > username LIKE CONCAT('%', #{username}, '%')  AND </if> " +
            " <if test=\"isBinding == true\" > role_id = #{roleId} </if> " +
            " <if test=\"isBinding == false\" > role_id != #{roleId} </if> " +
            " </script>")
    List<GetUserPageRsp> selectByInRoleIdAndUsername(Long roleId, String username, Boolean isBinding);
}
