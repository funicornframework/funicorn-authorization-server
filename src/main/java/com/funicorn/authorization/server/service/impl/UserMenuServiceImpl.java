package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.UserMenu;
import com.funicorn.authorization.server.mapper.UserMenuMapper;
import com.funicorn.authorization.server.service.IUserMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户与菜单关系表 服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-06-06
 */
@Service
public class UserMenuServiceImpl extends ServiceImpl<UserMenuMapper, UserMenu> implements IUserMenuService {

}
