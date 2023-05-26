package com.runjian.rbac.domain.dto.system;

import com.runjian.rbac.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysRoleInfoDTO
 * @Description 新建角色
 * @date 2023-01-10 周二 14:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "添加角色", description = "角色信息")
public class SysRoleInfoDTO {

    @ApiModelProperty("编号ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    //////////////////////权限配置//////////////////////
    // 1 系统权限
    // 1-1 功能应用类列表
    @ApiModelProperty("功能应用类列表，idStr的值")
    List<String> appIds;
    // 1-2 配置类应用列表
    @ApiModelProperty("配置类应用列表，idStr的值")
    List<String> configIds;
    // 1-3 运维类应用列表
    @ApiModelProperty("运维类应用列表，idStr的值")
    List<String> devopsIds;
    // 1-4 部门管理权限
    @ApiModelProperty("部门ID列表")
    List<Long> orgIds;

    // 1-5 安防区域权限
    @ApiModelProperty("安防区域ID列表")
    List<Long> areaIds;


}
