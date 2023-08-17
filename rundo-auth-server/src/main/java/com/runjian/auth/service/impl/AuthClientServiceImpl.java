package com.runjian.auth.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.auth.constant.AuthGrantType;
import com.runjian.auth.constant.ClientAuthMethod;
import com.runjian.auth.dao.OAuth2RegisteredClientDao;
import com.runjian.auth.entity.OAuth2RegisteredClientInfo;
import com.runjian.auth.service.AuthClientService;
import com.runjian.auth.utils.ClassToMapUtils;
import com.runjian.auth.vo.response.GetAuthClientPage;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户端服务
 * @author Miracle
 * @date 2023/8/15 9:49
 */
@Service
@RequiredArgsConstructor
public class AuthClientServiceImpl implements AuthClientService {

    private final OAuth2RegisteredClientDao oAuth2RegisteredClientDao;

    private final ClassToMapUtils classToMapUtils;

    private final PasswordEncoder passwordEncoder;

    private final RegisteredClientRepository repository;


    @Override
    public PageInfo<GetAuthClientPage> getClientPage(int page, int num, String clientId, String clientName) {
        PageHelper.startPage(page, num);
        List<OAuth2RegisteredClientInfo> oAuth2RegisteredClientInfoList = oAuth2RegisteredClientDao.findByClientIdLikeAndClientNameLike(clientId, clientName);
        if (oAuth2RegisteredClientInfoList.isEmpty()){
            return new PageInfo<>();
        }
        return new PageInfo<>(oAuth2RegisteredClientInfoList.stream().map(this::toObject).toList());
    }

    @Override
    public void addClient(String clientId, String clientSecret, String clientName, Set<Integer> clientAuthenticationMethods, Set<Integer> authorizationGrantTypes, Set<String> scopes, Set<String> redirectUris, Boolean requireAuthorizationConsent, Long accessTokenTimeToLiveSecond, Long refreshTokenTimeToLiveSecond, Long authCodeTimeToLiveSecond, LocalDateTime clientSecretExpiresAt) {
        RegisteredClient.Builder clientBuilder = RegisteredClient.withId(UUID.randomUUID().toString());
        if (Objects.nonNull(clientSecretExpiresAt)){
            clientBuilder.clientSecretExpiresAt(clientSecretExpiresAt.toInstant(ZoneOffset.of("+8")));
        }
        Set<AuthorizationGrantType> authGrantTypes = authorizationGrantTypes.stream().map(code -> AuthGrantType.getByCode(code).getAuthorizationGrantType()).collect(Collectors.toSet());
        Set<ClientAuthenticationMethod> clientAuthMethods = clientAuthenticationMethods.stream().map(code -> ClientAuthMethod.getByCode(code).getClientAuthenticationMethod()).collect(Collectors.toSet());
        RegisteredClient registeredClient = clientBuilder
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(clientSecret))
                .clientName(clientName)
                .clientIdIssuedAt(Instant.now())
                .authorizationGrantTypes(a -> a.addAll(authGrantTypes))
                .clientAuthenticationMethods(a -> a.addAll(clientAuthMethods))
                .scopes(a -> a.addAll(scopes))
                .redirectUris(Objects.nonNull(redirectUris) ? a -> a.addAll(redirectUris) : Set::clear)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(requireAuthorizationConsent)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofSeconds(Objects.nonNull(accessTokenTimeToLiveSecond) ? accessTokenTimeToLiveSecond : 3600L))
                        .authorizationCodeTimeToLive(Duration.ofSeconds(Objects.nonNull(refreshTokenTimeToLiveSecond) ? refreshTokenTimeToLiveSecond : 10800L))
                        .refreshTokenTimeToLive(Duration.ofSeconds(Objects.nonNull(authCodeTimeToLiveSecond) ? authCodeTimeToLiveSecond : 300L))
                        .reuseRefreshTokens(false)
                        .build()).build()
                ;
        repository.save(registeredClient);
    }

    @Override
    public void updateClient(String id, String clientSecret, String clientName, Set<Integer> clientAuthenticationMethods, Set<Integer> authorizationGrantTypes, Set<String> scopes, Set<String> redirectUris, Boolean requireAuthorizationConsent, Long accessTokenTimeToLiveSecond, Long refreshTokenTimeToLiveSecond, Long authCodeTimeToLiveSecond, LocalDateTime clientSecretExpiresAt) {
        RegisteredClient oldRegisteredClient = repository.findById(id);
        if (Objects.isNull(oldRegisteredClient)){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND);
        }

        RegisteredClient.Builder clientBuilder = RegisteredClient.from(oldRegisteredClient);
        if (Objects.nonNull(clientSecret)){
            clientBuilder.clientSecret(passwordEncoder.encode(clientSecret));
        }
        if (Objects.nonNull(clientSecretExpiresAt)){
            clientBuilder.clientSecretExpiresAt(clientSecretExpiresAt.toInstant(ZoneOffset.of("+8")));
        }

        RegisteredClient registeredClient = clientBuilder
                .clientName(clientName)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(requireAuthorizationConsent)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofSeconds(Objects.nonNull(accessTokenTimeToLiveSecond) ? accessTokenTimeToLiveSecond : 3600L))
                        .authorizationCodeTimeToLive(Duration.ofSeconds(Objects.nonNull(refreshTokenTimeToLiveSecond) ? refreshTokenTimeToLiveSecond : 10800L))
                        .refreshTokenTimeToLive(Duration.ofSeconds(Objects.nonNull(authCodeTimeToLiveSecond) ? authCodeTimeToLiveSecond : 300L))
                        .reuseRefreshTokens(false)
                        .build()).build()
                ;
        registeredClient.getRedirectUris().clear();
        registeredClient.getRedirectUris().addAll(redirectUris);
        registeredClient.getClientAuthenticationMethods().clear();
        registeredClient.getClientAuthenticationMethods().addAll(clientAuthenticationMethods.stream().map(code -> ClientAuthMethod.getByCode(code).getClientAuthenticationMethod()).collect(Collectors.toSet()));
        registeredClient.getScopes().clear();
        registeredClient.getScopes().addAll(scopes);
        registeredClient.getAuthorizationGrantTypes().clear();
        registeredClient.getAuthorizationGrantTypes().addAll(authorizationGrantTypes.stream().map(code -> AuthGrantType.getByCode(code).getAuthorizationGrantType()).collect(Collectors.toSet()));
        repository.save(registeredClient);
    }

    @Override
    public void deleteClient(String id) {
        Optional<OAuth2RegisteredClientInfo> oAuth2RegisteredClientInfoOp = oAuth2RegisteredClientDao.findById(id);
        if (oAuth2RegisteredClientInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND);
        }
        oAuth2RegisteredClientDao.deleteById(id);
    }

    public GetAuthClientPage toObject(OAuth2RegisteredClientInfo client) {
        Set<Integer> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(
                client.getClientAuthenticationMethods()).stream().map(clientAuthMethod -> ClientAuthMethod.getByMsg(clientAuthMethod).getCode()).collect(Collectors.toSet());
        Set<Integer> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(
                client.getAuthorizationGrantTypes()).stream().map(authGrantType -> AuthGrantType.getByMsg(authGrantType).getCode()).collect(Collectors.toSet());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(
                client.getRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(
                client.getScopes());
        Map<String, Object> clientSettingsMap = classToMapUtils.parseMap(client.getClientSettings());
        Map<String, Object> tokenSettingsMap = classToMapUtils.parseMap(client.getTokenSettings());
        GetAuthClientPage.GetAuthClientPageBuilder getAuthClientPageBuilder = GetAuthClientPage.builder()
                .id(client.getId())
                .clientId(client.getClientId())
                .clientIdIssuedAt(LocalDateTime.ofInstant(client.getClientIdIssuedAt(), ZoneOffset.systemDefault()))
                .clientSecretExpiresAt(Objects.nonNull(client.getClientSecretExpiresAt()) ? LocalDateTime.ofInstant(client.getClientSecretExpiresAt(), ZoneOffset.systemDefault()) : null)
                .clientName(client.getClientName())
                .authorizationGrantTypes(authorizationGrantTypes)
                .redirectUris(redirectUris)
                .scopes(clientScopes)
                .clientAuthenticationMethods(clientAuthenticationMethods)
                .requireAuthConsent((Boolean) clientSettingsMap.get("settings.client.require-authorization-consent"))
                .accessTokenTimeToLiveSecond(((Duration) tokenSettingsMap.get("settings.token.access-token-time-to-live")).getSeconds());


        Object refreshTokenTimeToLive = tokenSettingsMap.get("settings.token.refresh-token-time-to-live");
        if (Objects.nonNull(refreshTokenTimeToLive)){
            getAuthClientPageBuilder.refreshTokenTimeToLiveSecond(((Duration)refreshTokenTimeToLive).getSeconds());
        }
        Object authCodeTimeToLive = tokenSettingsMap.get("settings.token.authorization-code-time-to-live");
        if (Objects.nonNull(authCodeTimeToLive)){
            getAuthClientPageBuilder.authCodeTimeToLiveSecond(((Duration)authCodeTimeToLive).getSeconds());
        }
        return getAuthClientPageBuilder.build();
    }
}
