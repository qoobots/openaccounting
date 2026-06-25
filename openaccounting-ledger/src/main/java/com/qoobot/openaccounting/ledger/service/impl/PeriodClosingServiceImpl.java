package com.qoobot.openaccounting.ledger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qoobot.openaccounting.common.exception.BusinessException;
import com.qoobot.openaccounting.ledger.dto.PeriodCloseRequest;
import com.qoobot.openaccounting.ledger.dto.TrialBalanceRequest;
import com.qoobot.openaccounting.ledger.dto.VoucherCreateRequest;
import com.qoobot.openaccounting.ledger.dto.VoucherEntryRequest;
import com.qoobot.openaccounting.ledger.entity.GlAccount;
import com.qoobot.openaccounting.ledger.entity.GlBalance;
import com.qoobot.openaccounting.ledger.mapper.GlAccountMapper;
import com.qoobot.openaccounting.ledger.mapper.GlBalanceMapper;
import com.qoobot.openaccounting.ledger.service.GlVoucherService;
import com.qoobot.openaccounting.ledger.service.PeriodClosingService;
import com.qoobot.openaccounting.ledger.vo.TrialBalanceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 期末处理Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class PeriodClosingServiceImpl implements PeriodClosingService {

    private final GlBalanceMapper balanceMapper;
    private final GlAccountMapper accountMapper;
    private final GlVoucherService voucherService;

    @Override
    public TrialBalanceVO trialBalance(TrialBalanceRequest request) {
        // 查询当期科目余额
        LambdaQueryWrapper<GlBalance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GlBalance::getCompanyId, request.getCompanyId())
                .eq(GlBalance::getAccountingYear, request.getAccountingYear())
                .eq(GlBalance::getAccountingPeriod, request.getAccountingPeriod());

        List<GlBalance> balances = balanceMapper.selectList(wrapper);

        // 计算借方合计和贷方合计
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (GlBalance balance : balances) {
            BigDecimal currentDebit = balance.getCurrentDebit() != null ?
                    balance.getCurrentDebit() : BigDecimal.ZERO;
            BigDecimal currentCredit = balance.getCurrentCredit() != null ?
                    balance.getCurrentCredit() : BigDecimal.ZERO;

            totalDebit = totalDebit.add(currentDebit);
            totalCredit = totalCredit.add(currentCredit);
        }

        TrialBalanceVO vo = new TrialBalanceVO();
        vo.setTotalDebit(totalDebit);
        vo.setTotalCredit(totalCredit);
        vo.setDifference(totalDebit.subtract(totalCredit));
        vo.setBalanced(totalDebit.compareTo(totalCredit) == 0);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long transferProfit(PeriodCloseRequest request) {
        // 检查试算平衡
        TrialBalanceRequest trialRequest = new TrialBalanceRequest();
        trialRequest.setCompanyId(request.getCompanyId());
        trialRequest.setAccountingYear(request.getAccountingYear());
        trialRequest.setAccountingPeriod(request.getAccountingPeriod());

        TrialBalanceVO trialResult = trialBalance(trialRequest);
        if (!trialResult.getBalanced()) {
            throw new BusinessException("试算不平衡，无法结转损益");
        }

        // 获取需要结转的收入科目余额
        List<GlAccount> revenueAccounts = getRevenueAccounts(request);
        BigDecimal totalRevenue = calculateAccountBalance(revenueAccounts, request);

        // 获取需要结转的费用科目余额
        List<GlAccount> expenseAccounts = getExpenseAccounts(request);
        BigDecimal totalExpense = calculateAccountBalance(expenseAccounts, request);

        // 转入科目（本年利润）
        GlAccount profitAccount = accountMapper.selectById(request.getProfitTransferAccountId());
        if (profitAccount == null) {
            throw new BusinessException("本年利润科目不存在");
        }

        // 创建结转凭证
        VoucherCreateRequest voucherRequest = new VoucherCreateRequest();
        voucherRequest.setCompanyId(request.getCompanyId());
        voucherRequest.setAccountingYear(request.getAccountingYear());
        voucherRequest.setAccountingPeriod(request.getAccountingPeriod());
        voucherRequest.setVoucherDate(java.time.LocalDate.now());
        voucherRequest.setVoucherType("transfer");
        voucherRequest.setAbstract(request.getSummary());

        List<VoucherEntryRequest> entries = new ArrayList<>();

        // 收入科目结转（借方）
        for (GlAccount account : revenueAccounts) {
            GlBalance balance = getAccountBalance(account.getId(), request);
            if (balance != null && balance.getCurrentCredit() != null && balance.getCurrentCredit().compareTo(BigDecimal.ZERO) > 0) {
                VoucherEntryRequest entry = new VoucherEntryRequest();
                entry.setAccountId(account.getId());
                entry.setAbstract(request.getSummary());
                entry.setDebitAmount(balance.getCurrentCredit());
                entry.setCreditAmount(BigDecimal.ZERO);
                entries.add(entry);
            }
        }

        // 费用科目结转（贷方）
        for (GlAccount account : expenseAccounts) {
            GlBalance balance = getAccountBalance(account.getId(), request);
            if (balance != null && balance.getCurrentDebit() != null && balance.getCurrentDebit().compareTo(BigDecimal.ZERO) > 0) {
                VoucherEntryRequest entry = new VoucherEntryRequest();
                entry.setAccountId(account.getId());
                entry.setAbstract(request.getSummary());
                entry.setDebitAmount(BigDecimal.ZERO);
                entry.setCreditAmount(balance.getCurrentDebit());
                entries.add(entry);
            }
        }

        // 本年利润科目
        BigDecimal profit = totalRevenue.subtract(totalExpense);
        VoucherEntryRequest profitEntry = new VoucherEntryRequest();
        profitEntry.setAccountId(request.getProfitTransferAccountId());
        profitEntry.setAbstract(request.getSummary());

        if (profit.compareTo(BigDecimal.ZERO) >= 0) {
            // 盈利，贷记本年利润
            profitEntry.setDebitAmount(BigDecimal.ZERO);
            profitEntry.setCreditAmount(profit);
        } else {
            // 亏损，借记本年利润
            profitEntry.setDebitAmount(profit.abs());
            profitEntry.setCreditAmount(BigDecimal.ZERO);
        }
        entries.add(profitEntry);

        voucherRequest.setEntries(entries);

        // 创建凭证（当前用户ID需要从上下文获取，这里简化处理）
        Long voucherId = voucherService.createVoucher(voucherRequest, 1L, "系统");

        return voucherId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long closePeriodAndTransfer(PeriodCloseRequest request) {
        // 1. 试算平衡检查
        TrialBalanceRequest trialRequest = new TrialBalanceRequest();
        trialRequest.setCompanyId(request.getCompanyId());
        trialRequest.setAccountingYear(request.getAccountingYear());
        trialRequest.setAccountingPeriod(request.getAccountingPeriod());

        TrialBalanceVO trialResult = trialBalance(trialRequest);
        if (!trialResult.getBalanced()) {
            throw new BusinessException("试算不平衡，无法结账");
        }

        // 2. 结转损益
        Long voucherId = transferProfit(request);

        // 3. 关闭期间（通过期间管理服务）

        return voucherId;
    }

    /**
     * 获取收入科目
     */
    private List<GlAccount> getRevenueAccounts(PeriodCloseRequest request) {
        LambdaQueryWrapper<GlAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GlAccount::getCompanyId, request.getCompanyId())
                .eq(GlAccount::getAccountType, "revenue")
                .eq(GlAccount::getStatus, 0);

        if (request.getRevenueAccountIds() != null && !request.getRevenueAccountIds().isEmpty()) {
            wrapper.in(GlAccount::getId, request.getRevenueAccountIds());
        }

        return accountMapper.selectList(wrapper);
    }

    /**
     * 获取费用科目
     */
    private List<GlAccount> getExpenseAccounts(PeriodCloseRequest request) {
        LambdaQueryWrapper<GlAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GlAccount::getCompanyId, request.getCompanyId())
                .eq(GlAccount::getAccountType, "expense")
                .eq(GlAccount::getStatus, 0);

        if (request.getExpenseAccountIds() != null && !request.getExpenseAccountIds().isEmpty()) {
            wrapper.in(GlAccount::getId, request.getExpenseAccountIds());
        }

        return accountMapper.selectList(wrapper);
    }

    /**
     * 计算科目余额
     */
    private BigDecimal calculateAccountBalance(List<GlAccount> accounts, PeriodCloseRequest request) {
        BigDecimal total = BigDecimal.ZERO;

        for (GlAccount account : accounts) {
            GlBalance balance = getAccountBalance(account.getId(), request);
            if (balance != null) {
                if ("debit".equals(account.getBalanceDirection())) {
                    total = total.add(balance.getCurrentDebit() != null ?
                            balance.getCurrentDebit() : BigDecimal.ZERO);
                    total = total.subtract(balance.getCurrentCredit() != null ?
                            balance.getCurrentCredit() : BigDecimal.ZERO);
                } else {
                    total = total.add(balance.getCurrentCredit() != null ?
                            balance.getCurrentCredit() : BigDecimal.ZERO);
                    total = total.subtract(balance.getCurrentDebit() != null ?
                            balance.getCurrentDebit() : BigDecimal.ZERO);
                }
            }
        }

        return total;
    }

    /**
     * 获取科目余额
     */
    private GlBalance getAccountBalance(Long accountId, PeriodCloseRequest request) {
        return balanceMapper.selectByAccountAndPeriod(
                request.getCompanyId(),
                accountId,
                request.getAccountingYear(),
                request.getAccountingPeriod()
        );
    }
}
