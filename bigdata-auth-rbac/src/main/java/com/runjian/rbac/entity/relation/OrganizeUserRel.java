package com.runjian.rbac.entity.relation;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/14 16:08
 */
public class OrganizeUserRel {

    /**
     * 逐渐id
     */
    private Long id;

    /**
     * 组织id
     */
    private Long organizeId;

    /**
     * 用户id
     */
    private Long userId;

    private LocalDateTime createTime;

}
