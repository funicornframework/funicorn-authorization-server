package com.funicorn.authorization.server.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aimee
 * @since 2024/3/13 15:08
 */
@Configuration
@ConfigurationProperties(prefix = "authorization")
public class AuthorizationProperties {

    /**
     * baseUrl
     */
    private String baseUrl = "/";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
