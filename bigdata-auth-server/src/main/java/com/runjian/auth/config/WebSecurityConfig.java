package com.runjian.auth.config;

import com.runjian.auth.handler.AuthFailureHandlerImpl;
import com.runjian.auth.handler.AuthSuccessHandlerImpl;
import com.runjian.auth.handler.LogoutSuccessHandlerImpl;
import com.runjian.auth.oauth2.OAuth2PasswordTokenAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Miracle
 * @date 2023/4/13 16:41
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthFailureHandlerImpl authenticationFailureHandler;

    private final AuthSuccessHandlerImpl authenticationSuccessHandler;

    private final LogoutSuccessHandlerImpl logoutSuccessHandler;


    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests(req -> req.requestMatchers("/auth-server/logout", "/auth-server/sign-out").permitAll().anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/auth/login")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler))
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(logoutSuccessHandler))
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                ;
        ;
        return http.build();
    }

    /**
     * 自定义jwt解析
     * @return
     */
    private Converter<Jwt, AbstractAuthenticationToken> customJwtAuthenticationTokenConverter() {
        return jwt -> {
            List<String> userAuthorities = jwt.getClaimAsStringList("authorities");
            List<String> scopes = jwt.getClaimAsStringList("scope");
            List<GrantedAuthority> combinedAuthorities = Stream.concat(
                            userAuthorities.stream(),
                            scopes.stream().map(scope -> "SCOPE_" + scope))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            String username = jwt.getClaimAsString("user_name");
            return new UsernamePasswordAuthenticationToken(username, null, combinedAuthorities);
        };
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


//    @Autowired
//    public void configure(AuthenticationManagerBuilder auth){
//        auth.authenticationProvider(new OAuth2PasswordTokenAuthenticationProvider(authorizationService, httpSecurity, userDetailsService, passwordEncoder));
//    }

}
