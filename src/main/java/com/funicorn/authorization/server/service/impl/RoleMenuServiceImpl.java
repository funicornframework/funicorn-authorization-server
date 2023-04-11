package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.RoleMenu;
import com.funicorn.authorization.server.mapper.RoleMenuMapper;
import com.funicorn.authorization.server.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色与菜单关系表 服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

}
