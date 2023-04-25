package com.funicorn.authorization.server.core;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funicorn.authorization.server.entity.Oauth2RegisteredClient;
import com.funicorn.authorization.server.service.IOauth2RegisteredClientService;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Aimee
 * @since 2023/4/13 9:08
 */
@Component
public class MybatisRegisteredClientRepository implements RegisteredClientRepository {

    @Resource
    private IOauth2RegisteredClientService registeredClientService;

    private final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = MybatisRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        return objectMapper;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        long count = registeredClientService.count(Wrappers.<Oauth2RegisteredClient>lambdaQuery().eq(Oauth2RegisteredClient::getClientId,registeredClient.getClientId()));
        if (count>0) {
            throw new IllegalArgumentException("client_id exist");
        }
        Oauth2RegisteredClient oauth2RegisteredClient = transferTo(registeredClient);
        registeredClientService.save(oauth2RegisteredClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        Oauth2RegisteredClient oauth2RegisteredClient = registeredClientService.getById(id);
        if (oauth2RegisteredClient==null) {
            return null;
        }
        return reversalTo(oauth2RegisteredClient);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Oauth2RegisteredClient oauth2RegisteredClient = registeredClientService.getOne(Wrappers.<Oauth2RegisteredClient>lambdaQuery()
                .eq(Oauth2RegisteredClient::getClientId,clientId));
        if (oauth2RegisteredClient==null) {
            return null;
        }
        return reversalTo(oauth2RegisteredClient);
    }

    private RegisteredClient reversalTo(Oauth2RegisteredClient oauth2RegisteredClient){
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(oauth2RegisteredClient.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(oauth2RegisteredClient.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(oauth2RegisteredClient.getRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(oauth2RegisteredClient.getScopes());

        // @formatter:off
        RegisteredClient.Builder builder = RegisteredClient.withId(oauth2RegisteredClient.getId())
                .clientId(oauth2RegisteredClient.getClientId())
                .clientIdIssuedAt(oauth2RegisteredClient.getClientIdIssuedAt() != null ? oauth2RegisteredClient.getClientIdIssuedAt().toInstant() : null)
                .clientSecret(oauth2RegisteredClient.getClientSecret())
                .clientSecretExpiresAt(oauth2RegisteredClient.getClientSecretExpiresAt() != null ? oauth2RegisteredClient.getClientSecretExpiresAt().toInstant() : null)
                .clientName(oauth2RegisteredClient.getClientName())
                .clientAuthenticationMethods((authenticationMethods) ->
                        clientAuthenticationMethods.forEach(authenticationMethod ->
                                authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes));
        // @formatter:on

        Map<String, Object> clientSettingsMap = parseMap(oauth2RegisteredClient.getClientSettings());
        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

        Map<String, Object> tokenSettingsMap = parseMap(oauth2RegisteredClient.getTokenSettings());
        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.withSettings(tokenSettingsMap);
        if (!tokenSettingsMap.containsKey(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT)) {
            tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        }
        builder.tokenSettings(tokenSettingsBuilder.build());

        return builder.build();
    }

    private Oauth2RegisteredClient transferTo(RegisteredClient registeredClient){
        Oauth2RegisteredClient oauth2RegisteredClient = new Oauth2RegisteredClient();
        oauth2RegisteredClient.setClientId(registeredClient.getClientId());
        Timestamp clientIdIssuedAt = registeredClient.getClientIdIssuedAt() != null ?
                Timestamp.from(registeredClient.getClientIdIssuedAt()) : Timestamp.from(Instant.now());
        oauth2RegisteredClient.setClientIdIssuedAt(clientIdIssuedAt);
        oauth2RegisteredClient.setClientSecret(registeredClient.getClientSecret());
        Timestamp clientSecretExpiresAt = registeredClient.getClientSecretExpiresAt() != null ?
                Timestamp.from(registeredClient.getClientSecretExpiresAt()) : null;
        oauth2RegisteredClient.setClientSecretExpiresAt(clientSecretExpiresAt);
        oauth2RegisteredClient.setClientName(registeredClient.getClientName());
        oauth2RegisteredClient.setClientAuthenticationMethods(
                StringUtils.collectionToCommaDelimitedString(
                        registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toList())
                )
        );
        oauth2RegisteredClient.setAuthorizationGrantTypes(
                StringUtils.collectionToCommaDelimitedString(
                        registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.toList())
                )
        );
        oauth2RegisteredClient.setRedirectUris(
                StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris())
        );
        oauth2RegisteredClient.setScopes(
                StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes())
        );
        oauth2RegisteredClient.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
        oauth2RegisteredClient.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));
        oauth2RegisteredClient.setCreatedBy("admin");
        return oauth2RegisteredClient;
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }

    private String writeMap(Map<String, Object> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private Map<String, Object> parseMap(String data) {
        try {
            return objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
