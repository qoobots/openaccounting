package com.qoobot.openaccounting.ledger.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qoobot.openaccounting.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 会计期间实体
 *
 * @author openaccounting
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gl_accounting_period")
public class GlAccountingPeriod extends BaseEntity {

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
     * 状态：opened开启, closed已关闭
     */
    @TableField("status")
    private String status;

    /**
     * 结账时间
     */
    @TableField("close_time")
    private java.time.LocalDateTime closeTime;

    /**
     * 结账人ID
     */
    @TableField("closer_id")
    private Long closerId;

    /**
     * 结账人
     */
    @TableField("closer_name")
    private String closerName;
}
