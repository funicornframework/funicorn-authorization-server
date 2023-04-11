package com.funicorn.authorization.server.handler;

import com.alibaba.fastjson2.JSONObject;
import com.funicorn.authorization.server.exception.ExceptionCode;
import com.funicorn.boot.starter.model.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Aimee
 * @since 2023/4/10 15:37
 */
public class RevocationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        Result<?> result = Result.error(ExceptionCode.REVOKE_TOKEN_FAIL.getCode(),
                ExceptionCode.REVOKE_TOKEN_FAIL.getMsg());
        response.setContentType("application/json;charset=utf-8");
        response.getOutputStream().write(JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8));
    }
}
