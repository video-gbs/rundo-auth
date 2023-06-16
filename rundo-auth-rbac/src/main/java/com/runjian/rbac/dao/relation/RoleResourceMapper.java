package com.runjian.rbac.dao.relation;

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
 * @date 2023/5/31 14:26
 */
@Mapper
@Repository
public interface RoleResourceMapper {

    String ROLE_RESOURCE_TABLE_NAME = "rbac_role_resource";

    @Insert(" <script> " +
            " INSERT INTO " + ROLE_RESOURCE_TABLE_NAME +
            " (role_id, resource_id, create_by, create_time) VALUES " +
            " <foreach collection='resourceIds' item='item' separator=','>(#{roleId}, #{item}, #{createBy}, #{createTime})</foreach> " +
            " </script>")
    void saveAll(Long roleId, Set<Long> resourceIds, String createBy, LocalDateTime createTime);

    @Delete(" DELETE FROM " + ROLE_RESOURCE_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    void deleteAllByRoleId(Long roleId);

    @Select(" SELECT resource_id FROM " + ROLE_RESOURCE_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    Set<Long> selectResourceIdByRoleId(Long roleId);

    @Delete(" <script> " +
            " DELETE FROM " + ROLE_RESOURCE_TABLE_NAME +
            " WHERE role_id = #{roleId} " +
            " AND resource_id IN <foreach collection='resourceIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    void deleteAllByRoleIdAndResourceIds(Long roleId, Set<Long> resourceIds);

    @Select(" <script> " +
            " SELECT *  FROM " + ROLE_RESOURCE_TABLE_NAME +
            " WHERE role_id IN <foreach collection='roleIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    Set<Long> selectResourceIdByRoleIds(List<Long> roleIds);
}
