package com.runjian.auth.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runjian.auth.config.storage.IbatisOAuth2AuthorizationConsentService;
import com.runjian.auth.config.storage.IbatisOAuth2AuthorizationService;
import com.runjian.auth.config.storage.IbatisRegisteredClientRepository;
import com.runjian.auth.dao.OAuth2AuthorizationConsentDao;
import com.runjian.auth.dao.OAuth2AuthorizationDao;
import com.runjian.auth.dao.OAuth2RegisteredClientDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author Miracle
 * @date 2023/4/26 15:22
 */
@Configuration
public class AuthorizationStorageConfig {

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    public AuthorizationStorageConfig(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = new ObjectMapper();
        ClassLoader classLoader = JdbcRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    /**
     * 客户端配置
     * @param oAuth2RegisteredClientDao
     * @return
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(OAuth2RegisteredClientDao oAuth2RegisteredClientDao) {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("rundo-gbs-view")
                .clientSecret(passwordEncoder.encode("rundo888"))
                .clientIdIssuedAt(Instant.now())
                // 可以基于 basic 的方式和授权服务器进行认证
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                //.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                //.redirectUri("http://127.0.0.1:9000/auth/oauth2")
                .scope("all")
                //.scope(OidcScopes.PROFILE)
                //.scope(OidcScopes.OPENID)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        //.authorizationCodeTimeToLive(Duration.ofMinutes(10))
                        .refreshTokenTimeToLive(Duration.ofHours(3))
                        .reuseRefreshTokens(false)
                        .build())
                .build();
        IbatisRegisteredClientRepository ibatisRegisteredClientRepository = new IbatisRegisteredClientRepository(oAuth2RegisteredClientDao, objectMapper);
        if (null == ibatisRegisteredClientRepository.findByClientId("rundo-gbs-view")) {
            ibatisRegisteredClientRepository.save(registeredClient);
        }
        return ibatisRegisteredClientRepository;
    }

    /**
     * 授权信息配置
     * @param oAuth2AuthorizationDao
     * @param registeredClientRepository
     * @return
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(OAuth2AuthorizationDao oAuth2AuthorizationDao, RegisteredClientRepository registeredClientRepository) {
        return new IbatisOAuth2AuthorizationService(oAuth2AuthorizationDao, registeredClientRepository, objectMapper);
    }


    /**
     * 授权范围信息配置
     * @param oAuth2AuthorizationConsentDao
     * @param registeredClientRepository
     * @return
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(OAuth2AuthorizationConsentDao oAuth2AuthorizationConsentDao, RegisteredClientRepository registeredClientRepository) {
        return new IbatisOAuth2AuthorizationConsentService(oAuth2AuthorizationConsentDao, registeredClientRepository);
    }


}
