package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 凭证分录请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "凭证分录请求")
public class VoucherEntryRequest {

    @NotNull(message = "科目ID不能为空")
    @Schema(description = "科目ID", required = true)
    private Long accountId;

    @Schema(description = "摘要")
    private String abstract;

    @Schema(description = "借方金额")
    private BigDecimal debitAmount;

    @Schema(description = "贷方金额")
    private BigDecimal creditAmount;

    @Schema(description = "币种", example = "CNY")
    private String currency = "CNY";

    @Schema(description = "汇率", example = "1.0")
    private BigDecimal exchangeRate = BigDecimal.ONE;

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
