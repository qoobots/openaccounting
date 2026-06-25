package com.qoobot.openaccounting.ledger.controller;

import com.qoobot.openaccounting.common.result.Result;
import com.qoobot.openaccounting.ledger.dto.DetailLedgerQueryRequest;
import com.qoobot.openaccounting.ledger.dto.GeneralLedgerQueryRequest;
import com.qoobot.openaccounting.ledger.service.GlLedgerQueryService;
import com.qoobot.openaccounting.ledger.vo.DetailLedgerVO;
import com.qoobot.openaccounting.ledger.vo.GeneralLedgerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 账簿查询Controller
 *
 * @author openaccounting
 */
@Tag(name = "账簿查询", description = "账簿查询相关接口")
@RestController
@RequestMapping("/api/ledger/queries")
@RequiredArgsConstructor
public class GlLedgerQueryController {

    private final GlLedgerQueryService ledgerQueryService;

    @Operation(summary = "查询总账")
    @PostMapping("/general-ledger")
    public Result<List<GeneralLedgerVO>> queryGeneralLedger(@RequestBody GeneralLedgerQueryRequest request) {
        List<GeneralLedgerVO> ledger = ledgerQueryService.queryGeneralLedger(request);
        return Result.success(ledger);
    }

    @Operation(summary = "查询明细账")
    @PostMapping("/detail-ledger")
    public Result<List<DetailLedgerVO>> queryDetailLedger(@RequestBody DetailLedgerQueryRequest request) {
        List<DetailLedgerVO> ledger = ledgerQueryService.queryDetailLedger(request);
        return Result.success(ledger);
    }

    @Operation(summary = "查询科目余额表")
    @GetMapping("/account-balance")
    public Result<List<GeneralLedgerVO>> queryAccountBalance(
            @RequestParam Long companyId,
            @RequestParam Integer accountingYear,
            @RequestParam Integer accountingPeriod,
            @RequestParam(required = false) String accountCode,
            @RequestParam(required = false) String accountName) {
        List<GeneralLedgerVO> balance = ledgerQueryService.queryAccountBalance(
                companyId, accountingYear, accountingPeriod, accountCode, accountName);
        return Result.success(balance);
    }
}
