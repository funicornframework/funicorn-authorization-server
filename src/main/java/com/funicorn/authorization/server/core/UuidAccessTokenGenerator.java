package com.funicorn.authorization.server.core;

import com.funicorn.framework.common.base.json.JsonUtil;
import com.funicorn.framework.common.context.core.LoginUser;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Aimee
 * @since 2023/4/4 10:11
 */
@Component
public class UuidAccessTokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {

    @Override
    public OAuth2AccessToken generate(OAuth2TokenContext context) {
        if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) ||
                !OAuth2TokenFormat.REFERENCE.equals(context.getRegisteredClient().getTokenSettings().getAccessTokenFormat())) {
            return null;
        }

        String issuer = null;
        if (context.getAuthorizationServerContext() != null) {
            issuer = context.getAuthorizationServerContext().getIssuer();
        }
        RegisteredClient registeredClient = context.getRegisteredClient();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());

        // @formatter:off
        OAuth2TokenClaimsSet.Builder claimsBuilder = OAuth2TokenClaimsSet.builder();
        if (StringUtils.hasText(issuer)) {
            claimsBuilder.issuer(issuer);
        }
        claimsBuilder
                .subject(context.getPrincipal().getName())
                .audience(Collections.singletonList(registeredClient.getClientId()))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .notBefore(issuedAt)
                .id(UUID.randomUUID().toString());
        if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
            claimsBuilder.claim(OAuth2ParameterNames.SCOPE, context.getAuthorizedScopes());
        }

        //添加附加信息
        setAdditionInformation(claimsBuilder,context);

        OAuth2TokenClaimsSet accessTokenClaimsSet = claimsBuilder.build();

        return new RedisAccessTokenClaims(OAuth2AccessToken.TokenType.BEARER,
                UUID.randomUUID().toString(), accessTokenClaimsSet.getIssuedAt(), accessTokenClaimsSet.getExpiresAt(),
                context.getAuthorizedScopes(), accessTokenClaimsSet.getClaims());
    }

    /**
     * 设置附加信息
     *
     * @param claimsBuilder 主张构建器
     * @param context       上下文
     */
    private void setAdditionInformation(OAuth2TokenClaimsSet.Builder claimsBuilder,OAuth2TokenContext context){
        if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
            if (context.getPrincipal()!=null && context.getPrincipal().getPrincipal()!=null
                    && context.getPrincipal().getPrincipal() instanceof LoginUserDetails) {
                LoginUserDetails loginUserDetails = (LoginUserDetails) context.getPrincipal().getPrincipal();
                //用户信息
                LoginUser additionInformation = new LoginUser();
                additionInformation.setUsername(loginUserDetails.getUsername());
                if (context.getAuthorizedScopes().contains(FunicornScopes.PROFILE)) {
                    additionInformation = JsonUtil.object2Object(loginUserDetails,LoginUser.class);
                }

                if (context.getAuthorizedScopes().contains(FunicornScopes.EMAIL)) {
                    additionInformation.setEmail(loginUserDetails.getEmail());
                } else {
                    additionInformation.setEmail(null);
                }

                if (context.getAuthorizedScopes().contains(FunicornScopes.PHONE)) {
                    additionInformation.setPhone(loginUserDetails.getPhone());
                } else {
                    additionInformation.setPhone(null);
                }

                claimsBuilder.claim("loginUser", additionInformation);
            }
        }
    }

    private static final class RedisAccessTokenClaims extends OAuth2AccessToken implements ClaimAccessor {
        private final Map<String, Object> claims;

        private RedisAccessTokenClaims(TokenType tokenType, String tokenValue,
                                        Instant issuedAt, Instant expiresAt, Set<String> scopes, Map<String, Object> claims) {
            super(tokenType, tokenValue, issuedAt, expiresAt, scopes);
            this.claims = claims;
        }

        @Override
        public Map<String, Object> getClaims() {
            return this.claims;
        }
    }
}
