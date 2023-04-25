package com.funicorn.authorization.server.mapper;

import com.funicorn.authorization.server.entity.Oauth2RegisteredClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Aimee
 * @since 2023-04-13
 */
@Mapper
public interface Oauth2RegisteredClientMapper extends BaseMapper<Oauth2RegisteredClient> {

}
