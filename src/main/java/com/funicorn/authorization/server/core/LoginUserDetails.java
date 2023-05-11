package com.funicorn.authorization.server.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.funicorn.framework.common.context.constant.UserType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aimee
 * @since 2023/3/31 9:59
 */
@JsonIgnoreProperties({"authorities","accountNonExpired", "accountNonLocked", "credentialsNonExpired","credentialsNonExpired"})
public class LoginUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 用户id
     * */
    private String userId;

    /**
     * 用户名账号
     * */
    private String username;

    /**
     * 登录密码
     * */
    private String password;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 邮箱
     * */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 证件类型
     * */
    private String idType;

    /**
     * 联系地址
     * */
    private String address;

    /**
     * 昵称
     * */
    private String nickName;

    /**
     * 用户头像
     * */
    private String headLogo;

    /**
     * 账号是否可用 0：不可用 ；1：可用
     * */
    private Integer enabled;

    /**
     * 密码失效时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime expireTime;

    /**
     * 账号是否被锁定 0：锁定；1：正常
     * */
    private Integer locked;

    /**
     * 租户id
     * */
    private String tenantId;

    /**
     * 租户名称
     * */
    private String tenantName;

    /**
     * 机构id
     * */
    private String orgId;

    /**
     * 机构名称
     * */
    private String orgName;

    /**
     * 注册时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime registerTime;

    /**
     * 上次登录时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime lastLoginTime;

    /**
     * 角色编码列表
     * */
    private List<String> roles;
    /**
     * 角色权限列表
     * */
    private List<String> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> authList = new ArrayList<>();
        if (this.permissions!=null && !this.permissions.isEmpty()){
            authList.addAll(this.permissions);
        }
        if (this.roles!=null && !roles.isEmpty()){
            for (String code:this.roles) {
                if (code.startsWith("ROLE_")){
                    authList.add(code);
                }else {
                    authList.add("ROLE_" + code);
                }
            }
        }
        if (authList.isEmpty()){
            return null;
        }
        authList = authList.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (authList.isEmpty()){
            return null;
        }
        return AuthorityUtils.createAuthorityList(authList.toArray(new String[0]));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.locked == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (this.expireTime==null) {
            return true;
        }
        return this.expireTime.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isEnabled() {
        return this.enabled==1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadLogo() {
        return headLogo;
    }

    public void setHeadLogo(String headLogo) {
        this.headLogo = headLogo;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public UserType getUserType() {
        if (StringUtils.isBlank(this.userType)) {
            return UserType.NONE;
        }
        return UserType.valueOf(this.userType);
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
