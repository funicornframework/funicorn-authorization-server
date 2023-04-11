package com.funicorn.authorization.server.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Aimee
 * @since 2023/4/1 9:32
 */
public class TenantNotFoundException extends AuthenticationException {

    public TenantNotFoundException(String msg) {
        super(msg);
    }

    public TenantNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
