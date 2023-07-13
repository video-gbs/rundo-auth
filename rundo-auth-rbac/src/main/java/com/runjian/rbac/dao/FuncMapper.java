package com.runjian.rbac.dao;


import com.github.pagehelper.PageInfo;
import com.runjian.rbac.entity.FuncInfo;
import com.runjian.rbac.vo.response.GetFuncPageRsp;
import com.runjian.rbac.vo.response.GetFuncRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/31 10:32
 */

@Mapper
@Repository
public interface FuncMapper {

    String FUNC_TABLE_NAME = "rbac_func";

    @Select(" SELECT * FROM " + FUNC_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<FuncInfo> selectById(Long id);

    @Update(" UPDATE "  + FUNC_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " disabled = #{disabled} " +
            " WHERE id = #{id} ")
    void updateDisabled(FuncInfo funcInfo);

    @Select(" SELECT * FROM " + FUNC_TABLE_NAME +
            " WHERE path = #{path} ")
    Optional<FuncInfo> selectByPath(String path);

    @Update(" <script> " +
            " UPDATE "  + FUNC_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test=\"menuId != null\" > , menu_id = #{menuId} </if> " +
            " <if test=\"serviceName != null\" > , service_name = #{serviceName} </if> " +
            " <if test=\"funcName != null\" > , func_name = #{funcName} </if> " +
            " <if test=\"scope != null\" > , scope = #{scope} </if> " +
            " <if test=\"path != null\" > , path = #{path} </if> " +
            " <if test=\"method != null\" > , method = #{method} </if> " +
            " <if test=\"disabled != null\" > , disabled = #{disabled} </if> " +
            " WHERE id = #{id} " +
            " </script>")
    void update(FuncInfo funcInfo);

    @Delete(" <script> " +
            " DELETE FROM " + FUNC_TABLE_NAME +
            " WHERE id = #{id} " +
            " </script>")
    void deleteById(Long id);

    @Select(" <script> " +
            " SELECT * FROM " + FUNC_TABLE_NAME +
            " WHERE menu_id IN <foreach collection='menuIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " <if test=\"serviceName != null\" > AND service_name LIKE CONCAT('%', #{serviceName}, '%') </if> " +
            " <if test=\"funcName != null\" > AND func_name LIKE CONCAT('%', #{funcName}, '%') </if> " +
            " </script> ")
    List<GetFuncPageRsp> selectAllByMenuIdAndServiceNameLikeAndFuncNameLike(Set<Long> menuIds, String serviceName, String funcName);

    @Insert(" INSERT INTO " + FUNC_TABLE_NAME +
            " (menu_id, service_name, func_name, scope, path, method, disabled, create_time, update_time) " +
            " VALUES " +
            " (#{menuId}, #{serviceName}, #{funcName}, #{scope}, #{path}, #{method}, #{disabled}, #{createTime}, #{updateTime})")
    void save(FuncInfo funcInfo);

    @Select(" <script> " +
            " SELECT * FROM " + FUNC_TABLE_NAME +
            " WHERE id IN <foreach collection='funcIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " AND menu_id = #{menuId} AND disabled = 0 " +
            " </script>")
    List<GetFuncRsp> selectAllByMenuIdAndFuncIds(Integer menuId, List<Long> funcIds);

    @Select(" SELECT * FROM " + FUNC_TABLE_NAME +
            " WHERE menu_id = #{menuId} AND disabled = 0 ")
    List<GetFuncRsp> selectAllByMenuId(Integer menuId);

    @Select(" SELECT * FROM " + FUNC_TABLE_NAME +
            " WHERE path = #{path} AND method = #{method}")
    Optional<FuncInfo> selectByPathAndMethod(Integer method, String path);

    @Select(" <script> " +
            " SELECT * FROM " + FUNC_TABLE_NAME +
            " WHERE id IN <foreach collection='funcIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    List<FuncInfo> selectAllByIds(Set<Long> funcIds);
}
