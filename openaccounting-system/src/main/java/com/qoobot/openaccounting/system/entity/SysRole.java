package com.qoobot.openaccounting.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.system.base.SystemBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色信息实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends SystemBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色编码
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 数据范围（1-全部，2-自定义，3-本部门，4-本部门及以下，5-仅本人）
     */
    @TableField("data_scope")
    private Integer dataScope;

    /**
     * 角色排序
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
