package com.qoobot.openaccounting.ledger.service;

import com.qoobot.openaccounting.ledger.dto.DetailLedgerQueryRequest;
import com.qoobot.openaccounting.ledger.dto.GeneralLedgerQueryRequest;
import com.qoobot.openaccounting.ledger.vo.DetailLedgerVO;
import com.qoobot.openaccounting.ledger.vo.GeneralLedgerVO;

import java.util.List;

/**
 * 账簿查询Service
 *
 * @author openaccounting
 */
public interface GlLedgerQueryService {

    /**
     * 查询总账
     *
     * @param request 查询请求
     * @return 总账列表
     */
    List<GeneralLedgerVO> queryGeneralLedger(GeneralLedgerQueryRequest request);

    /**
     * 查询明细账
     *
     * @param request 查询请求
     * @return 明细账列表
     */
    List<DetailLedgerVO> queryDetailLedger(DetailLedgerQueryRequest request);

    /**
     * 查询科目余额表
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @param accountingPeriod 会计期间
     * @param accountCodeLike 科目编码(模糊)
     * @param accountNameLike 科目名称(模糊)
     * @return 科目余额表
     */
    List<GeneralLedgerVO> queryAccountBalance(Long companyId, Integer accountingYear,
                                              Integer accountingPeriod, String accountCodeLike,
                                              String accountNameLike);
}
