package com.runjian.rbac.dao;

import com.runjian.rbac.entity.DictInfo;
import com.runjian.rbac.vo.response.GetDictGroupRsp;
import com.runjian.rbac.vo.response.GetDictPageRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/30 15:06
 */
@Mapper
@Repository
public interface DictMapper {

    String DICT_TABLE_NAME = "rbac_dict";

    @Select(" SELECT * FROM " + DICT_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<DictInfo> selectById(Long id);

    @Insert(" INSERT INTO " + DICT_TABLE_NAME +
            " (group_name, group_code, item_name, item_value, description, create_time, update_time) " +
            " VALUES " +
            " (#{groupName}, #{groupCode}, #{itemName}, #{itemValue}, #{description},#{createTime}, #{updateTime})")
    void save(DictInfo dictInfo);

    @Delete(" <script> " +
            " DELETE FROM " + DICT_TABLE_NAME +
            " WHERE id IN <foreach collection='dictIds' item='item' open='(' separator=',' close=')'> #{item} </foreach>  " +
            " </script>")
    void batchDeleted(Set<Long> dictIds);


    @Update(" UPDATE "  + DICT_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " AND <if test=\"groupName != null\" > groupName = #{groupName} </if> " +
            " AND <if test=\"groupCode != null\" > group_code = #{groupCode} </if> " +
            " AND <if test=\"itemName != null\" > item_name = #{itemName} </if> " +
            " AND <if test=\"itemValue != null\" > item_value = #{itemValue} </if> " +
            " AND <if test=\"description != null\" > description = #{description} </if> " +
            " WHERE id = #{id} ")
    void update(DictInfo dictInfo);

    @Select(" <script> " +
            " SELECT * FROM " + DICT_TABLE_NAME +
            " WHERE 1=1 " +
            " AND <if test=\"groupName != null\" > group_name LIKE CONCAT('%', #{groupName}, '%') </if> " +
            " AND <if test=\"itemName != null\" > item_name LIKE CONCAT('%', #{itemName}, '%') </if> " +
            " </script>")
    List<GetDictPageRsp> selectByGroupNameAndItemName(String groupName, String itemName);


    @Select(" SELECT * FROM " + DICT_TABLE_NAME +
            " WHERE group_code = #{groupCode} ")
    List<GetDictGroupRsp> selectByGroupCode(String groupCode);
}
