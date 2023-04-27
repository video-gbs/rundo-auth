package com.runjian.auth.domain.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author Miracle
 * @date 2023/4/26 11:05
 */
@Data
public class OAuth2RegisteredClientInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String clientId;

    private String clientSecret;

    private Instant clientIdIssuedAt;

    private Instant clientSecretExpiresAt;

    private String clientName;

    private String clientAuthenticationMethods;

    private String authorizationGrantTypes;

    private String redirectUris;

    private String scopes;

    private String clientSettings;

    private String tokenSettings;
}
