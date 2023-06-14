package com.runjian.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.runjian.auth.utils.JwksUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * @author Miracle
 * @date 2023/4/19 15:09
 */
@Configuration
public class JoseConfig {

    /**
     * JWT解码配置
     * @param jwkSource
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * JWK配置
     * @return
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        JWKSet jwkSet = new JWKSet(JwksUtils.getRsaKey());
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    /**
     * JWT配置
     */
    public static class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
        @Override
        public void customize(JwtEncodingContext context) {
            // 只在调用/oauth2/introspect时生效
            context.getJwsHeader().header("client-id", context.getRegisteredClient().getClientId());
        }
    }
}
