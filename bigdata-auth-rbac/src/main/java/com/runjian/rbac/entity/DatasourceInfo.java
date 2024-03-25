package com.runjian.rbac.entity;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/14 15:35
 */
@Data
public class DatasourceInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 组织id
     */
    private Long organizationId;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 类型
     */
    private String type;

    /**
     * 组件
     */
    private String module;

    /**
     * 负责人id
     */
    private Long principalId;

    /**
     * 配置
     */
    private String config;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
