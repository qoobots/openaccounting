package com.qoobot.openaccounting.budget.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 预算监控视图对象
 *
 * @author openaccounting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "预算监控视图对象")
public class BudgetMonitorVO {

    @Schema(description = "预算ID")
    private Long budgetId;

    @Schema(description = "预算名称")
    private String budgetName;

    @Schema(description = "预算年度")
    private Integer budgetYear;

    @Schema(description = "预算总额")
    private BigDecimal budgetAmount;

    @Schema(description = "已执行金额")
    private BigDecimal executedAmount;

    @Schema(description = "剩余预算")
    private BigDecimal remainingAmount;

    @Schema(description = "执行进度(%)")
    private BigDecimal executionRate;

    @Schema(description = "执行状态(on_track/over_budget/at_risk)")
    private String executionStatus;

    @Schema(description = "明细列表")
    private java.util.List<BudgetDetailMonitorVO> details;
}
