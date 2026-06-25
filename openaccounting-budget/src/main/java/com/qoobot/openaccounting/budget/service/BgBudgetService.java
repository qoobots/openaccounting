package com.qoobot.openaccounting.budget.service.BgBudgetService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.budget.dto.BudgetAnalysisRequest;
import com.qoobot.openaccounting.budget.dto.BudgetCreateRequest;
import com.qoobot.openaccounting.budget.dto.BudgetMonitorRequest;
import com.qoobot.openaccounting.budget.entity.BgBudget;
import com.qoobot.openaccounting.budget.vo.BudgetAnalysisVO;
import com.qoobot.openaccounting.budget.vo.BudgetMonitorVO;

/**
 * 预算Service
 *
 * @author openaccounting
 */
public interface BgBudgetService extends IService<BgBudget> {

    /**
     * 创建预算
     *
     * @param request 创建请求
     * @param submitterId 提交人ID
     * @param submitterName 提交人
     * @return 预算ID
     */
    Long createBudget(BudgetCreateRequest request, Long submitterId, String submitterName);

    /**
     * 提交预算
     *
     * @param id 预算ID
     */
    void submitBudget(Long id);

    /**
     * 审批预算
     *
     * @param id 预算ID
     * @param approverId 审批人ID
     * @param approverName 审批人
     */
    void approveBudget(Long id, Long approverId, String approverName);

    /**
     * 激活预算
     *
     * @param id 预算ID
     */
    void activateBudget(Long id);

    /**
     * 预算监控
     *
     * @param request 监控请求
     * @return 监控结果
     */
    BudgetMonitorVO monitorBudget(BudgetMonitorRequest request);

    /**
     * 更新预算实际金额
     *
     * @param budgetId 预算ID
     * @param accountId 科目ID
     * @param period 期间
     * @param actualAmount 实际金额
     */
    void updateActualAmount(Long budgetId, Long accountId, Integer period, java.math.BigDecimal actualAmount);

    /**
     * 预算分析
     *
     * @param request 分析请求
     * @return 分析结果
     */
    BudgetAnalysisVO analyzeBudget(BudgetAnalysisRequest request);
}
