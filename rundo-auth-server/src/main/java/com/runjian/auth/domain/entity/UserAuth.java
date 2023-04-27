package com.runjian.auth.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/4/13 9:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth implements UserDetails {

    /**
     * id
     */
    private Long id;

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
    private Set<RoleAuth> authorities;

}
