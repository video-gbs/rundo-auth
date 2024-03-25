package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/14 15:37
 */
@Data
public class EngineInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 配置
     */
    private String config;

    /**
     * 创建人
     */
    private Long createBy;

    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private Long updateBy;

    private LocalDateTime updateTime;
}
