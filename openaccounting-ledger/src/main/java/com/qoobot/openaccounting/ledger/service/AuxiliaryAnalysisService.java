package com.qoobot.openaccounting.ledger.service;

import com.qoobot.openaccounting.ledger.dto.AuxiliaryAnalysisRequest;
import com.qoobot.openaccounting.ledger.vo.AuxiliaryAnalysisVO;

import java.util.List;

/**
 * 辅助核算分析Service
 *
 * @author openaccounting
 */
public interface AuxiliaryAnalysisService {

    /**
     * 辅助核算余额分析
     *
     * @param request 请求
     * @return 分析结果
     */
    List<AuxiliaryAnalysisVO> analyzeBalance(AuxiliaryAnalysisRequest request);

    /**
     * 客户应收账款账龄分析
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @param customerId 客户ID
     * @return 账龄分析结果
     */
    AuxiliaryAnalysisVO analyzeCustomerAge(Long companyId, Integer accountingYear,
                                          Integer accountingPeriod, Long customerId);

    /**
     * 供应商应付账款账龄分析
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @param supplierId 供应商ID
     * @return 账龄分析结果
     */
    AuxiliaryAnalysisVO analyzeSupplierAge(Long companyId, Integer accountingYear,
                                           Integer accountingPeriod, Long supplierId);
}
