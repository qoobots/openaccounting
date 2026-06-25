package com.qoobot.openaccounting.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.asset.dto.AssetCreateRequest;
import com.qoobot.openaccounting.asset.entity.FaAsset;

import java.math.BigDecimal;

/**
 * 资产Service接口
 *
 * @author openaccounting
 */
public interface FaAssetService extends IService<FaAsset> {

    /**
     * 创建资产
     *
     * @param request 创建请求
     * @param userId 操作人ID
     * @param username 操作人
     * @return 资产ID
     */
    Long createAsset(AssetCreateRequest request, Long userId, String username);

    /**
     * 计算折旧
     *
     * @param assetId 资产ID
     * @param depreciationYear 折旧年度
     * @param depreciationPeriod 折旧期间
     */
    void calculateDepreciation(Long assetId, Integer depreciationYear, Integer depreciationPeriod);

    /**
     * 批量计算折旧
     *
     * @param companyId 公司ID
     * @param depreciationYear 折旧年度
     * @param depreciationPeriod 折旧期间
     */
    void batchCalculateDepreciation(Long companyId, Integer depreciationYear, Integer depreciationPeriod);

    /**
     * 资产处置
     *
     * @param assetId 资产ID
     * @param disposalDate 处置日期
     */
    void disposeAsset(Long assetId, java.time.LocalDate disposalDate);
}
