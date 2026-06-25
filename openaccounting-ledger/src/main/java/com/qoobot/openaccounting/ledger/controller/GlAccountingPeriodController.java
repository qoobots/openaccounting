package com.qoobot.openaccounting.ledger.controller;

import com.qoobot.openaccounting.common.result.Result;
import com.qoobot.openaccounting.ledger.dto.PeriodCreateRequest;
import com.qoobot.openaccounting.ledger.entity.GlAccountingPeriod;
import com.qoobot.openaccounting.ledger.service.GlAccountingPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会计期间Controller
 *
 * @author openaccounting
 */
@Tag(name = "会计期间管理", description = "会计期间相关接口")
@RestController
@RequestMapping("/api/ledger/periods")
@RequiredArgsConstructor
public class GlAccountingPeriodController {

    private final GlAccountingPeriodService periodService;

    @Operation(summary = "创建会计期间")
    @PostMapping
    public Result<Void> createPeriod(@Valid @RequestBody PeriodCreateRequest request) {
        periodService.createPeriod(request);
        return Result.success();
    }

    @Operation(summary = "删除会计期间")
    @DeleteMapping("/{id}")
    public Result<Void> deletePeriod(@Parameter(description = "期间ID") @PathVariable Long id) {
        periodService.deletePeriod(id);
        return Result.success();
    }

    @Operation(summary = "开启会计期间")
    @PutMapping("/{id}/open")
    public Result<Void> openPeriod(@Parameter(description = "期间ID") @PathVariable Long id) {
        periodService.openPeriod(id);
        return Result.success();
    }

    @Operation(summary = "关闭会计期间")
    @PutMapping("/{id}/close")
    public Result<Void> closePeriod(
            @Parameter(description = "期间ID") @PathVariable Long id,
            @AuthenticationPrincipal Long userId,
            @AuthenticationPrincipal String username) {
        periodService.closePeriod(id, userId, username);
        return Result.success();
    }

    @Operation(summary = "反结账")
    @PutMapping("/{id}/unclose")
    public Result<Void> unClosePeriod(@Parameter(description = "期间ID") @PathVariable Long id) {
        periodService.unClosePeriod(id);
        return Result.success();
    }

    @Operation(summary = "初始化会计年度")
    @PostMapping("/init-year")
    public Result<Void> initAccountingYear(
            @Parameter(description = "公司ID") @RequestParam Long companyId,
            @Parameter(description = "会计年度") @RequestParam Integer year) {
        periodService.initAccountingYear(companyId, year);
        return Result.success();
    }

    @Operation(summary = "查询当前开启的期间")
    @GetMapping("/current")
    public Result<GlAccountingPeriod> getCurrentOpenedPeriod(
            @Parameter(description = "公司ID") @RequestParam Long companyId) {
        GlAccountingPeriod period = periodService.getCurrentOpenedPeriod(companyId);
        return Result.success(period);
    }

    @Operation(summary = "查询年度期间列表")
    @GetMapping
    public Result<List<GlAccountingPeriod>> listPeriods(
            @Parameter(description = "公司ID") @RequestParam Long companyId,
            @Parameter(description = "会计年度") @RequestParam Integer accountingYear) {
        List<GlAccountingPeriod> periods = periodService.listPeriods(companyId, accountingYear);
        return Result.success(periods);
    }
}
