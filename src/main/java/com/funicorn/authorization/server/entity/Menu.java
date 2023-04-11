package com.funicorn.authorization.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.funicorn.framework.common.datasource.model.BaseEntity;
import java.io.Serializable;

/**
 * <p>
 * 菜单管理表
 * </p>
 *
 * @author Aimee
 * @since 2023-03-31
 */
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 类型(menu/button)
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 权限
     */
    private String permission;

    /**
     * 路由
     */
    private String router;

    /**
     * status: hidden->隐藏,  show->显示
     */
    private String status;

    /**
     * 权限 public/private
     */
    private String level;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序 升序
     */
    private Integer sort;


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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Menu{" +
        "id=" + id +
        ", parentId=" + parentId +
        ", appId=" + appId +
        ", type=" + type +
        ", name=" + name +
        ", permission=" + permission +
        ", router=" + router +
        ", status=" + status +
        ", level=" + level +
        ", icon=" + icon +
        ", sort=" + sort +
        "}";
    }
}
