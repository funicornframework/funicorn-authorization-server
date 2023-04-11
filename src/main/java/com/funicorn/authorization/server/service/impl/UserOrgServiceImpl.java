package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.UserOrg;
import com.funicorn.authorization.server.mapper.UserOrgMapper;
import com.funicorn.authorization.server.service.IUserOrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户与机构关系表 服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Service
public class UserOrgServiceImpl extends ServiceImpl<UserOrgMapper, UserOrg> implements IUserOrgService {

}
