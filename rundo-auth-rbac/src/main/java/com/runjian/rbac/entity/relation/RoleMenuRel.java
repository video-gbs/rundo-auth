package com.runjian.rbac.entity.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/30 15:59
 */

@Data
public class RoleMenuRel {

    private Long id;

    private Long roleId;

    private Long menuId;

    private String createBy;

    private LocalDateTime createTime;
}
