package com.qoobot.openaccounting.budget.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.budget.dto.BudgetAnalysisRequest;
import com.qoobot.openaccounting.budget.dto.BudgetCreateRequest;
import com.qoobot.openaccounting.budget.dto.BudgetDetailRequest;
import com.qoobot.openaccounting.budget.dto.BudgetMonitorRequest;
import com.qoobot.openaccounting.budget.entity.BgBudget;
import com.qoobot.openaccounting.budget.entity.BgBudgetDetail;
import com.qoobot.openaccounting.budget.mapper.BgBudgetDetailMapper;
import com.qoobot.openaccounting.budget.mapper.BgBudgetMapper;
import com.qoobot.openaccounting.budget.service.BgBudgetService;
import com.qoobot.openaccounting.budget.vo.BudgetAnalysisVO;
import com.qoobot.openaccounting.budget.vo.BudgetDetailMonitorVO;
import com.qoobot.openaccounting.budget.vo.BudgetMonitorVO;
import com.qoobot.openaccounting.ledger.entity.GlAccount;
import com.qoobot.openaccounting.ledger.mapper.GlAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预算Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class BgBudgetServiceImpl extends ServiceImpl<BgBudgetMapper, BgBudget> implements BgBudgetService {

    private final BgBudgetDetailMapper budgetDetailMapper;
    private final GlAccountMapper accountMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBudget(BudgetCreateRequest request, Long submitterId, String submitterName) {
        // 创建预算主表
        BgBudget budget = new BgBudget();
        budget.setCompanyId(request.getCompanyId());
        budget.setBudgetCode(request.getBudgetCode());
        budget.setBudgetName(request.getBudgetName());
        budget.setBudgetYear(request.getBudgetYear());
        budget.setBudgetType(request.getBudgetType());
        budget.setTargetId(request.getTargetId());
        budget.setTargetName(request.getTargetName());
        budget.setBudgetAmount(request.getBudgetAmount());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());
        budget.setStatus("draft");
        budget.setSubmitterId(submitterId);
        budget.setSubmitterName(submitterName);

        save(budget);

        // 创建预算明细
        createBudgetDetails(budget.getId(), request.getDetails());

        return budget.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitBudget(Long id) {
        BgBudget budget = getById(id);
        if (budget == null) {
            throw new RuntimeException("预算不存在");
        }

        if (!"draft".equals(budget.getStatus())) {
            throw new RuntimeException("只有草稿状态的预算可以提交");
        }

        budget.setStatus("submitted");
        updateById(budget);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveBudget(Long id, Long approverId, String approverName) {
        BgBudget budget = getById(id);
        if (budget == null) {
            throw new RuntimeException("预算不存在");
        }

        if (!"submitted".equals(budget.getStatus())) {
            throw new RuntimeException("只有已提交状态的预算可以审批");
        }

        budget.setStatus("approved");
        budget.setApproverId(approverId);
        budget.setApproverName(approverName);
        budget.setApproveTime(java.time.LocalDateTime.now());

        updateById(budget);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activateBudget(Long id) {
        BgBudget budget = getById(id);
        if (budget == null) {
            throw new RuntimeException("预算不存在");
        }

        if (!"approved".equals(budget.getStatus())) {
            throw new RuntimeException("只有已审批状态的预算可以激活");
        }

        budget.setStatus("active");
        updateById(budget);
    }

    @Override
    public BudgetMonitorVO monitorBudget(BudgetMonitorRequest request) {
        // 查询预算信息
        BgBudget budget = getById(request.getBudgetId());
        if (budget == null) {
            throw new RuntimeException("预算不存在");
        }

        // 查询预算明细
        LambdaQueryWrapper<BgBudgetDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BgBudgetDetail::getBudgetId, request.getBudgetId());
        if (request.getAccountId() != null) {
            wrapper.eq(BgBudgetDetail::getAccountId, request.getAccountId());
        }
        if (request.getPeriod() != null) {
            wrapper.eq(BgBudgetDetail::getPeriod, request.getPeriod());
        }
        wrapper.orderByAsc(BgBudgetDetail::getAccountId, BgBudgetDetail::getPeriod);

        List<BgBudgetDetail> details = budgetDetailMapper.selectList(wrapper);

        // 计算汇总数据
        BigDecimal totalBudget = BigDecimal.ZERO;
        BigDecimal totalActual = BigDecimal.ZERO;

        List<BudgetDetailMonitorVO> detailVOs = new ArrayList<>();
        for (BgBudgetDetail detail : details) {
            BudgetDetailMonitorVO detailVO = BudgetDetailMonitorVO.builder()
                    .accountId(detail.getAccountId())
                    .accountCode(detail.getAccountCode())
                    .accountName(detail.getAccountName())
                    .period(detail.getPeriod())
                    .budgetAmount(detail.getBudgetAmount())
                    .actualAmount(detail.getActualAmount())
                    .difference(detail.getBudgetAmount().subtract(detail.getActualAmount()))
                    .executionRate(calculateExecutionRate(detail.getActualAmount(), detail.getBudgetAmount()))
                    .executionStatus(calculateExecutionStatus(detail.getActualAmount(), detail.getBudgetAmount()))
                    .build();
            detailVOs.add(detailVO);

            totalBudget = totalBudget.add(detail.getBudgetAmount());
            totalActual = totalActual.add(detail.getActualAmount());
        }

        BigDecimal remainingAmount = totalBudget.subtract(totalActual);
        BigDecimal executionRate = calculateExecutionRate(totalActual, totalBudget);
        String executionStatus = calculateExecutionStatus(totalActual, totalBudget);

        return BudgetMonitorVO.builder()
                .budgetId(budget.getId())
                .budgetName(budget.getBudgetName())
                .budgetYear(budget.getBudgetYear())
                .budgetAmount(totalBudget)
                .executedAmount(totalActual)
                .remainingAmount(remainingAmount)
                .executionRate(executionRate)
                .executionStatus(executionStatus)
                .details(detailVOs)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateActualAmount(Long budgetId, Long accountId, Integer period, BigDecimal actualAmount) {
        LambdaQueryWrapper<BgBudgetDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BgBudgetDetail::getBudgetId, budgetId);
        wrapper.eq(BgBudgetDetail::getAccountId, accountId);
        wrapper.eq(BgBudgetDetail::getPeriod, period);

        BgBudgetDetail detail = budgetDetailMapper.selectOne(wrapper);
        if (detail == null) {
            throw new RuntimeException("预算明细不存在");
        }

        detail.setActualAmount(actualAmount != null ? actualAmount : BigDecimal.ZERO);
        detail.setDifference(detail.getBudgetAmount().subtract(detail.getActualAmount()));
        detail.setExecutionRate(calculateExecutionRate(detail.getActualAmount(), detail.getBudgetAmount()));

        budgetDetailMapper.updateById(detail);
    }

    @Override
    public BudgetAnalysisVO analyzeBudget(BudgetAnalysisRequest request) {
        BgBudget budget = getById(request.getBudgetId());
        if (budget == null) {
            throw new RuntimeException("预算不存在");
        }

        LambdaQueryWrapper<BgBudgetDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BgBudgetDetail::getBudgetId, request.getBudgetId());
        wrapper.orderByAsc(BgBudgetDetail::getAccountId, BgBudgetDetail::getPeriod);

        List<BgBudgetDetail> details = budgetDetailMapper.selectList(wrapper);

        // 汇总数据
        BigDecimal totalBudget = details.stream()
                .map(BgBudgetDetail::getBudgetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalActual = details.stream()
                .map(BgBudgetDetail::getActualAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalVariance = totalBudget.subtract(totalActual);
        BigDecimal totalVarianceRate = calculateVarianceRate(totalBudget, totalActual);

        // 科目分析
        List<BudgetAnalysisVO.AccountAnalysisVO> accountAnalysis = analyzeByAccount(details);

        // 期间趋势分析
        List<BudgetAnalysisVO.PeriodTrendVO> periodTrends = analyzeTrendByPeriod(details);

        return BudgetAnalysisVO.builder()
                .budgetId(budget.getId())
                .budgetName(budget.getBudgetName())
                .budgetYear(budget.getBudgetYear())
                .analysisType(request.getAnalysisType())
                .totalBudget(totalBudget)
                .totalActual(totalActual)
                .totalVariance(totalVariance)
                .totalVarianceRate(totalVarianceRate)
                .accountAnalysis(accountAnalysis)
                .periodTrends(periodTrends)
                .build();
    }

    /**
     * 按科目分析
     */
    private List<BudgetAnalysisVO.AccountAnalysisVO> analyzeByAccount(List<BgBudgetDetail> details) {
        return details.stream()
                .collect(Collectors.groupingBy(
                        BgBudgetDetail::getAccountId,
                        Collectors.reducing(
                                (a, b) -> {
                                    BgBudgetDetail merged = new BgBudgetDetail();
                                    merged.setAccountId(a.getAccountId());
                                    merged.setAccountCode(a.getAccountCode());
                                    merged.setAccountName(a.getAccountName());
                                    merged.setBudgetAmount(a.getBudgetAmount().add(b.getBudgetAmount()));
                                    merged.setActualAmount(a.getActualAmount().add(b.getActualAmount()));
                                    return merged;
                                }
                        )
                ))
                .values().stream()
                .filter(opt -> opt.isPresent())
                .map(opt -> opt.get())
                .map(detail -> {
                    BigDecimal variance = detail.getBudgetAmount().subtract(detail.getActualAmount());
                    BigDecimal varianceRate = calculateVarianceRate(detail.getBudgetAmount(), detail.getActualAmount());
                    return BudgetAnalysisVO.AccountAnalysisVO.builder()
                            .accountId(detail.getAccountId())
                            .accountCode(detail.getAccountCode())
                            .accountName(detail.getAccountName())
                            .budgetAmount(detail.getBudgetAmount())
                            .actualAmount(detail.getActualAmount())
                            .variance(variance)
                            .varianceRate(varianceRate)
                            .varianceStatus(calculateVarianceStatus(varianceRate))
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 按期间趋势分析
     */
    private List<BudgetAnalysisVO.PeriodTrendVO> analyzeTrendByPeriod(List<BgBudgetDetail> details) {
        Map<Integer, List<BgBudgetDetail>> byPeriod = details.stream()
                .collect(Collectors.groupingBy(BgBudgetDetail::getPeriod));

        List<BudgetAnalysisVO.PeriodTrendVO> trends = new ArrayList<>();
        BigDecimal accumulatedBudget = BigDecimal.ZERO;
        BigDecimal accumulatedActual = BigDecimal.ZERO;

        List<Integer> sortedPeriods = byPeriod.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        for (Integer period : sortedPeriods) {
            List<BgBudgetDetail> periodDetails = byPeriod.get(period);
            BigDecimal periodBudget = periodDetails.stream()
                    .map(BgBudgetDetail::getBudgetAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal periodActual = periodDetails.stream()
                    .map(BgBudgetDetail::getActualAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            accumulatedBudget = accumulatedBudget.add(periodBudget);
            accumulatedActual = accumulatedActual.add(periodActual);

            trends.add(BudgetAnalysisVO.PeriodTrendVO.builder()
                    .period(period)
                    .budgetAmount(periodBudget)
                    .actualAmount(periodActual)
                    .accumulatedBudget(accumulatedBudget)
                    .accumulatedActual(accumulatedActual)
                    .build());
        }

        return trends;
    }

    /**
     * 计算执行率
     */
    private BigDecimal calculateExecutionRate(BigDecimal actual, BigDecimal budget) {
        if (budget == null || budget.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return actual.multiply(BigDecimal.valueOf(100))
                .divide(budget, 2, RoundingMode.HALF_UP);
    }

    /**
     * 计算差异率
     */
    private BigDecimal calculateVarianceRate(BigDecimal budget, BigDecimal actual) {
        if (budget == null || budget.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return budget.subtract(actual)
                .multiply(BigDecimal.valueOf(100))
                .divide(budget, 2, RoundingMode.HALF_UP);
    }

    /**
     * 计算执行状态
     */
    private String calculateExecutionStatus(BigDecimal actual, BigDecimal budget) {
        if (budget == null || budget.compareTo(BigDecimal.ZERO) == 0) {
            return "unknown";
        }
        BigDecimal rate = calculateExecutionRate(actual, budget);
        if (rate.compareTo(BigDecimal.valueOf(90)) <= 0) {
            return "on_track";
        } else if (rate.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return "at_risk";
        } else {
            return "over_budget";
        }
    }

    /**
     * 计算差异状态
     */
    private String calculateVarianceStatus(BigDecimal varianceRate) {
        if (varianceRate.compareTo(BigDecimal.valueOf(-10)) <= 0) {
            return "negative"; // 超支
        } else if (varianceRate.compareTo(BigDecimal.valueOf(10)) <= 0) {
            return "normal";
        } else {
            return "positive"; // 节余
        }
    }

    /**
     * 创建预算明细
     */
    private void createBudgetDetails(Long budgetId, List<BudgetDetailRequest> detailRequests) {
        List<BgBudgetDetail> details = new ArrayList<>();

        for (BudgetDetailRequest detailRequest : detailRequests) {
            GlAccount account = accountMapper.selectById(detailRequest.getAccountId());
            if (account == null) {
                continue;
            }

            Map<Integer, BigDecimal> periodAmounts = detailRequest.getPeriodAmounts();
            if (periodAmounts == null) {
                continue;
            }

            for (Map.Entry<Integer, BigDecimal> entry : periodAmounts.entrySet()) {
                BgBudgetDetail detail = new BgBudgetDetail();
                detail.setBudgetId(budgetId);
                detail.setAccountId(detailRequest.getAccountId());
                detail.setAccountCode(account.getAccountCode());
                detail.setAccountName(account.getAccountName());
                detail.setPeriod(entry.getKey());
                detail.setBudgetAmount(entry.getValue() != null ? entry.getValue() : BigDecimal.ZERO);
                detail.setActualAmount(BigDecimal.ZERO);
                detail.setDifference(detail.getBudgetAmount());
                detail.setExecutionRate(BigDecimal.ZERO);

                details.add(detail);
            }
        }

        if (!details.isEmpty()) {
            budgetDetailMapper.batchInsert(details);
        }
    }
}
