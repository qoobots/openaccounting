package com.qoobot.openaccounting.budget.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 预算明细监控视图对象
 *
 * @author openaccounting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "预算明细监控视图对象")
public class BudgetDetailMonitorVO {

    @Schema(description = "科目ID")
    private Long accountId;

    @Schema(description = "科目编码")
    private String accountCode;

    @Schema(description = "科目名称")
    private String accountName;

    @Schema(description = "期间")
    private Integer period;

    @Schema(description = "预算金额")
    private BigDecimal budgetAmount;

    @Schema(description = "实际金额")
    private BigDecimal actualAmount;

    @Schema(description = "差异金额")
    private BigDecimal difference;

    @Schema(description = "执行率(%)")
    private BigDecimal executionRate;

    @Schema(description = "执行状态")
    private String executionStatus;
}
