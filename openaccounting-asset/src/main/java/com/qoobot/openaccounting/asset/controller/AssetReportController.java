package com.qoobot.openaccounting.asset.controller;

import com.qoobot.openaccounting.asset.dto.AssetReportRequest;
import com.qoobot.openaccounting.asset.entity.FaAsset;
import com.qoobot.openaccounting.asset.entity.FaDepreciation;
import com.qoobot.openaccounting.asset.mapper.FaAssetMapper;
import com.qoobot.openaccounting.asset.mapper.FaDepreciationMapper;
import com.qoobot.openaccounting.asset.vo.AssetReportVO;
import com.qoobot.openaccounting.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资产报表Controller
 *
 * @author openaccounting
 */
@Tag(name = "资产报表", description = "资产报表相关接口")
@RestController
@RequestMapping("/api/asset-reports")
@RequiredArgsConstructor
public class AssetReportController {

    private final FaAssetMapper assetMapper;
    private final FaDepreciationMapper depreciationMapper;

    @Operation(summary = "生成资产报表")
    @PostMapping
    public Result<AssetReportVO> generateAssetReport(@Valid @RequestBody AssetReportRequest request) {
        // 查询所有资产
        List<FaAsset> assets = assetMapper.selectList(null);

        // 汇总数据
        Integer totalAssets = assets.size();
        BigDecimal totalOriginalValue = assets.stream()
                .map(FaAsset::getOriginalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAccumulatedDepreciation = assets.stream()
                .map(FaAsset::getAccumulatedDepreciation)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalNetValue = assets.stream()
                .map(FaAsset::getNetValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 查询本年折旧
        List<FaDepreciation> yearDepreciations = depreciationMapper.selectList(null);
        BigDecimal currentYearDepreciation = yearDepreciations.stream()
                .filter(d -> d.getDepreciationYear().equals(request.getReportYear()))
                .map(FaDepreciation::getCurrentDepreciation)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 查询本月折旧
        BigDecimal currentPeriodDepreciation = BigDecimal.ZERO;
        if (request.getReportPeriod() != null) {
            currentPeriodDepreciation = yearDepreciations.stream()
                    .filter(d -> d.getDepreciationYear().equals(request.getReportYear())
                            && d.getDepreciationPeriod().equals(request.getReportPeriod()))
                    .map(FaDepreciation::getCurrentDepreciation)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        // 按类别汇总
        List<AssetReportVO.CategorySummaryVO> categorySummary = generateCategorySummary(assets, totalOriginalValue);

        AssetReportVO report = AssetReportVO.builder()
                .reportYear(request.getReportYear())
                .reportPeriod(request.getReportPeriod())
                .totalAssets(totalAssets)
                .totalOriginalValue(totalOriginalValue)
                .totalAccumulatedDepreciation(totalAccumulatedDepreciation)
                .totalNetValue(totalNetValue)
                .currentYearDepreciation(currentYearDepreciation)
                .currentPeriodDepreciation(currentPeriodDepreciation)
                .categorySummary(categorySummary)
                .build();

        return Result.success(report);
    }

    /**
     * 生成类别汇总
     */
    private List<AssetReportVO.CategorySummaryVO> generateCategorySummary(List<FaAsset> assets, BigDecimal totalValue) {
        Map<String, List<FaAsset>> byCategory = assets.stream()
                .collect(Collectors.groupingBy(a -> a.getAssetCategory() != null ? a.getAssetCategory() : "未分类"));

        return byCategory.entrySet().stream()
                .map(entry -> {
                    List<FaAsset> categoryAssets = entry.getValue();
                    String category = entry.getKey();
                    Integer assetCount = categoryAssets.size();
                    BigDecimal originalValue = categoryAssets.stream()
                            .map(FaAsset::getOriginalValue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal accumulatedDepreciation = categoryAssets.stream()
                            .map(FaAsset::getAccumulatedDepreciation)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal netValue = categoryAssets.stream()
                            .map(FaAsset::getNetValue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal percentage = totalValue.compareTo(BigDecimal.ZERO) > 0
                            ? originalValue.multiply(BigDecimal.valueOf(100))
                                .divide(totalValue, 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    return AssetReportVO.CategorySummaryVO.builder()
                            .category(category)
                            .assetCount(assetCount)
                            .originalValue(originalValue)
                            .accumulatedDepreciation(accumulatedDepreciation)
                            .netValue(netValue)
                            .percentage(percentage)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
