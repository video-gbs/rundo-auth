package com.runjian.rbac.dao.relation;

import com.runjian.rbac.dao.FuncMapper;
import com.runjian.rbac.entity.relation.RoleFuncRel;
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
 * @date 2023/5/31 14:24
 */
@Mapper
@Repository
public interface RoleFuncMapper {

    String ROLE_FUNC_TABLE_NAME = "rbac_role_func";

    @Insert(" <script> " +
            " INSERT INTO " + ROLE_FUNC_TABLE_NAME +
            " (role_id, func_id, create_by, create_time) VALUES " +
            " <foreach collection='funcIds' item='item' separator=','>(#{roleId}, #{item}, #{createBy}, #{createTime})</foreach> " +
            " </script>")
    void saveAll(Long roleId, Set<Long> funcIds, String createBy, LocalDateTime createTime);

    @Delete(" DELETE FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    void deleteAllByRoleId(Long roleId);

    @Select(" SELECT * func_id FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    Set<Long> selectFuncIdByRoleId(Long roleId);

    @Delete(" <script> " +
            " DELETE FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE role_id = #{roleId} " +
            " AND func_id IN <foreach collection='funcIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> "+
            " </script>")
    void deleteAllByRoleIdAndFuncIds(Long roleId, Set<Long> funcIds);

    @Select(" <script> " +
            " SELECT func_id FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE role_id IN <foreach collection='roleIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> "+
            " </script>")
    List<Long> selectFuncIdByRoleIds(List<Long> roleIds);

    @Select(" SELECT role_id FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE func_id = #{funcId} ")
    List<Long> selectRoleIdsByFuncId(Long funcId);

    @Delete(" DELETE FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE func_id = #{funcId} ")
    void deleteAllByFuncId(Long funcId);

    @Select(" SELECT func_id FROM " + ROLE_FUNC_TABLE_NAME + " rft " +
            " LEFT JOIN " + FuncMapper.FUNC_TABLE_NAME + " ft ON " + " rft.func_id = ft.id " +
            " WHERE role_id = #{roleId} AND ft.menu_id = #{menuId} ")
    Set<Long> selectFuncIdByRoleIdAndMenuId(Long roleId, Long menuId);

    @Select(" <script> " +
            " SELECT * FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE func_id IN <foreach collection='funcIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    List<RoleFuncRel> selectByFuncIdIn(List<Long> funcIds);
}
