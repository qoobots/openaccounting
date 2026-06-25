package com.qoobot.openaccounting.ledger.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 项目档案实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ba_project")
public class BaProject extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公司ID
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 项目编码
     */
    @TableField("project_code")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目类型
     */
    @TableField("project_type")
    private String projectType;

    /**
     * 负责人
     */
    @TableField("manager")
    private String manager;

    /**
     * 开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 预算金额
     */
    @TableField("budget_amount")
    private java.math.BigDecimal budgetAmount;

    /**
     * 项目描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态：0启用 1停用
     */
    @TableField("status")
    private Integer status;
}
