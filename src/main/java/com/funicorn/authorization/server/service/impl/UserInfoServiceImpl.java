package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.UserInfo;
import com.funicorn.authorization.server.mapper.UserInfoMapper;
import com.funicorn.authorization.server.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息管理 服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
