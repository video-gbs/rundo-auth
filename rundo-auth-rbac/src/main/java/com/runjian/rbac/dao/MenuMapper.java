package com.runjian.rbac.dao;

import com.runjian.rbac.entity.MenuInfo;
import com.runjian.rbac.vo.response.GetMenuTreeRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/30 15:53
 */
@Mapper
@Repository
public interface MenuMapper {

    String MENU_TABLE_NAME = "rbac_menu";

    @Select(" SELECT * FROM " + MENU_TABLE_NAME)
    List<GetMenuTreeRsp> selectAll();

    @Select("SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE id = #{id}")
    Optional<MenuInfo> selectById(Long id);

    @Select("SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE level LIKE CONCAT(#{level},'%')")
    List<GetMenuTreeRsp> selectByNameLikeAndPathLike(String name, String path);

    @Select(" <script> " +
            "SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE 1=1 " +
            " AND <if test=\"name != null\" > name = #{name} </if> " +
            " AND <if test=\"path != null\" > path = #{path} </if> " +
            " </script> ")
    List<GetMenuTreeRsp> selectAllByLevelLike(String level);

    @Select("SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE menu_pid = #{pid} ")
    Set<Long> selectIdByPid(Long pid);

    @Delete(" <script> " +
            " DELETE FROM " + MENU_TABLE_NAME +
            " WHERE id IN <foreach collection='menuIds' item='item' open='(' separator=',' close=')'> #{item} </foreach>  " +
            " </script>")
    void deleteAll(Set<Long> menuIds);

    @Update(" UPDATE "  + MENU_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " disabled = #{disabled} " +
            " WHERE id = #{id} ")
    void updateDisabled(MenuInfo menuInfo);

    @Update(" UPDATE "  + MENU_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " hidden = #{hidden} " +
            " WHERE id = #{id} ")
    void updateHidden(MenuInfo menuInfo);

    @Insert(" INSERT INTO " + MENU_TABLE_NAME +
            " (menu_pid, menu_sort, menu_type, path, component, name, icon, description, level, hidden, disabled, create_time, update_time) " +
            " VALUES " +
            " (#{menuPid}, #{menuSort}, #{menuType}, #{path}, #{component}, #{name}, #{icon}, #{description}, #{level}, #{hidden}, #{disabled}, #{createTime}, #{updateTime})")
    void save(MenuInfo menuInfo);

    @Update(" UPDATE "  + MENU_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " , <if test=\"menuPid != null\" > menu_pid = #{menuPid} </if> " +
            " , <if test=\"menu_sort != null\" > menuSort = #{menu_sort} </if> " +
            " , <if test=\"menu_type != null\" > menuType = #{menu_type} </if> " +
            " , <if test=\"level != null\" > level = #{level} </if> " +
            " , <if test=\"hidden != null\" > hidden = #{hidden} </if> " +
            " , <if test=\"disabled != null\" > disabled = #{disabled} </if> " +
            " , path = #{path} " +
            " , component = #{component} " +
            " , name = #{name} " +
            " , icon = #{icon} " +
            " , description = #{description} " +
            " WHERE id = #{id} ")
    void update(MenuInfo menuInfo);
}
