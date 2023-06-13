package com.runjian.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Miracle
 * @date 2023/4/13 9:55
 */
@Data
@AllArgsConstructor
public class RoleAuth implements GrantedAuthority {

    /**
     * 角色名称
     */
    private String authority;


}
