package com.qoobot.openaccounting.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 预算监控查询请求
 *
 * @author openaccounting
 */
@Data
@Schema(description = "预算监控查询请求")
public class BudgetMonitorRequest {

    @NotNull(message = "预算ID不能为空")
    @Schema(description = "预算ID", required = true)
    private Long budgetId;

    @Schema(description = "科目ID")
    private Long accountId;

    @Schema(description = "期间")
    private Integer period;
}
