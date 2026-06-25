package com.qoobot.openaccounting.budget.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 预算明细实体
 *
 * @author openaccounting
 */
@Data
@TableName("bg_budget_detail")
public class BgBudgetDetail {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 预算ID
     */
    @TableField("budget_id")
    private Long budgetId;

    /**
     * 科目ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 科目编码
     */
    @TableField("account_code")
    private String accountCode;

    /**
     * 科目名称
     */
    @TableField("account_name")
    private String accountName;

    /**
     * 期间(1-12)
     */
    @TableField("period")
    private Integer period;

    /**
     * 预算金额
     */
    @TableField("budget_amount")
    private BigDecimal budgetAmount;

    /**
     * 实际发生额
     */
    @TableField("actual_amount")
    private BigDecimal actualAmount;

    /**
     * 差异
     */
    @TableField("difference")
    private BigDecimal difference;

    /**
     * 执行率
     */
    @TableField("execution_rate")
    private BigDecimal executionRate;
}
