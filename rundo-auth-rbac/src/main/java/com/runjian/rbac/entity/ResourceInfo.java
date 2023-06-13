package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/31 10:36
 */
@Data
public class ResourceInfo {

    private Long id;

    /**
     * 资源父id
     */
    private Long resourcePid;

    /**
     * 资源类型 1-节点 2-资源
     */
    private Integer resourceType;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源key
     */
    private String resourceKey;

    /**
     * 资源value
     */
    private String resourceValue;

    /**
     * 层级
     */
    private String level;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
