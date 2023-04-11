package com.funicorn.authorization.server.mapper;

import com.funicorn.authorization.server.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色管理 Mapper 接口
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}
