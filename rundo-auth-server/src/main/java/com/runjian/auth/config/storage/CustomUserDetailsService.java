package com.runjian.auth.config.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Miracle
 * @date 2023/4/19 15:13
 */
@Configuration
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final PasswordEncoder passwordEncoder;

    /**
     * 用户端配置
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails userDetails = User.builder()
                .username("user")
                .password(passwordEncoder.encode("12345678"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
}
