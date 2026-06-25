package com.qoobot.openaccounting.ledger.controller;

import com.qoobot.openaccounting.common.result.Result;
import com.qoobot.openaccounting.ledger.dto.AccountCreateRequest;
import com.qoobot.openaccounting.ledger.dto.AccountQueryRequest;
import com.qoobot.openaccounting.ledger.dto.AccountUpdateRequest;
import com.qoobot.openaccounting.ledger.service.GlAccountService;
import com.qoobot.openaccounting.ledger.vo.AccountVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会计科目Controller
 *
 * @author openaccounting
 */
@Tag(name = "会计科目管理", description = "会计科目相关接口")
@RestController
@RequestMapping("/api/ledger/accounts")
@RequiredArgsConstructor
public class GlAccountController {

    private final GlAccountService accountService;

    @Operation(summary = "创建科目")
    @PostMapping
    public Result<Long> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        Long accountId = accountService.createAccount(request);
        return Result.success(accountId);
    }

    @Operation(summary = "更新科目")
    @PutMapping("/{id}")
    public Result<Void> updateAccount(
            @Parameter(description = "科目ID") @PathVariable Long id,
            @Valid @RequestBody AccountUpdateRequest request) {
        accountService.updateAccount(id, request);
        return Result.success();
    }

    @Operation(summary = "删除科目")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAccount(@Parameter(description = "科目ID") @PathVariable Long id) {
        accountService.deleteAccount(id);
        return Result.success();
    }

    @Operation(summary = "批量删除科目")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteAccounts(@RequestBody List<Long> ids) {
        accountService.batchDeleteAccounts(ids);
        return Result.success();
    }

    @Operation(summary = "查询科目详情")
    @GetMapping("/{id}")
    public Result<AccountVO> getAccount(@Parameter(description = "科目ID") @PathVariable Long id) {
        AccountVO account = accountService.getAccountById(id);
        return Result.success(account);
    }

    @Operation(summary = "分页查询科目列表")
    @GetMapping
    public Result<List<AccountVO>> listAccounts(
            @ModelAttribute AccountQueryRequest request,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        List<AccountVO> accounts = accountService.listAccounts(request, pageNum, pageSize);
        return Result.success(accounts);
    }

    @Operation(summary = "查询科目总数")
    @GetMapping("/count")
    public Result<Long> countAccounts(@ModelAttribute AccountQueryRequest request) {
        Long count = accountService.countAccounts(request);
        return Result.success(count);
    }

    @Operation(summary = "查询科目树")
    @GetMapping("/tree")
    public Result<List<AccountVO>> getAccountTree(
            @Parameter(description = "公司ID") @RequestParam Long companyId,
            @Parameter(description = "状态: 0-启用, 1-停用, null-全部") @RequestParam(required = false) Integer status) {
        List<AccountVO> tree = accountService.getAccountTree(companyId, status);
        return Result.success(tree);
    }

    @Operation(summary = "启用/停用科目")
    @PutMapping("/{id}/status")
    public Result<Void> updateAccountStatus(
            @Parameter(description = "科目ID") @PathVariable Long id,
            @Parameter(description = "状态: 0-启用, 1-停用") @RequestParam Integer status) {
        accountService.updateAccountStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "检查科目编码是否存在")
    @GetMapping("/check-code")
    public Result<Boolean> checkAccountCodeExists(
            @Parameter(description = "科目编码") @RequestParam String accountCode,
            @Parameter(description = "排除的ID(更新时使用)") @RequestParam(required = false) Long excludeId) {
        boolean exists = accountService.checkAccountCodeExists(accountCode, excludeId);
        return Result.success(exists);
    }

    @Operation(summary = "获取叶子节点科目")
    @GetMapping("/leaf")
    public Result<List<AccountVO>> getLeafAccounts(
            @Parameter(description = "公司ID") @RequestParam Long companyId,
            @Parameter(description = "状态: 0-启用, 1-停用, null-全部") @RequestParam(required = false) Integer status) {
        List<AccountVO> accounts = accountService.getLeafAccounts(companyId, status);
        return Result.success(accounts);
    }
}
