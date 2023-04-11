package com.funicorn.authorization.server.handler;

import com.alibaba.fastjson2.JSONObject;
import com.funicorn.authorization.server.exception.ExceptionCode;
import com.funicorn.authorization.server.exception.TenantNotFoundException;
import com.funicorn.boot.starter.model.Result;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Aimee
 * @since 2023/3/31 11:18
 */
@Configuration
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Result<?> result = Result.error(ExceptionCode.USER_CREDENTIALS_ERROR.getCode(),ExceptionCode.USER_CREDENTIALS_ERROR.getMsg());
        if (e instanceof AccountExpiredException) {
            //账号过期
            result = Result.error(ExceptionCode.USER_ACCOUNT_EXPIRED.getCode(),ExceptionCode.USER_ACCOUNT_EXPIRED.getMsg());
        } else if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
            //用户名或密码错误
            result = Result.error(ExceptionCode.USER_CREDENTIALS_ERROR.getCode(),ExceptionCode.USER_CREDENTIALS_ERROR.getMsg());
        } else if (e instanceof CredentialsExpiredException) {
            //密码过期
            result = Result.error(ExceptionCode.USER_CREDENTIALS_EXPIRED.getCode(),ExceptionCode.USER_CREDENTIALS_EXPIRED.getMsg());
        } else if (e instanceof DisabledException) {
            //账号不可用
            result = Result.error(ExceptionCode.USER_ACCOUNT_DISABLE.getCode(),ExceptionCode.USER_ACCOUNT_DISABLE.getMsg());
        } else if (e instanceof LockedException) {
            //账号锁定
            result = Result.error(ExceptionCode.USER_ACCOUNT_LOCKED.getCode(),ExceptionCode.USER_ACCOUNT_LOCKED.getMsg());
        } else if (e.getCause() instanceof TenantNotFoundException) {
            //所属租户不存在或已被注销
            result = Result.error(ExceptionCode.TENANT_NOT_EXIST.getCode(),e.getMessage());
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writeResponse(response, result);
    }
    /**
     * 写入response流
     * @param response 返回流
     * @param obj 对象
     * @throws IOException 异常
     * */
    public void writeResponse(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        if (obj instanceof String){
            String msg = (String)obj;
            response.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
        }else {
            response.getOutputStream().write(JSONObject.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
        }
    }
}
