package com.runjian.auth.dao;

import com.runjian.auth.entity.OAuth2AuthorizationInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/4/26 10:30
 */
@Mapper
@Repository
public interface OAuth2AuthorizationDao {

    String OAUTH2_AUTHORIZATION_TABLE_NAME = "oauth2_authorization";

    @Insert(" INSERT INTO " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " (id, registered_client_id, principal_name, authorization_grant_type, authorized_scopes, attributes, state," +
            " authorization_code_value, authorization_code_issued_at, authorization_code_expires_at, authorization_code_metadata, " +
            " access_token_value, access_token_issued_at, access_token_expires_at, access_token_metadata, access_token_type, access_token_scopes, " +
            " oidc_id_token_value, oidc_id_token_issued_at, oidc_id_token_expires_at, oidc_id_token_metadata," +
            " refresh_token_value, refresh_token_issued_at, refresh_token_expires_at, refresh_token_metadata) " +
            " VALUES " +
            " (#{id}, #{registeredClientId}, #{principalName}, #{authorizationGrantType}, #{authorizedScopes}, #{attributes}, #{state}, " +
            " #{authorizationCodeValue}, #{authorizationCodeIssuedAt}, #{authorizationCodeExpiresAt}, #{authorizationCodeMetadata}, " +
            " #{accessTokenValue}, #{accessTokenIssuedAt}, #{accessTokenExpiresAt}, #{accessTokenMetadata}, #{accessTokenType}, #{accessTokenScopes}, " +
            " #{oidcIdTokenValue}, #{oidcIdTokenIssuedAt}, #{oidcIdTokenExpiresAt}, #{oidcIdTokenMetadata}, " +
            " #{refreshTokenValue}, #{refreshTokenIssuedAt}, #{refreshTokenExpiresAt}, #{refreshTokenMetadata}) ")
    void save(OAuth2AuthorizationInfo entity);

    @Update(" UPDATE " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " SET registered_client_id = #{registeredClientId}  " +
            " , principal_name = #{principalName} " +
            " , authorization_grant_type = #{authorizationGrantType} " +
            " , authorized_scopes = #{authorizedScopes} " +
            " , attributes = #{attributes} " +
            " , state = #{state} " +
            " , authorization_code_value = #{authorizationCodeValue} " +
            " , authorization_code_issued_at = #{authorizationCodeIssuedAt} " +
            " , authorization_code_expires_at = #{authorizationCodeExpiresAt} " +
            " , authorization_code_metadata = #{authorizationCodeMetadata} " +
            " , access_token_value = #{accessTokenValue} " +
            " , access_token_issued_at = #{accessTokenIssuedAt} " +
            " , access_token_expires_at = #{accessTokenExpiresAt} " +
            " , access_token_metadata = #{accessTokenMetadata} " +
            " , access_token_type = #{accessTokenType} " +
            " , access_token_scopes = #{accessTokenScopes} " +
            " , oidc_id_token_value = #{oidcIdTokenValue} " +
            " , oidc_id_token_issued_at = #{oidcIdTokenIssuedAt} " +
            " , oidc_id_token_expires_at = #{oidcIdTokenExpiresAt} " +
            " , oidc_id_token_metadata = #{oidcIdTokenMetadata} " +
            " , refresh_token_value = #{refreshTokenValue} " +
            " , refresh_token_issued_at = #{refreshTokenIssuedAt} " +
            " , refresh_token_expires_at = #{refreshTokenExpiresAt} " +
            " , refresh_token_metadata = #{refreshTokenMetadata} " +
            " where id = #{id} ")
    void update(OAuth2AuthorizationInfo entity);

    @Delete(" DELETE FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " WHERE id = #{id} ")
    void deleteById(String id);

    @Delete(" DELETE FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " WHERE principal_name = #{principalName}")
    void deleteByPrincipalName(String principalName);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " WHERE id = #{id} for update" +
            " </script>")
    Optional<OAuth2AuthorizationInfo> findById(String id);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " WHERE state = #{token} OR " +
            " authorization_code_value = #{token} OR " +
            " access_token_value = #{token} OR " +
            " refresh_token_value = #{token} " +
            " </script>")
    Optional<OAuth2AuthorizationInfo> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(String token);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " WHERE state = #{token} " +
            " </script>")
    Optional<OAuth2AuthorizationInfo> findByState(String token);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " WHERE authorization_code_value = #{token} " +
            " </script>")
    Optional<OAuth2AuthorizationInfo> findByAuthorizationCodeValue(String token);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " WHERE access_token_value = #{token} " +
            " </script>")
    Optional<OAuth2AuthorizationInfo> findByAccessTokenValue(String token);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME +
            " WHERE refresh_token_value = #{token} " +
            " </script>")
    Optional<OAuth2AuthorizationInfo> findByRefreshTokenValue(String token);


    @Delete(" DELETE FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME)
    void deleteAll();

    @Delete(" <script>" +
            " DELETE FROM " + OAUTH2_AUTHORIZATION_TABLE_NAME
            + " WHERE (refresh_token_expires_at IS NULL AND access_token_expires_at &lt;= #{nowTime}) OR refresh_token_expires_at &lt;= #{nowTime} "+
            " </script>")
    void deleteOutTimeToken(LocalDateTime nowTime);
}
