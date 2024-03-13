package com.funicorn.authorization.server.handler;

import com.alibaba.fastjson2.JSONObject;
import com.funicorn.authorization.server.exception.ExceptionCode;
import com.funicorn.authorization.server.property.AuthorizationProperties;
import com.funicorn.boot.starter.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Aimee
 * @since 2023/3/31 11:15
 */
@Configuration
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private AuthorizationProperties authorizationProperties;

    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUri;
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        int code = ExceptionCode.OAUTH2_LOGIN_PARAM_INVALID.getCode();
        if (savedRequest!=null) {
            redirectUri = savedRequest.getRedirectUrl();
            if (redirectUri.contains("error=")) {
                redirectUri = "/oauth2/error?" + new URL(redirectUri).getQuery().replace("error=","error_desc=");
            } else {
                code = Result.SUCCESS;
            }
        } else {
            String responseType = request.getParameter("response_type");
            String clientId = request.getParameter("client_id");
            String scope = request.getParameter("scope");
            String state = request.getParameter("state");
            String thirdRedirectUrl = request.getParameter("redirect_uri");

            if(StringUtils.isEmpty(responseType)) {
                redirectUri = "/oauth2/error?error_desc=response_type not be empty";
            } else if(StringUtils.isEmpty(thirdRedirectUrl)) {
                redirectUri = "/oauth2/error?error_desc=redirect_uri not be empty";
            } else if(StringUtils.isEmpty(clientId)) {
                redirectUri = "/oauth2/error?error_desc=client_id not be empty";
            } else if (StringUtils.isEmpty(scope)) {
                redirectUri = "/oauth2/error?error_desc=scope not be empty";
            } else {
                redirectUri = buildRedirectUrl(clientId,thirdRedirectUrl,state,scope,responseType);
                code = Result.SUCCESS;
            }
        }

        //是否异步
        boolean isAsync = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (isAsync) {
            response.setStatus(HttpServletResponse.SC_OK);
            Result<?> result;
            if (code!=Result.SUCCESS) {
                result = Result.error(ExceptionCode.OAUTH2_LOGIN_PARAM_INVALID.getCode(), redirectUri);
            } else {
                result = Result.ok(redirectUri, "login success");
            }
            writeResponse(response, result);
        } else {
            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader("Location", redirectUri);
        }
    }

    /**
     * 重定向url
     * 这里重定向到登陆时访问的那台服务器，可以重定向到服务网关
     * */
    protected String buildRedirectUrl(String clientId, String redirectUrl, String state, String scope,String responseType) throws UnsupportedEncodingException {
        //对redirectUrl编码
        redirectUrl = URLEncoder.encode(redirectUrl,"UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append(authorizationProperties.getBaseUrl()).append("/oauth2/authorize?client_id=").append(clientId)
                .append("&redirect_uri=").append(redirectUrl)
                .append("&response_type=").append(responseType);

        if(StringUtils.isNotEmpty(scope)) {
            sb.append("&scope=").append(scope);
        } else {
            sb.append("&scope=");
        }

        if(StringUtils.isNotEmpty(state)) {
            sb.append("&state=").append(state);
        } else {
            sb.append("&state=");
        }
        return sb.toString();
    }

    /**
     * 写入response流
     * @param response 返回流
     * @param obj 对象
     * @throws IOException 异常
     * */
    private void writeResponse(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        if (obj instanceof String){
            String msg = (String)obj;
            response.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
        }else {
            response.getOutputStream().write(JSONObject.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
        }
    }
}
