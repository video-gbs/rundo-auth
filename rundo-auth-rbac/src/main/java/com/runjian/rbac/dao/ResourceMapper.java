package com.runjian.rbac.dao;

import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.vo.response.GetResourcePageRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/6 10:34
 */
@Mapper
@Repository
public interface ResourceMapper {

    String RESOURCE_TABLE_NAME = "rbac_resource";

    @Select(" SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<ResourceInfo> selectById(Long id);

    @Select(" <script> " +
            " SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE 1 = 1 " +
            " AND <if test=\"groupName != null\" > group_name LIKE CONCAT('%', #{groupName}, '%') </if> " +
            " AND <if test=\"resourceKey != null\" > resource_key LIKE CONCAT('%', #{resourceKey}, '%') </if> " +
            " </script>")
    List<GetResourcePageRsp> selectByNameAndResourceKeyLike(String groupName, String resourceKey);

    @Delete(" <script> " +
            " DELETE FROM " + RESOURCE_TABLE_NAME +
            " WHERE id IN <foreach collection='resourceIds' item='item' open='(' separator=',' close=')'> #{item} </foreach>  " +
            " </script>")
    void batchDelete(Set<Long> resourceIds);

    @Update(" UPDATE "  + RESOURCE_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " AND <if test=\"groupName != null\" > group_name = #{groupName} </if> " +
            " AND <if test=\"resourceName != null\" > resource_name = #{resourceName} </if> " +
            " AND <if test=\"resourceKey != null\" > resource_key = #{resourceKey} </if> " +
            " AND <if test=\"resourceValue != null\" > resource_value = #{resourceValue} </if> " +
            " WHERE id = #{id} ")
    void update(ResourceInfo resourceInfo);

    @Select(" <script> " +
            " SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE resource_key = #{resourceKey} " +
            " AND resource_value IN <foreach collection='resourceValues' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    Set<String> selectResourceValueByResourceKeyAndResourceValueIn(String resourceKey, Set<String> resourceValues);

    @Insert({" <script> " +
            " INSERT INTO " + RESOURCE_TABLE_NAME + "(group_name, resource_name, resource_key, resource_value, create_time, update_time) values " +
            " <foreach collection='saveList' item='item' separator=','>(#{groupName}, #{resourceName}, #{resourceKey}, #{item}, #{nowTime}, #{nowTime})</foreach> " +
            " </script>"})
    void batchAdd(String groupName, String resourceName, String resourceKey, Set<String> resourceValues, LocalDateTime nowTime);
}
