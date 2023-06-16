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


    @Select(" <script> " +
            " SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE 1 = 1 " +
            " AND <if test=\"name != null\" > name LIKE CONCAT('%', #{name}, '%')  </if> " +
            " AND <if test=\"path != null\" > path LIKE CONCAT('%', #{path}, '%')  </if> " +
            " </script> ")
    List<GetMenuTreeRsp> selectByNameLikeAndPathLike(String name, String path);

    @Select(" SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE level LIKE CONCAT(#{level},'%')")
    List<GetMenuTreeRsp> selectAllByLevelLike(String level);

    @Select(" SELECT id FROM " + MENU_TABLE_NAME +
            " WHERE level LIKE CONCAT(#{level},'%')")
    Set<Long> selectIdByLevelLike(String level);

    @Select(" SELECT * FROM " + MENU_TABLE_NAME +
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
            " (menu_pid, sort, menu_type, path, component, name, icon, description, level, hidden, disabled, create_time, update_time) " +
            " VALUES " +
            " (#{menuPid}, #{sort}, #{menuType}, #{path}, #{component}, #{name}, #{icon}, #{description}, #{level}, #{hidden}, #{disabled}, #{createTime}, #{updateTime})")
    void save(MenuInfo menuInfo);

    @Update(" UPDATE "  + MENU_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " , <if test=\"menuPid != null\" > menu_pid = #{menuPid} </if> " +
            " , <if test=\"sort != null\" > sort = #{sort} </if> " +
            " , <if test=\"menu_type != null\" > menu_type = #{menu_type} </if> " +
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


    @Select(" <script> " +
            " SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE id IN <foreach collection='ids' item='item' open='(' separator=',' close=')'> #{item} </foreach>  " +
            " </script> ")
    List<GetMenuTreeRsp> selectAllByIds(Set<Long> ids);


    @Select(" <script> " +
            " SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE level_num &gt;= #{levelNumStart} " +
            " <if test=\"levelNumEnd != null\" > AND level_num &lt;= #{levelNumEnd} </if> " +
            " </script> ")
    List<GetMenuTreeRsp> selectAllByLevelNumStartAndLevelNumEnd(Integer levelNumStart, Integer levelNumEnd);

    @Select(" <script> " +
            " SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE id IN <foreach collection='menuIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " AND level_num &gt;= #{levelNumStart} " +
            " <if test=\"levelNumEnd != null\" > AND level_num &lt;= #{levelNumEnd} </if> " +
            " </script> ")
    List<GetMenuTreeRsp> selectAllByLevelNumStartAndLevelNumEndAndMenuIdsIn(Integer levelNumStart, Integer levelNumEnd, Set<Long> menuIds);
}
