package com.runjian.auth.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Miracle
 * @date 2023/4/13 9:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAuth implements GrantedAuthority {

    /**
     * 角色id
     */
    private Long id;

    /**
     * 角色名称
     */
    private String authority;


}
