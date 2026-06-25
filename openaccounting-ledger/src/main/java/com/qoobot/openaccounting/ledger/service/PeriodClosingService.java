package com.qoobot.openaccounting.ledger.service;

import com.qoobot.openaccounting.ledger.dto.PeriodCloseRequest;
import com.qoobot.openaccounting.ledger.dto.TrialBalanceRequest;
import com.qoobot.openaccounting.ledger.vo.TrialBalanceVO;

/**
 * 期末处理Service
 *
 * @author openaccounting
 */
public interface PeriodClosingService {

    /**
     * 试算平衡
     *
     * @param request 请求
     * @return 试算平衡结果
     */
    TrialBalanceVO trialBalance(TrialBalanceRequest request);

    /**
     * 损益结转
     *
     * @param request 请求
     * @return 生成的凭证ID
     */
    Long transferProfit(PeriodCloseRequest request);

    /**
     * 结转本期损益并生成凭证
     *
     * @param request 请求
     * @return 生成的凭证ID
     */
    Long closePeriodAndTransfer(PeriodCloseRequest request);
}
