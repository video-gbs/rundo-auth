package com.runjian.rbac.dao.relation;

import com.runjian.rbac.entity.relation.FuncResourceRel;
import com.runjian.rbac.vo.dto.CacheFuncDto;
import com.runjian.rbac.vo.response.GetFuncResourceRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/31 14:37
 */
@Mapper
@Repository
public interface FuncResourceMapper {

    String FUNC_RESOURCE_TABLE_NAME = "rbac_func_resource";

    @Delete(" <script> " +
            " DELETE FROM " + FUNC_RESOURCE_TABLE_NAME +
            " WHERE id IN <foreach collection='resourceIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    void deleteAllByResourceIds(Set<Long> resourceIds);

    @Insert(" INSERT INTO " + FUNC_RESOURCE_TABLE_NAME +
            " (func_id, resource_key, validate_param, multi_group, disabled, create_time, update_time) " +
            " VALUES " +
            " (#{funcId}, #{resourceKey}, #{validateParam}, #{multiGroup}, #{disabled}, #{createTime}, #{updateTime})")
    void save(FuncResourceRel funcResourceRel);

    @Delete(" DELETE FROM " + FUNC_RESOURCE_TABLE_NAME +
            " WHERE func_id = #{funcId} ")
    void deleteAllByFuncId(Long funcId);


    @Select(" SELECT * FROM " + FUNC_RESOURCE_TABLE_NAME +
            " WHERE func_id = #{funcId} ")
    List<GetFuncResourceRsp> selectByFuncId(Long funcId);

    @Select(" SELECT resource_key, validate_param, multi_group FROM " + FUNC_RESOURCE_TABLE_NAME +
            " WHERE func_id = #{funcId} ")
    List<CacheFuncDto.FuncResourceData> selectFuncResourceDataByFuncId(Long funcId);

    @Select(" SELECT * FROM " + FUNC_RESOURCE_TABLE_NAME +
            " WHERE id = #{funcResourceId} ")
    Optional<FuncResourceRel> selectById(Long funcResourceId);

    @Delete(" DELETE FROM " + FUNC_RESOURCE_TABLE_NAME +
            " WHERE id = #{funcResourceId} ")
    void deleteById(Long funcResourceId);

    @Update(" UPDATE "  + FUNC_RESOURCE_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " disabled = #{disabled} " +
            " WHERE id = #{id} ")
    void updateDisabled(FuncResourceRel funcResourceRel);

    @Update(" UPDATE "  + FUNC_RESOURCE_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " multi_group = #{multiGroup}, " +
            " resource_key = #{resourceKey}, " +
            " validate_param = #{validateParam} " +
            " WHERE id = #{id} ")
    void update(FuncResourceRel funcResourceRel);

    @Select(" <script> " +
            " SELECT * FROM " + FUNC_RESOURCE_TABLE_NAME +
            " WHERE func_id IN <foreach collection='funcIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    List<FuncResourceRel> selectByFuncIdIn(List<Long> funcIds);
}
