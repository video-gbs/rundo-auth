package com.runjian.rbac.dao;

import com.runjian.rbac.dao.relation.RoleResourceMapper;
import com.runjian.rbac.entity.ResourceInfo;
import com.runjian.rbac.vo.response.GetResourceRootRsp;
import com.runjian.rbac.vo.response.GetResourceTreeRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
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

    @Delete(" <script> " +
            " DELETE FROM " + RESOURCE_TABLE_NAME +
            " WHERE id IN <foreach collection='resourceIds' item='item' open='(' separator=',' close=')'> #{item} </foreach>  " +
            " </script>")
    void batchDelete(Set<Long> resourceIds);

    @Update(" <script> " +
            " UPDATE "  + RESOURCE_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test=\"resourceName != null\" > , resource_name = #{resourceName} </if> " +
            " <if test=\"resourceValue != null\" > , resource_value = #{resourceValue} </if> " +
            " WHERE id = #{id} "+
            " </script> ")
    void update(ResourceInfo resourceInfo);

    @Select(" <script> " +
            " SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE resource_key = #{resourceKey} " +
            " AND resource_value IN <foreach collection='resourceValues' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    Set<String> selectResourceValueByResourceKeyAndResourceValueIn(String resourceKey, Set<String> resourceValues);

    @Select(" <script> " +
            " SELECT rt.* FROM " + RESOURCE_TABLE_NAME + " rt " +
            " RIGHT JOIN " + RoleResourceMapper.ROLE_RESOURCE_TABLE_NAME + " rrt ON rrt.resource_id = rt.id " +
            " WHERE rt.id IN <foreach collection='roleIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    Set<ResourceInfo> selectByRoleIds(Set<Long> roleIds);

    @Insert(" <script> " +
            " INSERT INTO " + RESOURCE_TABLE_NAME + "(resource_pid, resource_type, resource_name, resource_key, resource_value, sort, level, create_time, update_time) VALUES " +
            " <foreach collection='saveList' item='item' separator=','> (#{item.resourcePid}, #{item.resourceType}, #{item.resourceName}, #{item.resourceKey}, #{item.resourceValue}, #{item.sort}, #{item.level}, #{item.createTime}, #{item.updateTime}) </foreach> " +
            " </script>")
    void batchAdd(List<ResourceInfo> saveList);

    @Select(" <script> " +
            " SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE resource_key = #{resourceKey} " +
            " <if test=\"isIncludeResource = false\" > AND resource_type = 1 </if> " +
            " </script>")
    List<GetResourceTreeRsp> selectAllByResourceKeyAndResourceType(String resourceKey, Boolean isIncludeResource);

    @Select(" SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE level LIKE CONCAT('%', #{level}, '%') ")
    List<ResourceInfo> selectAllLikeByLevel(String level);

    @Update(" <script> " +
            " <foreach collection='childList' item='item' separator=';'> " +
            " UPDATE " + RESOURCE_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " <if test=\"item.resourcePid != null\" > , resource_pid = #{item.resourcePid} </if> " +
            " <if test=\"item.sort != null\" > , sort = #{item.sort} </if> " +
            " <if test=\"item.level != null\" > , level = #{item.level} </if> " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void updateAll(List<ResourceInfo> childList);

    @Select(" <script> " +
            " SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE resource_pid = #{resourcePid} " +
            " </script>")
    List<ResourceInfo> selectChildByPid(Long resourcePid);

    @Select(" <script> " +
            " SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE resource_id IN <foreach collection='resourceIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " AND resource_key = #{resourceKey} " +
            " <if test=\"isIncludeResource = false\" > AND resource_type = 1 </if> " +
            " </script>")
    List<GetResourceTreeRsp> selectAllByResourceKeyAndResourceTypeAndResourceIdsIn(String resourceKey, Boolean isIncludeResource, Set<Long> resourceIds);

    @Select(" <script> " +
            " SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE resource_key = #{resourceKey} " +
            " AND resource_pid != 0 " +
            " <if test=\"isIncludeResource = false\" > AND resource_type = 1 </if> " +
            " </script>")
    List<GetResourceTreeRsp> selectChildByResourceKeyAndResourceType(String resourceKey, Boolean isIncludeResource);


    @Select(" SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE resource_pid = 0 AND resource_key = #{resourceKey}")
    Optional<GetResourceTreeRsp> selectRootByResourceKey(String resourceKey);

    @Insert(" INSERT INTO " + RESOURCE_TABLE_NAME + "(resource_pid, resource_type, resource_name, resource_key, resource_value, sort, level, create_time, update_time) VALUES " +
            " (#{resourcePid}, #{resourceType}, #{resourceName}, #{resourceKey}, #{resourceValue}, #{sort}, #{level}, #{createTime}, #{updateTime}) " )
    void save(ResourceInfo resourceInfo);

    @Select(" SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE resource_pid = 0")
    List<GetResourceRootRsp> selectAllRoot();

    @Select(" SELECT * FROM " + RESOURCE_TABLE_NAME +
            " WHERE resource_key = #{resourceKey} AND resource_type = #{resourceType}")
    List<ResourceInfo> selectAllByResourceKey(String resourceKey, Integer resourceType);
}
