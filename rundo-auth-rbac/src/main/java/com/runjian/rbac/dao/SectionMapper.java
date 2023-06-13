package com.runjian.rbac.dao;

import com.runjian.rbac.entity.SectionInfo;
import com.runjian.rbac.vo.response.GetSectionTreeRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/5/30 15:02
 */
@Mapper
@Repository
public interface SectionMapper {


    String SECTION_TABLE_NAME = "rbac_section";

    @Select("SELECT id, section_pid, section_name, section_sort, leader_name, phone, level, description FROM " + SECTION_TABLE_NAME)
    List<GetSectionTreeRsp> selectAllByTree();

    @Insert(" INSERT INTO " + SECTION_TABLE_NAME +
            " (section_pid, section_name, section_sort, leader_name, phone, level, description, create_time, update_time) " +
            " VALUES " +
            " (#{sectionPid}, #{sectionName}, #{sectionSort}, #{leaderName}, #{phone}, #{level}, #{description}, #{createTime}, #{updateTime})")
    void save(SectionInfo sectionInfo);

    @Select("SELECT * FROM " + SECTION_TABLE_NAME +
            " WHERE id = #{sectionId}")
    Optional<SectionInfo> selectById(Long sectionId);

    @Select("SELECT count(*) FROM " + SECTION_TABLE_NAME +
            " WHERE section_pid = #{sectionPid}")
    Integer selectChildCountByPid(Long sectionPid);


    @Delete(" <script> " +
            " DELETE FROM " + SECTION_TABLE_NAME +
            " WHERE id = #{sectionId} " +
            " </script>")
    void deleteById(Long sectionId);

    @Select(" SELECT * FROM " + SECTION_TABLE_NAME +
            " WHERE level LIKE CONCAT(#{level},'%') ")
    List<SectionInfo> selectAllLikeByLevel(String level);

    @Update(" <script> " +
            " <foreach collection='sectionList' item='item' separator=';'> " +
            " UPDATE " + SECTION_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " ,<if test=\"item.sectionPid != null\" > section_pid = #{item.sectionPid} </if> " +
            " ,<if test=\"item.sectionName != null\" > section_name = #{item.sectionName} </if> " +
            " ,<if test=\"item.sectionSort != null\" > section_sort = #{item.sectionSort} </if> " +
            " ,<if test=\"item.level != null\" > level = #{item.level} </if> " +
            " , leader_name = #{item.leaderName} " +
            " , phone = #{item.phone}" +
            " , description = #{item.description} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void updateAll(List<SectionInfo> sectionList);

    @Select(" SELECT * FROM " + SECTION_TABLE_NAME +
            " WHERE section_pid = #{sectionPid} ")
    List<SectionInfo> selectChildByPid(Long sectionPid);
}
