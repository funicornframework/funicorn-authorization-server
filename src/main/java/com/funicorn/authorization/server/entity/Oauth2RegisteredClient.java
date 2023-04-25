package com.funicorn.authorization.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.funicorn.framework.common.datasource.model.BaseEntity;

import java.sql.Timestamp;

/**
 * <p>
 * 
 * </p>
 *
 * @author Aimee
 * @since 2023-04-13
 */
@TableName("oauth2_registered_client")
public class Oauth2RegisteredClient extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 第三方客户端标识
     */
    private String clientId;

    /**
     * 创建时间
     */
    private Timestamp clientIdIssuedAt;

    /**
     * 第三方客户端密钥
     */
    private String clientSecret;

    /**
     * 第三方客户端密钥过期日期
     */
    private Timestamp clientSecretExpiresAt;

    /**
     * 第三方客户端标识
     */
    private String clientName;

    /**
     * 第三方客户端认证方式
     */
    private String clientAuthenticationMethods;

    /**
     * 授权批准类型
     */
    private String authorizationGrantTypes;

    /**
     * 第三方重定向地址
     */
    private String redirectUris;

    /**
     * 作用域
     */
    private String scopes;

    /**
     * 第三方客户端设置
     */
    private String clientSettings;

    /**
     * 令牌设置
     */
    private String tokenSettings;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Timestamp getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public void setClientIdIssuedAt(Timestamp clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    public Timestamp getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(Timestamp clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    public String getTokenSettings() {
        return tokenSettings;
    }

    public void setTokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
    }

    @Override
    public String toString() {
        return "Oauth2RegisteredClient{" +
        "id=" + id +
        ", clientId=" + clientId +
        ", clientIdIssuedAt=" + clientIdIssuedAt +
        ", clientSecret=" + clientSecret +
        ", clientSecretExpiresAt=" + clientSecretExpiresAt +
        ", clientName=" + clientName +
        ", clientAuthenticationMethods=" + clientAuthenticationMethods +
        ", authorizationGrantTypes=" + authorizationGrantTypes +
        ", redirectUris=" + redirectUris +
        ", scopes=" + scopes +
        ", clientSettings=" + clientSettings +
        ", tokenSettings=" + tokenSettings +
        "}";
    }
}
