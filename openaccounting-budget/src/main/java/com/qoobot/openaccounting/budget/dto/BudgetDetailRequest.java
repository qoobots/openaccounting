package com.qoobot.openaccounting.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 预算明细请求DTO
 *
 * @author openaccounting
 */
@Data
@Schema(description = "预算明细请求")
public class BudgetDetailRequest {

    @NotNull(message = "科目ID不能为空")
    @Schema(description = "科目ID", required = true)
    private Long accountId;

    @Schema(description = "期间预算金额Map(key: 期间1-12, value: 预算金额)")
    private Map<Integer, BigDecimal> periodAmounts;
}
