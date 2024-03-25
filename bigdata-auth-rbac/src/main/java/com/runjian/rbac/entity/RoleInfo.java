package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/30 14:55
 */
@Data
public class RoleInfo {

    private Long id;

    /**
     * 组织id
     */
    private Long organizationId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 禁用状态
     */
    private Integer disabled;

    /**
     * 软删除状态
     */
    private Integer deleted;

    /**
     * 创建人
     */
    private String createBy;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
