package com.runjian.auth.domain.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author Miracle
 * @date 2023/4/26 10:04
 */

@Data
public class OAuth2AuthorizationInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String registeredClientId;

    private String principalName;

    private String authorizationGrantType;

    private String authorizedScopes;

    private String attributes;

    private String state;

    private String authorizationCodeValue;

    private Instant authorizationCodeIssuedAt;

    private Instant authorizationCodeExpiresAt;

    private String authorizationCodeMetadata;

    private String accessTokenValue;

    private Instant accessTokenIssuedAt;

    private Instant accessTokenExpiresAt;

    private String accessTokenMetadata;

    private String accessTokenType;

    private String accessTokenScopes;

    private String refreshTokenValue;

    private Instant refreshTokenIssuedAt;

    private Instant refreshTokenExpiresAt;

    private String refreshTokenMetadata;

    private String oidcIdTokenValue;

    private Instant oidcIdTokenIssuedAt;

    private Instant oidcIdTokenExpiresAt;

    private String oidcIdTokenMetadata;



}
