package com.funicorn.authorization.server.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Aimee
 * @since 2023/4/13 17:33
 */
public class AuthorizeFailureHandler  implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String redirectUri = "/oauth2/error?error_desc=" + exception.getMessage() + " Is Invalid";
        response.setStatus(HttpStatus.FOUND.value());
        response.setHeader("Location", redirectUri);
    }
}
