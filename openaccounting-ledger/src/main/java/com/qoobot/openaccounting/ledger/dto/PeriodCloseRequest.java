package com.qoobot.openaccounting.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 期末结转请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "期末结转请求")
public class PeriodCloseRequest {

    @NotNull(message = "公司ID不能为空")
    @Schema(description = "公司ID", required = true)
    private Long companyId;

    @NotNull(message = "会计年度不能为空")
    @Schema(description = "会计年度", required = true)
    private Integer accountingYear;

    @NotNull(message = "会计期间不能为空")
    @Schema(description = "会计期间", required = true)
    private Integer accountingPeriod;

    @Schema(description = "是否结转损益")
    private Boolean transferProfit = true;

    @Schema(description = "损益转入科目ID(本年利润)")
    private Long profitTransferAccountId;

    @Schema(description = "需要结转的收入科目ID列表")
    private List<Long> revenueAccountIds;

    @Schema(description = "需要结转的费用科目ID列表")
    private List<Long> expenseAccountIds;

    @Schema(description = "摘要")
    private String summary = "期末损益结转";
}
