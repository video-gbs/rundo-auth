package com.runjian.rbac.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@TableName("role_area")
@ApiModel(value = "角色区域映射对象", description = "角色区域")
public class RoleArea  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Long roleId;

    /**
     * 区划ID
     */
    @ApiModelProperty("安防区域ID")
    private Long areaId;
}