package com.funicorn.authorization.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.funicorn.framework.common.datasource.model.BaseEntity;

/**
 * <p>
 * 组织机构管理表
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
public class Organization extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 机构名称·
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序 升序
     */
    private Integer sort;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "Organization{" +
        "id=" + id +
        ", parentId=" + parentId +
        ", name=" + name +
        ", remark=" + remark +
        ", sort=" + sort +
        ", tenantId=" + tenantId +
        "}";
    }
}
