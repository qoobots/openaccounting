package com.qoobot.openaccounting.ledger.controller;

import com.qoobot.openaccounting.common.result.Result;
import com.qoobot.openaccounting.ledger.dto.VoucherCreateRequest;
import com.qoobot.openaccounting.ledger.dto.VoucherUpdateRequest;
import com.qoobot.openaccounting.ledger.service.GlVoucherService;
import com.qoobot.openaccounting.ledger.vo.VoucherVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 凭证Controller
 *
 * @author openaccounting
 */
@Tag(name = "凭证管理", description = "凭证相关接口")
@RestController
@RequestMapping("/api/ledger/vouchers")
@RequiredArgsConstructor
public class GlVoucherController {

    private final GlVoucherService voucherService;

    @Operation(summary = "创建凭证")
    @PostMapping
    public Result<Long> createVoucher(
            @Valid @RequestBody VoucherCreateRequest request,
            @AuthenticationPrincipal Long userId,
            @AuthenticationPrincipal String username) {
        Long voucherId = voucherService.createVoucher(request, userId, username);
        return Result.success(voucherId);
    }

    @Operation(summary = "更新凭证")
    @PutMapping("/{id}")
    public Result<Void> updateVoucher(
            @Parameter(description = "凭证ID") @PathVariable Long id,
            @Valid @RequestBody VoucherUpdateRequest request) {
        voucherService.updateVoucher(id, request);
        return Result.success();
    }

    @Operation(summary = "删除凭证")
    @DeleteMapping("/{id}")
    public Result<Void> deleteVoucher(@Parameter(description = "凭证ID") @PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return Result.success();
    }

    @Operation(summary = "批量删除凭证")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteVouchers(@RequestBody List<Long> ids) {
        voucherService.batchDeleteVouchers(ids);
        return Result.success();
    }

    @Operation(summary = "查询凭证详情")
    @GetMapping("/{id}")
    public Result<VoucherVO> getVoucher(@Parameter(description = "凭证ID") @PathVariable Long id) {
        VoucherVO voucher = voucherService.getVoucherById(id);
        return Result.success(voucher);
    }

    @Operation(summary = "分页查询凭证列表")
    @GetMapping
    public Result<List<VoucherVO>> listVouchers(
            @Parameter(description = "公司ID") @RequestParam Long companyId,
            @Parameter(description = "会计年度") @RequestParam Integer accountingYear,
            @Parameter(description = "会计期间") @RequestParam Integer accountingPeriod,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "凭证号") @RequestParam(required = false) String voucherNo,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        List<VoucherVO> vouchers = voucherService.listVouchers(
                companyId, accountingYear, accountingPeriod, status, voucherNo, pageNum, pageSize);
        return Result.success(vouchers);
    }

    @Operation(summary = "统计凭证数量")
    @GetMapping("/count")
    public Result<Long> countVouchers(
            @Parameter(description = "公司ID") @RequestParam Long companyId,
            @Parameter(description = "会计年度") @RequestParam Integer accountingYear,
            @Parameter(description = "会计期间") @RequestParam Integer accountingPeriod,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        Long count = voucherService.countVouchers(companyId, accountingYear, accountingPeriod, status);
        return Result.success(count);
    }

    @Operation(summary = "提交凭证")
    @PutMapping("/{id}/submit")
    public Result<Void> submitVoucher(@Parameter(description = "凭证ID") @PathVariable Long id) {
        voucherService.submitVoucher(id);
        return Result.success();
    }

    @Operation(summary = "审核凭证")
    @PutMapping("/{id}/audit")
    public Result<Void> auditVoucher(
            @Parameter(description = "凭证ID") @PathVariable Long id,
            @AuthenticationPrincipal Long userId,
            @AuthenticationPrincipal String username) {
        voucherService.auditVoucher(id, userId, username);
        return Result.success();
    }

    @Operation(summary = "取消审核")
    @PutMapping("/{id}/unaudit")
    public Result<Void> unAuditVoucher(@Parameter(description = "凭证ID") @PathVariable Long id) {
        voucherService.unAuditVoucher(id);
        return Result.success();
    }

    @Operation(summary = "过账凭证")
    @PutMapping("/{id}/post")
    public Result<Void> postVoucher(@Parameter(description = "凭证ID") @PathVariable Long id) {
        voucherService.postVoucher(id);
        return Result.success();
    }

    @Operation(summary = "反过账凭证")
    @PutMapping("/{id}/unpost")
    public Result<Void> unPostVoucher(@Parameter(description = "凭证ID") @PathVariable Long id) {
        voucherService.unPostVoucher(id);
        return Result.success();
    }

    @Operation(summary = "复制凭证")
    @PostMapping("/{id}/copy")
    public Result<Long> copyVoucher(@Parameter(description = "凭证ID") @PathVariable Long id) {
        Long newVoucherId = voucherService.copyVoucher(id);
        return Result.success(newVoucherId);
    }
}
