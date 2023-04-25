package com.funicorn.authorization.server.service.impl;

import com.funicorn.authorization.server.entity.Oauth2RegisteredClient;
import com.funicorn.authorization.server.mapper.Oauth2RegisteredClientMapper;
import com.funicorn.authorization.server.service.IOauth2RegisteredClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Aimee
 * @since 2023-04-13
 */
@Service
public class Oauth2RegisteredClientServiceImpl extends ServiceImpl<Oauth2RegisteredClientMapper, Oauth2RegisteredClient> implements IOauth2RegisteredClientService {

}
