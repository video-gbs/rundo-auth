package com.runjian.auth.dao;

import com.runjian.auth.entity.OAuth2RegisteredClientInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/4/26 10:30
 */
@Mapper
@Repository
public interface OAuth2RegisteredClientDao {

    String OAUTH2_REGISTERED_CLIENT_TABLE_NAME = "oauth2_registered_client";

    @Insert(" INSERT INTO " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " (id, client_id, client_secret, client_id_issued_at, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, scopes, client_settings, token_settings) " +
            " VALUES " +
            " (#{id}, #{clientId}, #{clientSecret}, #{clientIdIssuedAt}, #{clientSecretExpiresAt}, #{clientName}, #{clientAuthenticationMethods}, #{authorizationGrantTypes}, #{redirectUris}, #{scopes}, #{clientSettings}, #{tokenSettings})")
    void save(OAuth2RegisteredClientInfo entity);

    @Select(" SELECT COUNT(*) FROM " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " WHERE client_id = #{clientId} ")
    Integer countClientId(String clientId);

    @Update(" <script>" +
            " UPDATE " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " SET client_name = #{clientName} " +
            " , client_authentication_methods = #{clientAuthenticationMethods} " +
            " , authorization_grant_types = #{authorizationGrantTypes} " +
            " , redirect_uris = #{redirectUris} " +
            " , scopes = #{scopes} " +
            " , client_settings = #{clientSettings} " +
            " , token_settings = #{tokenSettings} " +
            " , client_secret_expires_at = #{clientSecretExpiresAt} " +
            " <if test=\"clientSecret != null\" > , client_secret = #{clientSecret} </if> " +
            " WHERE id = #{id} " +
            " </script>")
    void update(OAuth2RegisteredClientInfo entity);

    @Select(" SELECT * FROM " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<OAuth2RegisteredClientInfo> findById(String id);

    @Select(" SELECT * FROM " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " WHERE client_id = #{clientId}")
    Optional<OAuth2RegisteredClientInfo> findByClientId(String clientId);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " WHERE 1=1 " +
            " <if test=\"clientId != null\" > AND client_id LIKE CONCAT('%', #{clientId}, '%') </if> " +
            " <if test=\"clientName != null\" > AND client_name LIKE CONCAT('%', #{clientName}, '%') </if> " +
            " </script>")
    List<OAuth2RegisteredClientInfo> findByClientIdLikeAndClientNameLike(String clientId, String clientName);

    @Delete(" DELETE FROM " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " WHERE id = #{id}")
    void deleteById(String id);
}
