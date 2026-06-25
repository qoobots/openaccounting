package com.qoobot.openaccounting.ledger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qoobot.openaccounting.common.exception.BusinessException;
import com.qoobot.openaccounting.ledger.dto.PeriodCreateRequest;
import com.qoobot.openaccounting.ledger.entity.GlAccountingPeriod;
import com.qoobot.openaccounting.ledger.mapper.GlAccountingPeriodMapper;
import com.qoobot.openaccounting.ledger.service.GlAccountingPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * 会计期间Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class GlAccountingPeriodServiceImpl extends ServiceImpl<GlAccountingPeriodMapper, GlAccountingPeriod>
        implements GlAccountingPeriodService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPeriod(PeriodCreateRequest request) {
        // 检查期间是否已存在
        GlAccountingPeriod existPeriod = baseMapper.selectByCompanyAndPeriod(
                request.getCompanyId(), request.getAccountingYear(), request.getAccountingPeriod());
        if (existPeriod != null) {
            throw new BusinessException("该会计期间已存在");
        }

        GlAccountingPeriod period = new GlAccountingPeriod();
        period.setCompanyId(request.getCompanyId());
        period.setAccountingYear(request.getAccountingYear());
        period.setAccountingPeriod(request.getAccountingPeriod());
        period.setStartDate(request.getStartDate());
        period.setEndDate(request.getEndDate());
        period.setStatus("opened");

        save(period);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePeriod(Long id) {
        GlAccountingPeriod period = getById(id);
        if (period == null) {
            throw new BusinessException("会计期间不存在");
        }

        if ("closed".equals(period.getStatus())) {
            throw new BusinessException("已关闭的期间不能删除");
        }

        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void openPeriod(Long id) {
        GlAccountingPeriod period = getById(id);
        if (period == null) {
            throw new BusinessException("会计期间不存在");
        }

        if ("opened".equals(period.getStatus())) {
            throw new BusinessException("期间已开启");
        }

        period.setStatus("opened");
        period.setCloserId(null);
        period.setCloserName(null);
        period.setCloseTime(null);

        updateById(period);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closePeriod(Long id, Long closerId, String closerName) {
        GlAccountingPeriod period = getById(id);
        if (period == null) {
            throw new BusinessException("会计期间不存在");
        }

        if ("closed".equals(period.getStatus())) {
            throw new BusinessException("期间已关闭");
        }

        // 检查是否有未审核凭证
        Long unauditedCount = baseMapper.countUnauditedVouchers(
                period.getCompanyId(), period.getAccountingYear(), period.getAccountingPeriod());
        if (unauditedCount > 0) {
            throw new BusinessException("存在未审核的凭证，无法结账");
        }

        // TODO: 执行期末结账处理
        // 1. 结转损益
        // 2. 生成期末转账凭证
        // 3. 生成总账余额

        period.setStatus("closed");
        period.setCloserId(closerId);
        period.setCloserName(closerName);
        period.setCloseTime(LocalDateTime.now());

        updateById(period);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unClosePeriod(Long id) {
        GlAccountingPeriod period = getById(id);
        if (period == null) {
            throw new BusinessException("会计期间不存在");
        }

        if ("opened".equals(period.getStatus())) {
            throw new BusinessException("期间未关闭，无需反结账");
        }

        // TODO: 回滚期末结账处理
        // 1. 回滚结转损益
        // 2. 删除期末转账凭证
        // 3. 回滚总账余额

        period.setStatus("opened");
        period.setCloserId(null);
        period.setCloserName(null);
        period.setCloseTime(null);

        updateById(period);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initAccountingYear(Long companyId, Integer year) {
        // 检查年度是否已初始化
        List<GlAccountingPeriod> existPeriods = baseMapper.listByCompanyAndYear(companyId, year);
        if (!existPeriods.isEmpty()) {
            throw new BusinessException("该会计年度已初始化");
        }

        // 创建12个会计期间
        LocalDate yearDate = LocalDate.of(year, 1, 1);
        for (int month = 1; month <= 12; month++) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

            GlAccountingPeriod period = new GlAccountingPeriod();
            period.setCompanyId(companyId);
            period.setAccountingYear(year);
            period.setAccountingPeriod(month);
            period.setStartDate(startDate);
            period.setEndDate(endDate);
            period.setStatus("opened");

            save(period);
        }
    }

    @Override
    public GlAccountingPeriod getCurrentOpenedPeriod(Long companyId) {
        return baseMapper.selectCurrentOpenedPeriod(companyId);
    }

    @Override
    public List<GlAccountingPeriod> listPeriods(Long companyId, Integer accountingYear) {
        return baseMapper.listByCompanyAndYear(companyId, accountingYear);
    }
}
