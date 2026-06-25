package com.qoobot.openaccounting.budget.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 预算实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bg_budget")
public class BgBudget extends BaseEntity {

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
     * 预算编码
     */
    @TableField("budget_code")
    private String budgetCode;

    /**
     * 预算名称
     */
    @TableField("budget_name")
    private String budgetName;

    /**
     * 预算年度
     */
    @TableField("budget_year")
    private Integer budgetYear;

    /**
     * 预算类型: department-部门, project-项目, company-公司
     */
    @TableField("budget_type")
    private String budgetType;

    /**
     * 预算对象ID(部门/项目ID)
     */
    @TableField("target_id")
    private Long targetId;

    /**
     * 预算对象名称
     */
    @TableField("target_name")
    private String targetName;

    /**
     * 预算金额
     */
    @TableField("budget_amount")
    private java.math.BigDecimal budgetAmount;

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
     * 状态: draft-草稿, submitted-已提交, approved-已审批, active-生效
     */
    @TableField("status")
    private String status;

    /**
     * 提交人ID
     */
    @TableField("submitter_id")
    private Long submitterId;

    /**
     * 提交人
     */
    @TableField("submitter_name")
    private String submitterName;

    /**
     * 审批人ID
     */
    @TableField("approver_id")
    private Long approverId;

    /**
     * 审批人
     */
    @TableField("approver_name")
    private String approverName;

    /**
     * 审批时间
     */
    private java.time.LocalDateTime approveTime;
}
