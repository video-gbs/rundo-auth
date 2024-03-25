package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/21 14:50
 */
@Data
public class TeamInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 管理员id
     */
    private Long principalId;

    /**
     * 是否禁用
     */
    private Integer disabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
