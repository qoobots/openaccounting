package com.qoobot.openaccounting.ledger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qoobot.openaccounting.ledger.dto.AuxiliaryAnalysisRequest;
import com.qoobot.openaccounting.ledger.entity.BaCustomer;
import com.qoobot.openaccounting.ledger.entity.BaSupplier;
import com.qoobot.openaccounting.ledger.entity.BaProject;
import com.qoobot.openaccounting.ledger.entity.GlBalance;
import com.qoobot.openaccounting.ledger.mapper.BaCustomerMapper;
import com.qoobot.openaccounting.ledger.mapper.BaSupplierMapper;
import com.qoobot.openaccounting.ledger.mapper.BaProjectMapper;
import com.qoobot.openaccounting.ledger.mapper.GlBalanceMapper;
import com.qoobot.openaccounting.ledger.service.AuxiliaryAnalysisService;
import com.qoobot.openaccounting.ledger.vo.AuxiliaryAnalysisVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 辅助核算分析Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class AuxiliaryAnalysisServiceImpl implements AuxiliaryAnalysisService {

    private final GlBalanceMapper balanceMapper;
    private final BaCustomerMapper customerMapper;
    private final BaSupplierMapper supplierMapper;
    private final BaProjectMapper projectMapper;

    @Override
    public List<AuxiliaryAnalysisVO> analyzeBalance(AuxiliaryAnalysisRequest request) {
        LambdaQueryWrapper<GlBalance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GlBalance::getCompanyId, request.getCompanyId())
                .eq(GlBalance::getAccountingYear, request.getAccountingYear())
                .eq(GlBalance::getAccountingPeriod, request.getAccountingPeriod());

        // 根据辅助核算类型过滤
        switch (request.getAuxiliaryType()) {
            case "customer" -> wrapper.isNotNull(GlBalance::getCustomerId);
            case "supplier" -> wrapper.isNotNull(GlBalance::getSupplierId);
            case "project" -> wrapper.isNotNull(GlBalance::getProjectId);
        }

        if (request.getAuxiliaryId() != null) {
            switch (request.getAuxiliaryType()) {
                case "customer" -> wrapper.eq(GlBalance::getCustomerId, request.getAuxiliaryId());
                case "supplier" -> wrapper.eq(GlBalance::getSupplierId, request.getAuxiliaryId());
                case "project" -> wrapper.eq(GlBalance::getProjectId, request.getAuxiliaryId());
            }
        }

        if (request.getAccountId() != null) {
            wrapper.eq(GlBalance::getAccountId, request.getAccountId());
        }

        List<GlBalance> balances = balanceMapper.selectList(wrapper);
        return balances.stream()
                .map(this::convertToAnalysisVO)
                .collect(Collectors.toList());
    }

    @Override
    public AuxiliaryAnalysisVO analyzeCustomerAge(Long companyId, Integer accountingYear,
                                                  Integer accountingPeriod, Long customerId) {
        // 获取客户信息
        BaCustomer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            return null;
        }

        AuxiliaryAnalysisVO vo = new AuxiliaryAnalysisVO();
        vo.setAuxiliaryId(customerId);
        vo.setAuxiliaryCode(customer.getCustomerCode());
        vo.setAuxiliaryName(customer.getCustomerName());
        vo.setAuxiliaryType("customer");

        // TODO: 查询应收账款余额并计算账龄
        // 这里简化处理，实际需要查询凭证分录表计算
        vo.setWithin30Days(BigDecimal.ZERO);
        vo.setDays31To60(BigDecimal.ZERO);
        vo.setDays61To90(BigDecimal.ZERO);
        vo.setDays91To180(BigDecimal.ZERO);
        vo.setDays181To360(BigDecimal.ZERO);
        vo.setOver360Days(BigDecimal.ZERO);

        return vo;
    }

    @Override
    public AuxiliaryAnalysisVO analyzeSupplierAge(Long companyId, Integer accountingYear,
                                                  Integer accountingPeriod, Long supplierId) {
        // 获取供应商信息
        BaSupplier supplier = supplierMapper.selectById(supplierId);
        if (supplier == null) {
            return null;
        }

        AuxiliaryAnalysisVO vo = new AuxiliaryAnalysisVO();
        vo.setAuxiliaryId(supplierId);
        vo.setAuxiliaryCode(supplier.getSupplierCode());
        vo.setAuxiliaryName(supplier.getSupplierName());
        vo.setAuxiliaryType("supplier");

        // TODO: 查询应付账款余额并计算账龄
        // 这里简化处理，实际需要查询凭证分录表计算
        vo.setWithin30Days(BigDecimal.ZERO);
        vo.setDays31To60(BigDecimal.ZERO);
        vo.setDays61To90(BigDecimal.ZERO);
        vo.setDays91To180(BigDecimal.ZERO);
        vo.setDays181To360(BigDecimal.ZERO);
        vo.setOver360Days(BigDecimal.ZERO);

        return vo;
    }

    /**
     * 转换为分析VO
     */
    private AuxiliaryAnalysisVO convertToAnalysisVO(GlBalance balance) {
        AuxiliaryAnalysisVO vo = new AuxiliaryAnalysisVO();
        vo.setAccountId(balance.getAccountId());
        vo.setAccountCode(balance.getAccountCode());
        vo.setAccountName(balance.getAccountName());

        // 设置辅助核算信息
        if (balance.getCustomerId() != null) {
            BaCustomer customer = customerMapper.selectById(balance.getCustomerId());
            if (customer != null) {
                vo.setAuxiliaryId(balance.getCustomerId());
                vo.setAuxiliaryCode(customer.getCustomerCode());
                vo.setAuxiliaryName(customer.getCustomerName());
                vo.setAuxiliaryType("customer");
            }
        } else if (balance.getSupplierId() != null) {
            BaSupplier supplier = supplierMapper.selectById(balance.getSupplierId());
            if (supplier != null) {
                vo.setAuxiliaryId(balance.getSupplierId());
                vo.setAuxiliaryCode(supplier.getSupplierCode());
                vo.setAuxiliaryName(supplier.getSupplierName());
                vo.setAuxiliaryType("supplier");
            }
        } else if (balance.getProjectId() != null) {
            BaProject project = projectMapper.selectById(balance.getProjectId());
            if (project != null) {
                vo.setAuxiliaryId(balance.getProjectId());
                vo.setAuxiliaryCode(project.getProjectCode());
                vo.setAuxiliaryName(project.getProjectName());
                vo.setAuxiliaryType("project");
            }
        }

        // 设置余额
        vo.setBeginningBalance(calculateBalance(balance.getBeginningDebit(), balance.getBeginningCredit()));
        vo.setCurrentDebit(balance.getCurrentDebit() != null ? balance.getCurrentDebit() : BigDecimal.ZERO);
        vo.setCurrentCredit(balance.getCurrentCredit() != null ? balance.getCurrentCredit() : BigDecimal.ZERO);
        vo.setEndingBalance(calculateBalance(balance.getEndingDebit(), balance.getEndingCredit()));

        return vo;
    }

    /**
     * 计算余额
     */
    private BigDecimal calculateBalance(BigDecimal debit, BigDecimal credit) {
        BigDecimal d = debit != null ? debit : BigDecimal.ZERO;
        BigDecimal c = credit != null ? credit : BigDecimal.ZERO;
        return d.subtract(c);
    }
}
