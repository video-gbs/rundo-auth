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

    @Select("SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE id = #{id}")
    Optional<MenuInfo> selectById(Long id);


    @Select(" <script> " +
            " SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE 1 = 1 " +
            " <if test=\"name != null\" > AND name LIKE CONCAT('%', #{name}, '%')  </if> " +
            " <if test=\"path != null\" > AND path LIKE CONCAT('%', #{path}, '%')  </if> " +
            " </script> ")
    List<GetMenuTreeRsp> selectByNameLikeAndPathLike(String name, String path);

    @Select(" SELECT id FROM " + MENU_TABLE_NAME +
            " WHERE level LIKE CONCAT(#{level},'%')")
    Set<Long> selectIdByLevelLike(String level);

    @Select(" SELECT id FROM " + MENU_TABLE_NAME )
    Set<Long> selectAllId();

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

    @Insert(" INSERT INTO " + MENU_TABLE_NAME +
            " (menu_pid, sort, menu_type, path, component, name, icon, description, level, level_num, is_full_screen, disabled, create_time, update_time) " +
            " VALUES " +
            " (#{menuPid}, #{sort}, #{menuType}, #{path}, #{component}, #{name}, #{icon}, #{description}, #{level}, #{levelNum}, #{isFullScreen}, #{disabled}, #{createTime}, #{updateTime})")
    void save(MenuInfo menuInfo);

    @Update(" <script> " +
            " UPDATE "  + MENU_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test=\"menuPid != null\" > , menu_pid = #{menuPid} </if> " +
            " <if test=\"sort != null\" > , sort = #{sort} </if> " +
            " <if test=\"menuType != null\" > , menu_type = #{menuType} </if> " +
            " <if test=\"level != null\" > , level = #{level} </if> " +
            " <if test=\"isFullScreen != null\" > , is_full_screen = #{isFullScreen} </if> " +
            " <if test=\"disabled != null\" > , disabled = #{disabled} </if> " +
            " , path = #{path} " +
            " , component = #{component} " +
            " , name = #{name} " +
            " , icon = #{icon} " +
            " , description = #{description} " +
            " WHERE id = #{id} "+
            " </script> ")
    void update(MenuInfo menuInfo);


    @Select(" <script> " +
            " SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE id IN <foreach collection='ids' item='item' open='(' separator=',' close=')'> #{item} </foreach>  " +
            " </script> ")
    List<GetMenuTreeRsp> selectAllByIds(Set<Long> ids);


    @Select(" <script> " +
            " SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE  level_num &gt;= #{levelNumStart} " +
            " AND menu_type != 0 " +
            " <if test=\"levelNumEnd != null\" > AND level_num &lt;= #{levelNumEnd} </if> " +
            " </script> ")
    List<GetMenuTreeRsp> selectAllByLevelNumStartAndLevelNumEnd(Integer levelNumStart, Integer levelNumEnd);

    @Select(" <script> " +
            " SELECT * FROM " + MENU_TABLE_NAME +
            " WHERE id IN <foreach collection='menuIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " AND level_num &gt;= #{levelNumStart} " +
            " AND disabled = 0 " +
            " AND menu_type != 0 " +
            " <if test=\"levelNumEnd != null\" > AND level_num &lt;= #{levelNumEnd} </if> " +
            " </script> ")
    List<GetMenuTreeRsp> selectAllByLevelNumStartAndLevelNumEndAndMenuIdsIn(Integer levelNumStart, Integer levelNumEnd, Set<Long> menuIds);
}
