package com.qoobot.openaccounting.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 预算分析请求
 *
 * @author openaccounting
 */
@Data
@Schema(description = "预算分析请求")
public class BudgetAnalysisRequest {

    @NotNull(message = "预算ID不能为空")
    @Schema(description = "预算ID", required = true)
    private Long budgetId;

    @Schema(description = "分析类型(variance/trend/compare)")
    private String analysisType;

    @Schema(description = "对比预算ID")
    private Long compareBudgetId;
}
