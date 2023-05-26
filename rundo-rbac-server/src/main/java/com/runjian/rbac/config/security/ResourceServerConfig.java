package com.runjian.rbac.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests(req -> req.requestMatchers("/rbac/login").permitAll().anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        ;
        return http.build();
    }
}
