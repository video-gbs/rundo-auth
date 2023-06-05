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
 * @date 2023/5/31 14:24
 */
@Mapper
@Repository
public interface RoleFuncMapper {

    String ROLE_FUNC_TABLE_NAME = "rbac_role_func";

    @Insert({" <script> " +
            " INSERT INTO " + ROLE_FUNC_TABLE_NAME + "(role_id, func_id, create_by, create_time) values " +
            " <foreach collection='funcIds' item='item' separator=','>(#{roleId}, #{item}, #{createBy}, #{createTime})</foreach> " +
            " </script>"})
    void saveAll(Long roleId, Set<Long> funcIds, String createBy, LocalDateTime createTime);

    @Delete(" DELETE FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    void deleteAllByRoleId(Long roleId);

    @Select(" SELECT menu_id FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE role_id = #{roleId} ")
    Set<Long> selectMenuIdByFuncId(Long roleId);

    @Delete(" DELETE FROM " + ROLE_FUNC_TABLE_NAME +
            " WHERE role_id = #{roleId} " +
            " AND func_id IN <foreach collection='funcIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> "
    )
    void deleteAllByRoleIdAndFuncIds(Long roleId, Set<Long> funcIds);
}
