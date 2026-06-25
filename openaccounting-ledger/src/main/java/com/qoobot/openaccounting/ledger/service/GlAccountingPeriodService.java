package com.qoobot.openaccounting.ledger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qoobot.openaccounting.ledger.dto.PeriodCreateRequest;
import com.qoobot.openaccounting.ledger.entity.GlAccountingPeriod;

import java.util.List;

/**
 * 会计期间Service
 *
 * @author openaccounting
 */
public interface GlAccountingPeriodService extends IService<GlAccountingPeriod> {

    /**
     * 创建会计期间
     *
     * @param request 创建请求
     */
    void createPeriod(PeriodCreateRequest request);

    /**
     * 删除会计期间
     *
     * @param id 期间ID
     */
    void deletePeriod(Long id);

    /**
     * 开启会计期间
     *
     * @param id 期间ID
     */
    void openPeriod(Long id);

    /**
     * 关闭会计期间
     *
     * @param id 期间ID
     * @param closerId 结账人ID
     * @param closerName 结账人
     */
    void closePeriod(Long id, Long closerId, String closerName);

    /**
     * 反结账
     *
     * @param id 期间ID
     */
    void unClosePeriod(Long id);

    /**
     * 初始化会计年度
     *
     * @param companyId 公司ID
     * @param year 会计年度
     */
    void initAccountingYear(Long companyId, Integer year);

    /**
     * 查询当前开启的期间
     *
     * @param companyId 公司ID
     * @return 当前开启的期间
     */
    GlAccountingPeriod getCurrentOpenedPeriod(Long companyId);

    /**
     * 查询公司年度期间列表
     *
     * @param companyId 公司ID
     * @param accountingYear 会计年度
     * @return 期间列表
     */
    List<GlAccountingPeriod> listPeriods(Long companyId, Integer accountingYear);
}
