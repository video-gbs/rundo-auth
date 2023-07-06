package com.runjian.rbac.dao;

import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.vo.response.GetUserPageRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
            " SELECT ut.*,st.section_name FROM " + USER_TABLE_NAME + " ut " +
            " LEFT JOIN " + SectionMapper.SECTION_TABLE_NAME + " st ON ut.section_id = st.id " +
            " WHERE ut.deleted != 1 " +
            " AND st.level LIKE CONCAT(#{sectionLevel}, '%') " +
            " <if test=\"username != null\" > AND ut.username LIKE CONCAT('%', #{username}, '%') </if> " +
            " <if test=\"workName != null\" > AND ut.work_name LIKE CONCAT('%', #{workName}, '%') </if> " +
            " </script>")
    List<GetUserPageRsp> selectAllUserBySectionLevelLikeAndUsernameAndWorkName(String sectionLevel, String username, String workName);

    @Select(" <script> " +
            " SELECT ut.*,st.section_name FROM " + USER_TABLE_NAME + " ut " +
            " LEFT JOIN " + SectionMapper.SECTION_TABLE_NAME + " st ON ut.section_id = st.id" +
            " WHERE ut.deleted != 1 " +
            " AND ut.section_id = #{sectionId} " +
            " <if test=\"username != null\" > AND ut.username LIKE CONCAT('%', #{username}, '%') </if> " +
            " <if test=\"workName != null\" > AND ut.work_name LIKE CONCAT('%', #{workName}, '%') </if> " +
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
            " SET update_time = #{updateTime} " +
            " <if test=\"password != null\" > , password = #{password} </if> " +
            " <if test=\"workName != null\" > , work_name = #{workName} </if> " +
            " <if test=\"phone != null\" > , phone = #{phone} </if> " +
            " <if test=\"workNum != null\" > , work_num = #{workNum} </if> " +
            " <if test=\"sectionId != null\" > , section_id = #{sectionId} </if> " +
            " <if test=\"address != null\" > , address = #{address} </if> " +
            " <if test=\"expiryStartTime != null\" > , expiry_start_time = #{expiryStartTime} </if> " +
            " <if test=\"description != null\" > , description = #{description} </if> " +
            " <if test=\"disabled != null\" > , disabled = #{disabled} </if> " +
            " WHERE id = #{id} " +
            " </script> ")
    void update(UserInfo userInfo);

    @Update(" <script> " +
            " <foreach collection='userIds' item='item' separator=';'> " +
            " UPDATE " + USER_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , deleted = #{deleted} " +
            " WHERE id = #{item} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateDeleted(Set<Long> userIds, Integer deleted, LocalDateTime updateTime);

    @Select(" <script> " +
            " SELECT ut.*,st.section_name FROM " + USER_TABLE_NAME + " ut " +
            " LEFT JOIN " + SectionMapper.SECTION_TABLE_NAME + " st ON ut.section_id = st.id" +
            " WHERE ut.deleted != 1 AND " +
            " ut.id IN <foreach collection='userIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " <if test=\"username != null\" > AND ut.username LIKE CONCAT('%', #{username}, '%') </if> " +
            " </script>")
    List<GetUserPageRsp> selectByUserIdsInAndUsername(Set<Long> userIds, String username);

    @Select(" <script> " +
            " SELECT ut.*,st.section_name FROM " + USER_TABLE_NAME + " ut " +
            " LEFT JOIN " + SectionMapper.SECTION_TABLE_NAME + " st ON ut.section_id = st.id" +
            " WHERE ut.deleted != 1 AND " +
            " ut.id NOT IN <foreach collection='userIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " <if test=\"username != null\" > AND ut.username LIKE CONCAT('%', #{username}, '%') </if> " +
            " </script>")
    List<GetUserPageRsp> selectByUserIdsNotInAndUsername(Set<Long> userIds, String username);

    @Select(" <script> " +
            " SELECT ut.*,st.section_name FROM " + USER_TABLE_NAME + " ut " +
            " LEFT JOIN " + SectionMapper.SECTION_TABLE_NAME + " st ON ut.section_id = st.id" +
            " WHERE ut.deleted != 1 " +
            " <if test=\"username != null\" > AND ut.username LIKE CONCAT('%', #{username}, '%') </if> " +
            " <if test=\"workName != null\" > AND ut.work_name LIKE CONCAT('%', #{workName}, '%') </if> " +
            " </script>")
    List<GetUserPageRsp> selectByUsernameAndWorkName(String username, String workName);

    @Select(" <script> " +
            " SELECT ut.*,st.section_name FROM " + USER_TABLE_NAME + " ut " +
            " LEFT JOIN " + SectionMapper.SECTION_TABLE_NAME + " st ON ut.section_id = st.id" +
            " WHERE ut.deleted != 1 " +
            " <if test=\"username != null\" > AND ut.username LIKE CONCAT('%', #{username}, '%') </if> " +
            " </script>")
    List<GetUserPageRsp> selectByUsernameLike(String username);
}
