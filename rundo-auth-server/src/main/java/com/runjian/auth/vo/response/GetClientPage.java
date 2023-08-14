package com.runjian.auth.vo.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/8/14 9:56
 */
@Data
public class GetClientPage {

    private String id;

    private String clientId;

    private LocalDateTime clientIdIssuedAt;

    private LocalDateTime clientSecretExpiresAt;

    private String clientName;

    private Set<String> authorizationGrantTypes;

    private String redirectUris;

    private Set<String> scopes;

    private Boolean requireAuthorizationConsent;

    private Long accessTokenTimeToLiveSecond;

    private Long refreshTokenTimeToLiveSecond;
}
