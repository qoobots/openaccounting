package com.qoobot.openaccounting.asset.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.asset.dto.AssetCreateRequest;
import com.qoobot.openaccounting.asset.entity.FaAsset;
import com.qoobot.openaccounting.asset.entity.FaDepreciation;
import com.qoobot.openaccounting.asset.mapper.FaAssetMapper;
import com.qoobot.openaccounting.asset.mapper.FaDepreciationMapper;
import com.qoobot.openaccounting.asset.service.FaAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * 资产Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class FaAssetServiceImpl extends ServiceImpl<FaAssetMapper, FaAsset> implements FaAssetService {

    private final FaDepreciationMapper depreciationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAsset(AssetCreateRequest request, Long userId, String username) {
        // 计算残值
        BigDecimal salvageValue = request.getOriginalValue()
                .multiply(request.getSalvageRate() != null ? request.getSalvageRate() : BigDecimal.valueOf(5))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // 创建资产
        FaAsset asset = new FaAsset();
        asset.setCompanyId(request.getCompanyId());
        asset.setAssetCode(request.getAssetCode());
        asset.setAssetName(request.getAssetName());
        asset.setAssetCategory(request.getAssetCategory());
        asset.setSpecification(request.getSpecification());
        asset.setModel(request.getModel());
        asset.setDepartmentId(request.getDepartmentId());
        asset.setResponsiblePersonId(request.getResponsiblePersonId());
        asset.setLocation(request.getLocation());
        asset.setOriginalValue(request.getOriginalValue());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setStartDate(request.getStartDate() != null ? request.getStartDate() : request.getPurchaseDate());
        asset.setDepreciationMethod(request.getDepreciationMethod() != null ? request.getDepreciationMethod() : "直线法");
        asset.setDepreciationYears(request.getDepreciationYears() != null ? request.getDepreciationYears() : 5);
        asset.setSalvageRate(request.getSalvageRate() != null ? request.getSalvageRate() : BigDecimal.valueOf(5));
        asset.setSalvageValue(salvageValue);
        asset.setAccumulatedDepreciation(BigDecimal.ZERO);
        asset.setNetValue(request.getOriginalValue());
        asset.setAssetStatus("在用");
        asset.setSupplier(request.getSupplier());
        asset.setInvoiceNumber(request.getInvoiceNumber());
        asset.setRemark(request.getRemark());

        save(asset);

        return asset.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateDepreciation(Long assetId, Integer depreciationYear, Integer depreciationPeriod) {
        FaAsset asset = getById(assetId);
        if (asset == null) {
            throw new RuntimeException("资产不存在");
        }

        if (!"在用".equals(asset.getAssetStatus())) {
            throw new RuntimeException("只有在用状态的资产可以计提折旧");
        }

        // 计算折旧金额
        BigDecimal depreciationAmount = calculateDepreciationAmount(asset);

        // 检查是否已计提
        // TODO: 检查当前期间是否已计提折旧

        // 创建折旧记录
        FaDepreciation depreciation = new FaDepreciation();
        depreciation.setCompanyId(asset.getCompanyId());
        depreciation.setAssetId(assetId);
        depreciation.setAssetCode(asset.getAssetCode());
        depreciation.setAssetName(asset.getAssetName());
        depreciation.setDepreciationYear(depreciationYear);
        depreciation.setDepreciationPeriod(depreciationPeriod);
        depreciation.setDepreciationDate(LocalDate.now());
        depreciation.setBeginningValue(asset.getOriginalValue());
        depreciation.setCurrentDepreciation(depreciationAmount);
        depreciation.setAccumulatedDepreciation(asset.getAccumulatedDepreciation().add(depreciationAmount));
        depreciation.setEndingNetValue(asset.getNetValue().subtract(depreciationAmount));
        depreciation.setStatus("已提");

        depreciationMapper.insert(depreciation);

        // 更新资产累计折旧和净值
        asset.setAccumulatedDepreciation(asset.getAccumulatedDepreciation().add(depreciationAmount));
        asset.setNetValue(asset.getNetValue().subtract(depreciationAmount));
        updateById(asset);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCalculateDepreciation(Long companyId, Integer depreciationYear, Integer depreciationPeriod) {
        List<FaAsset> assets = lambdaQuery()
                .eq(FaAsset::getCompanyId, companyId)
                .eq(FaAsset::getAssetStatus, "在用")
                .list();

        for (FaAsset asset : assets) {
            try {
                calculateDepreciation(asset.getId(), depreciationYear, depreciationPeriod);
            } catch (Exception e) {
                // 记录错误日志，继续处理其他资产
                System.err.println("资产 " + asset.getAssetCode() + " 折旧计算失败: " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disposeAsset(Long assetId, LocalDate disposalDate) {
        FaAsset asset = getById(assetId);
        if (asset == null) {
            throw new RuntimeException("资产不存在");
        }

        asset.setAssetStatus("报废");
        updateById(asset);

        // TODO: 生成资产处置凭证
    }

    /**
     * 计算折旧金额（直线法）
     */
    private BigDecimal calculateDepreciationAmount(FaAsset asset) {
        // 计算已使用的期间数
        int periodCount = calculateUsedPeriods(asset);

        return DepreciationCalculator.calculateDepreciation(asset, periodCount);
    }

    /**
     * 计算已使用的期间数
     */
    private int calculateUsedPeriods(FaAsset asset) {
        if (asset.getStartDate() == null) {
            return 0;
        }

        java.time.LocalDate startDate = asset.getStartDate();
        java.time.LocalDate now = java.time.LocalDate.now();

        long months = java.time.temporal.ChronoUnit.MONTHS.between(startDate, now);
        return (int) months;
    }
}
