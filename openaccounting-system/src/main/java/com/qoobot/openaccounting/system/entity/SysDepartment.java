package com.qoobot.openaccounting.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.system.base.SystemBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门信息实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_department")
public class SysDepartment extends SystemBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 上级部门ID
     */
    private Long parentId;

    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;

    /**
     * 部门编码
     */
    @TableField("department_code")
    private String departmentCode;

    /**
     * 祖级列表（逗号分隔）
     */
    private String ancestors;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
