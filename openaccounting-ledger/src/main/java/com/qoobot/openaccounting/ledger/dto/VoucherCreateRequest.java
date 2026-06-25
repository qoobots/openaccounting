package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 创建凭证请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "创建凭证请求")
public class VoucherCreateRequest {

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @NotNull(message = "会计年度不能为空")
    @Schema(description = "会计年度", required = true, example = "2024")
    private Integer accountingYear;

    @NotNull(message = "会计期间不能为空")
    @Schema(description = "会计期间", required = true, example = "1")
    private Integer accountingPeriod;

    @NotNull(message = "凭证日期不能为空")
    @Schema(description = "凭证日期", required = true)
    private LocalDate voucherDate;

    @Schema(description = "凭证类型: receipt-收款, payment-付款, transfer-转账, general-通用")
    private String voucherType = "general";

    @Schema(description = "摘要")
    private String abstract;

    @NotEmpty(message = "凭证分录不能为空")
    @Valid
    @Schema(description = "凭证分录列表", required = true)
    private List<VoucherEntryRequest> entries;
}
