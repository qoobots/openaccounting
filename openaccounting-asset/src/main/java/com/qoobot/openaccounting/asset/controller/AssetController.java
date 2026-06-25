package com.qoobot.openaccounting.asset.controller;

import com.qoobot.openaccounting.asset.dto.AssetCreateRequest;
import com.qoobot.openaccounting.asset.service.FaAssetService;
import com.qoobot.openaccounting.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 资产Controller
 *
 * @author openaccounting
 */
@Tag(name = "资产管理", description = "资产管理相关接口")
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final FaAssetService assetService;

    @Operation(summary = "创建资产")
    @PostMapping
    public Result<Long> createAsset(
            @Valid @RequestBody AssetCreateRequest request,
            @AuthenticationPrincipal Long userId,
            @AuthenticationPrincipal String username) {
        Long assetId = assetService.createAsset(request, userId, username);
        return Result.success(assetId);
    }

    @Operation(summary = "计提折旧")
    @PostMapping("/{id}/depreciation")
    public Result<Void> calculateDepreciation(
            @Parameter(description = "资产ID") @PathVariable Long id,
            @Parameter(description = "折旧年度") @RequestParam Integer depreciationYear,
            @Parameter(description = "折旧期间") @RequestParam Integer depreciationPeriod) {
        assetService.calculateDepreciation(id, depreciationYear, depreciationPeriod);
        return Result.success();
    }

    @Operation(summary = "批量计提折旧")
    @PostMapping("/batch-depreciation")
    public Result<Void> batchCalculateDepreciation(
            @Parameter(description = "公司ID") @RequestParam Long companyId,
            @Parameter(description = "折旧年度") @RequestParam Integer depreciationYear,
            @Parameter(description = "折旧期间") @RequestParam Integer depreciationPeriod) {
        assetService.batchCalculateDepreciation(companyId, depreciationYear, depreciationPeriod);
        return Result.success();
    }

    @Operation(summary = "资产处置")
    @PostMapping("/{id}/dispose")
    public Result<Void> disposeAsset(
            @Parameter(description = "资产ID") @PathVariable Long id,
            @Parameter(description = "处置日期") @RequestParam java.time.LocalDate disposalDate) {
        assetService.disposeAsset(id, disposalDate);
        return Result.success();
    }
}
