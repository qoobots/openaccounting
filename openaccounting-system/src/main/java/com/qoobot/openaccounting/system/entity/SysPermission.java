package com.qoobot.openaccounting.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.system.base.SystemBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限信息实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class SysPermission extends SystemBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 祖级列表（逗号分隔）
     */
    private String ancestors;

    /**
     * 权限名称
     */
    @TableField("permission_name")
    private String permissionName;

    /**
     * 权限编码
     */
    @TableField("permission_code")
    private String permissionCode;

    /**
     * 权限类型（1-菜单，2-按钮）
     */
    @TableField("permission_type")
    private String permissionType;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
