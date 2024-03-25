package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/14 15:28
 */
@Data
public class OrganizeInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否禁用
     */
    private Integer disabled;

    /**
     * 是否删除
     */
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime endTime;
}
