package com.funicorn.authorization.server.mapper;

import com.funicorn.authorization.server.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色与菜单关系表 Mapper 接口
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}
