package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.Organization;
import com.funicorn.authorization.server.mapper.OrganizationMapper;
import com.funicorn.authorization.server.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 组织机构管理表 服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

}
