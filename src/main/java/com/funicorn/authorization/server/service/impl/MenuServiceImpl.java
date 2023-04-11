package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.Menu;
import com.funicorn.authorization.server.mapper.MenuMapper;
import com.funicorn.authorization.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单管理表 服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
