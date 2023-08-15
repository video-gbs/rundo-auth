package com.runjian.auth.config.storage;

import com.runjian.auth.constant.ClientAuthMethod;
import com.runjian.auth.dao.OAuth2RegisteredClientDao;
import com.runjian.auth.entity.OAuth2RegisteredClientInfo;
import com.runjian.auth.utils.ClassToMapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.*;

/**
 * @author Miracle
 * @date 2023/4/26 10:28
 */
@RequiredArgsConstructor
public class IbatisRegisteredClientRepository implements RegisteredClientRepository {

    private final OAuth2RegisteredClientDao registeredClientDao;

    private final ClassToMapUtils classToMapUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RegisteredClient registeredClient) {
        Optional<OAuth2RegisteredClientInfo> oAuth2RegisteredClientInfo = registeredClientDao.findById(registeredClient.getId());
        if (oAuth2RegisteredClientInfo.isEmpty()){
            Integer countClientId = registeredClientDao.countClientId(registeredClient.getClientId());
            if (countClientId > 0){
                throw new IllegalArgumentException("Registered client must be unique. " +
                        "Found duplicate client identifier: " + registeredClient.getClientId());
            }
            registeredClientDao.save(toEntity(registeredClient));
        }else {
            registeredClientDao.update(toEntity(registeredClient));
        }

    }

    @Override
    public RegisteredClient findById(String id) {
        return registeredClientDao.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return registeredClientDao.findByClientId(clientId).map(this::toObject).orElse(null);
    }

    private RegisteredClient toObject(OAuth2RegisteredClientInfo client) {
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(
                client.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(
                client.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(
                client.getRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(
                client.getScopes());

        RegisteredClient.Builder builder = RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientName(client.getClientName())
                .clientAuthenticationMethods(authenticationMethods -> clientAuthenticationMethods
                        .forEach(authenticationMethod -> authenticationMethods
                                .add(ClientAuthMethod.getByMsg(authenticationMethod).getClientAuthenticationMethod())))
                .authorizationGrantTypes((grantTypes) -> authorizationGrantTypes
                        .forEach(grantType -> grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes));

        Map<String, Object> clientSettingsMap = classToMapUtils.parseMap(client.getClientSettings());
        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

        Map<String, Object> tokenSettingsMap = classToMapUtils.parseMap(client.getTokenSettings());
        builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());

        return builder.build();
    }

    private OAuth2RegisteredClientInfo toEntity(RegisteredClient registeredClient) {
        List<String> clientAuthenticationMethods = new ArrayList<>(
                registeredClient.getClientAuthenticationMethods().size());
        registeredClient.getClientAuthenticationMethods().forEach(
                clientAuthenticationMethod -> clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));

        List<String> authorizationGrantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
        registeredClient.getAuthorizationGrantTypes()
                .forEach(authorizationGrantType -> authorizationGrantTypes.add(authorizationGrantType.getValue()));

        OAuth2RegisteredClientInfo entity = new OAuth2RegisteredClientInfo();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(
                StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(classToMapUtils.writeMap(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(classToMapUtils.writeMap(registeredClient.getTokenSettings().getSettings()));

        return entity;
    }


    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }

}
