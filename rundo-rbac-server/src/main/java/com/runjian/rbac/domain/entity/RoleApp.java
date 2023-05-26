package com.runjian.rbac.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色组织关联表
 */
@Data
@EqualsAndHashCode
@TableName("role_app")
@ApiModel(value = "角色应用关联表", description = "角色应用")
public class RoleApp {
    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Long roleId;

    /**
     * 组织ID
     */
    @ApiModelProperty("应用ID")
    private Long appId;

}