package com.funicorn.authorization.server.controller;

import com.funicorn.authorization.server.entity.UserInfo;
import com.funicorn.boot.starter.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息管理 前端控制器
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @GetMapping("/login")
    public Result<UserInfo> getUserInfo(){
        return Result.ok();
    }
}
