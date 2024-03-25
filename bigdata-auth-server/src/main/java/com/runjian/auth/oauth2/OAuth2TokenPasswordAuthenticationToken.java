package com.runjian.auth.oauth2;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/4/21 10:57
 */
public class OAuth2TokenPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private final String username;

    private final String password;

    private final Set<String> scopes;

    /**
     * Sub-class constructor.
     * @param clientPrincipal        the authenticated client principal
     * @param additionalParameters   the additional parameters
     */
    protected OAuth2TokenPasswordAuthenticationToken(String username, String password, @Nullable Set<String> scopes, Authentication clientPrincipal, @Nullable Map<String, Object> additionalParameters) {
        super(AuthorizationGrantType.PASSWORD, clientPrincipal, additionalParameters);
        Assert.hasText(username, "username cannot be empty");
        Assert.hasText(password, "password cannot be empty");
        this.username = username;
        this.password = password;
        this.scopes = Collections.unmodifiableSet(
                scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getScopes() {
        return scopes;
    }
}
