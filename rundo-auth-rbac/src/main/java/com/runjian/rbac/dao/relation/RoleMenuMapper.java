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
 * @date 2023/5/31 14:25
 */
@Mapper
@Repository
public interface RoleMenuMapper {

    String ROLE_MENU_TABLE_NAME = "rbac_role_menu";

    @Insert(" <script> " +
            " INSERT INTO " + ROLE_MENU_TABLE_NAME +
            " (role_id, menu_id, create_by, create_time) VALUES " +
            " <foreach collection='menuIds' item='item' separator=','>(#{roleId}, #{item}, #{createBy}, #{createTime})</foreach> " +
            " </script>")
    void saveAll(Long id, Set<Long> menuIds, String createBy, LocalDateTime createTime);

    @Delete(" DELETE FROM " + ROLE_MENU_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    void deleteAllByRoleId(Long roleId);

    @Select(" SELECT menu_id FROM " + ROLE_MENU_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    Set<Long> selectMenuIdByRoleId(Long roleId);

    @Delete(" <script> " +
            " DELETE FROM " + ROLE_MENU_TABLE_NAME +
            " WHERE role_id = #{roleId} " +
            " AND menu_id IN <foreach collection='menuIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    void deleteAllByRoleIdAndMenuIds(Long roleId, Set<Long> menuIds);

    @Delete(" <script> " +
            " DELETE FROM " + ROLE_MENU_TABLE_NAME +
            " WHERE menu_id IN <foreach collection='menuIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    void deleteAllByMenuIds(Set<Long> menuIds);

    @Select(" <script> " +
            " SELECT menu_id FROM " + ROLE_MENU_TABLE_NAME +
            " WHERE role_id IN <foreach collection='roleIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    Set<Long> selectMenuIdByRoleIds(List<Long> roleIds);
}
