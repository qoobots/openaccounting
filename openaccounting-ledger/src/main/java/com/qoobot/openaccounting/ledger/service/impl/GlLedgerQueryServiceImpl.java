package com.qoobot.openaccounting.ledger.service.impl;

import com.qoobot.openaccounting.ledger.dto.DetailLedgerQueryRequest;
import com.qoobot.openaccounting.ledger.dto.GeneralLedgerQueryRequest;
import com.qoobot.openaccounting.ledger.entity.GlAccount;
import com.qoobot.openaccounting.ledger.entity.GlBalance;
import com.qoobot.openaccounting.ledger.entity.GlVoucher;
import com.qoobot.openaccounting.ledger.entity.GlVoucherEntry;
import com.qoobot.openaccounting.ledger.mapper.GlAccountMapper;
import com.qoobot.openaccounting.ledger.mapper.GlBalanceMapper;
import com.qoobot.openaccounting.ledger.mapper.GlVoucherEntryMapper;
import com.qoobot.openaccounting.ledger.mapper.GlVoucherMapper;
import com.qoobot.openaccounting.ledger.service.GlLedgerQueryService;
import com.qoobot.openaccounting.ledger.vo.DetailLedgerVO;
import com.qoobot.openaccounting.ledger.vo.GeneralLedgerVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账簿查询Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class GlLedgerQueryServiceImpl implements GlLedgerQueryService {

    private final GlBalanceMapper balanceMapper;
    private final GlAccountMapper accountMapper;
    private final GlVoucherMapper voucherMapper;
    private final GlVoucherEntryMapper voucherEntryMapper;

    @Override
    public List<GeneralLedgerVO> queryGeneralLedger(GeneralLedgerQueryRequest request) {
        // 查询总账
        List<GlBalance> balances = balanceMapper.selectGeneralLedger(
                request.getCompanyId(),
                request.getAccountingYear(),
                request.getStartPeriod() != null ? request.getStartPeriod() : 1,
                request.getEndPeriod() != null ? request.getEndPeriod() : 12,
                request.getAccountCode(),
                request.getAccountName(),
                request.getOnlyWithBalance()
        );

        return balances.stream()
                .map(this::convertToGeneralLedgerVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailLedgerVO> queryDetailLedger(DetailLedgerQueryRequest request) {
        // 获取科目信息
        GlAccount account = accountMapper.selectById(request.getAccountId());
        if (account == null) {
            throw new RuntimeException("科目不存在");
        }

        // 构建查询条件
        int startPeriod = request.getStartPeriod() != null ? request.getStartPeriod() : 1;
        int endPeriod = request.getEndPeriod() != null ? request.getEndPeriod() : 12;

        // 查询相关凭证分录
        // 这里简化处理，实际应该从凭证表中查询
        List<GlVoucherEntry> entries = voucherEntryMapper.selectList(
                accountMapper.getBaseMapper().lambdaQuery()
                        .eq(GlVoucherEntry::getAccountId, request.getAccountId())
                        .getWrapper()
        );

        // 计算期初余额
        BigDecimal beginningBalance = calculateBeginningBalance(
                request.getCompanyId(), request.getAccountId(),
                request.getAccountingYear(), startPeriod);

        // 转换为VO并计算余额
        BigDecimal runningBalance = beginningBalance;
        return entries.stream()
                .map(entry -> {
                    DetailLedgerVO vo = convertToDetailLedgerVO(entry, account);

                    // 计算余额
                    if ("debit".equals(account.getBalanceDirection())) {
                        runningBalance = runningBalance.add(entry.getDebitAmount() != null ?
                                entry.getDebitAmount() : BigDecimal.ZERO);
                        runningBalance = runningBalance.subtract(entry.getCreditAmount() != null ?
                                entry.getCreditAmount() : BigDecimal.ZERO);
                    } else {
                        runningBalance = runningBalance.add(entry.getCreditAmount() != null ?
                                entry.getCreditAmount() : BigDecimal.ZERO);
                        runningBalance = runningBalance.subtract(entry.getDebitAmount() != null ?
                                entry.getDebitAmount() : BigDecimal.ZERO);
                    }

                    vo.setBalance(runningBalance);
                    vo.setBalanceDirection(account.getBalanceDirection());

                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<GeneralLedgerVO> queryAccountBalance(Long companyId, Integer accountingYear,
                                                     Integer accountingPeriod, String accountCodeLike,
                                                     String accountNameLike) {
        GeneralLedgerQueryRequest request = new GeneralLedgerQueryRequest();
        request.setCompanyId(companyId);
        request.setAccountingYear(accountingYear);
        request.setStartPeriod(1);
        request.setEndPeriod(accountingPeriod);
        request.setAccountCode(accountCodeLike);
        request.setAccountName(accountNameLike);
        request.setOnlyWithBalance(false);

        return queryGeneralLedger(request);
    }

    /**
     * 计算期初余额
     */
    private BigDecimal calculateBeginningBalance(Long companyId, Long accountId,
                                               Integer accountingYear, Integer period) {
        if (period == 1) {
            // 第一期间，期初余额为0
            return BigDecimal.ZERO;
        }

        // 查询上一期间的期末余额
        GlBalance prevBalance = balanceMapper.selectByAccountAndPeriod(
                companyId, accountId, accountingYear, period - 1);
        if (prevBalance == null) {
            return BigDecimal.ZERO;
        }

        // 根据余额方向计算期初余额
        GlAccount account = accountMapper.selectById(accountId);
        if (account == null) {
            return BigDecimal.ZERO;
        }

        if ("debit".equals(account.getBalanceDirection())) {
            return prevBalance.getEndingDebit() != null ?
                    prevBalance.getEndingDebit() : BigDecimal.ZERO;
        } else {
            return prevBalance.getEndingCredit() != null ?
                    prevBalance.getEndingCredit() : BigDecimal.ZERO;
        }
    }

    /**
     * 转换为总账VO
     */
    private GeneralLedgerVO convertToGeneralLedgerVO(GlBalance balance) {
        GeneralLedgerVO vo = new GeneralLedgerVO();
        vo.setAccountId(balance.getAccountId());
        vo.setAccountCode(balance.getAccountCode());
        vo.setAccountName(balance.getAccountName());
        vo.setBeginningDebit(balance.getBeginningDebit() != null ?
                balance.getBeginningDebit() : BigDecimal.ZERO);
        vo.setBeginningCredit(balance.getBeginningCredit() != null ?
                balance.getBeginningCredit() : BigDecimal.ZERO);
        vo.setCurrentDebit(balance.getCurrentDebit() != null ?
                balance.getCurrentDebit() : BigDecimal.ZERO);
        vo.setCurrentCredit(balance.getCurrentCredit() != null ?
                balance.getCurrentCredit() : BigDecimal.ZERO);
        vo.setEndingDebit(balance.getEndingDebit() != null ?
                balance.getEndingDebit() : BigDecimal.ZERO);
        vo.setEndingCredit(balance.getEndingCredit() != null ?
                balance.getEndingCredit() : BigDecimal.ZERO);

        // 获取科目类型和余额方向
        GlAccount account = accountMapper.selectById(balance.getAccountId());
        if (account != null) {
            vo.setAccountType(account.getAccountType());
            vo.setAccountTypeName(getAccountTypeName(account.getAccountType()));
            vo.setBalanceDirection(account.getBalanceDirection());

            // 计算余额
            if ("debit".equals(account.getBalanceDirection())) {
                BigDecimal beginning = vo.getBeginningDebit().subtract(vo.getBeginningCredit());
                BigDecimal current = vo.getCurrentDebit().subtract(vo.getCurrentCredit());
                BigDecimal ending = vo.getEndingDebit().subtract(vo.getEndingCredit());

                vo.setBeginningBalance(beginning);
                vo.setCurrentBalance(current);
                vo.setEndingBalance(ending);
            } else {
                BigDecimal beginning = vo.getBeginningCredit().subtract(vo.getBeginningDebit());
                BigDecimal current = vo.getCurrentCredit().subtract(vo.getCurrentDebit());
                BigDecimal ending = vo.getEndingCredit().subtract(vo.getEndingDebit());

                vo.setBeginningBalance(beginning);
                vo.setCurrentBalance(current);
                vo.setEndingBalance(ending);
            }
        }

        return vo;
    }

    /**
     * 转换为明细账VO
     */
    private DetailLedgerVO convertToDetailLedgerVO(GlVoucherEntry entry, GlAccount account) {
        DetailLedgerVO vo = new DetailLedgerVO();
        vo.setEntryId(entry.getId());
        vo.setAccountId(entry.getAccountId());
        vo.setAccountCode(entry.getAccountCode());
        vo.setAccountName(entry.getAccountName());
        vo.setAbstract(entry.getAbstract());
        vo.setDebitAmount(entry.getDebitAmount() != null ? entry.getDebitAmount() : BigDecimal.ZERO);
        vo.setCreditAmount(entry.getCreditAmount() != null ? entry.getCreditAmount() : BigDecimal.ZERO);
        vo.setDeptId(entry.getDeptId());
        vo.setProjectId(entry.getProjectId());
        vo.setCustomerId(entry.getCustomerId());
        vo.setSupplierId(entry.getSupplierId());
        vo.setEmployeeId(entry.getEmployeeId());

        // 获取凭证信息
        GlVoucher voucher = voucherMapper.selectById(entry.getVoucherId());
        if (voucher != null) {
            vo.setVoucherId(voucher.getId());
            vo.setVoucherNo(voucher.getVoucherNo());
            vo.setVoucherDate(voucher.getVoucherDate());
            vo.setVoucherType(voucher.getVoucherType());
        }

        return vo;
    }

    /**
     * 获取科目类型名称
     */
    private String getAccountTypeName(String accountType) {
        return switch (accountType) {
            case "assets" -> "资产";
            case "liabilities" -> "负债";
            case "equity" -> "所有者权益";
            case "revenue" -> "收入";
            case "expense" -> "费用";
            default -> "未知";
        };
    }
}
