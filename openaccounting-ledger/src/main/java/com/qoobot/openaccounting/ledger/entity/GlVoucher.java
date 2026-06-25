package com.qoobot.openaccounting.ledger.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 凭证实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gl_voucher")
public class GlVoucher extends BaseEntity {

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
     * 凭证号
     */
    @TableField("voucher_no")
    private String voucherNo;

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
     * 凭证日期
     */
    @TableField("voucher_date")
    private LocalDate voucherDate;

    /**
     * 凭证类型
     */
    @TableField("voucher_type")
    private String voucherType;

    /**
     * 摘要
     */
    @TableField("abstract")
    private String abstract;

    /**
     * 借方合计
     */
    @TableField("total_debit")
    private BigDecimal totalDebit;

    /**
     * 贷方合计
     */
    @TableField("total_credit")
    private BigDecimal totalCredit;

    /**
     * 分录数量
     */
    @TableField("entry_count")
    private Integer entryCount;

    /**
     * 状态：draft草稿 submitted已提交 audited已审核 posted已过账
     */
    @TableField("status")
    private String status;

    /**
     * 制单人ID
     */
    @TableField("maker_id")
    private Long makerId;

    /**
     * 制单人
     */
    @TableField("maker_name")
    private String makerName;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private java.time.LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    @TableField("auditor_id")
    private Long auditorId;

    /**
     * 审核人
     */
    @TableField("auditor_name")
    private String auditorName;

    /**
     * 过账时间
     */
    @TableField("post_time")
    private java.time.LocalDateTime postTime;
}
