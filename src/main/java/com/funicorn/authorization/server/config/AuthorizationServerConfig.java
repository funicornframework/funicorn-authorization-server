package com.funicorn.authorization.server.config;

import com.funicorn.authorization.server.core.MybatisRegisteredClientRepository;
import com.funicorn.authorization.server.core.UuidAccessTokenGenerator;
import com.funicorn.authorization.server.core.UuidRefreshTokenGenerator;
import com.funicorn.authorization.server.handler.*;
import com.funicorn.framework.common.security.whitelist.PermitUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.Resource;

/**
 * @author Aimee
 * @since 2023/3/31 22:05
 */
@Configuration
public class AuthorizationServerConfig {

    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    private static final String CUSTOM_LOGIN_PAGE_URI = "/oauth2/login";

    public static final String CUSTOM_LOGIN_PROCESS_URL = "/user/login";

    @Resource
    private LoginSuccessHandler loginSuccessHandler;
    @Resource
    private LoginFailureHandler loginFailureHandler;
    @Resource
    private UuidRefreshTokenGenerator uuidRefreshTokenGenerator;
    @Resource
    private UuidAccessTokenGenerator uuidAccessTokenGenerator;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private MybatisRegisteredClientRepository mybatisRegisteredClientRepository;

    /**
     * 密码编码器
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置Authorization Server实例
     * 0.3.x 版本叫ProviderSettings 0.4.x 改名AuthorizationServerSettings
     * @return ProviderSettings
     */
    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 注册授权同意服务
     *
     * @return JdbcOAuth2AuthorizationConsentService
     */
    @Bean
    public JdbcOAuth2AuthorizationConsentService jdbcOAuth2AuthorizationConsentService(){
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate,mybatisRegisteredClientRepository);
    }

    /**
     * oauth2授权服务
     *
     * @return JdbcOAuth2AuthorizationService
     */
    public JdbcOAuth2AuthorizationService jdbcOAuth2AuthorizationService(){
        return new JdbcOAuth2AuthorizationService(jdbcTemplate,mybatisRegisteredClientRepository);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        http.apply(authorizationServerConfigurer);
        authorizationServerConfigurer.authorizationConsentService(jdbcOAuth2AuthorizationConsentService());
        authorizationServerConfigurer.authorizationService(jdbcOAuth2AuthorizationService());
        //授权端点设置
        authorizationServerConfigurer.authorizationEndpoint(endpoint-> {
            endpoint.consentPage(CUSTOM_CONSENT_PAGE_URI);
            endpoint.errorResponseHandler(new AuthorizeFailureHandler());
        });

        //令牌生成器设置
        authorizationServerConfigurer.tokenGenerator(new DelegatingOAuth2TokenGenerator(
                uuidAccessTokenGenerator, uuidRefreshTokenGenerator
        ));

        //撤销令牌设置
        authorizationServerConfigurer.tokenRevocationEndpoint(oAuth2TokenRevocationEndpointConfigurer -> {
            oAuth2TokenRevocationEndpointConfigurer.revocationResponseHandler(new RevocationSuccessHandler());
            oAuth2TokenRevocationEndpointConfigurer.errorResponseHandler(new RevocationFailureHandler());
        });

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        //拦截暴露端点以外的所有请求
        http.requestMatcher(endpointsMatcher)
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .csrf().disable();
        http
                //认证失败跳转页面
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(CUSTOM_LOGIN_PAGE_URI)));

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests.antMatchers(PermitUtil.getPermitUrl().toArray(new String[0])).permitAll()
                                .antMatchers(
                                        CUSTOM_LOGIN_PROCESS_URL + "/**", CUSTOM_LOGIN_PAGE_URI +"/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin(formLoginConfigurer->{
                    formLoginConfigurer.loginPage(CUSTOM_LOGIN_PAGE_URI);
                    formLoginConfigurer.loginProcessingUrl(CUSTOM_LOGIN_PROCESS_URL);
                    formLoginConfigurer.successHandler(loginSuccessHandler);
                    formLoginConfigurer.failureHandler(loginFailureHandler);
                    formLoginConfigurer.permitAll();
                })
                .csrf().disable();
        return http.build();
    }
}
