package com.runjian.auth.vo.response;

import lombok.Data;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/6 16:12
 */
@Data
public class AuthUserRsp {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 是否过期
     */
    private boolean accountNonExpired;

    /**
     * 是否上锁
     */
    private boolean accountNonLocked;

    /**
     * 密码是否过期
     */
    private boolean credentialsNonExpired;

    /**
     * 是否开启多因子校验
     */
    private boolean usingMfa;

    /**
     * 多因子key
     */
    private String mfaKey;

    /**
     * 授权角色
     */
    private Set<String> authorities;

}
