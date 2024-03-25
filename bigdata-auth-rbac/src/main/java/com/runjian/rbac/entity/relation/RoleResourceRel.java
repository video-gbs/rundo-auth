package com.runjian.rbac.entity.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/31 10:38
 */
@Data
public class RoleResourceRel {

    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 功能Id
     */
    private Long functionId;

    /**
     * 功能权限
     */
    private String authFunc;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
