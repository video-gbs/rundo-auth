package com.runjian.auth.dao;

import com.runjian.auth.entity.OAuth2RegisteredClientInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

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

    @Update(" UPDATE " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " SET client_name = #{clientName} " +
            " , client_authentication_methods = #{clientAuthenticationMethods} " +
            " , authorization_grant_types = #{authorizationGrantTypes} " +
            " , redirect_uris = #{redirectUris} " +
            " , scopes = #{scopes} " +
            " , client_settings = #{clientSettings} " +
            " , token_settings = #{tokenSettings} " +
            " WHERE id = #{id} ")
    void update(OAuth2RegisteredClientInfo entity);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " WHERE id = #{id} " +
            " </script>")
    Optional<OAuth2RegisteredClientInfo> findById(String id);

    @Select(" <script>" +
            " SELECT * FROM " + OAUTH2_REGISTERED_CLIENT_TABLE_NAME +
            " WHERE client_id = #{clientId} " +
            " </script>")
    Optional<OAuth2RegisteredClientInfo> findByClientId(String clientId);


}
