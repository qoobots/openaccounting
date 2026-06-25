package com.qoobot.openaccounting.ledger.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 凭证分录实体
 *
 * @author openaccounting
 */
@Data
@TableName("gl_voucher_entry")
public class GlVoucherEntry {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 凭证ID
     */
    @TableField("voucher_id")
    private Long voucherId;

    /**
     * 行号
     */
    @TableField("line_no")
    private Integer lineNo;

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
     * 摘要
     */
    @TableField("abstract")
    private String abstract;

    /**
     * 借方金额
     */
    @TableField("debit_amount")
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    @TableField("credit_amount")
    private BigDecimal creditAmount;

    /**
     * 币种
     */
    @TableField("currency")
    private String currency;

    /**
     * 汇率
     */
    @TableField("exchange_rate")
    private BigDecimal exchangeRate;

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
