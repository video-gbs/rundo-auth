package com.runjian.auth.dao;

import com.runjian.auth.domain.entity.OAuth2AuthorizationConsentInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/4/26 10:30
 */

@Mapper
@Repository
public interface OAuth2AuthorizationConsentDao {

    String OAUTH2_AUTHORIZATION_CONSENT_TABLE_NAME = "oauth2_authorization_consent";

    @Insert(" INSERT INTO " + OAUTH2_AUTHORIZATION_CONSENT_TABLE_NAME +
            " (registered_client_id, principal_name, authorities) " +
            " VALUES " +
            " (#{registeredClientId}, #{principalName}, #{authorities})")
    void save(OAuth2AuthorizationConsentInfo entity);

    @Update(" UPDATE " + OAUTH2_AUTHORIZATION_CONSENT_TABLE_NAME +
            " SET authorities = #{authorities}  " +
            " where registered_client_id = #{registeredClientId} AND " +
            " principal_name = #{principalName} ")
    void update(OAuth2AuthorizationConsentInfo entity);

    @Delete(" DELETE FROM " + OAUTH2_AUTHORIZATION_CONSENT_TABLE_NAME +
            " WHERE registered_client_id = #{registeredClientId} AND " +
            " principal_name = #{principalName} ")
    void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_AUTHORIZATION_CONSENT_TABLE_NAME +
            " WHERE registered_client_id = #{registeredClientId} AND principal_name = #{principalName} " +
            " </script>")
    Optional<OAuth2AuthorizationConsentInfo> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);


}
