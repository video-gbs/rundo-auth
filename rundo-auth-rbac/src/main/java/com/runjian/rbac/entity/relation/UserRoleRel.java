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

    private Long userId;

    private Long roleId;

    private String createBy;

    private LocalDateTime createTime;
}
