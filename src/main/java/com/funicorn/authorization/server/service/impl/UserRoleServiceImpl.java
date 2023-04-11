package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.UserRole;
import com.funicorn.authorization.server.mapper.UserRoleMapper;
import com.funicorn.authorization.server.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户与角色关系 服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
