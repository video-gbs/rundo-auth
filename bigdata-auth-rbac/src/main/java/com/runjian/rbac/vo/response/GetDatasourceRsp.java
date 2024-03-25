package com.runjian.rbac.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/20 17:59
 */
@Data
public class GetDatasourceRsp {

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
     * 配置
     */
    private String config;

    /**
     * 负责人名称
     */
    private String principalName;

    /**
     * 负责人id
     */
    private Long principalId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
