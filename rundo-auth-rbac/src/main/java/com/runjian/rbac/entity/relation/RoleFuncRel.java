package com.runjian.rbac.entity.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/31 10:40
 */
@Data
public class RoleFuncRel {

    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 功能id
     */
    private Long functionId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
