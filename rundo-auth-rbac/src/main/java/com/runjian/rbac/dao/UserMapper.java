package com.runjian.rbac.dao;

import com.github.pagehelper.PageInfo;
import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.vo.response.GetUserPageRsp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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

    @Select(" SELECT ut.* FROM " + SectionMapper.SECTION_TABLE_NAME + " st " +
            " LEFT JOIN " + USER_TABLE_NAME + " ut ON ut.section_id = st.id " +
            " WHERE st.level LIKE CONCAT(#{sectionLevel}, '%') " +
            " AND <if test=\"username != null\" > username LIKE CONCAT('%', #{username}, '%') </if> " +
            " AND <if test=\"workName != null\" > work_name LIKE CONCAT('%', #{workName}, '%') </if> "
            )
    List<GetUserPageRsp> selectAllUserBySectionLevelLikeAndUsernameAndWorkName(String sectionLevel, String username, String workName);

    @Select(" SELECT * FROM " + USER_TABLE_NAME +
            " WHERE section_id = #{sectionId} " +
            " AND <if test=\"username != null\" > username LIKE CONCAT('%', #{username}, '%') </if> " +
            " AND <if test=\"workName != null\" > work_name LIKE CONCAT('%', #{workName}, '%') </if> "
    )
    List<GetUserPageRsp> selectAllUserBySectionIdAndUsernameAndWorkName(Long sectionId, String username, String workName);

    @Select(" SELECT * FROM " + USER_TABLE_NAME +
            " WHERE id = #{userId} ")
    Optional<UserInfo> selectById(Long userId);

    void updateDisabled(UserInfo userInfo);

    @Select(" SELECT * FROM " + USER_TABLE_NAME +
            " WHERE username = #{username} ")
    Optional<UserInfo> selectByUsername(String username);

    void save(UserInfo userInfo);

    void update(UserInfo userInfo);

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
