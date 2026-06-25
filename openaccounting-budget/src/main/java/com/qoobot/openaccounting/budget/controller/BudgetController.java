package com.qoobot.openaccounting.budget.controller;

import com.qoobot.openaccounting.budget.dto.BudgetAnalysisRequest;
import com.qoobot.openaccounting.budget.dto.BudgetCreateRequest;
import com.qoobot.openaccounting.budget.dto.BudgetMonitorRequest;
import com.qoobot.openaccounting.budget.service.BgBudgetService;
import com.qoobot.openaccounting.budget.vo.BudgetAnalysisVO;
import com.qoobot.openaccounting.budget.vo.BudgetMonitorVO;
import com.qoobot.openaccounting.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 预算Controller
 *
 * @author openaccounting
 */
@Tag(name = "预算管理", description = "预算管理相关接口")
@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BgBudgetService budgetService;

    @Operation(summary = "创建预算")
    @PostMapping
    public Result<Long> createBudget(
            @Valid @RequestBody BudgetCreateRequest request,
            @AuthenticationPrincipal Long userId,
            @AuthenticationPrincipal String username) {
        Long budgetId = budgetService.createBudget(request, userId, username);
        return Result.success(budgetId);
    }

    @Operation(summary = "提交预算")
    @PutMapping("/{id}/submit")
    public Result<Void> submitBudget(@Parameter(description = "预算ID") @PathVariable Long id) {
        budgetService.submitBudget(id);
        return Result.success();
    }

    @Operation(summary = "审批预算")
    @PutMapping("/{id}/approve")
    public Result<Void> approveBudget(
            @Parameter(description = "预算ID") @PathVariable Long id,
            @AuthenticationPrincipal Long userId,
            @AuthenticationPrincipal String username) {
        budgetService.approveBudget(id, userId, username);
        return Result.success();
    }

    @Operation(summary = "激活预算")
    @PutMapping("/{id}/activate")
    public Result<Void> activateBudget(@Parameter(description = "预算ID") @PathVariable Long id) {
        budgetService.activateBudget(id);
        return Result.success();
    }

    @Operation(summary = "预算监控")
    @PostMapping("/monitor")
    public Result<BudgetMonitorVO> monitorBudget(@Valid @RequestBody BudgetMonitorRequest request) {
        BudgetMonitorVO result = budgetService.monitorBudget(request);
        return Result.success(result);
    }

    @Operation(summary = "预算分析")
    @PostMapping("/analyze")
    public Result<BudgetAnalysisVO> analyzeBudget(@Valid @RequestBody BudgetAnalysisRequest request) {
        BudgetAnalysisVO result = budgetService.analyzeBudget(request);
        return Result.success(result);
    }
}
