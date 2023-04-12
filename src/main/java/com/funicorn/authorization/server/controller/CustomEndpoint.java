package com.funicorn.authorization.server.controller;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.*;

/**
 * @author Aimee
 * @since 2023/4/1 11:12
 */
@Controller
public class CustomEndpoint {

    @Resource
    private RegisteredClientRepository registeredClientRepository;

    /**
     * 错误
     *
     * @return {@link String}
     */
    @RequestMapping("/oauth2/error")
    public String error(){
        return "error";
    }

    /**
     * 登录页面
     *
     * String
     */
    @RequestMapping("/oauth2/login")
    public String login(){
        return "login";
    }

    /**
     * 授权页面
     *
     * @param model 模型
     * @return String
     */
    @GetMapping("/oauth2/consent")
    public String consent(Principal principal, Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state) {

        Set<String> scopesToApprove = new LinkedHashSet<>();
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
        assert registeredClient != null;
        Set<String> scopes = registeredClient.getScopes();
        if (org.apache.commons.lang3.StringUtils.isBlank(scope)) {
            scopesToApprove.addAll(scopes);
        } else {
            for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
                if (scopes.contains(requestedScope)) {
                    scopesToApprove.add(requestedScope);
                }
            }
        }

        model.addAttribute("clientId", clientId);
        model.addAttribute("clientName", registeredClient.getClientName());
        model.addAttribute("state", state);
        model.addAttribute("scopes", withDescription(scopesToApprove));
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("redirectUri", registeredClient.getRedirectUris().iterator().next());
        return "consent";
    }

    private static List<ScopeWithDescription> withDescription(Set<String> scopes) {
        List<ScopeWithDescription> scopeWithDescriptions = new LinkedList<>();
        for (String scope : scopes) {
            scopeWithDescriptions.add(new ScopeWithDescription(scope));

        }
        return scopeWithDescriptions;
    }

    public static class ScopeWithDescription {
        private static final String DEFAULT_DESCRIPTION = "未知作用域";
        private static final Map<String, String> SCOPE_DESC = new HashMap<>();

        static {
            SCOPE_DESC.put(
                    "idcard",
                    "获取您的身份证号码"
            );
            SCOPE_DESC.put(
                    "email",
                    "获取您的邮箱地址"
            );
            SCOPE_DESC.put(
                    "phone",
                    "获取您的手机号码"
            );
            SCOPE_DESC.put(
                    "address",
                    "获取您的联系地址"
            );
            SCOPE_DESC.put(
                    "profile",
                    "获取您的头像和昵称"
            );
        }

        public final String scope;
        public final String description;

        ScopeWithDescription(String scope) {
            this.scope = scope;
            this.description = SCOPE_DESC.getOrDefault(scope, DEFAULT_DESCRIPTION);
        }
    }
}
