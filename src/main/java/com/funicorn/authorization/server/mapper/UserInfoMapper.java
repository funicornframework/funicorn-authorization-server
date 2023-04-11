package com.funicorn.authorization.server.mapper;

import com.funicorn.authorization.server.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息管理 Mapper 接口
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
