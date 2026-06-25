package com.qoobot.openaccounting.budget.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 预算分析视图对象
 *
 * @author openaccounting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "预算分析视图对象")
public class BudgetAnalysisVO {

    @Schema(description = "预算ID")
    private Long budgetId;

    @Schema(description = "预算名称")
    private String budgetName;

    @Schema(description = "预算年度")
    private Integer budgetYear;

    @Schema(description = "分析类型")
    private String analysisType;

    @Schema(description = "预算总额")
    private BigDecimal totalBudget;

    @Schema(description = "实际总额")
    private BigDecimal totalActual;

    @Schema(description = "总差异")
    private BigDecimal totalVariance;

    @Schema(description = "总差异率(%)")
    private BigDecimal totalVarianceRate;

    @Schema(description = "科目分析列表")
    private List<AccountAnalysisVO> accountAnalysis;

    @Schema(description = "期间趋势数据")
    private List<PeriodTrendVO> periodTrends;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "科目分析")
    public static class AccountAnalysisVO {
        @Schema(description = "科目ID")
        private Long accountId;

        @Schema(description = "科目编码")
        private String accountCode;

        @Schema(description = "科目名称")
        private String accountName;

        @Schema(description = "预算金额")
        private BigDecimal budgetAmount;

        @Schema(description = "实际金额")
        private BigDecimal actualAmount;

        @Schema(description = "差异金额")
        private BigDecimal variance;

        @Schema(description = "差异率(%)")
        private BigDecimal varianceRate;

        @Schema(description = "差异状态")
        private String varianceStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "期间趋势")
    public static class PeriodTrendVO {
        @Schema(description = "期间")
        private Integer period;

        @Schema(description = "预算金额")
        private BigDecimal budgetAmount;

        @Schema(description = "实际金额")
        private BigDecimal actualAmount;

        @Schema(description = "累计预算")
        private BigDecimal accumulatedBudget;

        @Schema(description = "累计实际")
        private BigDecimal accumulatedActual;
    }
}
