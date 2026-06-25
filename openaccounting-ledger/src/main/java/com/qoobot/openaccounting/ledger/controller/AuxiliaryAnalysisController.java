package com.qoobot.openaccounting.ledger.controller;

import com.qoobot.openaccounting.common.result.Result;
import com.qoobot.openaccounting.ledger.dto.AuxiliaryAnalysisRequest;
import com.qoobot.openaccounting.ledger.service.AuxiliaryAnalysisService;
import com.qoobot.openaccounting.ledger.vo.AuxiliaryAnalysisVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 辅助核算分析Controller
 *
 * @author openaccounting
 */
@Tag(name = "辅助核算分析", description = "辅助核算分析相关接口")
@RestController
@RequestMapping("/api/ledger/auxiliary")
@RequiredArgsConstructor
public class AuxiliaryAnalysisController {

    private final AuxiliaryAnalysisService auxiliaryAnalysisService;

    @Operation(summary = "辅助核算余额分析")
    @PostMapping("/analyze")
    public Result<List<AuxiliaryAnalysisVO>> analyzeBalance(@RequestBody AuxiliaryAnalysisRequest request) {
        List<AuxiliaryAnalysisVO> result = auxiliaryAnalysisService.analyzeBalance(request);
        return Result.success(result);
    }

    @Operation(summary = "客户应收账款账龄分析")
    @GetMapping("/customer-age")
    public Result<AuxiliaryAnalysisVO> analyzeCustomerAge(
            @Parameter(description = "公司ID") @RequestParam Long companyId,
            @Parameter(description = "会计年度") @RequestParam Integer accountingYear,
            @Parameter(description = "会计期间") @RequestParam Integer accountingPeriod,
            @Parameter(description = "客户ID") @RequestParam Long customerId) {
        AuxiliaryAnalysisVO result = auxiliaryAnalysisService.analyzeCustomerAge(
                companyId, accountingYear, accountingPeriod, customerId);
        return Result.success(result);
    }

    @Operation(summary = "供应商应付账款账龄分析")
    @GetMapping("/supplier-age")
    public Result<AuxiliaryAnalysisVO> analyzeSupplierAge(
            @Parameter(description = "公司ID") @RequestParam Long companyId,
            @Parameter(description = "会计年度") @RequestParam Integer accountingYear,
            @Parameter(description = "会计期间") @RequestParam Integer accountingPeriod,
            @Parameter(description = "供应商ID") @RequestParam Long supplierId) {
        AuxiliaryAnalysisVO result = auxiliaryAnalysisService.analyzeSupplierAge(
                companyId, accountingYear, accountingPeriod, supplierId);
        return Result.success(result);
    }
}
