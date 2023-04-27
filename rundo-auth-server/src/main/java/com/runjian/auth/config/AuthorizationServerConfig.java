package com.runjian.auth.config;

import com.runjian.auth.oauth2.OAuth2PasswordTokenAuthenticationProvider;
import com.runjian.auth.oauth2.OAuth2TokenPasswordAuthenticationConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 授权服务器
 * @author Miracle
 * @date 2023/4/17 15:45
 */
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final OAuth2AuthorizationService authorizationService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain oauthSecurityFilterChain(HttpSecurity http) throws Exception{
        // 设置jwt token个性化
        http.setSharedObject(OAuth2TokenCustomizer.class, new JoseConfig.CustomOAuth2TokenCustomizer());
        // 授权服务器配置
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();


        authorizationServerConfigurer.tokenEndpoint(token ->
                token.accessTokenRequestConverter(new OAuth2TokenPasswordAuthenticationConvert())
                        .authenticationProvider(new OAuth2PasswordTokenAuthenticationProvider(authorizationService, http, userDetailsService, passwordEncoder))
        );
        http.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .csrf((csrf) -> {csrf.ignoringRequestMatchers(new RequestMatcher[]{endpointsMatcher});})
                .apply(authorizationServerConfigurer)
                ;
        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder()
                .issuer("http://127.0.0.1:9000")
                .build();
    }

}
