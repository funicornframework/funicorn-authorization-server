package com.funicorn.authorization.server.handler;

import com.alibaba.fastjson2.JSONObject;
import com.funicorn.boot.starter.model.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Aimee
 * @since 2023/4/10 15:37
 */
public class RevocationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        Result<?> result = Result.ok("revoke success");
        response.setContentType("application/json;charset=utf-8");
        response.getOutputStream().write(JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8));
    }
}
