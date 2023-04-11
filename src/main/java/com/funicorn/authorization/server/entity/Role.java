package com.funicorn.authorization.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.funicorn.framework.common.datasource.model.BaseEntity;

/**
 * <p>
 * 角色管理
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编号
     */
    private String code;

    /**
     * 租户id 默认-1
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "Role{" +
        "id=" + id +
        ", name=" + name +
        ", code=" + code +
        ", tenantId=" + tenantId +
        "}";
    }
}
