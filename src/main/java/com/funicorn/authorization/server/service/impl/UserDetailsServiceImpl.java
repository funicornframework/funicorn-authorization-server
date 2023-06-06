package com.funicorn.authorization.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.funicorn.authorization.server.config.AuthorizationServerConfig;
import com.funicorn.authorization.server.core.LoginUserDetails;
import com.funicorn.authorization.server.entity.*;
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
    private UserMenuMapper userMenuMapper;

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
        LoginUserDetails loginUser = JsonUtil.object2Object(userInfo,LoginUserDetails.class);
        Tenant tenant = tenantMapper.selectById(userInfo.getTenantId());
        if (tenant!=null) {
            loginUser.setTenantName(tenant.getTenantName());
        }

        //user_role
        List<UserRole> userRoles = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId,userInfo.getUserId()).eq(UserRole::getTenantId,userInfo.getTenantId()));
        if (!userRoles.isEmpty()) {
            List<String> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            List<Role> roleInfos = roleMapper.selectList(Wrappers.<Role>lambdaQuery().in(Role::getId,roleIds));
            if (!roleInfos.isEmpty()) {
                loginUser.setRoleCode(roleInfos.get(0).getCode());
                loginUser.setRoleName(roleInfos.get(0).getName());
                List<RoleMenu> roleMenus = roleMenuMapper.selectList(Wrappers.<RoleMenu>lambdaQuery()
                        .eq(RoleMenu::getTenantId,userInfo.getTenantId()).eq(RoleMenu::getRoleId,roleInfos.get(0).getId()));
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

            Organization organization = organizationMapper.selectById(userRoles.get(0).getOrganizationId());
            if (organization!=null) {
                loginUser.setOrgId(organization.getId());
                loginUser.setOrgName(organization.getName());
            }
        }

        //user_menu
        List<UserMenu> userMenuList = userMenuMapper.selectList(Wrappers.<UserMenu>lambdaQuery().eq(UserMenu::getUserId,loginUser.getUserId()));
        if (!userMenuList.isEmpty()) {
            List<String> menuIds = userMenuList.stream().map(UserMenu::getMenuId).collect(Collectors.toList());
            if (!menuIds.isEmpty()) {
                List<Menu> menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery().in(Menu::getId,menuIds));
                if (!menus.isEmpty()) {
                    List<String> permissions = menus.stream().map(Menu::getPermission).filter(StringUtils::isNotBlank).collect(Collectors.toList());
                    if (loginUser.getPermissions()==null) {
                        loginUser.setPermissions(permissions);
                    } else {
                        loginUser.getPermissions().addAll(permissions);
                    }
                }
            }
        }

        //注册时间
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
