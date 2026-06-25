package com.qoobot.openaccounting.budget.mapper.BgBudgetDetailMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qoobot.openaccounting.budget.entity.BgBudgetDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算明细Mapper
 *
 * @author openaccounting
 */
@Mapper
public interface BgBudgetDetailMapper extends BaseMapper<BgBudgetDetail> {

    /**
     * 根据预算ID查询明细
     *
     * @param budgetId 预算ID
     * @return 明细列表
     */
    List<BgBudgetDetail> listByBudgetId(@Param("budgetId") Long budgetId);

    /**
     * 批量插入明细
     *
     * @param details 明细列表
     * @return 插入数量
     */
    int batchInsert(@Param("details") List<BgBudgetDetail> details);
}
