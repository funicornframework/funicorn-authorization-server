package com.funicorn.authorization.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.funicorn.framework.common.datasource.model.BaseEntity;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息管理
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
@TableName("user_info")
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String userId;

    /**
     * 用户名账号
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户类型 SUPER/ADMIN/NORMAL
     * */
    private String userType;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 证件类型 0身份证 1港澳通行证 2学生证 3护照 4士官证 5驾驶证
     */
    private Integer idType;

    /**
     * 证件号码
     */
    private String idCard;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String headLogo;

    /**
     * 账号是否可用:0可用,1不可用
     */
    private Integer enabled;

    /**
     * 历史密码记录，保存最近3个
     */
    private String historyPwd;

    /**
     * 密码失效时间
     */
    private LocalDateTime expireTime;

    /**
     * 账号是否被锁定:0正常,1锁定
     */
    private Integer locked;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
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

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public String getHistoryPwd() {
        return historyPwd;
    }

    public void setHistoryPwd(String historyPwd) {
        this.historyPwd = historyPwd;
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

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
        "userId=" + userId +
        ", username=" + username +
        ", password=" + password +
        ", email=" + email +
        ", phone=" + phone +
        ", idType=" + idType +
        ", idCard=" + idCard +
        ", address=" + address +
        ", nickName=" + nickName +
        ", headLogo=" + headLogo +
        ", enabled=" + enabled +
        ", historyPwd=" + historyPwd +
        ", expireTime=" + expireTime +
        ", locked=" + locked +
        "}";
    }
}
