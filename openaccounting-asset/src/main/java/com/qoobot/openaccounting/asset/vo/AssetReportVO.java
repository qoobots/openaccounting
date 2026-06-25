package com.qoobot.openaccounting.asset.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资产报表视图对象
 *
 * @author openaccounting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资产报表视图对象")
public class AssetReportVO {

    @Schema(description = "报表年度")
    private Integer reportYear;

    @Schema(description = "报表期间")
    private Integer reportPeriod;

    @Schema(description = "资产总数")
    private Integer totalAssets;

    @Schema(description = "资产原值合计")
    private BigDecimal totalOriginalValue;

    @Schema(description = "累计折旧合计")
    private BigDecimal totalAccumulatedDepreciation;

    @Schema(description = "净值合计")
    private BigDecimal totalNetValue;

    @Schema(description = "本年折旧合计")
    private BigDecimal currentYearDepreciation;

    @Schema(description = "本月折旧合计")
    private BigDecimal currentPeriodDepreciation;

    @Schema(description = "按类别汇总")
    private List<CategorySummaryVO> categorySummary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "类别汇总")
    public static class CategorySummaryVO {
        @Schema(description = "资产类别")
        private String category;

        @Schema(description = "资产数量")
        private Integer assetCount;

        @Schema(description = "原值合计")
        private BigDecimal originalValue;

        @Schema(description = "累计折旧")
        private BigDecimal accumulatedDepreciation;

        @Schema(description = "净值")
        private BigDecimal netValue;

        @Schema(description = "占比(%)")
        private BigDecimal percentage;
    }
}
