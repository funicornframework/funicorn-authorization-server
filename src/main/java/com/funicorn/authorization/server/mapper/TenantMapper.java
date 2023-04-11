package com.funicorn.authorization.server.mapper;

import com.funicorn.authorization.server.entity.Tenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 租户管理 Mapper 接口
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

}
