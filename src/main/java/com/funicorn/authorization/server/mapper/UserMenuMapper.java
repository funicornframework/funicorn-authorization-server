package com.funicorn.authorization.server.mapper;

import com.funicorn.authorization.server.entity.UserMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户与菜单关系表 Mapper 接口
 * </p>
 *
 * @author Aimee
 * @since 2023-06-06
 */
@Mapper
public interface UserMenuMapper extends BaseMapper<UserMenu> {

}
