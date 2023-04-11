package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.Role;
import com.funicorn.authorization.server.mapper.RoleMapper;
import com.funicorn.authorization.server.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色管理 服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
