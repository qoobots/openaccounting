package com.qoobot.openaccounting.ledger.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 明细账响应VO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "明细账信息")
public class DetailLedgerVO {

    @Schema(description = "凭证ID")
    private Long voucherId;

    @Schema(description = "凭证号")
    private String voucherNo;

    @Schema(description = "凭证日期")
    private LocalDate voucherDate;

    @Schema(description = "凭证类型")
    private String voucherType;

    @Schema(description = "分录ID")
    private Long entryId;

    @Schema(description = "科目ID")
    private Long accountId;

    @Schema(description = "科目编码")
    private String accountCode;

    @Schema(description = "科目名称")
    private String accountName;

    @Schema(description = "摘要")
    private String abstract;

    @Schema(description = "借方金额")
    private BigDecimal debitAmount;

    @Schema(description = "贷方金额")
    private BigDecimal creditAmount;

    @Schema(description = "余额")
    private BigDecimal balance;

    @Schema(description = "余额方向")
    private String balanceDirection;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "供应商ID")
    private Long supplierId;

    @Schema(description = "员工ID")
    private Long employeeId;
}
