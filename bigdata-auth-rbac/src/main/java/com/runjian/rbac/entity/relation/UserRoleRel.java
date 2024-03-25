package com.runjian.rbac.entity.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户角色关系表
 * @author Miracle
 * @date 2023/5/30 15:57
 */
@Data
public class UserRoleRel {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
