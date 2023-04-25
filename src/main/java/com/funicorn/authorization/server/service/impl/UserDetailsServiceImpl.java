package com.funicorn.authorization.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.funicorn.authorization.server.config.AuthorizationServerConfig;
import com.funicorn.authorization.server.core.LoginUserDetails;
import com.funicorn.authorization.server.entity.*;
import com.funicorn.authorization.server.exception.TenantNotFoundException;
import com.funicorn.authorization.server.mapper.*;
import com.funicorn.framework.common.base.json.JsonUtil;
import com.funicorn.logger.core.sys.SysLog;
import com.funicorn.logger.module.OperateType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aimee
 * @since 2023/3/31 9:52
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final AntPathMatcher ANTPATHMATCHER = new AntPathMatcher();

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private UserOrgMapper userOrgMapper;

    @SysLog(value = "用户登录",OperateType = OperateType.LOGIN)
    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUsername,username));
        if (userInfo==null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        Tenant tenant = tenantMapper.selectById(userInfo.getTenantId());
        if (tenant==null) {
            throw new TenantNotFoundException("所属租户不存在或已被注销");
        }
        LoginUserDetails loginUser = JsonUtil.object2Object(userInfo,LoginUserDetails.class);
        loginUser.setTenantName(tenant.getTenantName());
        List<UserRole> userRoles = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId,userInfo.getUserId()).eq(UserRole::getTenantId,userInfo.getTenantId()));
        if (!userRoles.isEmpty()) {
            List<String> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            List<Role> roleInfos = roleMapper.selectList(Wrappers.<Role>lambdaQuery().in(Role::getId,roleIds));
            if (!roleInfos.isEmpty()) {
                List<String> roles = roleInfos.stream().map(Role::getCode).filter(StringUtils::isNotBlank).collect(Collectors.toList());
                loginUser.setRoles(roles);
                List<RoleMenu> roleMenus = roleMenuMapper.selectList(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getTenantId,userInfo.getTenantId()).in(RoleMenu::getRoleId,roles));
                if (!roleMenus.isEmpty()) {
                    List<String> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
                    if (!menuIds.isEmpty()) {
                        List<Menu> menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery().in(Menu::getId,menuIds));
                        if (!menus.isEmpty()) {
                            List<String> permissions = menus.stream().map(Menu::getPermission).filter(StringUtils::isNotBlank).collect(Collectors.toList());
                            loginUser.setPermissions(permissions);
                        }
                    }
                }
            }
        }
        UserOrg userOrg = userOrgMapper.selectOne(Wrappers.<UserOrg>lambdaQuery().eq(UserOrg::getUserId,userInfo.getUserId()).last("limit 1"));
        if (userOrg!=null) {
            Organization organization = organizationMapper.selectById(userOrg.getId());
            if (organization!=null) {
                loginUser.setOrgId(organization.getId());
                loginUser.setOrgName(organization.getName());
            }
        }

        loginUser.setRegisterTime(userInfo.getCreatedTime());

        //更新本次登录时间点
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            if ((ANTPATHMATCHER.match(AuthorizationServerConfig.CUSTOM_LOGIN_PROCESS_URL,request.getRequestURI()))){
                LambdaUpdateWrapper<UserInfo> userInfoUpdateWrapper = new LambdaUpdateWrapper<>();
                userInfoUpdateWrapper.set(UserInfo::getLastLoginTime, LocalDateTime.now());
                userInfoUpdateWrapper.eq(UserInfo::getUserId,userInfo.getUserId());
                userInfoMapper.update(null,userInfoUpdateWrapper);
            }
        }
        return loginUser;
    }
}
