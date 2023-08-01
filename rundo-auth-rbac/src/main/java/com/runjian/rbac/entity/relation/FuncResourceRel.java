package com.runjian.rbac.entity.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/31 14:35
 */
@Data
public class FuncResourceRel {

    private Long id;

    /**
     * 功能id
     */
    private Long funcId;

    /**
     * 资源key
     */
    private String resourceKey;

    /**
     * 校验参数
     */
    private String validateParam;

    /**
     * 多维校验
     */
    private Integer enableMultiCheck;

    /**
     * 是否禁用
     */
    private Integer disabled;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
