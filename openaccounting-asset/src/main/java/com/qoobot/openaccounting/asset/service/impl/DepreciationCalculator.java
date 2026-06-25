package com.qoobot.openaccounting.asset.service.impl;

import com.qoobot.openaccounting.asset.entity.FaAsset;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 折旧计算器
 *
 * @author openaccounting
 */
public class DepreciationCalculator {

    /**
     * 计算折旧金额
     *
     * @param asset 资产
     * @param periodCount 已使用期间数
     * @return 折旧金额
     */
    public static BigDecimal calculateDepreciation(FaAsset asset, int periodCount) {
        if (asset == null) {
            throw new IllegalArgumentException("资产不能为空");
        }

        if ("直线法".equals(asset.getDepreciationMethod())) {
            return calculateStraightLine(asset);
        } else if ("双倍余额递减法".equals(asset.getDepreciationMethod())) {
            return calculateDoubleDecliningBalance(asset, periodCount);
        } else if ("年数总和法".equals(asset.getDepreciationMethod())) {
            return calculateSumOfYearsDigits(asset, periodCount);
        }

        return calculateStraightLine(asset);
    }

    /**
     * 直线法
     * 年折旧额 = (原值 - 残值) / 折旧年限
     * 月折旧额 = 年折旧额 / 12
     */
    private static BigDecimal calculateStraightLine(FaAsset asset) {
        BigDecimal annualDepreciation = asset.getOriginalValue()
                .subtract(asset.getSalvageValue())
                .divide(BigDecimal.valueOf(asset.getDepreciationYears()), 2, RoundingMode.HALF_UP);

        return annualDepreciation.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    /**
     * 双倍余额递减法
     * 年折旧率 = 2 / 折旧年限
     * 年折旧额 = 账面净值 * 年折旧率
     * 最后两年改为直线法
     */
    private static BigDecimal calculateDoubleDecliningBalance(FaAsset asset, int periodCount) {
        int totalPeriods = asset.getDepreciationYears() * 12;
        int remainingPeriods = totalPeriods - periodCount;

        // 最后两年改为直线法
        if (remainingPeriods <= 24) {
            return asset.getNetValue()
                    .subtract(asset.getSalvageValue())
                    .divide(BigDecimal.valueOf(remainingPeriods), 2, RoundingMode.HALF_UP);
        }

        // 双倍余额递减法
        BigDecimal depreciationRate = BigDecimal.valueOf(2.0)
                .divide(BigDecimal.valueOf(asset.getDepreciationYears()), 4, RoundingMode.HALF_UP);

        BigDecimal annualDepreciation = asset.getNetValue()
                .multiply(depreciationRate)
                .setScale(2, RoundingMode.HALF_UP);

        return annualDepreciation.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    /**
     * 年数总和法
     * 年折旧额 = (原值 - 残值) * (剩余年限 / 年数总和)
     * 年数总和 = n*(n+1)/2
     */
    private static BigDecimal calculateSumOfYearsDigits(FaAsset asset, int periodCount) {
        int depreciationYears = asset.getDepreciationYears();
        int remainingYears = depreciationYears - (periodCount / 12);

        if (remainingYears <= 0) {
            return BigDecimal.ZERO;
        }

        // 计算年数总和
        int sumOfYears = depreciationYears * (depreciationYears + 1) / 2;

        // 年折旧额
        BigDecimal annualDepreciation = asset.getOriginalValue()
                .subtract(asset.getSalvageValue())
                .multiply(BigDecimal.valueOf(remainingYears))
                .divide(BigDecimal.valueOf(sumOfYears), 2, RoundingMode.HALF_UP);

        // 月折旧额
        return annualDepreciation.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }
}
