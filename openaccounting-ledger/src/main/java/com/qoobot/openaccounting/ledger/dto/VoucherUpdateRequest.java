package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 更新凭证请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "更新凭证请求")
public class VoucherUpdateRequest {

    @Schema(description = "凭证日期")
    private LocalDate voucherDate;

    @Schema(description = "凭证类型: receipt-收款, payment-付款, transfer-转账, general-通用")
    private String voucherType;

    @Schema(description = "摘要")
    private String abstract;

    @NotEmpty(message = "凭证分录不能为空")
    @Valid
    @Schema(description = "凭证分录列表", required = true)
    private List<VoucherEntryRequest> entries;
}
