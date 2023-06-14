package com.runjian.rbac.config;

import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.dto.AuthUserDto;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/9 11:19
 */
@Data
@Configuration
public class AuthProperties {

    /**
     * 超管用户名
     */
    @Value("${auth.admin.username}")
    private String adminUsername;

    /**
     * 超管密码
     */
    @Value("${auth.admin.password}")
    private String adminPassword;

    /**
     * 超管角色名
     */
    @Value("${auth.admin.roleName}")
    private String adminRoleName;

    /**
     * 超管用户
     */
    private AuthUserDto adminUser;

    /**
     * 超管数据
     */
    private AuthDataDto adminData;

    /**
     * 密码
     */
    public static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostConstruct
    public void createAdminUser(){
        this.adminUser = new AuthUserDto();
        this.adminUser.setUsername(adminUsername);
        this.adminUser.setPassword(passwordEncoder.encode(adminPassword));
        this.adminUser.setUsingMfa(false);
        this.adminUser.setAccountNonLocked(true);
        this.adminUser.setAccountNonExpired(true);
        this.adminUser.setCredentialsNonExpired(true);
        this.adminUser.setEnabled(true);
        this.adminUser.setAuthorities(Set.of(adminRoleName));

        this.adminData = new AuthDataDto();
        this.adminData.setIsAdmin(true);
        this.adminData.setIsAuthorized(true);
        this.adminData.setUsername(adminUsername);
    }

}
