package com.funicorn.authorization.server.mapper;

import com.funicorn.authorization.server.entity.UserOrg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户与机构关系表 Mapper 接口
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@Mapper
public interface UserOrgMapper extends BaseMapper<UserOrg> {

}
