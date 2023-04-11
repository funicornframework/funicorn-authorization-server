package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.Tenant;
import com.funicorn.authorization.server.mapper.TenantMapper;
import com.funicorn.authorization.server.service.ITenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户管理 服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

}
