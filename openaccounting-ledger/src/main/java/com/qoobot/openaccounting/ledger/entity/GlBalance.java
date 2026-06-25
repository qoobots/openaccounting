package com.qoobot.openaccounting.ledger.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 科目余额实体
 *
 * @author openaccounting
 */
@Data
@TableName("gl_balance")
public class GlBalance {

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
     * 会计年度
     */
    @TableField("accounting_year")
    private Integer accountingYear;

    /**
     * 会计期间
     */
    @TableField("accounting_period")
    private Integer accountingPeriod;

    /**
     * 期初借方
     */
    @TableField("beginning_debit")
    private BigDecimal beginningDebit;

    /**
     * 期初贷方
     */
    @TableField("beginning_credit")
    private BigDecimal beginningCredit;

    /**
     * 本期借方
     */
    @TableField("current_debit")
    private BigDecimal currentDebit;

    /**
     * 本期贷方
     */
    @TableField("current_credit")
    private BigDecimal currentCredit;

    /**
     * 期末借方
     */
    @TableField("ending_debit")
    private BigDecimal endingDebit;

    /**
     * 期末贷方
     */
    @TableField("ending_credit")
    private BigDecimal endingCredit;

    /**
     * 部门ID
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 供应商ID
     */
    @TableField("supplier_id")
    private Long supplierId;

    /**
     * 员工ID
     */
    @TableField("employee_id")
    private Long employeeId;
}
