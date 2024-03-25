package com.runjian.auth.config.storage;

import com.runjian.auth.dao.OAuth2AuthorizationConsentDao;
import com.runjian.auth.entity.OAuth2AuthorizationConsentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/4/26 10:34
 */
@RequiredArgsConstructor
public class IbatisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final OAuth2AuthorizationConsentDao authorizationConsentDao;

    private final RegisteredClientRepository registeredClientRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        Optional<OAuth2AuthorizationConsentInfo> authorizationConsentInfo = this.authorizationConsentDao.findByRegisteredClientIdAndPrincipalName(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        if (authorizationConsentInfo.isEmpty()){
            this.authorizationConsentDao.save(toEntity(authorizationConsent));
        }else {
            this.authorizationConsentDao.update(toEntity(authorizationConsent));
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        this.authorizationConsentDao.deleteByRegisteredClientIdAndPrincipalName(
                authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return this.authorizationConsentDao.findByRegisteredClientIdAndPrincipalName(
                registeredClientId, principalName).map(this::toObject).orElse(null);
    }

    private OAuth2AuthorizationConsent toObject(OAuth2AuthorizationConsentInfo authorizationConsent) {
        String registeredClientId = authorizationConsent.getRegisteredClientId();
        RegisteredClient registeredClient = this.registeredClientRepository.findById(registeredClientId);
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + registeredClientId + "' was not found in the RegisteredClientRepository.");
        }

        OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(
                registeredClientId, authorizationConsent.getPrincipalName());
        if (authorizationConsent.getAuthorities() != null) {
            for (String authority : StringUtils.commaDelimitedListToSet(authorizationConsent.getAuthorities())) {
                builder.authority(new SimpleGrantedAuthority(authority));
            }
        }
        return builder.build();
    }

    private OAuth2AuthorizationConsentInfo toEntity(OAuth2AuthorizationConsent authorizationConsent) {
        OAuth2AuthorizationConsentInfo entity = new OAuth2AuthorizationConsentInfo();
        entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
        entity.setPrincipalName(authorizationConsent.getPrincipalName());

        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority authority : authorizationConsent.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));

        return entity;
    }
}
