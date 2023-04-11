package com.funicorn.authorization.server.exception;

import com.funicorn.framework.common.base.exception.ErrorCode;

/**
 * @author Aimee
 * @since 2023/3/31 11:20
 * -100 ~ -199
 */
public enum ExceptionCode implements ErrorCode {

    /**
     * 账号过期
     * */
    USER_ACCOUNT_EXPIRED(-100,"账号已过期"),

    /**
     * 密码错误
     * */
    USER_CREDENTIALS_ERROR(-101,"用户名或密码错误"),

    /**
     * 密码过期
     * */
    USER_CREDENTIALS_EXPIRED(-102,"密码已过期"),

    /**
     * 账号不可用
     * */
    USER_ACCOUNT_DISABLE(-103,"账号不可用"),

    /**
     * 账号被锁定
     * */
    USER_ACCOUNT_LOCKED(-104,"账号被锁定"),

    /**
     * 所属租户不存在或已被注销
     * */
    TENANT_NOT_EXIST(-105,"所属租户不存在或已被注销"),

    /**
     * 撤销令牌失败
     */
    REVOKE_TOKEN_FAIL(-106,"撤销令牌失败"),

    /**
     * OAUTH2登录参数无效
     */
    OAUTH2_LOGIN_PARAM_INVALID(-107,"登录参数无效"),

    ;

    private final int code;

    private final String msg;

    ExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
