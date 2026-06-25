package com.qoobot.openaccounting.ledger.controller;

import com.qoobot.openaccounting.common.result.Result;
import com.qoobot.openaccounting.ledger.dto.PeriodCloseRequest;
import com.qoobot.openaccounting.ledger.dto.TrialBalanceRequest;
import com.qoobot.openaccounting.ledger.service.PeriodClosingService;
import com.qoobot.openaccounting.ledger.vo.TrialBalanceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 期末处理Controller
 *
 * @author openaccounting
 */
@Tag(name = "期末处理", description = "期末处理相关接口")
@RestController
@RequestMapping("/api/ledger/closing")
@RequiredArgsConstructor
public class PeriodClosingController {

    private final PeriodClosingService periodClosingService;

    @Operation(summary = "试算平衡")
    @PostMapping("/trial-balance")
    public Result<TrialBalanceVO> trialBalance(@Valid @RequestBody TrialBalanceRequest request) {
        TrialBalanceVO result = periodClosingService.trialBalance(request);
        return Result.success(result);
    }

    @Operation(summary = "损益结转")
    @PostMapping("/transfer-profit")
    public Result<Long> transferProfit(@Valid @RequestBody PeriodCloseRequest request) {
        Long voucherId = periodClosingService.transferProfit(request);
        return Result.success(voucherId);
    }

    @Operation(summary = "期末结账并结转损益")
    @PostMapping("/close-period")
    public Result<Long> closePeriodAndTransfer(@Valid @RequestBody PeriodCloseRequest request) {
        Long voucherId = periodClosingService.closePeriodAndTransfer(request);
        return Result.success(voucherId);
    }
}
