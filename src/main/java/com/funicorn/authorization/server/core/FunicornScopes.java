package com.funicorn.authorization.server.core;

/**
 * @author Aimee
 * @since 2023/4/6 14:52
 */
public interface FunicornScopes {

    /**
     * 身份信息 用户名、昵称、用户头像，所属租户
     */
    String PROFILE = "profile";

    /**
     * 电子邮件
     */
    String EMAIL = "email";

    /**
     * 电话
     */
    String PHONE = "phone";
}
