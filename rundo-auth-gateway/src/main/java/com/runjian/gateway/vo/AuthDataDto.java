package com.runjian.gateway.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/4/24 11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDataDto {

    /**
     * 是否授权
     */
    private Boolean isAuthorized;

    /**
     * 是否是超管
     */
    private Boolean isAdmin;

    /**
     * 用户名
     */
    private String username;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 角色id数组
     */
    private List<Long> roleIds;

    /**
     * 授权的资源
     */
    private String resourceKey;

    /**
     * 消息
     */
    private String msg;

}
