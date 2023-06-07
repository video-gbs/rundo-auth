package com.runjian.rbac.vo.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/7 9:49
 */
@Data
public class UserCacheDto {

    /**
     * 是否已授权
     */
    private Boolean isAuthorized;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色信息
     */
    private Set<Long> roleIds;


}
