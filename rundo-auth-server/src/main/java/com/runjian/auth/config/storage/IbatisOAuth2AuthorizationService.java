package com.runjian.auth.config.storage;

import com.runjian.auth.constant.AuthGrantType;
import com.runjian.auth.dao.OAuth2AuthorizationDao;
import com.runjian.auth.entity.OAuth2AuthorizationInfo;
import com.runjian.auth.utils.ClassToMapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Miracle
 * @date 2023/4/26 10:34
 */
@Slf4j
@RequiredArgsConstructor
public class IbatisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final OAuth2AuthorizationDao auth2AuthorizationDao;

    private final RegisteredClientRepository registeredClientRepository;

    private final ClassToMapUtils classToMapUtils;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        Optional<OAuth2AuthorizationInfo> oAuth2AuthorizationInfo = this.auth2AuthorizationDao.findById(authorization.getId());
        if (oAuth2AuthorizationInfo.isEmpty()){
            this.auth2AuthorizationDao.save(toEntity(authorization));
        }else {
            this.auth2AuthorizationDao.update(toEntity(authorization));
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        this.auth2AuthorizationDao.deleteById(authorization.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.auth2AuthorizationDao.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        Optional<OAuth2AuthorizationInfo> result;
        if (tokenType == null) {
            result = this.auth2AuthorizationDao.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            result = this.auth2AuthorizationDao.findByState(token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            result = this.auth2AuthorizationDao.findByAuthorizationCodeValue(token);
        } else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
            result = this.auth2AuthorizationDao.findByAccessTokenValue(token);
        } else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
            result = this.auth2AuthorizationDao.findByRefreshTokenValue(token);
        } else {
            result = Optional.empty();
        }

        return result.map(this::toObject).orElse(null);
    }

    private OAuth2Authorization toObject(OAuth2AuthorizationInfo entity) {
        RegisteredClient registeredClient = this.registeredClientRepository.findById(entity.getRegisteredClientId());
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + entity.getRegisteredClientId() + "' was not found in the RegisteredClientRepository.");
        }

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(entity.getId())
                .principalName(entity.getPrincipalName())
                .authorizationGrantType(AuthGrantType.getByMsg(entity.getAuthorizationGrantType()).getAuthorizationGrantType())
                .attributes(attributes -> attributes.putAll(classToMapUtils.parseMap(entity.getAttributes())))
                .authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()));
        if (entity.getState() != null) {
            builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
        }

        if (entity.getAuthorizationCodeValue() != null) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
                    entity.getAuthorizationCodeValue(),
                    entity.getAuthorizationCodeIssuedAt(),
                    entity.getAuthorizationCodeExpiresAt());
            builder.token(authorizationCode, metadata -> metadata.putAll(classToMapUtils.parseMap(entity.getAuthorizationCodeMetadata())));
        }

        if (entity.getAccessTokenValue() != null) {
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    entity.getAccessTokenValue(),
                    entity.getAccessTokenIssuedAt(),
                    entity.getAccessTokenExpiresAt(),
                    StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
            builder.token(accessToken, metadata -> metadata.putAll(classToMapUtils.parseMap(entity.getAccessTokenMetadata())));
        }

        if (entity.getRefreshTokenValue() != null) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    entity.getRefreshTokenValue(),
                    entity.getRefreshTokenIssuedAt(),
                    entity.getRefreshTokenExpiresAt());
            builder.token(refreshToken, metadata -> metadata.putAll(classToMapUtils.parseMap(entity.getRefreshTokenMetadata())));
        }

        if (entity.getOidcIdTokenValue() != null) {
            Map<String, Object> oidcTokenMetadata = classToMapUtils.parseMap(entity.getOidcIdTokenMetadata());
            OidcIdToken idToken = new OidcIdToken(
                    entity.getOidcIdTokenValue(),
                    entity.getOidcIdTokenIssuedAt(),
                    entity.getOidcIdTokenExpiresAt(),
                    (Map<String, Object>) oidcTokenMetadata.get(OAuth2Authorization.Token.CLAIMS_METADATA_NAME));
            builder.token(idToken, metadata -> metadata.putAll(oidcTokenMetadata));
        }

        return builder.build();
    }

    private OAuth2AuthorizationInfo toEntity(OAuth2Authorization authorization) {
        OAuth2AuthorizationInfo entity = new OAuth2AuthorizationInfo();
        entity.setId(authorization.getId());
        entity.setRegisteredClientId(authorization.getRegisteredClientId());
        entity.setPrincipalName(authorization.getPrincipalName());
        entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
        entity.setAttributes(classToMapUtils.writeMap(authorization.getAttributes()));
        entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));
        entity.setAuthorizedScopes(StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), ","));


        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        setTokenValues(
                authorizationCode,
                entity::setAuthorizationCodeValue,
                entity::setAuthorizationCodeIssuedAt,
                entity::setAuthorizationCodeExpiresAt,
                entity::setAuthorizationCodeMetadata
        );

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                authorization.getToken(OAuth2AccessToken.class);

        setTokenValues(
                accessToken,
                entity::setAccessTokenValue,
                entity::setAccessTokenIssuedAt,
                entity::setAccessTokenExpiresAt,
                entity::setAccessTokenMetadata
        );
        if (accessToken != null) {
            entity.setAccessTokenType(accessToken.getToken().getTokenType().getValue());
            if (!CollectionUtils.isEmpty(accessToken.getToken().getScopes())) {
                entity.setAccessTokenScopes(StringUtils.collectionToDelimitedString(accessToken.getToken().getScopes(), ","));
            }
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        setTokenValues(
                refreshToken,
                entity::setRefreshTokenValue,
                entity::setRefreshTokenIssuedAt,
                entity::setRefreshTokenExpiresAt,
                entity::setRefreshTokenMetadata
        );

        OAuth2Authorization.Token<OidcIdToken> oidcIdToken =
                authorization.getToken(OidcIdToken.class);
        setTokenValues(
                oidcIdToken,
                entity::setOidcIdTokenValue,
                entity::setOidcIdTokenIssuedAt,
                entity::setOidcIdTokenExpiresAt,
                entity::setOidcIdTokenMetadata
        );

        return entity;
    }

    private void setTokenValues(
            OAuth2Authorization.Token<?> token,
            Consumer<String> tokenValueConsumer,
            Consumer<Instant> issuedAtConsumer,
            Consumer<Instant> expiresAtConsumer,
            Consumer<String> metadataConsumer) {
        if (token != null) {
            OAuth2Token oAuth2Token = token.getToken();
            tokenValueConsumer.accept(oAuth2Token.getTokenValue());
            issuedAtConsumer.accept(oAuth2Token.getIssuedAt());
            expiresAtConsumer.accept(oAuth2Token.getExpiresAt());
            metadataConsumer.accept(classToMapUtils.writeMap(token.getMetadata()));
        }
    }

}
