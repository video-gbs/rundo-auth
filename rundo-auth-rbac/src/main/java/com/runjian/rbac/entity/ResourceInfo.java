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
     * 资源名称
     */
    private String name;

    /**
     * 资源key
     */
    private String resourceKey;

    /**
     * 资源value
     */
    private String resourceValue;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
