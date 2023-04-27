package com.funicorn.authorization.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.funicorn.framework.common.datasource.model.BaseEntity;

/**
 * <p>
 * 租户管理
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
public class Tenant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 租户id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * logo地址
     */
    private String logo;

    /**
     * 描述
     */
    private String remark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Override
    public String toString() {
        return "Tenant{" +
        "id=" + id +
        ", tenantName=" + tenantName +
        ", logo=" + logo +
        ", remark=" + remark +
        "}";
    }
}
